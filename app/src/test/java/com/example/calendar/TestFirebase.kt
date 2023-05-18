package com.example.calendar

import com.example.calendar.model.User
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import org.junit.Test

class TestFirebase {

    @Test
    fun testCreateUser(){
        val db : DatabaseReference = FirebaseDatabase.getInstance().getReference("Users")
        val user = User("NguyenTan","tan@gmail.com","123")
        db.child("test").setValue(user).addOnSuccessListener {
            println("pass")
        }
    }
}