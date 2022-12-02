package com.example.bunkerburgerblind

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class RecyclerUserAdapter(private val items: ArrayList<MenuType>) : RecyclerView.Adapter<RecyclerUserAdapter.ViewHolder>() {

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: RecyclerUserAdapter.ViewHolder, position: Int) {
        val item = items[position]
        val listener = View.OnClickListener { it ->
            Toast.makeText(it.context, "Clicked -> name : ${item.name}, price : ${item.price}", Toast.LENGTH_SHORT).show()
        }
        Log.d("어댑터", items.size.toString())
        holder.apply {
            bind(listener, item)
            itemView.tag = item
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflatedView = LayoutInflater.from(parent.context).inflate(R.layout.menu_view, parent, false)
        return RecyclerUserAdapter.ViewHolder(inflatedView)
    }

    // 각 항목에 필요한 기능을 구현
    class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        private var view: View = v
        private var name: TextView = view.findViewById(R.id.menu_name)
        private var price: TextView = view.findViewById(R.id.price)
        private var examination: TextView = view.findViewById(R.id.menu_examination)
        private var imageView: ImageView = view.findViewById(R.id.menu_img)
        fun bind(listener: View.OnClickListener, item: MenuType) {
            name.text = item.name
            price.text = item.price.toString()
            examination.text = item.examination
            Glide.with(itemView).load(item.img).into(imageView);
            view.setOnClickListener(listener)
        }
    }
}