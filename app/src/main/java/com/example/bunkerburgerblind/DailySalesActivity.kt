package com.example.bunkerburgerblind

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.RadioGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

data class ItemView(val image: Int, val name: String, val quantity: Int, val price: Int)

class DailySalesActivity : AppCompatActivity() {
    val database = Firebase.database
    val myRef = database.getReference("bunkerburger")
    lateinit var goBack: Button
    lateinit var radioGroup: RadioGroup

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.daily_sales_main)

        //1일 판매 수량 보기-> 전체보기 바로 출력 x, 다른 button 눌렀다가 전체보기 누르면 보임
        //이미지 데이터 받아오기 필요

        val recyclerView: RecyclerView = findViewById(R.id.daily_recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        val burger = ArrayList<ItemView>()
        val side = ArrayList<ItemView>()
        val drink = ArrayList<ItemView>()
        val all = ArrayList<ItemView>()

        //DB연결
        myRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {

                // 버거 데이터 받아오기
                val burgerdata = snapshot.child("menu").child("burger")
                for (item in burgerdata.children) {
                    val burgers = item.getValue(MenuType::class.java)
                    if (burgers != null) {
                        burger.add(ItemView(R.drawable.logo, burgers.name, burgers.usage, burgers.price * burgers.usage)) } }
                all.addAll(burger)

                // side 데이터 받아오기
                val sidedata = snapshot.child("menu").child("side")
                for (item in sidedata.children) {
                    val sides = item.getValue(MenuType::class.java)
                    if (sides != null) {
                        side.add(ItemView(R.drawable.logo, sides.name, sides.usage, sides.price * sides.usage)) } }
                all.addAll(side)

                // beverage 데이터 받아오기
                val drinkdata = snapshot.child("menu").child("beverage")
                for (item in drinkdata.children) {
                    val drinks = item.getValue(MenuType::class.java)
                    if (drinks != null) {
                        drink.add(ItemView(R.drawable.logo, drinks.name, drinks.usage, drinks.price * drinks.usage)) } }
                all.addAll(drink)
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("DailySales_burger", "실패")
            }
        })


        // ---------------

        recyclerView.adapter = DailyAdapter(all)

        radioGroup = findViewById(R.id.daily_radioGroup)
        radioGroup.setOnCheckedChangeListener { group, checkedId ->
            Log.d("my", "클릭됨")
            if (checkedId == R.id.dailySalesShowAll) {
                Log.d("my", "전체 보기")
                recyclerView.adapter = DailyAdapter(all)

            } else if (checkedId == R.id.dailySalesShowSet) {
                Log.d("my", "단품만 보기")
                recyclerView.adapter = DailyAdapter(all)

            } else if (checkedId == R.id.dailySalesShowSingle) {
                Log.d("my", "단품만 보기")
                recyclerView.adapter = DailyAdapter(burger)

            } else if (checkedId == R.id.dailySalesShowSide) {
                Log.d("my", "사이드만 보기")
                recyclerView.adapter = DailyAdapter(side)

            } else if (checkedId == R.id.dailySalesShowDrink) {
                Log.d("my", "음료만 보기")
                recyclerView.adapter = DailyAdapter(drink)
            }
        }

        // 뒤로가기
        goBack = findViewById(R.id.goback)
        goBack.setOnClickListener {
            finish()
        }
    }

    class DailyViewHolder(val layout: View) : RecyclerView.ViewHolder(layout) {
        // layout : daily_sales_item.xml 의 상위 레이아웃

        // daily_sales_item.xml의 각 View들을 멤버 변수와 연결
        val imageView: ImageView = layout.findViewById(R.id.daily_item_image)
        val nameView: TextView = layout.findViewById(R.id.daily_item_name)
        val quantityView: TextView = layout.findViewById(R.id.daily_item_quantity)
        val priceView: TextView = layout.findViewById(R.id.daily_item_price)
    }

    class DailyAdapter(val dataList: List<ItemView>) :
        RecyclerView.Adapter<RecyclerView.ViewHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
            // LayoutInflater 객체 가져오기
            val viewInflater = LayoutInflater.from(parent.context)

            // item.xml 레이아웃을 inflate() 시켜서 ViewHolder 생성자에 전달
            val dailySalesItemLayout =
                viewInflater.inflate(R.layout.daily_sales_item, parent, false)
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