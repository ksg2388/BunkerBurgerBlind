package com.example.bunkerburgerblind

import android.app.Dialog
import android.content.Context
import android.os.CountDownTimer
import android.view.WindowManager
import com.example.bunkerburgerblind.databinding.HelpDialogBinding

class HelpDig(context: Context) {
    private val dialog = Dialog(context)
    private lateinit var binding: HelpDialogBinding

    fun HPDig(){
        binding = HelpDialogBinding.inflate(dialog.layoutInflater)

        dialog.setContentView(binding.root)

        dialog.window!!.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.WRAP_CONTENT)
        dialog.setCanceledOnTouchOutside(false)
        dialog.setCancelable(true)

        dialog.show()

        start()
    }

    fun start(){

        val countDown = object : CountDownTimer(1000 * 5, 1000) {
            override fun onTick(p0: Long) {
                binding.second.text = (p0 / 1000 + 1).toString()
            }

            override fun onFinish() {
                dialog.dismiss()
            }
        }.start()
    }
}