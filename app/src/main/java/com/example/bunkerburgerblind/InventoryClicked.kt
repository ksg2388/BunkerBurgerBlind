package com.example.bunkerburgerblind

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Window
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.bunkerburgerblind.databinding.InventoryDialogBinding
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase


class InventoryClicked : AppCompatActivity() {
    lateinit var menu_name: TextView        //메뉴이름
    lateinit var menu_stock: TextView       //보유량
    lateinit var using: TextView            //사용된 재료
    lateinit var orderquantity: EditText    //주문 할 수량
    lateinit var makeorder: Button          //주문하기 버튼

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        supportRequestWindowFeature(Window.FEATURE_NO_TITLE)

        val binding = InventoryDialogBinding.inflate(layoutInflater)
        setContentView(binding.root)

        menu_name = findViewById(R.id.using_name)
        menu_stock = findViewById(R.id.using_stock)
        using = findViewById(R.id.using_menu)
        orderquantity = findViewById(R.id.order_quantity)
        makeorder = findViewById(R.id.makeOrder)

        val intentName = intent.getStringExtra("inventory_name")
        val intentExamination = intent.getStringExtra("inventory_examination")
        val intentStock = intent.getStringExtra("inventory_stock")
        val intentType = intent.getStringExtra("inventory_type")!!
        var ordercount: String
        val datapath =
            Firebase.database.reference.child("bunkerburger").child("menu").child(intentType)
                .child(intentName.toString())


        menu_name.setText("${intentName} 사용 현황")
        using.setText(intentExamination)

        if (intentStock != null)
            menu_stock.setText("보유량 : ${intentStock}")


        makeorder.setOnClickListener {
            ordercount = orderquantity.text.toString()
            if (orderquantity.text.toString().equals("") || orderquantity.text.toString() == null || orderquantity.text.toString().equals("0")) {
                Log.e("edittext", "null")
                //종료
                Toast.makeText(this@InventoryClicked, "주문이 취소되었습니다.", Toast.LENGTH_LONG).show()
                finish()
            }
            else {
                datapath.child("stock").setValue(intentStock.toString().toInt() + ordercount.toInt())
                Toast.makeText(this@InventoryClicked, "주문이 완료되었습니다.", Toast.LENGTH_LONG).show()
                finish()
            }
        }


    }
}