package com.example.bunkerburgerblind

import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.bunkerburgerblind.databinding.EditMenuDetailBinding

class EditMenuActivity : AppCompatActivity() {
    lateinit var goBack : Button
    lateinit var submit : Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = EditMenuDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 뒤로가기
        goBack = findViewById(R.id.goback)
        goBack.setOnClickListener {
            finish()
        }

        // 저장하기 - 데이터베이스 업데이트

    }
}