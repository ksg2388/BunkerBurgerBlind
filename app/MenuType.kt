package com.example.bunkerburgerblind

import java.io.Serializable

class MenuType: Serializable {
    var usage:Int = 0
    var name:String = ""
    var examination:String = ""
    var id:Int = 0
    var price:Int = 0
    var stock:Int = 0
    var img:String = ""

    constructor() // 파이어베이스에서 데이터 변환을 위해서 필요
    constructor(usage:Int, name:String, examination:String,id: Int, price: Int, stock:Int, img:String) {
        this.name = name
        this.examination = examination
        this.price = price
        this.usage = usage
        this.id = id
        this.stock = stock
        this.img = img
    }
}