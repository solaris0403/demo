package com.example.demo

object RustLib {
    init {
        System.loadLibrary("android_rust")
    }

    external fun helloFromRust(): String
}