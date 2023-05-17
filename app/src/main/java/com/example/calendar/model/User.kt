package com.example.calendar.model

class User {
    private var fullName: String
    private var imgUrl: String
    private var email: String
    private var password: String

    var FullName:String
        get() = fullName
        set(value) {
            fullName = value
        }

    var ImgUrl:String
        get() = imgUrl
        set(value) {
            imgUrl = value
        }

    var Email:String
        get() = email
        set(value) {
            email = value
        }

    var Password:String
        get() = password
        set(value) {
            password = value
        }

    constructor(fullName: String, imgUrl: String, email: String, password: String) {
        this.fullName = fullName
        this.imgUrl = imgUrl
        this.email = email
        this.password = password
    }
}