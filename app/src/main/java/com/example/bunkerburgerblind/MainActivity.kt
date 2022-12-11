package com.example.bunkerburgerblind

import android.app.Dialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.WindowManager
import android.widget.Button
import android.widget.LinearLayout
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.example.bunkerburgerblind.databinding.ActivityMainBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity(), SendEventListener {
    private val database = Firebase.database
    private val myRef = database.getReference("bunkerburger")

    private lateinit var binding : ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val burger = ArrayList<MenuType>()
        val side = ArrayList<MenuType>()
        val beverage = ArrayList<MenuType>()

        // 세트메뉴 받아오기
        if (intent.hasExtra("setList")) {
            val shoppingBag = intent.getSerializableExtra("setList") as ArrayList<String>
            Log.d("데이터 확인", shoppingBag[0])
            Log.d("데이터 확인", shoppingBag[1])
            Log.d("데이터 확인", shoppingBag[2])
        }
        if (intent.hasExtra("totalCost")) {
            val totalCost = intent.getIntExtra("totalCost", 0)
            Log.d("데이터 확인", totalCost.toString())
        }

        myRef.addValueEventListener(object: ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val menu = snapshot.child("menu")
                val test = snapshot.child("menu").child("burger")
//                val testList = menu.getValue(MenuListType::class.java)

                Log.d("확인용", menu.value.toString())
//                Log.d("확인용", testList.toString())

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


        binding.orderSet.setOnClickListener {
            //Dialog 만들기
            val mDialogView = LayoutInflater.from(this).inflate(R.layout.set_choice, null)
            val mBuilder = AlertDialog.Builder(this)
                .setView(mDialogView)
//                .setTitle("세트 주문하기")

            val mAlertDialog = mBuilder.show()
            mAlertDialog.window?.setLayout(1200,WindowManager.LayoutParams.WRAP_CONTENT)

            val btnSet1 = mDialogView.findViewById<LinearLayout>(R.id.set1)
            btnSet1.setOnClickListener {
               //버튼 클릭시 커스텀프래그먼트 다이얼로그 실행
               val dialog = Dialog9500Fragment()
                setDataAtFragment(dialog, burger, side, beverage)
            }

            val noButton = mDialogView.findViewById<Button>(R.id.cancel_button)
            noButton.setOnClickListener {
                mAlertDialog.dismiss()
            }
        }
    }

    private fun setDataAtFragment(fragment:DialogFragment, burger:ArrayList<MenuType>, side:ArrayList<MenuType>, beverage:ArrayList<MenuType>) {
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
}