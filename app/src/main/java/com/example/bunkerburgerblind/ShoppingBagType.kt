package com.example.bunkerburgerblind

import java.io.Serializable

class ShoppingBagType : Serializable {
    var burger:String = ""
    var side:String = ""
    var beverage:String = ""

    constructor() // 파이어베이스에서 데이터 변환을 위해서 필요
    constructor(burger: String, side: String, beverage: String) {
        this.burger = burger
        this.side = side
        this.beverage = beverage
    }

    @JvmName("setBurger1")
    fun setBurger(name: String) {
        this.burger = name
    }

    @JvmName("setSide1")
    fun setSide(name: String) {
        this.side = side
    }
}