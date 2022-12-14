package com.example.bunkerburgerblind

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.WindowManager
import com.example.bunkerburgerblind.databinding.PaymentSucceedDialogBinding
import com.example.bunkerburgerblind.databinding.ShoppingBasketDialogBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import java.security.AccessController.getContext

class PaymentSucceedDig(context: Context) {
    private val dialog = Dialog(context)
    lateinit var menuActivity: SimpleMenuActivity

    private lateinit var binding: PaymentSucceedDialogBinding
    lateinit var database: DatabaseReference

    private lateinit var onClickListener: PaymentPlaceChoiceDig.ButtonClickListener

    fun PHCDig(SBList: ArrayList<shopping_basket_data>){
        database = Firebase.database.reference
        val myRef = database.child("bunkerburger").child("menu")

        binding = PaymentSucceedDialogBinding.inflate(dialog.layoutInflater)

        dialog.setContentView(binding.root)
        dialog.window!!.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.WRAP_CONTENT)
        dialog.setCanceledOnTouchOutside(false)
        dialog.setCancelable(true)

        dialog.show()

        binding.ok.setOnClickListener {
            dialog.dismiss()

            for (item in SBList){
                println(item.stock)
                println(item.stock - item.cnt)
                myRef.addValueEventListener(object: ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        val menu = snapshot.child("menu")

                        val st = menu.child(item.type).child(item.name[0]).child("stock").value
                        println(st)
                    }

                    override fun onCancelled(error: DatabaseError) {
                        Log.e("실패", "실패")
                    }
                })
                val datapaths = myRef.child(item.type).child(item.name[0])
                datapaths.child("stock").setValue(600)
            }
            SBList.clear()

            val intent = Intent(dialog.context, MainActivity::class.java)
            dialog.context.startActivity(intent)
        }

    }
}