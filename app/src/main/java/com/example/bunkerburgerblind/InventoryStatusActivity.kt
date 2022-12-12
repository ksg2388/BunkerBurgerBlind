package com.example.bunkerburgerblind

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.bunkerburgerblind.databinding.InventoryStatusMainBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

data class InventroyItemView(
    val image: String,
    val name: String,
    val stock: Int,
    val usage: Int,
    val examination: String,
    val type: String
)

class InventoryStatusActivity : AppCompatActivity() {
    val database = Firebase.database
    val myRef = database.getReference("bunkerburger")

    lateinit var goBack: Button

    val burgerList = ArrayList<MenuType>()
    val sideList = ArrayList<MenuType>()
    val beverageList = ArrayList<MenuType>()

    var burger = ArrayList<InventroyItemView>()
    var side = ArrayList<InventroyItemView>()
    var beverage = ArrayList<InventroyItemView>()
    var all = ArrayList<InventroyItemView>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = InventoryStatusMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.inventoryRecyclerView.layoutManager = LinearLayoutManager(this)

        // DB connect
        myRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                burgerList.clear()
                sideList.clear()
                beverageList.clear()
                burger.clear()
                side.clear()
                beverage.clear()
                all.clear()
                //burger data
                val burgerdata = snapshot.child("menu").child("burger")
                Log.e("burger", burgerdata.toString())
                for(item in burgerdata.children){
                    val burgers = item.getValue(MenuType::class.java)
                    if (burgers != null) {
                        burgerList.add(burgers)
                    } }

                //side data
                val sidedata = snapshot.child("menu").child("side")
                for(item in sidedata.children){
                    val sides = item.getValue(MenuType::class.java)
                    if (sides != null) {
                        sideList.add(sides)
                    } }

                //beverage data
                val beveragedata = snapshot.child("menu").child("beverage")
                for(item in beveragedata.children){
                    val beverages = item.getValue(MenuType::class.java)
                    if (beverages != null) {
                        beverageList.add(beverages)
                    } }

                for (item in burgerList) {
                    burger.add(InventroyItemView(item.img, item.name, item.stock, item.usage,item.examination,"burger"))
                }
                for (item in sideList) {
                    side.add(InventroyItemView(item.img, item.name, item.stock, item.usage,item.examination,"side"))
                }
                for (item in beverageList) {
                    beverage.add(InventroyItemView(item.img, item.name, item.stock, item.usage,item.examination,"beverage"))
                }

                all.addAll(burger)
                all.addAll(side)
                all.addAll(beverage)

                binding.inventoryRecyclerView.adapter = InventoryAdapter(all)
                (binding.inventoryRecyclerView.adapter as InventoryAdapter).notifyDataSetChanged()

            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("DailySales_burger", "실패")
            }
        })

        goBack = findViewById(R.id.goback)
        goBack.setOnClickListener {
            finish()
        }
    }

    // 뷰 객체 생성
    class InventoryViewHolder(val layout: View) : RecyclerView.ViewHolder(layout) {
        val imageView: ImageView = layout.findViewById(R.id.inventory_item_image)
        val nameBtn: TextView = layout.findViewById(R.id.inventory_item_name)
        val stock: TextView = layout.findViewById(R.id.inventory_item_stock)
        val usage: TextView = layout.findViewById(R.id.inventory_item_usage)
    }

    class InventoryAdapter(val dataList: ArrayList<InventroyItemView>) :
        RecyclerView.Adapter<RecyclerView.ViewHolder>() {
        override fun onCreateViewHolder(
            parent: ViewGroup,
            viewType: Int
        ): RecyclerView.ViewHolder {

            val viewInflater = LayoutInflater.from(parent.context)

            val inventoryStatusItemLayout =
                viewInflater.inflate(R.layout.inventory_status_item, parent, false)
            return InventoryViewHolder(inventoryStatusItemLayout)
        }

        // onCreateViewHolder()에서 반환한 뷰 홀더 객체는 자동으로 onBindViewHolder()의 매개변수로 전달
        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            val oneViewItem = dataList[position]
            val viewholder = holder as InventoryViewHolder

            Glide.with(holder.itemView.context)
                .load(oneViewItem.image)
                .into(viewholder.imageView)

            viewholder.nameBtn.text = oneViewItem.name
            viewholder.stock.text = "보유량( ${oneViewItem.stock} )"
            viewholder.usage.text = "사용량( ${oneViewItem.usage} )"

            viewholder.nameBtn.setOnClickListener {
                val intent = Intent(holder.itemView.context, InventoryClicked::class.java)
                intent.putExtra("inventory_name", oneViewItem.name)
                intent.putExtra("inventory_examination", oneViewItem.examination)
                intent.putExtra("inventory_stock", oneViewItem.stock.toString())
                intent.putExtra("inventory_type", oneViewItem.type)


                Log.d("재고", oneViewItem.stock.toString())

                holder.itemView.context.startActivity(intent)
            }

        }

        override fun getItemCount(): Int {
            return dataList.size
        }
    }

}