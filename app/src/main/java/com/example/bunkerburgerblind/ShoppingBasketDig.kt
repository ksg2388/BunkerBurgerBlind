package com.example.bunkerburgerblind

import android.app.Dialog
import android.content.Context
import android.view.View
import android.view.WindowManager
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.bunkerburgerblind.databinding.ShoppingBasketDialogBinding

class ShoppingBasketDig(context: Context, val itemList: ArrayList<shopping_basket_data>) {
    private val dialog = Dialog(context)
    private lateinit var binding: ShoppingBasketDialogBinding
    private lateinit var adapter:SBListAdapter

    fun SBDig(){
        binding = ShoppingBasketDialogBinding.inflate(dialog.layoutInflater)
        adapter=SBListAdapter(itemList) //어댑터 객체 만듦
        binding.RvMenu.adapter=adapter //리사이클러뷰에 어댑터 연결
        binding.RvMenu.layoutManager=GridLayoutManager(dialog.context,2) //레이아웃 매니저 연결

        dialog.setContentView(binding.root)

        dialog.window!!.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.MATCH_PARENT)
        dialog.setCanceledOnTouchOutside(false)
        dialog.setCancelable(true)

        dialog.show()

        ClickEvent()

        adapter.notifyDataSetChanged()

        SetPrice()
    }

    interface CallbackListener{
        fun onClicked()
    }

    private lateinit var onClickListener: CallbackListener

    fun setOnClickListener(listener: CallbackListener){
        onClickListener = listener
    }

    fun SetPrice(){
        var sum = 0
        if(itemList.isEmpty()==true)
            binding.price.setText("0")
        else{
            for (item in itemList){
                sum = sum + item.price.toInt() * item.cnt
            }
            binding.price.setText(sum.toString())
        }
    }

    fun ClickEvent(){
        binding.btnSbBuy.setOnClickListener{
            onClickListener.onClicked()
            dialog.dismiss()
        }

        binding.btnSbCancel.setOnClickListener{
            onClickListener.onClicked()
            dialog.dismiss()
        }

        adapter.setItemClickListener(object: SBListAdapter.OnItemClickListener{
            override fun onClick(v: View, position: Int) {
                // 클릭 시 이벤트 작성
                itemList.removeAt(position)
                adapter.notifyDataSetChanged()
                SetPrice()
            }
        })
    }
}