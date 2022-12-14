package com.example.bunkerburgerblind

import android.app.Dialog
import android.content.Context
import android.view.WindowManager
import com.example.bunkerburgerblind.databinding.PaymentHowBinding
import com.example.bunkerburgerblind.databinding.PlaceChooseDialogBinding

class PaymentHowChoiceDig(context: Context) {
    private val dialog = Dialog(context)
    val cardDialog = PaymentCardDig(context)
    val cashDialog = PaymentCashDig(context)

    private lateinit var binding: PaymentHowBinding

    //private lateinit var onClickListener: PaymentPlaceChoiceDig.ButtonClickListener

    fun PHCDig(SBList: ArrayList<shopping_basket_data>){

        binding = PaymentHowBinding.inflate(dialog.layoutInflater)

        dialog.setContentView(binding.root)

        dialog.window!!.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.WRAP_CONTENT)
        dialog.setCanceledOnTouchOutside(false)
        dialog.setCancelable(true)

        dialog.show()

        binding.card.setOnClickListener {
            cardDialog.PCDig(SBList)
            dialog.dismiss()
        }

        binding.cash.setOnClickListener {
            cashDialog.PCashDig(SBList)
            dialog.dismiss()
        }

        binding.cancel.setOnClickListener {
            dialog.dismiss()
        }
    }
}