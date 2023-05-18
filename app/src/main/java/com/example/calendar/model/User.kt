package com.example.calendar.model

data class User(
    var id: String? = null, var fullName:String? = null, var email: String? = null,
    var password: String?= null, var imgUrl :String? = null, var phoneNumber:String? = null) {
}