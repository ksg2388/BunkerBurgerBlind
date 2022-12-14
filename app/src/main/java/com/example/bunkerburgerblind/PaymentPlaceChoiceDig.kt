package com.example.bunkerburgerblind

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.view.WindowManager
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.bumptech.glide.Glide
import com.example.bunkerburgerblind.databinding.PaymentSucceedDialogBinding
import com.example.bunkerburgerblind.databinding.PlaceChooseDialogBinding


class PaymentPlaceChoiceDig(context: Context){
    private val dialog = Dialog(context)
    val dialog2 = PaymentHowChoiceDig(context)

    private lateinit var binding: PlaceChooseDialogBinding


    fun PPCDig(SBList: ArrayList<shopping_basket_data>){
        binding = PlaceChooseDialogBinding.inflate(dialog.layoutInflater)

        dialog.setContentView(binding.root)

        dialog.window!!.setLayout(WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.WRAP_CONTENT)
        dialog.setCanceledOnTouchOutside(false)
        dialog.setCancelable(true)

        binding.atHouse.setOnClickListener {
            dialog.dismiss()
            dialog2.PHCDig(SBList)
        }

        binding.atShop.setOnClickListener {
            dialog.dismiss()
            dialog2.PHCDig(SBList)
        }

        binding.cancel.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()


    }

    interface ButtonClickListener{
        fun onClicked()
    }

    private lateinit var onClickListener: ButtonClickListener

    fun setOnClickListener(listener: ButtonClickListener){
        onClickListener = listener
    }
}