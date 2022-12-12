package com.example.bunkerburgerblind

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class ListAdapter (val itemList: ArrayList<item_data>): RecyclerView.Adapter<ListAdapter.ViewHolder>(){
    // (1) 아이템 레이아웃과 결합
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_menu, parent, false)
        return ViewHolder(view)
    }
    // (2) 리스트 내 아이템 개수
    override fun getItemCount(): Int {
        return itemList.size
    }
    // (3) View에 내용 입력
    override fun onBindViewHolder(holder: ListAdapter.ViewHolder, position: Int) {
        holder.name.text = itemList[position].name
        holder.price.text = itemList[position].price.toString()
        holder.info.text = itemList[position].examination
        if(itemList[position].stock == 0){
            holder.stock.text = "품절"
            holder.stock.setTextColor(Color.parseColor("#DC143C"))
        }
        else holder.stock.text = itemList[position].stock.toString()

        holder.apply {
            Glide.with(holder.itemView)
                .load(itemList[position].img)
                .into(img)
        }

        holder.name.setSelected(true)
        // (1-1) 리스트 내 항목 클릭 시 onClick() 호출
        holder.itemView.setOnClickListener {
            itemClickListener.onClick(it, position)
        }
    }
    // (4) 레이아웃 내 View 연결
    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val img: ImageView = itemView.findViewById(R.id.img_rv_photo)
        val name: TextView = itemView.findViewById(R.id.tv_rv_name)
        val price: TextView = itemView.findViewById(R.id.tv_rv_price)
        val info: TextView = itemView.findViewById(R.id.tv_rv_menu_info)
        val stock: TextView = itemView.findViewById(R.id.tv_rv_stock)
    }

    // (1-2) 리스너 인터페이스
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