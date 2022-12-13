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
import com.bumptech.glide.Glide
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase

data class ItemView(val image: String, val name: String, val quantity: Int, val price: Int)

class DailySalesActivity : AppCompatActivity() {
    val database = Firebase.database
    val myRef = database.getReference("bunkerburger")

    lateinit var goBack: Button
    lateinit var radioGroup: RadioGroup
    lateinit var salesPrice: TextView
    lateinit var salesQuantity: TextView

    var total_count: Int = 0
    var total_sales: Int = 0
    var set95_count: Int = 0
    var set105_count: Int = 0

    var burgerList = ArrayList<MenuType>()
    var sideList = ArrayList<MenuType>()
    var beverageList = ArrayList<MenuType>()

    var burgers = ArrayList<ItemView>()
    var sides = ArrayList<ItemView>()
    var beverages = ArrayList<ItemView>()
    var all = ArrayList<ItemView>()
    var setmenu = ArrayList<ItemView>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.daily_sales_main)

        salesPrice = findViewById(R.id.salesPrice)
        salesQuantity = findViewById(R.id.salesQuantity)

        val recyclerView: RecyclerView = findViewById(R.id.daily_recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        //db connection
        myRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                //burger data
                val burgerdata = snapshot.child("menu").child("burger")
                for (item in burgerdata.children) {
                    val burgers = item.getValue(MenuType::class.java)
                    if (burgers != null) {
                        burgerList.add(burgers)
                    }
                }
                //side data
                val sidedata = snapshot.child("menu").child("side")
                for (item in sidedata.children) {
                    val sides = item.getValue(MenuType::class.java)
                    if (sides != null) {
                        sideList.add(sides)
                    }
                }
                //beverage data
                val beveragedata = snapshot.child("menu").child("beverage")
                for (item in beveragedata.children) {
                    val beverages = item.getValue(MenuType::class.java)
                    if (beverages != null) {
                        beverageList.add(beverages)
                    }
                }
                //set data
                val test1=snapshot.child("order").children
                var count=0
                for (i in test1+1) {
                    val test = snapshot.child("order").child(count.toString()).child("name").getValue()
                    if(test=="10,500 세트"){
                        Log.e("order name",test.toString() )
                        set105_count+=1
                    }
                    else if(test=="9,500 세트"){
                        Log.e("order name",test.toString() )
                        set95_count+=1
                    }
                    count+=1
                }

                for (item in burgerList) {
                    burgers.add(ItemView(item.img, item.name, item.usage, item.price * item.usage))
                    total_count += item.usage
                    total_sales += item.price * item.usage
                }

                for (item in sideList) {
                    sides.add(ItemView(item.img, item.name, item.usage, item.price * item.usage))
                    total_count += item.usage
                    total_sales += item.price * item.usage
                }

                for (item in beverageList) {
                    beverages.add(ItemView(item.img, item.name, item.usage, item.price * item.usage))
                    total_count += item.usage
                    total_sales += item.price * item.usage
                }
                setmenu.add(ItemView("https://ldb-phinf.pstatic.net/20211231_5/1640910271545DWPcR_JPEG/iyDzBVLyX_amrf1hxw4QExVlh74nY7yRaXYV0NT6CKM%3D.jpg", "9,500 세트", set95_count, set95_count * 9500))
                setmenu.add(ItemView("https://ldb-phinf.pstatic.net/20220107_13/1641535879315peyUt_JPEG/MhijmaKlTEF2g5QOvUvnUBGl7ZKqizQNECULrnggtMA%3D.jpg", "10,500 세트", set105_count, set105_count * 10500))
                total_count += set95_count
                total_count += set105_count
                total_sales += set95_count * 9500
                total_sales += set105_count * 10500

                all.addAll(burgers)
                all.addAll(sides)
                all.addAll(beverages)

                salesQuantity.setText("총 판매 수량 ( ${total_count} )")
                salesPrice.setText("총 판매 금액 ( ${total_sales} )")

                recyclerView.adapter = DailyAdapter(all)
            }


            override fun onCancelled(error: DatabaseError) {
                Log.e("실패", "실패")
            }
        })

        // ---------------

        radioGroup = findViewById(R.id.daily_radioGroup)
        radioGroup.check(R.id.dailySalesShowAll)
        radioGroup.setOnCheckedChangeListener { group, checkedId ->
            if (checkedId == R.id.dailySalesShowAll) {
                Log.d("my", "전체 보기")
                recyclerView.adapter = DailyAdapter(all)
            } else if (checkedId == R.id.dailySalesShowSet) {
                Log.d("my", "세트 보기")
                recyclerView.adapter = DailyAdapter(setmenu)
            } else if (checkedId == R.id.dailySalesShowSingle) {
                Log.d("my", "단품만 보기")
                recyclerView.adapter = DailyAdapter(burgers)
            } else if (checkedId == R.id.dailySalesShowSide) {
                Log.d("my", "사이드만 보기")
                recyclerView.adapter = DailyAdapter(sides)
            } else if (checkedId == R.id.dailySalesShowbeverage) {
                Log.d("my", "음료만 보기")
                recyclerView.adapter = DailyAdapter(beverages)
            }
            (recyclerView.adapter as DailyAdapter).notifyDataSetChanged()
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
        override fun onCreateViewHolder(
            parent: ViewGroup,
            viewType: Int
        ): RecyclerView.ViewHolder {
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

            Glide.with(holder.itemView.context)
                .load(oneViewItem.image)
                .into(viewHolder.imageView)

            viewHolder.nameView.text = oneViewItem.name
            viewHolder.quantityView.text = "총 판매 수량 : " + oneViewItem.quantity.toString()
            viewHolder.priceView.text = "총 판매 금액 : " + oneViewItem.price.toString()
        }

        override fun getItemCount(): Int {
            return dataList.size
        }
    }
}