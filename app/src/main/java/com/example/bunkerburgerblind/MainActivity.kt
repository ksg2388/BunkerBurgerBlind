package com.example.bunkerburgerblind

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.example.bunkerburgerblind.databinding.ActivityMainBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity(), SendEventListener {

    val itemList = arrayListOf<item_data>()      // 단품 메뉴 아이템 배열
    val listAdapter = ListAdapter(itemList)     // 단품 메뉴 rv 어댑터
    var SBList = arrayListOf<shopping_basket_data>() //장바구니 아이템 배열

    private val database = Firebase.database
    private val myRef = database.getReference("bunkerburger")

    private lateinit var binding : ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if(intent.hasExtra("SBListFromSingle")) {
            SBList = intent.getSerializableExtra("SBListFromSingle") as ArrayList<shopping_basket_data>
            SetPrice()
        }

        val burger = ArrayList<MenuType>()
        val side = ArrayList<MenuType>()
        val beverage = ArrayList<MenuType>()

        // 세트메뉴 받아오기
        if (intent.hasExtra("setList")) {
            val shoppingBag = intent.getSerializableExtra("setList") as ArrayList<String>
        }
        if (intent.hasExtra("totalCost")) {
            val totalCost = intent.getIntExtra("totalCost", 0)
        }

        myRef.addValueEventListener(object: ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val menu = snapshot.child("menu")

                for (item in menu.child("burger").children) {
                    val temp = item.getValue(MenuType::class.java)
                    temp?.let { burger.add(it) }
                }

                for (item in menu.child("side").children) {
                    val temp = item.getValue(MenuType::class.java)
                    temp?.let { side.add(it) }
                }

                for (item in menu.child("beverage").children) {
                    val temp = item.getValue(MenuType::class.java)
                    temp?.let { beverage.add(it) }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("실패", "실패")
            }
        })

        binding.help.setOnClickListener {
            val dialog = HelpDig(this@MainActivity)
            dialog.HPDig()
        }

        binding.SBbtn.setOnClickListener { //장바구니 다이얼로그
            val dialog = ShoppingBasketDig(this@MainActivity, SBList)
            dialog.SBDig()
            dialog.setOnClickListener(object: ShoppingBasketDig.CallbackListener{
                override fun onClicked() {
                    SetPrice()
                    listAdapter.notifyDataSetChanged()
                }
            })
        }

        binding.orderSet.setOnClickListener {
            val dialog = Dialog9500Fragment()
            setDataAtFragment(dialog, burger, side, beverage)
        }

        binding.orderSingle.setOnClickListener {
            val intent = Intent(this@MainActivity,SimpleMenuActivity::class.java)
            intent.putExtra("SBListFromMain", SBList)
            startActivity(intent)
        }

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
    }

    private fun setDataAtFragment(fragment: DialogFragment, burger:ArrayList<MenuType>, side:ArrayList<MenuType>, beverage:ArrayList<MenuType>) {
        val bundle = Bundle()
        bundle.putSerializable("burger", burger)
        bundle.putSerializable("side", side)
        bundle.putSerializable("beverage", beverage)

        fragment.arguments = bundle
        fragment.show(supportFragmentManager, "Custom")
    }

    override fun sendMessage (message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
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

}