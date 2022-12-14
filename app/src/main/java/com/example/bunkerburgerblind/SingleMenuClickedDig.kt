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


class SingleMenuClickedDig(context: Context){
    private val dialog = Dialog(context)

    private lateinit var onClickListener: ButtonClickListener

    fun SMDig(itemData: item_data, SBList: ArrayList<shopping_basket_data>){

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
        val stockInfo = dialog.findViewById<TextView>(R.id.text_sm_stockinfo)
        var text = ""
        var cnt = 0
        var max = 0
        var flag = false
        var inStock = 0

        Glide.with(dialog.context)
            .load(itemData.img)
            .into(img)
        name.setText(itemData.name)
        price.setText(itemData.price.toString())
        info.setText(itemData.examination)

        max = itemData.stock

        for(item in SBList){
            if(item.name.equals(itemData.name[0])){
                flag = true
                inStock = item.cnt
            }
        }

        if(itemData.stock == 0) {
            stockInfo.setTextColor(Color.parseColor("#DC143C"))
            btnPut.isEnabled = false
            count.setText("0")
            uparrow.isEnabled = false
            downarrow.isEnabled = false
        }

        if(flag && inStock >= max) {
            stockInfo.text = "최대 주문 가능 수량에 도달하였습니다."
            stockInfo.setTextColor(Color.parseColor("#DC143C"))
            btnPut.isEnabled = false
            count.setText("0")
            uparrow.isEnabled = false
            downarrow.isEnabled = false
        } else if(flag && inStock + 1 >= max){
            uparrow.isEnabled = false
        }

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

            if(cnt+1 >= max || (flag && cnt+1 + inStock >= max)) {
                val toast = Toast.makeText(
                    dialog.context,
                    "최대 주문 가능 수량에 도달하였습니다.",
                    Toast.LENGTH_SHORT
                )
                toast.show()
                uparrow.isEnabled = false
            }
            else uparrow.isEnabled = true

            cnt = cnt + 1
            count.setText(cnt.toString())
        }

        downarrow.setOnClickListener {
            text = count.getText() as String
            cnt = text.toInt()
            if(cnt-1 <= 1) downarrow.isEnabled = false
            else downarrow.isEnabled = true

            if(cnt-1 >= max || (flag && cnt-1 + inStock >= max)) {
                val toast = Toast.makeText(
                    dialog.context,
                    "최대 주문 가능 수량에 도달하였습니다.",
                    Toast.LENGTH_SHORT
                )
                toast.show()
                uparrow.isEnabled = false
            }
            else uparrow.isEnabled = true

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