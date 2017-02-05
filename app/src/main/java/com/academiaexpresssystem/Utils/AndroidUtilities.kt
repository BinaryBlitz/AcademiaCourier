package com.academiaexpresssystem.Utils

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.Uri
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import com.google.gson.JsonElement

@SuppressWarnings("unused")
object AndroidUtilities {

    private val PLAY_SERVICES_RESOLUTION_REQUEST = 9000

    fun dpToPx(context: Context, dipValue: Float): Int {
        val scale = context.resources.displayMetrics.density
        return (dipValue * scale + 0.5f).toInt()
    }

    fun call(context: Context, phone: String) {
        val call = Uri.parse("tel:" + phone)
        val intent = Intent(Intent.ACTION_DIAL, call)
        context.startActivity(intent)
    }

    fun getStringFieldFromJson(element: JsonElement?): String {
        if (element == null || element.isJsonNull) return ""
        else return element.asString
    }

    fun getIntFieldFromJson(element: JsonElement?): Int {
        if (element == null || element.isJsonNull) return 0
        else return element.asInt
    }

    fun getDoubleFieldFromJson(element: JsonElement?): Double {
        if (element == null || element.isJsonNull) return 0.0
        else return element.asDouble
    }

    fun getBooleanFieldFromJson(element: JsonElement?): Boolean {
        if (element == null || element.isJsonNull) return false
        else return element.asBoolean
    }

    fun checkPlayServices(context: Activity): Boolean {
        val apiAvailability = GoogleApiAvailability.getInstance()
        val resultCode = apiAvailability.isGooglePlayServicesAvailable(context)
        if (resultCode != ConnectionResult.SUCCESS) {
            return onSuccess(apiAvailability, context, resultCode)
        }
        return true
    }

    private fun onSuccess(apiAvailability: GoogleApiAvailability, context: Activity, resultCode: Int): Boolean {
        if (apiAvailability.isUserResolvableError(resultCode)) {
            apiAvailability.getErrorDialog(context, resultCode, PLAY_SERVICES_RESOLUTION_REQUEST).show()
        } else {
            context.finish()
        }
        return false
    }

    fun convertDpToPixel(dp: Float, context: Context): Float {
        val resources = context.resources
        val metrics = resources.displayMetrics
        return dp * (metrics.densityDpi / 160f)
    }

    fun isConnected(context: Context): Boolean {
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork = cm.activeNetworkInfo

        return activeNetwork != null && activeNetwork.isConnectedOrConnecting
    }
}
