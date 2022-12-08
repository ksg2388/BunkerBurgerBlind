package com.example.bunkerburgerblind

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.bunkerburgerblind.databinding.InventoryStatusItemBinding
import com.example.bunkerburgerblind.databinding.InventoryStatusMainBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase

class InventoryStatusActivity : AppCompatActivity() {
    val database = Firebase.database
    val myRef = database.getReference("bunkerburger")

    lateinit var goBack: Button
    var inventoryHashMap = HashMap<String, Any>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = InventoryStatusMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 뒤로가기
        goBack = findViewById(R.id.goback)
        goBack.setOnClickListener {
            finish()
        }

        // 다이얼로그
        val dialog = InventoryDialog(this)
        dialog.myDig()

        // 재고 버튼
        val data = mutableListOf<String>()
//
//       data.add("빵")
//        data.add("패티")
//        data.add("청상추")
//        data.add("토마토")
//        data.add("체다치즈")
//        data.add("하우스 소스")
//        data.add("마요네즈")

        val arr = mutableListOf<String>()           //재료 데이터 임시 저장
        var ingredients: MutableList<String>   //중복 제거된 재료 데이터
        // 재료 데이터 받아오기
        myRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {

                Log.e("재료 데이터", "test")

                // burger - examination
                val burgerdata = snapshot.child("menu").child("burger")
                for (item in burgerdata.children) {
                    val burger = item.getValue(MenuType::class.java)
                    val ary = burger?.examination?.split(", ")
                    if (ary != null) { for (i in ary) { arr.add(i) } } }

                // side - examination
                val sidedata = snapshot.child("menu").child("side")
                for (item in sidedata.children) {
                    val side = item.getValue(MenuType::class.java)
                    val ary = side?.examination?.split(", ")
                    if (ary != null) { for (i in ary) { arr.add(i) } } }

                // drink - examination
                val drinkdata = snapshot.child("menu").child("beverage")
                for (item in drinkdata.children) {
                    val drink = item.getValue(MenuType::class.java)
                    val ary = drink?.examination?.split(", ")
                    if (ary != null) { for (i in ary) { arr.add(i) } } }

                // 재료 중복 제거
                ingredients= arr.distinct() as ArrayList<String>
                //재고 이름 넣기
                data.addAll(ingredients)
                Log.e("data", data.toString())
                binding.inventoryRecyclerView.adapter = InventoryAdapter(data)

            }
            override fun onCancelled(error: DatabaseError) {
                Log.e("DailySales_burger", "실패")
            }
        })

        binding.inventoryRecyclerView.layoutManager = LinearLayoutManager(this)
        //binding.inventoryRecyclerView.adapter = InventoryAdapter(data)

        // 해쉬맵
        inventoryHashMap.put("빵", "클래식버거")
        inventoryHashMap.put("빵", "더블치즈 해쉬브라운버거")
        inventoryHashMap.put("빵", "BBQ버거")
        inventoryHashMap.put("빵", "오리지날버거")
        inventoryHashMap.put("빵", "맥 앤 치즈버거")
        inventoryHashMap.put("빵", "칠리버거")

    }


    // 뷰 객체 생성
    class InventoryViewHolder(val binding: InventoryStatusItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    class InventoryAdapter(val dataSet: MutableList<String>) :
        RecyclerView.Adapter<RecyclerView.ViewHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
            return InventoryViewHolder(
                InventoryStatusItemBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
        }

        // onCreateViewHolder()에서 반환한 뷰 홀더 객체는 자동으로 onBindViewHolder()의 매개변수로 전달
        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            Log.d("RecyclerView", "onBindViewHolder() : $position")
            val binding = (holder as InventoryViewHolder).binding

            // 뷰에 데이터 출력
            binding.inventoryItemData.text = dataSet[position]

            // 뷰에 이벤트 추가
            binding.inventoryItemRoot.setOnClickListener {
                Log.d("RecyclerView", "item root click: $dataSet")
            }

            binding.inventoryItemData.setOnClickListener {
                Log.d("my", "${dataSet[position]} clicked")
            }
        }

        override fun getItemCount(): Int {
            Log.d("RecyclerView", "init data size: ${dataSet.size}")
            return dataSet.size
        }
    }

    // 다이얼로그 클래스
    class InventoryDialog(context: Context) {
        private val dialog = Dialog(context)

        fun myDig() {
            dialog.show()
        }

        fun MyDig() {
            dialog.setContentView(R.layout.inventory_dialog)
        }
    }


}