package com.example.bunkerburgerblind

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.RadioGroup
import androidx.appcompat.app.AppCompatActivity
import com.example.bunkerburgerblind.databinding.EditMenuDetailBinding
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class EditMenuActivity : AppCompatActivity() {

    lateinit var edit_img: EditText
    lateinit var edit_examination: EditText
    lateinit var edit_name: EditText
    lateinit var edit_price: EditText
    lateinit var edit_stock: EditText
    lateinit var edit_usage: EditText
    lateinit var edit_id: EditText

    lateinit var radioGroup: RadioGroup

    lateinit var type: String    // 음식 타입 : burger, side, beverage
    lateinit var option: String    // edit option : add, modify

    lateinit var goInternet: Button
    lateinit var goBack: Button
    lateinit var submit: Button

    lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = EditMenuDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        edit_img = findViewById(R.id.edit_menu_img)
        edit_examination = findViewById(R.id.edit_menu_examination)
        edit_name = findViewById(R.id.edit_menu_name)
        edit_price = findViewById(R.id.edit_menu_price)
        edit_stock = findViewById(R.id.edit_menu_stock)
        edit_usage = findViewById(R.id.edit_menu_usage)
        edit_id = findViewById(R.id.edit_menu_id)

        // 수정하려는 경우 (데이터 채워넣기)
        val intentImgSrc = intent.getStringExtra("imgSrc")
        val intentEx = intent.getStringExtra("examination")
        val intentName = intent.getStringExtra("name")
        val intentPrice = intent.getStringExtra("price")
        val intentStock = intent.getStringExtra("stock")
        val intentUsage = intent.getStringExtra("usage")
        val intentId = intent.getStringExtra("id")
        type = intent.getStringExtra("type").toString()
        var dirty = intent.getIntExtra("dirty", 0)

        if (intentEx != null)
            edit_examination.setText(intentEx)
        if (intentName != null)
            edit_name.setText(intentName)
        if (intentImgSrc != null)
            edit_img.setText(intentImgSrc)
        if (intentPrice != null)
            edit_price.setText(intentPrice.toString())
        if (intentStock != null)
            edit_stock.setText(intentStock.toString())
        if (intentUsage != null)
            edit_usage.setText(intentUsage.toString())
        if (intentId != null)
            edit_id.setText(intentId.toString())

        // 타입 결정 라디오버튼
        radioGroup = findViewById(R.id.edit_radioGroup)
        if (type.equals("burger"))
            radioGroup.check(R.id.edit_type_burger)
        else if (type.equals("side"))
            radioGroup.check(R.id.edit_type_side)
        else if (type.equals("beverage"))
            radioGroup.check(R.id.edit_type_beverage)
        else {
            // type 미결정인 경우 == 메뉴 추가하는 상황!
            radioGroup.setOnCheckedChangeListener { group, checkedId ->
                if (checkedId == R.id.edit_type_burger) {
                    Log.e("my", "메뉴의 타입은 burger입니다")
                    type = "burger"
                } else if (checkedId == R.id.edit_type_side) {
                    Log.e("my", "메뉴의 타입은 side입니다")
                    type = "side"
                } else if (checkedId == R.id.edit_type_beverage) {
                    Log.e("my", "메뉴의 타입은 beverage입니다")
                    type = "beverage"
                }
            }
        }
        Log.d("my", type)

        // 파이어베이스
        database = Firebase.database.reference
        val datapath = database.child("bunkerburger").child("menu")

        // 인터넷 검색
        goInternet = findViewById(R.id.goInternet)
        goInternet.setOnClickListener {
            var uri = Uri.parse("https://www.naver.com")
            var intent = Intent(Intent.ACTION_VIEW, uri)
            startActivity(intent)
        }

        // 뒤로가기
        goBack = findViewById(R.id.goback)
        goBack.setOnClickListener {
            finish()
        }

        // 저장하기
        submit = findViewById(R.id.submit)
        if (intentName != null) {
            // 저장하기 ( 수정 ) - 데이터베이스 업데이트
            submit.setOnClickListener {
                Log.d("my", "clicked")
                val datapaths = datapath.child(type).child(edit_name.text.toString())
                if (edit_name.text.toString().equals(intentName)) {
                    datapaths.child("name").setValue(edit_name.text.toString()) // 이름
                    Log.d("my", "이름 변경 안됨")
                } else {
                    val deldatapath = datapath.child(type).child(intentName)
                    deldatapath.setValue(null) // 데이터 삭제 후 추가
                    datapaths.child("name").setValue(edit_name.text.toString()) // 이름
                    Log.d("my", "이름 변경됨")
                }
                datapaths.child("img").setValue(edit_img.text.toString()) // 사진
                datapaths.child("examination").setValue(edit_examination.text.toString()) // 설명
                datapaths.child("id").setValue(edit_id.text.toString().toInt()) // id
                datapaths.child("price").setValue(edit_price.text.toString().toInt()) // price
                datapaths.child("stock").setValue(edit_stock.text.toString().toInt()) // stock
                datapaths.child("usage").setValue(edit_usage.text.toString().toInt()) // usage
                if (type.equals("burger"))
                    dirty = 0
                else if (type.equals("side"))
                    dirty = 1
                else if (type.equals("beverage"))
                    dirty = 2
                option = "modify"

                intent.putExtra("option", option)
                intent.putExtra("dir", dirty)
                intent.putExtra("name", intentName)
                intent.putExtra("examination", intentEx)
                intent.putExtra("price", intentPrice)
                intent.putExtra("id", intentId)
                intent.putExtra("img", intentImgSrc)
                intent.putExtra("usage", intentUsage)
                intent.putExtra("stock", intentStock)
                setResult(RESULT_OK, intent)
                finish()

                Log.d("my", "저장하기 - 추가")
                Log.d("my", "메뉴 최종 타입 : ${type}")
            }
        } else {
            // 저장하기 ( 추가 ) - 데이터베이스 업데이트
            submit.setOnClickListener {
                // 데이터베이스 업데이트
                datapath.child(type).child(edit_name.text.toString())
                    .setValue(edit_name.text.toString()) // 카테고리 내에서 메뉴 이름으로 카테고리 구분
                val datapaths = datapath.child(type).child(edit_name.text.toString())
                datapaths.child("name").setValue(edit_name.text.toString()) // 이름
                datapaths.child("img").setValue(edit_img.text.toString()) // 사진
                datapaths.child("examination").setValue(edit_examination.text.toString()) // 설명
                datapaths.child("id").setValue(edit_id.text.toString().toInt()) // id
                datapaths.child("price").setValue(edit_price.text.toString().toInt()) // price
                datapaths.child("stock").setValue(edit_stock.text.toString().toInt()) // stock
                datapaths.child("usage").setValue(edit_usage.text.toString().toInt()) // usage

                if (type.equals("burger"))
                    dirty = 0
                else if (type.equals("side"))
                    dirty = 1
                else if (type.equals("beverage"))
                    dirty = 2
                option = "add"


                intent.putExtra("option", option)
                intent.putExtra("dir", dirty)
                intent.putExtra("name", edit_name.text.toString())
                intent.putExtra("examination", intentEx)
                intent.putExtra("price", intentPrice)
                intent.putExtra("id", intentId)
                intent.putExtra("img", intentImgSrc)
                intent.putExtra("usage", intentUsage)
                intent.putExtra("stock", intentStock)
                setResult(RESULT_OK, intent)


                // finish()
                Log.d("my", "저장하기 - 추가")
                Log.d("my", "메뉴 최종 타입 : ${type}")
            }
        }
    }
}