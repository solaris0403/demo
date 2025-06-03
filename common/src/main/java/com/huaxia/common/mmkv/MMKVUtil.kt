package com.huaxia.common.mmkv

import com.tencent.mmkv.MMKV

object MMKVUtil {
    private val mmkv by lazy { MMKV.defaultMMKV() }

    fun putString(key: String, value: String) {
        mmkv.encode(key, value)
    }

    fun getString(key: String, defaultValue: String = ""): String {
        return mmkv.decodeString(key, defaultValue) ?: defaultValue
    }

    fun putInt(key: String, value: Int) {
        mmkv.encode(key, value)
    }

    fun getInt(key: String, defaultValue: Int = 0): Int {
        return mmkv.decodeInt(key, defaultValue)
    }

    fun putBoolean(key: String, value: Boolean) {
        mmkv.encode(key, value)
    }

    fun getBoolean(key: String, defaultValue: Boolean = false): Boolean {
        return mmkv.decodeBool(key, defaultValue)
    }

    fun removeKey(key: String) {
        mmkv.removeValueForKey(key)
    }

    fun clearAll() {
        mmkv.clearAll()
    }
}