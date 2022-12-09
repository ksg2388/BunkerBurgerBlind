package com.example.bunkerburgerblind

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.bunkerburgerblind.databinding.SingleMenuMainBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class SingleMenuActivity : AppCompatActivity() {
    private val database = Firebase.database
    private val myRef = database.getReference("bunkerburger")

    private lateinit var binding : SingleMenuMainBinding
    val itemList = arrayListOf<item_data>()      // 단품 메뉴 아이템 배열
    val listAdapter = ListAdapter(itemList)     // 단품 메뉴 rv 어댑터
    val SBList = arrayListOf<shopping_basket_data>() //장바구니 아이템 배열

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = SingleMenuMainBinding.inflate(layoutInflater)
        val view = binding.root
        var sum = 0
        setContentView(view)

        binding.RvMenu.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.RvMenu.adapter = listAdapter

        val SBbtn: Button = findViewById(R.id.SBbtn)

        listAdapter.setItemClickListener(object: ListAdapter.OnItemClickListener{ //단일메뉴(리사이클러뷰) 클릭 시 다이얼로그
            var flag : Boolean = false

            override fun onClick(v: View, position: Int) {
                // 메뉴 클릭
                val dialog = SimpleMenuClickedDig(this@SingleMenuActivity)
                dialog.SMDig(itemList[position])
                dialog.setOnClickedListener(object: SimpleMenuClickedDig.ButtonClickListener{
                    override fun PutSBonClick(text: Int){ //장바구니 추가
                        for (item in SBList){
                            if(item.name == itemList[position].name){
                                item.cnt = item.cnt + text
                                flag = true
                            }
                        }
                        if(flag == false) SBList.add(shopping_basket_data(itemList[position].name, itemList[position].price.toString(), text))
                        flag = false //장바구니에 아이템 추가(장바구니 데이터 구조 바꿀 필요 있을 듯 함)

                        if(SBList.isEmpty()==true) sum = 0
                        else{
                            sum = sum + itemList[position].price.toInt() * text
                        }

                        binding.price.setText(sum.toString())
                        println(SBList)
                    }
                })
            }
        })

        SBbtn.setOnClickListener { //장바구니 다이얼로그
            val dialog = ShoppingBasketDig(this@SingleMenuActivity, SBList)
            dialog.SBDig()
        }

        myRef.addValueEventListener(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val test = snapshot.child("menu")
                val burger = test.child("burger")
                for (ds in burger.children) {
                    val usage: Long = ds.child("usage").value as Long
                    val name: String = ds.child("name").value as String
                    val id: Long = ds.child("id").value as Long
                    val stock: Long = ds.child("stock").value as Long
                    val price: Long = ds.child("price").value as Long
                    val examination: String = ds.child("examination").value as String

                    itemList.add(item_data(usage.toInt(), name, examination,id.toInt(),price.toInt(), stock.toInt()))
                    listAdapter.notifyDataSetChanged()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("실패", "실패")
            }
        })

    }
}

