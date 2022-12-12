package com.example.bunkerburgerblind

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.bunkerburgerblind.databinding.EditMenuDetailBinding
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase


class EditMenuActivity : AppCompatActivity() {
    lateinit var submit_img : Button
    lateinit var submit_examination : Button
    lateinit var submit_name : Button
    lateinit var submit_price : Button

    lateinit var edit_img : ImageView
    lateinit var edit_examination : EditText
    lateinit var edit_name : EditText
    lateinit var edit_price : EditText

    lateinit var goBack : Button
    lateinit var submit : Button

    lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = EditMenuDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        edit_img = findViewById(R.id.edit_menu_img)
        edit_examination = findViewById(R.id.edit_menu_examination)
        edit_name = findViewById(R.id.edit_menu_name)
        edit_price = findViewById(R.id.edit_menu_price)

        submit_img = findViewById(R.id.edit_menu_img_submit)
        submit_examination = findViewById(R.id.edit_menu_ex_submit)
        submit_name = findViewById(R.id.edit_menu_name_submit)
        submit_price = findViewById(R.id.edit_menu_price_submit)

        // 수정하려는 경우 (데이터 채워넣기)
        val intentImgSrc = intent.getStringExtra("imgSrc")
        val intentEx = intent.getStringExtra("examination")
        val intentName = intent.getStringExtra("name")
        val intentPrice = intent.getStringExtra("price")
        val type = intent.getStringExtra("type")

        database = Firebase.database.reference
        val datapath = database.child("bunkerburger").child("menu").child(type.toString()).child(intentName.toString())

        if (intentEx != null)


        if (intentImgSrc != null) {
            Glide.with(this)
                .load(intentImgSrc)
                .into(edit_img)
        }
        if (intentEx != null)
            edit_examination.setText(intentEx)
        if (intentName != null)
            edit_name.setText(intentName)
        if (intentPrice != null)
            edit_price.setText(intentPrice.toString())



        submit_examination.setOnClickListener {
            val input = edit_examination.text.toString()
            edit_examination.setText(input)
            Log.d("my", "edit : ${edit_examination.text.toString()}")
        }

        submit_name.setOnClickListener {
            val input = edit_name.text.toString()
            edit_name.setText(input)
            Log.d("my", "name : ${edit_name.text.toString()}")
        }

        submit_price.setOnClickListener {
            val input = edit_price.text.toString()
            edit_price.setText(input)
            Log.d("my", "price : ${edit_price.text.toString()}")
        }


        // 뒤로가기
        goBack = findViewById(R.id.goback)
        goBack.setOnClickListener {
            finish()
        }

        // 저장하기 - 데이터베이스 업데이트
        submit = findViewById(R.id.submit)
        submit.setOnClickListener {
            // 이미지 업데이트 추가 구현 필요 !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
            datapath.child("examination").setValue(edit_examination.text.toString())
            datapath.child("name").setValue(edit_name.text.toString())
            datapath.child("price").setValue(edit_price.text.toString().toInt())
            // finish()
            Log.d("my", "저장하기")


        }

    }
}