package com.huaxia.xlib.ext

import android.view.View
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.findViewTreeLifecycleOwner
import androidx.lifecycle.lifecycleScope
import java.lang.reflect.InvocationHandler
import java.lang.reflect.Method
import java.lang.reflect.Proxy
import java.util.*

/**
 * @author: JayQiu
 * @date: 2022/3/9
 * @description:
 */
/**
 * @param during 防抖时间间隔
 * @param combine 一个接口中的多个回调方法是否共用防抖时间
 */
//@Suppress("UNCHECKED_CAST")
//fun <T : Any> T.throttle(during: Long = 500L, combine: Boolean = true): T {
//    return Proxy.newProxyInstance(this::class.java.classLoader, this::class.java.interfaces, object : InvocationHandler {
//        private val map = HashMap<Method?, Long>()
//        override fun invoke(proxy: Any, method: Method, args: Array<out Any>?): Any? {
//            val current = System.currentTimeMillis()
//            return if (current - (map[if (combine) null else method] ?: 0) > during) {
//                map[if (combine) null else method] = current
//                method.invoke(this@throttle, *args.orEmpty())
//            } else {
//                resolveDefaultReturnValue(method)
//            }
//        }
//    }) as T
//}
//
//private fun resolveDefaultReturnValue(method: Method): Any? {
//    return when (method.returnType.name.lowercase(Locale.US)) {
//        Void::class.java.simpleName.lowercase(Locale.US) -> null
//        else -> null
//    }
//}

val View.lifecycleOwner:LifecycleOwner? get() = this.findViewTreeLifecycleOwner()
val View.scope: LifecycleCoroutineScope? get() = this.lifecycleOwner?.lifecycleScope


infix fun View?.visibleBy(boolean: Boolean) {
    this?.visibility = if (boolean) View.VISIBLE else View.GONE
}

infix fun View?.visibleOr(boolean: Boolean) {
    val or = this?.visibility == View.VISIBLE
    this?.visibility = if (or || boolean) View.VISIBLE else View.GONE
}

infix fun View?.visibleAnd(boolean: Boolean) {
    val and = this?.visibility == View.VISIBLE
    this?.visibility = if (and && boolean) View.VISIBLE else View.GONE
}

infix fun View?.visibleWith(other: View?) {
    this?.visibility = other?.visibility ?: View.GONE
}
