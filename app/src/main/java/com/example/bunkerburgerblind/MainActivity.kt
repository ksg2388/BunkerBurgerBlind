package com.example.bunkerburgerblind

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.location.LocationListener
import android.location.LocationManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.Window
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.fragment.app.DialogFragment
import com.example.bunkerburgerblind.databinding.ActivityMainBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.JsonHttpResponseHandler
import com.loopj.android.http.RequestParams
import cz.msebera.android.httpclient.Header
import org.json.JSONObject

class MainActivity : AppCompatActivity(), SendEventListener {

    //날씨 관련 api 초기 설정
    companion object {
        const val API_KEY: String = "95e17a4fa68c3c2dc3189c71f73daba2"
        const val WEATHER_URL: String = "https://api.openweathermap.org/data/2.5/weather"
        const val MIN_TIME: Long = 5000
        const val MIN_DISTANCE: Float = 1000F
        const val WEATHER_REQUEST: Int = 102
    }

    val itemList = arrayListOf<item_data>()      // 단품 메뉴 아이템 배열
    val listAdapter = ListAdapter(itemList)     // 단품 메뉴 rv 어댑터
    var SBList = arrayListOf<shopping_basket_data>() //장바구니 아이템 배열

    private val database = Firebase.database
    private val myRef = database.getReference("bunkerburger")

    private lateinit var temperature: TextView
    private lateinit var weatherIcon: ImageView

    private lateinit var mLocationManager: LocationManager
    private lateinit var mLocationListener: LocationListener

    private lateinit var binding : ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        temperature = binding.weatherText
        weatherIcon = binding.weatherImg

        if(intent.hasExtra("SBListFromSingle")) {
            SBList.addAll(intent.getSerializableExtra("SBListFromSingle") as ArrayList<shopping_basket_data>)
            SetPrice()
        }

        if(intent.hasExtra("SBListFromSemMenu")) {
            SBList.addAll(intent.getSerializableExtra("SBListFromSemMenu") as ArrayList<shopping_basket_data>)
            SetPrice()
        }


        val burger = ArrayList<MenuType>()
        val side = ArrayList<MenuType>()
        val beverage = ArrayList<MenuType>()

        // 세트메뉴 받아오기
        if (intent.hasExtra("setList")) {
            var name = ArrayList<String>()

            val shoppingBag = intent.getSerializableExtra("setList") as ArrayList<String>
            val totalCost = intent.getIntExtra("totalCost", 0)
            val url = setMenuImg(shoppingBag[0])

            name.add("${shoppingBag[0]} 세트")
            name.add("${shoppingBag[0]}")
            name.add("${shoppingBag[1]}")
            name.add("${shoppingBag[2]}")

            SBList.add(shopping_basket_data("set",name, totalCost.toString(), url, 1, 1))
            SetPrice()
        }

        myRef.addValueEventListener(object: ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val menu = snapshot.child("menu")

                for (item in menu.child("burger").children) {
                    val temp = item.getValue(MenuType::class.java)
                    temp?.let { burger.add(it) }
                }

                for (item in menu.child("side").children) {
                    val temp = item.getValue(MenuType::class.java)
                    temp?.let { side.add(it) }
                }

                for (item in menu.child("beverage").children) {
                    val temp = item.getValue(MenuType::class.java)
                    temp?.let { beverage.add(it) }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("실패", "실패")
            }
        })

        binding.help.setOnClickListener {
            val dialog = HelpDig(this@MainActivity)
            dialog.HPDig()
        }

        binding.SBbtn.setOnClickListener { //장바구니 다이얼로그
            val dialog = ShoppingBasketDig(this@MainActivity, SBList)
            dialog.SBDig()
            dialog.setOnClickListener(object: ShoppingBasketDig.CallbackListener{
                override fun onClicked() {
                    SetPrice()
                    listAdapter.notifyDataSetChanged()
                }
            })
        }

        binding.payment.setOnClickListener {
            if(SBList.isEmpty()) {
                val toast = Toast.makeText(
                    this@MainActivity,
                    "담은 메뉴가 없습니다.",
                    Toast.LENGTH_SHORT
                )
                toast.show()
            }
            else{
                val dialog = PaymentPlaceChoiceDig(this@MainActivity)
                dialog.PPCDig(SBList)
            }
        }

        binding.orderSet.setOnClickListener {
            val dialog = Dialog9500Fragment()
            setDataAtFragment(dialog, burger, side, beverage, SBList)
        }

        binding.orderSingle.setOnClickListener {
            val intent = Intent(this@MainActivity,SimpleMenuActivity::class.java)
            intent.putExtra("SBListFromMain", SBList)
            startActivity(intent)
        }

        // 관리자 페이지로 이동
        val adminBtn = findViewById<Button>(R.id.admin_mode);
        adminBtn.setOnClickListener {
            val intent = Intent(this, ManPWActivity::class.java)
            startActivity(intent)
        }

        myRef.addValueEventListener(object: ValueEventListener {
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

    override fun onResume() {
        super.onResume()
        getWeatherInCurrentLocation()
    }

    private fun setDataAtFragment(fragment: DialogFragment, burger:ArrayList<MenuType>, side:ArrayList<MenuType>, beverage:ArrayList<MenuType>, SBList: ArrayList<shopping_basket_data>) {
        val bundle = Bundle()
        bundle.putSerializable("burger", burger)
        bundle.putSerializable("side", side)
        bundle.putSerializable("beverage", beverage)
        bundle.putSerializable("SBList", SBList)

        fragment.arguments = bundle
        fragment.show(supportFragmentManager, "Custom")
    }

    override fun sendMessage (message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    fun SetPrice(){ //결제 금액 set
        var sum = 0
        if(SBList.isEmpty())
            binding.price.text = "0"
        else{
            for (item in SBList){
                sum += item.price.toInt() * item.cnt
            }
            binding.price.text = sum.toString()
        }
    }

    private fun setMenuImg(name: String): String = when (name) {
        "클래식버거" -> "https://ldb-phinf.pstatic.net/20220107_278/1641535879321KaiSp_JPEG/PzZOC7kNrNLLnPijxXqSqjCmuiM6Q4kythrBJt9pz9k%3D.jpg"
        "오리지날버거" -> "https://ldb-phinf.pstatic.net/20220107_166/1641535879214K246K_JPEG/3UxoulxuWPXOy5yuw0MmJtkoX6cIt-tTvZsj_PGm6d0%3D.jpg"
        "맥 앤 치즈버거" -> "https://ldb-phinf.pstatic.net/20220107_25/1641535879543Bcz0w_JPEG/aTJLucIxij1jlRLQwAzqKMpFgiigDIUDz5DurxXNzHc%3D.jpg"
        "칠리버거" -> "https://ldb-phinf.pstatic.net/20220107_248/1641535879709OU7Jh_JPEG/DdUdHI2h6geZ8lacm2Y26unpiiQtzIliK4FozplNBHk%3D.jpg"
        "BBQ버거" -> "https://ldb-phinf.pstatic.net/20220107_13/1641535879315peyUt_JPEG/MhijmaKlTEF2g5QOvUvnUBGl7ZKqizQNECULrnggtMA%3D.jpg"
        "더블치즈 해쉬브라운버거" -> "https://ldb-phinf.pstatic.net/20220107_247/16415358793129mpH8_JPEG/PR2H9gj8sd015EnGmXnrsmQs_fMZR-ZjKITC382W55Q%3D.jpg"
        "치킨버거" -> "https://ldb-phinf.pstatic.net/20221109_1/1667952663926qpsMD_JPEG/0ceywCe64k3pD-5CzrB9Pdw7zyR7Xt6r9Dg89bW4drc%3D.jpg"
        else -> ""
    }

    private fun getWeatherInCurrentLocation(){
        mLocationManager = this.getSystemService(Context.LOCATION_SERVICE) as LocationManager

        mLocationListener = LocationListener { p0 ->
            val params: RequestParams = RequestParams()
            params.put("lat", p0.latitude)
            params.put("lon", p0.longitude)
            params.put("appid", API_KEY)
            doNetworking(params)
        }

        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(this, arrayOf<String>(Manifest.permission.ACCESS_FINE_LOCATION), WEATHER_REQUEST)
            return
        }
        mLocationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, MIN_TIME, MIN_DISTANCE, mLocationListener)
        mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, MIN_TIME, MIN_DISTANCE, mLocationListener)
    }

    private fun doNetworking(params: RequestParams) {
        var client = AsyncHttpClient()

        client.get(WEATHER_URL, params, object: JsonHttpResponseHandler(){
            override fun onSuccess(
                statusCode: Int,
                headers: Array<out Header>?,
                response: JSONObject?
            ) {
                val weatherData = WeatherData().fromJson(response)
                if (weatherData != null) {
                    updateWeather(weatherData)
                }
            }

        })
    }

    private fun updateWeather(weather: WeatherData) {
        temperature.text = weather.tempString+" ℃"
        val resourceID = resources.getIdentifier(weather.icon, "drawable", packageName)
        weatherIcon.setImageResource(resourceID)
    }

}