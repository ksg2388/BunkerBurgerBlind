package com.example.bunkerburgerblind

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Button
import android.widget.ImageView
import android.widget.RadioGroup
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kotlin.collections.ArrayList

data class ManagemenuView(val image:String, val name:String, val price:Int, val examination:String, val type:String)

class ManageMenuActivity : AppCompatActivity() {
    lateinit var goBack: Button
    lateinit var addMenu: Button
    lateinit var radioGroup: RadioGroup
    lateinit var recyclerView : RecyclerView

    var burgers = ArrayList<ManagemenuView>()
    var sides = ArrayList<ManagemenuView>()
    var beverages = ArrayList<ManagemenuView>()
    var all = ArrayList<ManagemenuView>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.manage_menu_main)

        recyclerView = findViewById(R.id.manage_recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        // 데이터 담기
        for (item in burgerList) {
            burgers.add(ManagemenuView(item.img, item.name, item.price, item.examination, "burger"))
        }
        for (item in sideList) {
            sides.add(ManagemenuView(item.img, item.name, item.price, item.examination, "side"))
        }
        for (item in beverageList) {
            beverages.add(ManagemenuView(item.img, item.name, item.price, item.examination, "beverage"))
        }
        all.addAll(burgers)
        all.addAll(sides)
        all.addAll(beverages)

        recyclerView.adapter = ManageAdapter(all)
        (recyclerView.adapter as ManageAdapter).notifyDataSetChanged()

        radioGroup = findViewById(R.id.manage_radioGroup)
        radioGroup.check(R.id.manageSalesShowAll) // 기본값 (전체 보기) 설정
        radioGroup.setOnCheckedChangeListener { group, checkedId ->
            if (checkedId == R.id.manageSalesShowAll) {
                Log.e("my", "전체 보기")
                recyclerView.adapter = ManageAdapter(all)
            } else if (checkedId == R.id.manageSalesShowSet) {
                Log.e("my", "세트만 보기")
                recyclerView.adapter = ManageAdapter(burgers)
            } else if (checkedId == R.id.manageSalesShowSingle) {
                Log.e("my", "단품만 보기")
                recyclerView.adapter = ManageAdapter(burgers)
            } else if (checkedId == R.id.manageSalesShowSide) {
                Log.e("my", "사이드만 보기")
                recyclerView.adapter = ManageAdapter(sides)
            } else if (checkedId == R.id.manageSalesShowbeverage) {
                Log.e("my", "음료만 보기")
                recyclerView.adapter = ManageAdapter(beverages)
            }
            (recyclerView.adapter as ManageAdapter).notifyDataSetChanged()
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

    class ManageAdapter(val dataList: ArrayList<ManagemenuView>) :
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
                .load(oneViewItem.image)
                .into(viewHolder.imageView)
            viewHolder.nameView.text = oneViewItem.name

            viewHolder.editBtn.setOnClickListener {
                val intent = Intent(holder.itemView.context, EditMenuActivity::class.java)
                // 단방향 데이터 전달
                intent.putExtra("name", oneViewItem.name)
                intent.putExtra("examination", oneViewItem.examination)
                intent.putExtra("price", oneViewItem.price.toString())
                intent.putExtra("imgSrc", oneViewItem.image)
                intent.putExtra("type", oneViewItem.type)

                holder.itemView.context.startActivity(intent)
            }

            viewHolder.deleteBtn.setOnClickListener {
                Log.d("my", "삭제하기 클릭")
                val intent = Intent(holder.itemView.context, DeleteMenuActivity::class.java)
                intent.putExtra("name", oneViewItem.name)
                intent.putExtra("imgSrc", oneViewItem.image)
                intent.putExtra("type", oneViewItem.type)
                holder.itemView.context.startActivity(intent)
            }
        }
        override fun getItemCount(): Int {
            return dataList.size
        }
    }
}


