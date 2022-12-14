package com.example.bunkerburgerblind

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.view.WindowManager
import com.example.bunkerburgerblind.databinding.PaymentSucceedDialogBinding
import com.example.bunkerburgerblind.databinding.ShoppingBasketDialogBinding
import java.security.AccessController.getContext

class PaymentSucceedDig(context: Context) {
    private val dialog = Dialog(context)
    lateinit var menuActivity: SimpleMenuActivity

    private lateinit var binding: PaymentSucceedDialogBinding

    private lateinit var onClickListener: PaymentPlaceChoiceDig.ButtonClickListener

    fun PHCDig(SBList: ArrayList<shopping_basket_data>){
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
            SBList.clear()


            val intent = Intent(dialog.context, MainActivity::class.java)
            dialog.context.startActivity(intent)
        }

    }
}