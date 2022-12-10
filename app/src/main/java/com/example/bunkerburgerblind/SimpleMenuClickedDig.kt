package com.example.bunkerburgerblind

import android.app.Dialog
import android.content.Context
import android.view.WindowManager
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide

class SimpleMenuClickedDig(context: Context){
    private val dialog = Dialog(context)

    private lateinit var onClickListener: ButtonClickListener

    fun SMDig(itemData: item_data){
        dialog.setContentView(R.layout.simple_menu_clicked_dialog)

        dialog.window!!.setLayout(WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.WRAP_CONTENT)
        dialog.setCanceledOnTouchOutside(false)
        dialog.setCancelable(true)

        dialog.show()

        val name = dialog.findViewById<TextView>(R.id.text_sm_name)
        val price = dialog.findViewById<TextView>(R.id.text_sm_price)
        val info = dialog.findViewById<TextView>(R.id.text_sm_info)
        val btnPut = dialog.findViewById<Button>(R.id.btn_sm_put)
        val btnCancel = dialog.findViewById<Button>(R.id.btn_sm_cancel)
        val uparrow = dialog.findViewById<Button>(R.id.uparrow)
        val downarrow = dialog.findViewById<Button>(R.id.downarrow)
        val count = dialog.findViewById<TextView>(R.id.count)
        val img = dialog.findViewById<ImageView>(R.id.img_sm_photo)
        var text = ""
        var cnt = 0

        Glide.with(dialog.context)
            .load(itemData.img)
            .into(img)
        name.setText(itemData.name)
        price.setText(itemData.price.toString())
        info.setText(itemData.examination)

        btnPut.setOnClickListener{
            text = count.getText() as String
            cnt = text.toInt()
            onClickListener.PutSBonClick(cnt) //개수 전달
            dialog.dismiss()
        }

        uparrow.setOnClickListener {
            text = count.getText() as String
            cnt = text.toInt()
            if(cnt+1 <= 1) downarrow.isEnabled = false
            else downarrow.isEnabled = true
            cnt = cnt + 1
            count.setText(cnt.toString())
        }

        downarrow.setOnClickListener {
            text = count.getText() as String
            cnt = text.toInt()
            if(cnt-1 <= 1) downarrow.isEnabled = false
            else downarrow.isEnabled = true
            cnt = cnt - 1
            count.setText(cnt.toString())
        }

        btnCancel.setOnClickListener{
            dialog.dismiss()
        }
    }

    interface ButtonClickListener{
        fun PutSBonClick(text: Int)
    }

    fun setOnClickedListener(listener: ButtonClickListener){
        onClickListener = listener
    }
}