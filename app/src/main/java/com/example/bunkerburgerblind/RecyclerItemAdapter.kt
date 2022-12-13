package com.example.bunkerburgerblind

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.bunkerburgerblind.databinding.ItemMenuListRecyclerviewBinding
class RecyclerUserAdapter(
    private val items: ArrayList<MenuType>,
    var link: Dialog9500Fragment.TestShoppingBag
) : RecyclerView.Adapter<RecyclerUserAdapter.ViewHolder>() {

    private var selectedPosition = 0


    override fun getItemCount(): Int = items.size


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemMenuListRecyclerviewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        Log.d("어댑터", "연결!!")
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Log.d("어댑터", items[0].name)
        val item = items[position]
        val listener = View.OnClickListener { it ->
            Toast.makeText(it.context, "Clicked -> name : ${item.name}, price : ${item.price}", Toast.LENGTH_SHORT).show()
        }
        holder.bind(item, listener)
    }

    // 각 항목에 필요한 기능을 구현
    inner class ViewHolder(private val binding: ItemMenuListRecyclerviewBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: MenuType, listener: View.OnClickListener) {
            binding.menuName.text = item.name
            binding.menuPrice.text = "+${item.price - 6500}"
            binding.menuExamination.text = item.examination
            binding.menuExamination.isSelected = true
            Glide.with(itemView).load(item.img).into(binding.menuImg)
            binding.root.setOnClickListener(listener)

            binding.menuCheckbox.isChecked = adapterPosition == selectedPosition
            binding.menuCheckbox.setOnClickListener {
                Toast.makeText(it.context, "${item.name}이(가) 선택되었습니다.", Toast.LENGTH_SHORT).show()
                selectedPosition = adapterPosition
                link.getSelectedIndex(adapterPosition)
                notifyDataSetChanged()
            }

            Log.d("포지션", adapterPosition.toString())
            Log.d("포지션", selectedPosition.toString())
        }
    }


}
