package com.academiaexpresssystem.Activities

import android.app.ProgressDialog
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import com.academiaexpresssystem.Data.Order
import com.academiaexpresssystem.R
import com.academiaexpresssystem.Server.DeviceInfoStore
import com.academiaexpresssystem.Server.ServerApi
import com.academiaexpresssystem.Utils.AndroidUtilities
import com.afollestad.materialdialogs.MaterialDialog
import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class OrderActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.order_layout)

        initElements()
        setOnClickListeners()
        initOrderList()
    }

    private fun initOrderList() {
        (findViewById(R.id.ingr) as LinearLayout).removeAllViews()
        if (order!!.ingredients == null || order!!.ingredients!!.size == 0) {
            findViewById(R.id.ingr).visibility = View.GONE
            findViewById(R.id.card_view2d3).visibility = View.GONE
            return
        }

        findViewById(R.id.ingr).visibility = View.VISIBLE
        findViewById(R.id.card_view2d3).visibility = View.VISIBLE

        createList()
    }

    private fun createList() {
        for (i in 0..order!!.ingredients!!.size - 1) {
            val v = LayoutInflater.from(this).inflate(R.layout.lunch_part_card, null)
            val parts = order!!.ingredients!![i].split("=".toRegex()).dropLastWhile(String::isEmpty).toTypedArray()

            (v.findViewById(R.id.textView43) as TextView).text = parts[1]
            (v.findViewById(R.id.textView44) as TextView).text = getString(R.string.x) + parts[0]
            (v.findViewById(R.id.textView4) as TextView).text = parts[3]

            (findViewById(R.id.ingr) as LinearLayout).addView(v)
        }
    }

    private fun initElements() {
        findViewById(R.id.fab13).visibility = if (order!!.isAssigned) View.VISIBLE else View.GONE
        findViewById(R.id.fab23).visibility = if (order!!.isAssigned) View.GONE else View.VISIBLE

        (findViewById(R.id.info_text) as TextView).text = order!!.time
        (findViewById(R.id.info_text4) as TextView).text = Integer.toString(order!!.price) + getString(R.string.ruble_sign)
        (findViewById(R.id.gender2) as TextView).text = order!!.location
        (findViewById(R.id.address) as TextView).text = order!!.userName
        (findViewById(R.id.gender) as TextView).text = order!!.userPhone
    }

    private fun showAddDialog() {
        MaterialDialog.Builder(this@OrderActivity)
                .title(R.string.dialog_title)
                .content(R.string.are_you_sure)
                .positiveText(R.string.yes)
                .positiveColor(blackColor)
                .negativeColor(blackColor)
                .negativeText(R.string.no)
                .onPositive { dialog, which -> attach() }
                .show()
    }

    private fun onInternetConnectionError() {
        Snackbar.make(findViewById(R.id.main), R.string.internet_connection_error, Snackbar.LENGTH_SHORT).show()
    }

    private fun attach() {
        val dialog = ProgressDialog(this)
        dialog.show()

        ServerApi.get(this).api().assignOrder(order!!.id, DeviceInfoStore.getToken(this)).enqueue(object : Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                dialog.dismiss()

                if (!response.isSuccessful) {
                    onInternetConnectionError()
                    return
                }

                findViewById(R.id.fab23).visibility = View.VISIBLE
                findViewById(R.id.fab13).visibility = View.GONE
            }

            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                dialog.dismiss()
                onInternetConnectionError()
            }
        })
    }

    private fun showConfirmDialog() {
        MaterialDialog.Builder(this@OrderActivity)
                .title(R.string.dialog_title)
                .content(R.string.are_you_sure)
                .positiveText(R.string.yes)
                .positiveColor(blackColor)
                .negativeColor(blackColor)
                .negativeText(R.string.no)
                .onPositive { dialog, which -> completeOrder() }
                .show()
    }

    private fun completeOrder() {
        val dialog = ProgressDialog(this)
        dialog.show()

        val `object` = JsonObject()
        `object`.addProperty("status", "delivered")

        val toSend = JsonObject()
        toSend.add("order", `object`)

        ServerApi.get(this).api().updateOrder(order!!.id, DeviceInfoStore.getToken(this)).enqueue(object : Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                dialog.dismiss()

                if (!response.isSuccessful) {
                    onInternetConnectionError()
                    return
                }

                finish()
            }

            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                dialog.dismiss()
                onInternetConnectionError()
            }
        })
    }

    private fun setOnClickListeners() {
        findViewById(R.id.imageView6).setOnClickListener { finish() }

        findViewById(R.id.fab13).setOnClickListener { showAddDialog() }

        findViewById(R.id.fab23).setOnClickListener { showConfirmDialog() }

        findViewById(R.id.button2).setOnClickListener(View.OnClickListener {
            if (order == null || order!!.userPhone == null) {
                return@OnClickListener
            }
            AndroidUtilities.call(this@OrderActivity, order!!.userPhone!!)
        })

        findViewById(R.id.gender2).setOnClickListener { openMap() }
    }

    private fun openMap() {
        if (order == null || order!!.position == null) {
            return
        }
        try {
            val coordinates = order!!.position!!.split("=".toRegex()).dropLastWhile(String::isEmpty).toTypedArray()
            val gmmIntentUri = Uri.parse("geo:" +
                    java.lang.Double.parseDouble(coordinates[0]) + "," + java.lang.Double.parseDouble(coordinates[1]) +
                    "?q=" + java.lang.Double.parseDouble(coordinates[0]) + "," + java.lang.Double.parseDouble(coordinates[1]))

            val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
            mapIntent.`package` = "com.google.android.apps.maps"
            if (mapIntent.resolveActivity(packageManager) != null) {
                startActivity(mapIntent)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    companion object {
        var order: Order? = null
        private val blackColor = Color.parseColor("#212121")
    }
}
