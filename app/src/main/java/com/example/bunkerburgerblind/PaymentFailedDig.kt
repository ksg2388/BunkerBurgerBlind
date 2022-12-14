package com.example.bunkerburgerblind

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.view.WindowManager
import com.example.bunkerburgerblind.databinding.PaymentFailedDialogBinding
import com.example.bunkerburgerblind.databinding.PaymentSucceedDialogBinding
import com.example.bunkerburgerblind.databinding.ShoppingBasketDialogBinding
import java.security.AccessController.getContext

class PaymentFailedDig(context: Context) {
    private val dialog = Dialog(context)
    lateinit var menuActivity: SimpleMenuActivity

    private lateinit var binding: PaymentFailedDialogBinding

    private lateinit var onClickListener: PaymentPlaceChoiceDig.ButtonClickListener

    fun PFDig(SBList: ArrayList<shopping_basket_data>){
        binding = PaymentFailedDialogBinding.inflate(dialog.layoutInflater)

        dialog.setContentView(binding.root)
        dialog.window!!.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.WRAP_CONTENT)
        dialog.setCanceledOnTouchOutside(false)
        dialog.setCancelable(true)

        dialog.show()

        binding.ok.setOnClickListener {
            dialog.dismiss()
        }
    }
}