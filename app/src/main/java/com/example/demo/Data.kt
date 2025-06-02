package com.example.demo

import android.os.Parcelable
import java.io.Serializable

class LoginData : Serializable {
    val code = -1
    val message = ""
    val data = Result()
}

class Result : Serializable {
    val userId = ""
    val token = ""
    val register = false
    val userName = ""
    val portrait = ""
    val resetCode: String? = null
}