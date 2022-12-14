package com.example.bunkerburgerblind

import android.app.Dialog
import android.content.Context
import android.view.WindowManager
import com.example.bunkerburgerblind.databinding.CardPaymentDialogBinding
import com.example.bunkerburgerblind.databinding.PaymentDialogBinding
import com.example.bunkerburgerblind.databinding.PaymentHowBinding

class PaymentCashDig (context: Context){
    private val dialog = Dialog(context)
    val dig2 = PaymentSucceedDig(context)

    private lateinit var binding: PaymentDialogBinding

    //private lateinit var onClickListener: PaymentPlaceChoiceDig.ButtonClickListener

    fun PCashDig(SBList: ArrayList<shopping_basket_data>){
        binding = PaymentDialogBinding.inflate(dialog.layoutInflater)

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

        if(sum>binding.input.text.toString().toInt()) binding.ok.isEnabled = false

        binding.thtest.setOnClickListener {
            binding.input.text= (binding.input.text.toString().toInt() + 1000).toString()

            if(sum>binding.input.text.toString().toInt()) binding.ok.isEnabled = false
            else binding.ok.isEnabled = true

            if(binding.input.text.toString().toInt()>binding.price.text.toString().toInt())
                binding.remain.text = (binding.input.text.toString().toInt() - binding.price.text.toString().toInt()).toString()

        }

        binding.mitest.setOnClickListener {
            binding.input.text= (binding.input.text.toString().toInt() + 10000).toString()
            if(sum>binding.input.text.toString().toInt()) binding.ok.isEnabled = false
            else binding.ok.isEnabled = true

            if(binding.input.text.toString().toInt()>binding.price.text.toString().toInt())
                binding.remain.text = (binding.input.text.toString().toInt() - binding.price.text.toString().toInt()).toString()

        }

        binding.ok.setOnClickListener {
            dig2.PHCDig(SBList)
            dialog.dismiss()
        }

        binding.cancel.setOnClickListener {
            dialog.dismiss()
        }

    }
}