package com.example.bunkerburgerblind

data class shopping_basket_data(//장바구니 데이터 클래스(장바구니 데이터 구조 바꿀 필요 있을 듯 함)
    var type: String, //burger, beverage, side, set.
    var name: ArrayList<String>, //set인 경우 [0]세트 이름, [1]burger 이름, [2]side 이름, [3]beverage 이름 순으로 저장
    var price: String,
    var img: String,
    var cnt: Int,
    var stock:Int = 0,
): java.io.Serializable