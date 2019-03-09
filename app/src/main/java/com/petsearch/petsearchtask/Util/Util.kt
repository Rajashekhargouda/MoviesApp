package com.petsearch.petsearchtask.Util

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.support.design.widget.Snackbar
import android.view.View
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*
import android.support.v4.content.ContextCompat.getSystemService





object Util {
    const val API_KEY = "9b0730e6257ac0de0599155b5ae6152f"

    fun showSnackbar(view:View,message:String,duration:Int){
        Snackbar.make(view,message,duration).show()
    }

    fun getDate(date:String):String{
        val sdf = SimpleDateFormat("yyyy-MM-dd")
        return DateFormat.getDateInstance(DateFormat.LONG,Locale.getDefault()).format(sdf.parse(date))
    }

    fun isNetworkOnline(context: Context): Boolean {
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager?
        val netInfo = cm!!.activeNetworkInfo
        return netInfo != null && netInfo.isConnected

    }


}