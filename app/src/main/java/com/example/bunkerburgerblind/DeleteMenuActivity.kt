package com.example.bunkerburgerblind

import android.app.Dialog
import android.view.WindowManager
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class DeleteMenuClickDig(context: ManageMenuActivity.ManageAdapter) {
    private val dialog = Dialog(ManageMenuActivity.ManageAdapter)

    fun DMDig(itemData: ManagemenuView) {
        dialog.setContentView(R.layout.delete_menu_dialog)
        dialog.window!!.setLayout(WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.WRAP_CONTENT)
        dialog.setCanceledOnTouchOutside(false)
        dialog.setCancelable(true)

        dialog.show()

        val img = dialog.findViewById<ImageView>(R.id.del_menu_img)
        val name = dialog.findViewById<TextView>(R.id.del_menu_name)
        val price = dialog.findViewById<TextView>(R.id.del_menu_price)
        val info = dialog.findViewById<TextView>(R.id.del_menu_examination)
        val btnDelete = dialog.findViewById<Button>(R.id.del_menu_submit)
        val btnCancel = dialog.findViewById<Button>(R.id.del_menu_cancel)
        val type : String

        Glide.with(dialog.context)
            .load(itemData.image)
            .into(img)

        name.setText(itemData.name)
        price.setText(itemData.price.toString())
        info.setText(itemData.examination)
        type = itemData.type

        val datapath = Firebase.database.reference.child("bunkerburger").child("menu").child(type.toString()).child(name.toString())

        btnDelete.setOnClickListener {
            datapath.setValue(null) // 데이터 삭제
            dialog.dismiss()
        }

        btnCancel.setOnClickListener {
            dialog.dismiss()
        }
    }
}