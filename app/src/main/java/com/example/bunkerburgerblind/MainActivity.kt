package com.example.bunkerburgerblind

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {
    private val database = Firebase.database
    private val myRef = database.getReference("bunkerburger")

    //관리자 모드
    var man_pw : String = "pw0" // 관리자모드 전환 비밀번호
    lateinit var inputPWBox : EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.man_pwd)

        var alertDialog = AlertDialog.Builder(this)

        inputPWBox = findViewById(R.id.manPwdInput)

        val submitPW = findViewById<Button>(R.id.manPwdSubmit)
        submitPW.setOnClickListener {
            // 비밀번호 비교
            val inputPW = inputPWBox.text.toString() // 입력한 비밀번호를 inputPW에 저장
            Log.d("my", "answer : ${man_pw}  -- inputString : ${inputPW}")

            if (man_pw.equals(inputPW)) {
                // 입력한 비밀번호가 일치할 경우
                val intent = Intent(this, ManMainActivity::class.java)
                startActivity(intent)
            } else {
                // 입력한 비밀번호가 일치하지 않을 경우
                alertDialog.setTitle("비밀번호 오류")
                alertDialog.setIcon(R.drawable.logo)
                alertDialog.setMessage("비밀번호가 틀렸습니다. 다시 입력해주세요.")
                alertDialog.setNegativeButton("닫기", null)
                alertDialog.show()
                inputPWBox.setText("")
            }
        }

        Log.d("테스트", myRef.toString())

        myRef.addValueEventListener(object: ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val test = snapshot.child("menu")
                Log.d("테스트", "ttt")
                for (ds in test.children) {
                    Log.e("스냅", ds.toString())
                    Log.d("테스트", "test")
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("실패", "실패")
            }
        })
    }

}