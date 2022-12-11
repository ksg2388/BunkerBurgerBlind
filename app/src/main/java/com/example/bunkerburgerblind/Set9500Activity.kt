package com.example.bunkerburgerblind

import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout;

class Set9500Activity: AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.set_9500)

        val adaptor = PagerAdaptor(supportFragmentManager)
        val viewPager = findViewById<ViewPager>(R.id.viewpager)
        val tapLayout = findViewById<TabLayout>(R.id.tabLayout)
        val burgerList = intent.getSerializableExtra("burger") as ArrayList<MenuType>
        val sideList = intent.getSerializableExtra("side") as ArrayList<MenuType>
        val beverageList = intent.getSerializableExtra("beverage") as ArrayList<MenuType>

        Log.d("확인용", burgerList[0].name)
        Log.d("확인용", sideList[0].name)
        Log.d("확인용", beverageList[0].name)
        adaptor.addFragment(BurgerFragment(), "햄버거")
        adaptor.addFragment(SideFragment(), "사이드")
        adaptor.addFragment(BeverageFragment(), "음료")
        viewPager.adapter = adaptor
        tapLayout.setupWithViewPager(viewPager)


        val btnReturn = findViewById<Button>(R.id.btnReturn)
        btnReturn.setOnClickListener { finish() }
    }
}