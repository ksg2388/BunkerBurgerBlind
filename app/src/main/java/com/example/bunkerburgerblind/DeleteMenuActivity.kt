package com.example.bunkerburgerblind

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import android.view.Window
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.bunkerburgerblind.databinding.DeleteMenuBinding
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlin.math.roundToInt

class DeleteMenuActivity : AppCompatActivity() {
    lateinit var del_img: ImageView
    lateinit var del_name: TextView
    lateinit var btnCancel: Button
    lateinit var btnDelete: Button

    lateinit var database: DatabaseReference

    lateinit var option : String    // edit option : add, modify

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        supportRequestWindowFeature(Window.FEATURE_NO_TITLE)

        val binding = DeleteMenuBinding.inflate(layoutInflater)
        setContentView(binding.root)

        del_img = findViewById(R.id.del_menu_img)
        del_name = findViewById(R.id.del_menu_name)

        // 데이터 채워넣기
        val intentImgSrc = intent.getStringExtra("imgSrc")
        val intentName = intent.getStringExtra("name")
        val type = intent.getStringExtra("type")
        var dirty = intent.getIntExtra("dirty", 0)

        database = Firebase.database.reference
        val datapath = database.child("bunkerburger").child("menu").child(type.toString()).child(intentName.toString())
        Log.d("my", datapath.toString())
        Log.d("my", "del intent name : ${intentName}")
        Glide.with(this)
            .load(intentImgSrc)
            .into(del_img)
        del_name.setText(intentName)

        if (type.equals("burger")) {
            dirty = 0
        } else if (type.equals("side")) {
            dirty = 1
        } else if (type.equals("beverage")) {
            dirty = 2
        }

        // 닫기
        btnCancel = findViewById(R.id.del_cancle)
        btnCancel.setOnClickListener {
            finish()
        }

        // 삭제하기
        btnDelete = findViewById(R.id.del_submit)
        btnDelete.setOnClickListener {
            datapath.setValue(null) // 데이터 삭제
            option = "delete"
            intent.putExtra("name", del_name.text.toString())
            intent.putExtra("option", option)
            intent.putExtra("dir", dirty)
            setResult(RESULT_OK, intent)
            finish()        }
    }
}