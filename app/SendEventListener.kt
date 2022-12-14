package com.example.bunkerburgerblind

interface SendEventListener {
    // Fargment -> Activity로 데이터 보내기용
    fun sendMessage(message: String)
}