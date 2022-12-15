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
        val orderRef = database.child("bunkerburger").child("order")


        var key = ""
        var id = 0
        var iter = 1

        binding = PaymentSucceedDialogBinding.inflate(dialog.layoutInflater)

        dialog.setContentView(binding.root)
        dialog.window!!.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.WRAP_CONTENT)
        dialog.setCanceledOnTouchOutside(false)
        dialog.setCancelable(true)

        dialog.show()

        orderRef.get().addOnSuccessListener {
            for (ds in it.children){
                key = ds.key as String
            }
            id = key.toInt() + 1

            if(id<10) binding.orderId.text = "00" + id.toString()
            else if(id<100) binding.orderId.text = "0" + id.toString()
        }

        binding.ok.setOnClickListener {
            dialog.dismiss()

            for (item in SBList){
                if(item.type != "set") {
                    myRef.get().addOnSuccessListener {
                        val stock = it.child(item.type).child(item.name[0]).child("stock").value
                        val usage = it.child(item.type).child(item.name[0]).child("usage").value

                        val datapaths = myRef.child(item.type).child(item.name[0])
                        datapaths.child("stock").setValue(stock.toString().toInt() - item.cnt)
                        datapaths.child("usage").setValue(usage.toString().toInt() + item.cnt)
                    }
                    orderRef.get().addOnSuccessListener {
                        for (ds in it.children){
                            key = ds.key as String
                        }
                        id = key.toInt() + 1
                        it.child("category")
                    }
                    val order = hashMapOf(
                        "category" to item.type,
                        "name" to item.name[0],
                        "orderId" to id,
                        "salePrice" to item.price,
                        "cnt" to item.cnt
                    )
                    orderRef.child(id.toString()).child(iter.toString()).setValue(order)
                }
                else{
                    myRef.get().addOnSuccessListener {
                        val stock = it.child("burger").child(item.name[1]).child("stock").value
                        val usage = it.child("burger").child(item.name[1]).child("usage").value

                        val datapaths = myRef.child("burger").child(item.name[1])
                        datapaths.child("stock").setValue(stock.toString().toInt() - 1)
                        datapaths.child("usage").setValue(usage.toString().toInt() + 1)
                    }

                    myRef.get().addOnSuccessListener {
                        val stock = it.child("side").child(item.name[2]).child("stock").value
                        val usage = it.child("side").child(item.name[2]).child("usage").value

                        val datapaths = myRef.child("side").child(item.name[2])
                        datapaths.child("stock").setValue(stock.toString().toInt() - 1)
                        datapaths.child("usage").setValue(usage.toString().toInt() + 1)
                    }

                    myRef.get().addOnSuccessListener {
                        val stock = it.child("beverage").child(item.name[3]).child("stock").value
                        val usage = it.child("beverage").child(item.name[3]).child("usage").value

                        val datapaths = myRef.child("beverage").child(item.name[3])
                        datapaths.child("stock").setValue(stock.toString().toInt() - 1)
                        datapaths.child("usage").setValue(usage.toString().toInt() + 1)
                    }

                    val order = hashMapOf(
                        "category" to item.type,
                        "name" to item.name[0],
                        "orderId" to id,
                        "salePrice" to item.price,
                        "cnt" to item.cnt
                    )
                    orderRef.child(id.toString()).child(iter.toString()).setValue(order)
                }
                iter++
            }

            SBList.clear()

            val intent = Intent(dialog.context, MainActivity::class.java)
            dialog.context.startActivity(intent)
        }

    }
}