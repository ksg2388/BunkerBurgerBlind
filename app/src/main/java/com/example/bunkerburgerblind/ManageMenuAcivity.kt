package com.example.bunkerburgerblind

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.RadioGroup
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.bumptech.glide.Glide
import com.example.bunkerburgerblind.databinding.ManageMenuMainBinding
import kotlin.collections.ArrayList

class ManageMenuActivity : AppCompatActivity() {
    val database = Firebase.database
    val myRef = database.getReference("bunkerburger")

    lateinit var goBack: Button
    lateinit var addMenu: Button
    lateinit var radioGroup: RadioGroup

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = ManageMenuMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.manageRecyclerView.layoutManager = LinearLayoutManager(this)

        val burgerList = ArrayList<MenuType>()
        val sideList = ArrayList<MenuType>()
        val beverageList = ArrayList<MenuType>()
        val allList = ArrayList<MenuType>()

        myRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                // 데이터 받아오기
                val burgerdata = snapshot.child("menu").child("burger")
                val sidedata = snapshot.child("menu").child("side")
                val beveragedata = snapshot.child("menu").child("beverage")

                for (item in burgerdata.children) {
                    val burgers = burgerdata.child(item.key.toString())
                    val burgerExDB = burgers.child("examination").value
                    val burgerIdDB = burgers.child("id").value
                    val burgerImgDB = burgers.child("img").value
                    val burgerNameDB = burgers.child("name").value
                    val burgerPriceDB = burgers.child("price").value
                    val burgerStockDB = burgers.child("stock").value
                    val burgerUsageDB = burgers.child("usage").value

                    burgerList.add(
                        MenuType(
                            burgerUsageDB.toString().toInt(),
                            burgerNameDB.toString(),
                            burgerExDB.toString(),
                            burgerIdDB.toString().toInt(),
                            burgerPriceDB.toString().toInt(),
                            burgerStockDB.toString().toInt(),
                            burgerImgDB.toString(),
                            burgerdata.key.toString()
                        )
                    )
                }


                for (item in sidedata.children) {
                    val sides = sidedata.child(item.key.toString())
                    val sideExDB = sides.child("examination").value
                    val sideIdDB = sides.child("id").value
                    val sideImgDB = sides.child("img").value
                    val sideNameDB = sides.child("name").value
                    val sidePriceDB = sides.child("price").value
                    val sideStockDB = sides.child("stock").value
                    val sideUsageDB = sides.child("usage").value

                    sideList.add(
                        MenuType(
                            sideUsageDB.toString().toInt(),
                            sideNameDB.toString(),
                            sideExDB.toString(),
                            sideIdDB.toString().toInt(),
                            sidePriceDB.toString().toInt(),
                            sideStockDB.toString().toInt(),
                            sideImgDB.toString(),
                            sidedata.key.toString()
                        )
                    )
                }

                for (item in beveragedata.children) {
                    val beverages = beveragedata.child(item.key.toString())
                    val beverageExDB = beverages.child("examination").value
                    val beverageIdDB = beverages.child("id").value
                    val beverageImgDB = beverages.child("img").value
                    val beverageNameDB = beverages.child("name").value
                    val beveragePriceDB = beverages.child("price").value
                    val beverageStockDB = beverages.child("stock").value
                    val beverageUsageDB = beverages.child("usage").value

                    beverageList.add(
                        MenuType(
                            beverageUsageDB.toString().toInt(),
                            beverageNameDB.toString(),
                            beverageExDB.toString(),
                            beverageIdDB.toString().toInt(),
                            beveragePriceDB.toString().toInt(),
                            beverageStockDB.toString().toInt(),
                            beverageImgDB.toString(),
                            beveragedata.key.toString()
                        )
                    )
                }

                allList.addAll(burgerList)
                allList.addAll(sideList)
                allList.addAll(beverageList)

            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("실패", "실패")
            }
        })
        binding.manageRecyclerView.adapter = ManageAdapter(allList)
        (binding.manageRecyclerView.adapter as ManageAdapter).notifyDataSetChanged()

        radioGroup = findViewById(R.id.manage_radioGroup)
        var test = radioGroup.check(R.id.manageSalesShowAll) // 기본값 (전체 보기) 설정
        Log.e("my", test.toString())
        radioGroup.setOnCheckedChangeListener { group, checkedId ->
            if (checkedId == R.id.manageSalesShowAll) {
                Log.e("my", "전체 보기")
                binding.manageRecyclerView.adapter = ManageAdapter(allList)
            } else if (checkedId == R.id.manageSalesShowSet) {
                Log.e("my", "세트만 보기")
                binding.manageRecyclerView.adapter = ManageAdapter(burgerList)
            } else if (checkedId == R.id.manageSalesShowSingle) {
                Log.e("my", "단품만 보기")
                binding.manageRecyclerView.adapter = ManageAdapter(burgerList)
            } else if (checkedId == R.id.manageSalesShowSide) {
                Log.e("my", "사이드만 보기")
                binding.manageRecyclerView.adapter = ManageAdapter(sideList)
            } else if (checkedId == R.id.manageSalesShowbeverage) {
                Log.e("my", "음료만 보기")
                binding.manageRecyclerView.adapter = ManageAdapter(beverageList)
            }
            (binding.manageRecyclerView.adapter as ManageAdapter).notifyDataSetChanged()
        }

        // 뒤로가기
        goBack = findViewById(R.id.goback)
        goBack.setOnClickListener {
            finish()
        }

        // 추가하기
        addMenu = findViewById(R.id.addMenu)
        addMenu.setOnClickListener {
            val intent = Intent(this, EditMenuActivity::class.java)
            startActivity(intent)
        }
    }

    class ManageViewHolder(val layout: View) : RecyclerView.ViewHolder(layout) {
        // layout : manage_sales_item.xml 의 상위 레이아웃

        // manage_menu_item.xml의 각 View들을 멤버 변수와 연결
        val imageView: ImageView = layout.findViewById(R.id.manage_item_image)
        val nameView: TextView = layout.findViewById(R.id.manage_item_name)
        val editBtn: Button = layout.findViewById(R.id.manage_edit_btn)
        val deleteBtn: Button = layout.findViewById(R.id.manage_delete_btn)
    }

    class ManageAdapter(val dataList: ArrayList<MenuType>) :
        RecyclerView.Adapter<RecyclerView.ViewHolder>() {
        override fun onCreateViewHolder(
            parent: ViewGroup,
            viewType: Int
        ): RecyclerView.ViewHolder {
            // LayoutInflater 객체 가져오기
            val viewInflater = LayoutInflater.from(parent.context)

            // item.xml 레이아웃을 inflate() 시켜서 ViewHolder 생성자에 전달
            val manageItemLayout =
                viewInflater.inflate(R.layout.manage_menu_item, parent, false)
            return ManageViewHolder(manageItemLayout)
        }

        //RecyclerView가 ViewHolder와 데이터를 연결할 때 호출 (onCreateViewHolder의 리턴값 자동 전달)
        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            val oneViewItem = dataList[position]
            val viewHolder = holder as ManageViewHolder

            Glide.with(holder.itemView.context)
                .load(oneViewItem.imgSrc)
                .into(viewHolder.imageView)
            viewHolder.nameView.text = oneViewItem.name

            viewHolder.editBtn.setOnClickListener {
                val intent = Intent(holder.itemView.context, EditMenuActivity::class.java)
                // 단방향 데이터 전달
                intent.putExtra("name", oneViewItem.name)
                intent.putExtra("examination", oneViewItem.examination)
                intent.putExtra("price", oneViewItem.price.toString())
                intent.putExtra("imgSrc", oneViewItem.imgSrc)
                intent.putExtra("type", oneViewItem.type)

                holder.itemView.context.startActivity(intent)
            }

            viewHolder.deleteBtn.setOnClickListener {
                val eventHandler = object : DialogInterface.OnClickListener {
                    override fun onClick(dialog: DialogInterface?, which: Int) {
                        if (which == DialogInterface.BUTTON_POSITIVE) {
                            Log.d("my", "삭제하기")
                        } else if (which == DialogInterface.BUTTON_NEGATIVE)
                            Log.d("my", "닫기")
                    }
                }
                AlertDialog.Builder(ManageMenuActivity()).run {
                    setTitle("메뉴 삭제하기")
                    setIcon(R.drawable.logo)
                    setMessage("정말 삭제하시겠습니까?")
                    setPositiveButton("삭제하기", null)
                    setNegativeButton("닫기", null)
                }

            }
            Log.d("my", "삭제하기 클릭")

        }
        override fun getItemCount(): Int {
            return dataList.size
        }
    }
}


