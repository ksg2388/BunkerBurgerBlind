package com.example.bunkerburgerblind

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.appcompat.app.AlertDialog
import com.example.bunkerburgerblind.databinding.ActivityMainBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {
    private val database = Firebase.database
    private val myRef = database.getReference("bunkerburger")

    private lateinit var binding : ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.orderSet.setOnClickListener {
            //Dialog 만들기
            val mDialogView = LayoutInflater.from(this).inflate(R.layout.set_choice, null)
            val mBuilder = AlertDialog.Builder(this)
                .setView(mDialogView)
                .setTitle("세트 주문하기")

            val mAlertDialog = mBuilder.show()

            val noButton = mDialogView.findViewById<Button>(R.id.cancel_button)
            noButton.setOnClickListener {
                mAlertDialog.dismiss()
            }
        }

        // 관리자 페이지로 이동
        val adminBtn = findViewById<Button>(R.id.admin_mode);
        adminBtn.setOnClickListener {
            val intent = Intent(this, ManPWActivity::class.java)
            startActivity(intent)
        }

        myRef.addValueEventListener(object: ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val test = snapshot.child("menu")
                for (ds in test.children) {
                    Log.e("스냅", ds.toString())
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("실패", "실패")
            }
        })
    }

}