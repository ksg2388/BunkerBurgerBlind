package com.example.bunkerburgerblind

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

var burgerList = ArrayList<MenuType>()
var sideList = ArrayList<MenuType>()
var beverageList = ArrayList<MenuType>()

class ManMainActivity : AppCompatActivity() {
    val database = Firebase.database
    val myRef = database.getReference("bunkerburger")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.man_main)
        myRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                // 버거 데이터 받아오기
                val burgerdata = snapshot.child("menu").child("burger")
                for (item in burgerdata.children) {
                    val burgers = item.getValue(MenuType::class.java)
                    if (burgers != null) {
                        burgerList.add(burgers)
                    }
                }

                // 사이드 데이터 받아오기
                val sidedata = snapshot.child("menu").child("side")
                for (item in sidedata.children) {
                    val sides = item.getValue(MenuType::class.java)
                    if (sides != null) {
                        sideList.add(sides)
                    }
                }

                val beveragedata = snapshot.child("menu").child("beverage")
                for (item in beveragedata.children) {
                    val beverages = item.getValue(MenuType::class.java)
                    if (beverages != null) {
                        beverageList.add(beverages)
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("실패", "실패")
            }
        })


        // 1일 판매 수량 보기 페이지로 이동
        val dailySalesBtn = findViewById<Button>(R.id.manDailySalesQuantity)
        dailySalesBtn.setOnClickListener {
            val intent = Intent(this, DailySalesActivity::class.java)
            startActivity(intent)
        }

        // 재고 현황 보기 페이지로 이동
        val inventoryStatus = findViewById<Button>(R.id.manInventoryStatus)
        inventoryStatus.setOnClickListener {
            val intent = Intent(this, InventoryStatusActivity::class.java)
            startActivity(intent)
        }

        // 메뉴 관리하기 페이지로 이동
        val managementMenu = findViewById<Button>(R.id.manManagementMenu)
        managementMenu.setOnClickListener {
            val intent = Intent(this, ManageMenuActivity::class.java)
            startActivity(intent)
        }
    }
}