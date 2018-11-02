package akme

import android.content.Context
import android.content.res.Resources
import android.os.Build
import android.util.Log
import android.util.TypedValue

val tag = "MQTT-HALLOWWEEN"

fun Akme.Companion.upperOrEqualTo26(): Boolean = Build.VERSION.SDK_INT >= Build.VERSION_CODES.O

fun Akme.Companion.upperOrEqualTo24(): Boolean = Build.VERSION.SDK_INT >= Build.VERSION_CODES.N

fun Akme.Companion.upperOrEqualTo23(): Boolean = Build.VERSION.SDK_INT >= Build.VERSION_CODES.M

fun Akme.Companion.upperOrEqualToLollipop(): Boolean = Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP

fun String.logD() = Log.d(tag, this)

fun String.logE(throwable: Throwable? = null) = Log.e(tag, this, throwable)

fun Resources.getCompatColor(res: Int): Int = if (Akme.upperOrEqualTo23())
    getColor(res, null) else getColor(res)

fun Context.getServer(): String? =
    getSharedPreferences("com.mqtthalloween", Context.MODE_PRIVATE).getString("server", null)

fun Context.setServer(server: String) {
    val pref = getSharedPreferences("com.mqtthalloween", Context.MODE_PRIVATE)
    pref.edit().putString("server", server).apply()
}