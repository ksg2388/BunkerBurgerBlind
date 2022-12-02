package com.example.bunkerburgerblind

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class ManMainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.man_main)

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