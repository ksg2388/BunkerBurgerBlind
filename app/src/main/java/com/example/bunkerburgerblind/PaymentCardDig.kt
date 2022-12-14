package com.example.bunkerburgerblind

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.view.WindowManager
import android.widget.Toast
import com.example.bunkerburgerblind.databinding.CardPaymentDialogBinding
import com.example.bunkerburgerblind.databinding.PaymentDialogBinding

class PaymentCardDig (context: Context){
    private val dialog = Dialog(context)
    val dig2 = PaymentSucceedDig(context)
    val dig3 = PaymentFailedDig(context)

    private lateinit var binding: CardPaymentDialogBinding

    //private lateinit var onClickListener: PaymentPlaceChoiceDig.ButtonClickListener

    fun PCDig(SBList: ArrayList<shopping_basket_data>){
        binding = CardPaymentDialogBinding.inflate(dialog.layoutInflater)

        dialog.setContentView(binding.root)

        dialog.window!!.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.WRAP_CONTENT)
        dialog.setCanceledOnTouchOutside(false)
        dialog.setCancelable(true)

        dialog.show()

        var sum = 0
        if(SBList.isEmpty()==true) {
            binding.price.setText("0")
        }
        else {
            for (item in SBList) {
                sum = sum + item.price.toInt() * item.cnt
            }
            binding.price.setText(sum.toString())
        }

        binding.ok.setOnClickListener {
            dig2.PHCDig(SBList)
            dialog.dismiss()
        }

        binding.no.setOnClickListener {
            dig3.PFDig(SBList)
            dialog.dismiss()
        }

        binding.cancel.setOnClickListener {
            dialog.dismiss()
        }
    }
}