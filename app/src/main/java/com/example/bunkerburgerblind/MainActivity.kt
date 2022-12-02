package com.example.bunkerburgerblind

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {
    private val database = Firebase.database
    private val myRef = database.getReference("bunkerburger")


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        val menuList: RecyclerView = findViewById(R.id.listMenu)
        val list = ArrayList<MenuType>()


//        Glide.with(this).load("https://ldb-phinf.pstatic.net/20220107_149/1641535879331Y3VVF_JPEG/m7-tZh3kBAfKUHokT7UsZyZWa4GkX4MXrbfUhwG4ggIEA6OlRRPP757-ZSotLIJa.jpg").into(imageView1);

        myRef.addValueEventListener(object: ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {
                val menu = snapshot.child("menu")
                val test = snapshot.child("menu").child("burger")

                for (item in menu.child("beverage").children) {
                    Log.d("로그", item.toString())
                    val burger = item.getValue(MenuType::class.java)
                    burger?.let {
                        Log.d("햄버거 id", it.id.toString())
                        Log.d("햄버거 이름", it.name)
                        Log.d("햄버거 가격", it.price.toString())
                        Log.d("햄버거 설명", it.stock.toString())}
                    burger?.let { list.add(it) }
                }

                val adapter = RecyclerUserAdapter(list)
                menuList.adapter = adapter
            }


            override fun onCancelled(error: DatabaseError) {
                Log.e("실패", "실패")
            }
        })
    }
}
