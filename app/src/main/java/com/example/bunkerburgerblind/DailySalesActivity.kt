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

data class ItemView(val image : Int, val name : String, val quantity : Int, val price : Int)

class DailySalesActivity : AppCompatActivity() {
    val database = Firebase.database
    val myRef = database.getReference("bunkerburger")
    lateinit var goBack : Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.daily_sales_main)

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


        val recyclerView : RecyclerView = findViewById(R.id.daily_recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        val burger = ArrayList<ItemView>()
        val side = ArrayList<ItemView>()
        val drink = ArrayList<ItemView>()
        val all = ArrayList<ItemView>()

        burger.add(ItemView(R.drawable.logo, "클래식버거", 100, 10000))
        burger.add(ItemView(R.drawable.logo, "더블치즈 해쉬브라운버거", 200, 20000))

        side.add(ItemView(R.drawable.logo, "하우스감자", 300, 30000))
        side.add(ItemView(R.drawable.logo, "칠리감자", 400, 40000))

        drink.add(ItemView(R.drawable.logo, "콜라", 500, 50000))
        drink.add(ItemView(R.drawable.logo, "청포도에이드", 600, 60000))

        all.addAll(burger)
        all.addAll(side)
        all.addAll(drink)

        val adapter = DailyAdapter(all)

        recyclerView.adapter = adapter

        // 뒤로가기
        goBack = findViewById(R.id.goback)
        goBack.setOnClickListener {
            finish()
        }
    }
    class DailyViewHolder(val layout: View) : RecyclerView.ViewHolder(layout) {
        // layout : daily_sales_item.xml 의 상위 레이아웃

        // daily_sales_item.xml의 각 View들을 멤버 변수와 연결
        val imageView : ImageView = layout.findViewById(R.id.daily_item_image)
        val nameView : TextView = layout.findViewById(R.id.daily_item_name)
        val quantityView : TextView = layout.findViewById(R.id.daily_item_quantity)
        val priceView : TextView = layout.findViewById(R.id.daily_item_price)
    }

    class DailyAdapter(val dataList : List<ItemView>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType : Int) : RecyclerView.ViewHolder {
            // LayoutInflater 객체 가져오기
            val viewInflater = LayoutInflater.from(parent.context)

            // item.xml 레이아웃을 inflate() 시켜서 ViewHolder 생성자에 전달
            val dailySalesItemLayout = viewInflater.inflate(R.layout.daily_sales_item, parent, false)
            return DailyViewHolder(dailySalesItemLayout)
        }

        //RecyclerView가 ViewHolder와 데이터를 연결할 때 호출 (onCreateViewHolder의 리턴값 자동 전달)
        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            val oneViewItem = dataList[position]

            val viewHolder = holder as DailyViewHolder
            viewHolder.imageView.setImageResource(oneViewItem.image)
            viewHolder.nameView.text = oneViewItem.name
            viewHolder.quantityView.text = "총 판매 수량 : " + oneViewItem.quantity.toString()
            viewHolder.priceView.text = "총 판매 금액 : " + oneViewItem.price.toString()
        }

        override fun getItemCount(): Int {
            return dataList.size
        }
    }
}