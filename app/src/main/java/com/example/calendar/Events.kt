package com.example.calendar

class Events {
    private lateinit var event:String
    var Event :String
        get() = event
        set(value) {
            event = value
        }

    private lateinit var time: String
    var Time:String
        get() = time
        set(value) {
            time = value
        }
    private lateinit var date :String
    var Date :String
        get() = date
        set(value) {
            date = value
        }
    private lateinit var month:String
    var Month : String
        get() = month
        set(value) {
            month = value
        }
    private lateinit var year: String
    var Year : String
        get() = year
        set(value) {
            year = value
        }
    constructor(event:String, time : String,date:String, month:String,year:String){
        this.event = event
        this.time = time
        this.date = date
        this.month = month
        this.year = year
    }
}