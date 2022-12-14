package com.example.bunkerburgerblind

class MenuListType {
    var burger: Array<MenuType> = emptyArray<MenuType>()
    var side: Array<MenuType> = emptyArray<MenuType>()
    var beverage: Array<MenuType> = emptyArray<MenuType>()

    constructor() // 파이어베이스에서 데이터 변환을 위해서 필요
    constructor(burger: Array<MenuType>, side: Array<MenuType>, beverage: Array<MenuType>) {
        this.burger = burger
        this.side = side
        this.beverage = beverage
    }
}