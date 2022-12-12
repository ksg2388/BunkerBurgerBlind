package com.example.bunkerburgerblind

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

data class InventoryView(
    val image: String,
    val name: String,
    val stock: Int,
    val usage: Int,
    val examination: String,
    val type: String
)


class InventoryStatusActivity : AppCompatActivity() {
    lateinit var goBack: Button
    lateinit var recyclerView: RecyclerView

    var burgers = ArrayList<InventoryView>()
    var sides = ArrayList<InventoryView>()
    var beverages = ArrayList<InventoryView>()
    var all = ArrayList<InventoryView>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.inventory_status_main)

        recyclerView = findViewById(R.id.inventory_recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        //put data
        for (item in burgerList) {
            burgers.add(
                InventoryView(
                    item.img,
                    item.name,
                    item.stock,
                    item.usage,
                    item.examination,
                    "burger"
                )
            )
        }
        for (item in sideList) {
            sides.add(
                InventoryView(
                    item.img,
                    item.name,
                    item.stock,
                    item.usage,
                    item.examination,
                    "side"
                )
            )
        }
        for (item in beverageList) {
            beverages.add(
                InventoryView(
                    item.img,
                    item.name,
                    item.stock,
                    item.usage,
                    item.examination,
                    "beverage"
                )
            )
        }
        all.addAll(burgers)
        all.addAll(sides)
        all.addAll(beverages)

        recyclerView.adapter = InventoryAdapter(all)
        (recyclerView.adapter as InventoryAdapter).notifyDataSetChanged()


        goBack = findViewById(R.id.goback)
        goBack.setOnClickListener {
            finish()
        }



    }
    override fun onRestart() {
        super.onRestart()


    }
    // 뷰 객체 생성
    class InventoryViewHolder(val layout: View) : RecyclerView.ViewHolder(layout) {
        val imageView: ImageView = layout.findViewById(R.id.inventory_item_image)
        val nameBtn: TextView = layout.findViewById(R.id.inventory_item_name)
        val stock: TextView = layout.findViewById(R.id.inventory_item_stock)
        val usage: TextView = layout.findViewById(R.id.inventory_item_usage)
    }

    class InventoryAdapter(val dataList: ArrayList<InventoryView>) :
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

                holder.itemView.context.startActivity(intent)
            }

        }

        override fun getItemCount(): Int {
            return dataList.size
        }
    }

}