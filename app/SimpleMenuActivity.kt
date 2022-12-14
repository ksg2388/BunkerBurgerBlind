package com.example.bunkerburgerblind

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.bunkerburgerblind.databinding.SingleMenuMainBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class SimpleMenuActivity : AppCompatActivity() {
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
        setContentView(view)

        binding.RvMenu.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.RvMenu.adapter = listAdapter

        binding.help.setOnClickListener {
            val dialog = HelpDig(this@SimpleMenuActivity)
            dialog.HPDig()
        }

        binding.BackBtn.setOnClickListener {
            startActivity(Intent(this@SimpleMenuActivity,MainActivity::class.java))
        }

        listAdapter.setItemClickListener(object: ListAdapter.OnItemClickListener{ //단일메뉴(리사이클러뷰) 클릭 시 다이얼로그
            var flag : Boolean = false

            override fun onClick(v: View, position: Int) {
                // 메뉴 클릭
                val dialog = SingleMenuClickedDig(this@SimpleMenuActivity)
                dialog.SMDig(itemList[position], SBList)
                dialog.setOnClickedListener(object: SingleMenuClickedDig.ButtonClickListener{
                    override fun PutSBonClick(text: Int){ //장바구니 추가
                        for (item in SBList){
                            if(item.name == itemList[position].name){
                                item.cnt = item.cnt + text
                                flag = true
                            }
                        }
                        if(flag == false) SBList.add(shopping_basket_data(itemList[position].name, itemList[position].price.toString(), itemList[position].img, text))
                        flag = false //장바구니에 아이템 추가(장바구니 데이터 구조 바꿀 필요 있을 듯 함)

                        SetPrice()
                    }
                })
            }
        })

        binding.SBbtn.setOnClickListener { //장바구니 다이얼로그
            val dialog = ShoppingBasketDig(this@SimpleMenuActivity, SBList)
            dialog.SBDig()
            dialog.setOnClickListener(object: ShoppingBasketDig.CallbackListener{
                override fun onClicked() {
                    SetPrice()
                    listAdapter.notifyDataSetChanged()
                }
            })
        }

        myRef.addValueEventListener(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val test = snapshot.child("menu")
                val burger = test.child("burger")
                for (ds in burger.children) {
                    val imageStr: String = ds.child("img").value as String
                    val usage: Long = ds.child("usage").value as Long
                    val name: String = ds.child("name").value as String
                    val id: Long = ds.child("id").value as Long
                    val stock: Long = ds.child("stock").value as Long
                    val price: Long = ds.child("price").value as Long
                    val examination: String = ds.child("examination").value as String
                    Log.e("스냅", ds.toString())
                    itemList.add(item_data(imageStr, usage.toInt(), name, examination,id.toInt(),price.toInt(), stock.toInt()))
                    listAdapter.notifyDataSetChanged()
                }
                Renew1stOrder()
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("실패", "실패")
            }
        })

        itemList.add(item_data("", 0, "품절 테스트", "재고가 품절이라면 우측에 품절이라고 뜹니다.",100,1000, 0))
        itemList.add(item_data("", 0, "최대 주문 테스트", "주문 수량이 재고를 넘을 수 없습니다.",100,1000, 10))
        listAdapter.notifyDataSetChanged()
    }

    fun SetPrice(){ //결제 금액 set
        var sum = 0
        if(SBList.isEmpty()==true)
            binding.price.setText("0")
        else{
            for (item in SBList){
                sum = sum + item.price.toInt() * item.cnt
            }
            binding.price.setText(sum.toString())
        }
    }

    fun Renew1stOrder(){
        var singlePosition = 0
        var i = 0
        var singleUsage = 0
        var setPosition = 0

        for (item in itemList){
            if(item.usage > singleUsage) {
                singlePosition = i
                singleUsage = item.usage
            }
            i++
        }

        binding.single1stName.text = itemList[singlePosition].name
        Glide.with(this)
            .load(itemList[singlePosition].img)
            .into(binding.single1stImg)
        //세트 주문량 1위 추가 필요(데이터 클래스 및 DB에서 불러오기)
    }
}

class paymentDig(context: Context){

}