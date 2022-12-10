package com.example.bunkerburgerblind

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.bunkerburgerblind.R

class SBListAdapter(val itemList: ArrayList<shopping_basket_data>): RecyclerView.Adapter<SBListAdapter.ViewHolder>() {
    // (1) 아이템 레이아웃과 결합
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SBListAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_sv, parent, false)
        return ViewHolder(view)
    }
    // (2) 리스트 내 아이템 개수
    override fun getItemCount(): Int {
        return itemList.size
    }
    // (3) View에 내용 입력
    override fun onBindViewHolder(holder: SBListAdapter.ViewHolder, position: Int) {
        holder.name.text = itemList[position].name
        holder.price.text = itemList[position].price
        holder.cnt.text = itemList[position].cnt.toString()

        holder.apply {
            Glide.with(holder.itemView)
                .load(itemList[position].img)
                .into(img)
        }

        holder.name.setSelected(true)

        // (1) 리스트 내 항목 클릭 시 onClick() 호출
        holder.cancel.setOnClickListener {
            itemClickListener.onClick(it, position)
        }
    }
    // (4) 레이아웃 내 View 연결
    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val img: ImageView = itemView.findViewById(R.id.item_sv_img)
        val name: TextView = itemView.findViewById(R.id.tv_rvsb_name)
        val price: TextView = itemView.findViewById(R.id.tv_rvsb_price)
        val cnt: TextView = itemView.findViewById(R.id.tv_rvsb_cnt)
        val cancel: Button = itemView.findViewById(R.id.bt_remove)
    }

    interface OnItemClickListener {
        fun onClick(v: View, position: Int)
    }
    // (1-3) 외부에서 클릭 시 이벤트 설정
    fun setItemClickListener(onItemClickListener: OnItemClickListener) {
        this.itemClickListener = onItemClickListener
    }
    // (1-4) setItemClickListener로 설정한 함수 실행
    private lateinit var itemClickListener : OnItemClickListener


}