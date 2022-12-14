package com.example.bunkerburgerblind

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
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
    val itemList = arrayListOf<item_data>()
    val burgerList = arrayListOf<item_data>()
    val beverageList = arrayListOf<item_data>()
    val sideList = arrayListOf<item_data>()
    val listAdapter = ListAdapter(itemList)     // 단품 메뉴 rv 어댑터
    var SBList = arrayListOf<shopping_basket_data>() //장바구니 아이템 배열

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = SingleMenuMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        val frAdapter = UserFragmentPagerAdapter(supportFragmentManager)

        binding.help.setOnClickListener {
            val dialog = HelpDig(this@SimpleMenuActivity)
            dialog.HPDig()
        }

        binding.BackBtn.setOnClickListener {
            val intent = Intent(this@SimpleMenuActivity,MainActivity::class.java)
            intent.putExtra("SBListFromSingle", SBList)
            startActivity(intent)
        }

        if(intent.hasExtra("SBListFromMain")) {
            SBList = intent.getSerializableExtra("SBListFromMain") as ArrayList<shopping_basket_data>
            SetPrice()
        }


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
                    burgerList.add(item_data(imageStr, usage.toInt(), name, examination,id.toInt(),price.toInt(), stock.toInt()))
                    listAdapter.notifyDataSetChanged()
                }
                Renew1stOrder()

                val side = test.child("side")
                for (ds in side.children) {
                    val imageStr: String = ds.child("img").value as String
                    val usage: Long = ds.child("usage").value as Long
                    val name: String = ds.child("name").value as String
                    val id: Long = ds.child("id").value as Long
                    val stock: Long = ds.child("stock").value as Long
                    val price: Long = ds.child("price").value as Long
                    val examination: String = ds.child("examination").value as String
                    Log.e("스냅", ds.toString())
                    sideList.add(item_data(imageStr, usage.toInt(), name, examination,id.toInt(),price.toInt(), stock.toInt()))
                    listAdapter.notifyDataSetChanged()
                }

                val beverage = test.child("beverage")
                for (ds in beverage.children) {
                    val imageStr: String = ds.child("img").value as String
                    val usage: Long = ds.child("usage").value as Long
                    val name: String = ds.child("name").value as String
                    val id: Long = ds.child("id").value as Long
                    val stock: Long = ds.child("stock").value as Long
                    val price: Long = ds.child("price").value as Long
                    val examination: String = ds.child("examination").value as String
                    Log.e("스냅", ds.toString())
                    beverageList.add(item_data(imageStr, usage.toInt(), name, examination,id.toInt(),price.toInt(), stock.toInt()))
                    listAdapter.notifyDataSetChanged()
                }

                itemList.addAll(burgerList)
                itemList.addAll(sideList)
                itemList.addAll(beverageList)

                frAdapter.addFragment(UserFragment(itemList, SBList), "메인")
                frAdapter.addFragment(UserFragment(burgerList, SBList), "버거")
                frAdapter.addFragment(UserFragment(sideList, SBList), "사이드")
                frAdapter.addFragment(UserFragment(beverageList, SBList), "음료")
                binding.viewpager.adapter = frAdapter
                binding.tabLayout.setupWithViewPager(binding.viewpager)
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("실패", "실패")
            }
        })

        itemList.add(item_data("", 0, "품절 테스트", "재고가 품절이라면 우측에 품절이라고 뜹니다.",100,1000, 0))
        itemList.add(item_data("", 0, "최대 주문 테스트", "주문 수량이 재고를 넘을 수 없습니다.",100,1000, 10))
        listAdapter.notifyDataSetChanged()

        binding.payment.setOnClickListener {
            if(SBList.isEmpty()) {
                val toast = Toast.makeText(
                    this@SimpleMenuActivity,
                    "담은 메뉴가 없습니다.",
                    Toast.LENGTH_SHORT
                )
                toast.show()
            }
            else{
                val dialog = PaymentPlaceChoiceDig(this@SimpleMenuActivity)
                dialog.PPCDig(SBList)
            }
        }
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

        for (item in burgerList){
            if(item.usage > singleUsage) {
                singlePosition = i
                singleUsage = item.usage
            }
            i++
        }

        binding.single1stName.text = burgerList[singlePosition].name
        Glide.with(this)
            .load(burgerList[singlePosition].img)
            .into(binding.single1stImg)
        //세트 주문량 1위 추가 필요(데이터 클래스 및 DB에서 불러오기)
    }
}