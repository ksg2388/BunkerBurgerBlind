package com.example.bunkerburgerblind

import android.content.ClipData
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import java.text.FieldPosition

data class manageImageView(val image : Int, val name : String, val quantity : Int, val price : Int)

class ManageMenuActivity : AppCompatActivity() {
    val database = Firebase.database
    val myRef = database.getReference("bunkerburger")
    lateinit var goBack : Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.manage_menu_main)

        // 데이터 가져오기
        myRef.addValueEventListener(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val test = snapshot.child("menu")
                for (ds in test.children) {
                    Log.e("스냅", ds.toString())
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("실패", "실패")
            }
        })


        val recyclerView : RecyclerView = findViewById(R.id.manage_recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        val burger = ArrayList<manageImageView>()
        val side = ArrayList<manageImageView>()
        val drink = ArrayList<manageImageView>()
        val all = ArrayList<manageImageView>()

        burger.add(manageImageView(R.drawable.logo, "클래식버거", 100, 10000))
        burger.add(manageImageView(R.drawable.logo, "더블치즈 해쉬브라운버거", 200, 20000))

        side.add(manageImageView(R.drawable.logo, "하우스감자", 300, 30000))
        side.add(manageImageView(R.drawable.logo, "칠리감자", 400, 40000))

        drink.add(manageImageView(R.drawable.logo, "콜라", 500, 50000))
        drink.add(manageImageView(R.drawable.logo, "청포도에이드", 600, 60000))

        all.addAll(burger)
        all.addAll(side)
        all.addAll(drink)

        val adapter = ManageAdapter(all)

        recyclerView.adapter = adapter

        // 뒤로가기
        goBack = findViewById(R.id.goback)
        goBack.setOnClickListener {
            finish()
        }
    }
    class ManageViewHolder(val layout: View) : RecyclerView.ViewHolder(layout) {
        // layout : manage_menu_item.xml 의 상위 레이아웃

        // manage_menu_item.xml의 각 View들을 멤버 변수와 연결
        val imageView : ImageView = layout.findViewById(R.id.manage_item_image)
        val nameView : TextView = layout.findViewById(R.id.manage_item_name)
        val editButton : TextView = layout.findViewById(R.id.manage_edit_btn)
        val deleteButton : TextView = layout.findViewById(R.id.manage_delete_btn)
    }

    class ManageAdapter(val dataList : List<manageImageView>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType : Int) : RecyclerView.ViewHolder {
            // LayoutInflater 객체 가져오기
            val viewInflater = LayoutInflater.from(parent.context)

            // item.xml 레이아웃을 inflate() 시켜서 ViewHolder 생성자에 전달
            val manageMenuItemLayout = viewInflater.inflate(R.layout.manage_menu_item, parent, false)
            return ManageViewHolder(manageMenuItemLayout)
        }

        //RecyclerView가 ViewHolder와 데이터를 연결할 때 호출 (onCreateViewHolder의 리턴값 자동 전달)
        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            val oneViewItem = dataList[position]

            val viewHolder = holder as ManageViewHolder
            viewHolder.imageView.setImageResource(oneViewItem.image)
            viewHolder.nameView.text = oneViewItem.name

        }

        override fun getItemCount(): Int {
            return dataList.size
        }
    }
}