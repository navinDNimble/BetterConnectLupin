package com.nimble.lupin.admin.utils


import android.app.Activity
import android.app.Dialog
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.util.Log
import androidx.appcompat.widget.AppCompatButton
import com.nimble.lupin.admin.R


class NetworkChangeListener (private val activity : Activity): BroadcastReceiver() {
    private  var dialog: Dialog? =null

    override fun onReceive(context: Context?, intent: Intent?) {

          if (dialog==null){
                   dialog = Dialog(activity)
                    dialog!!.setContentView(R.layout.dialog_internet_lost)
                   dialog!!.setCancelable(false)
                   dialog!!.setCanceledOnTouchOutside(false)
                   val retryButton =  dialog!!.findViewById<AppCompatButton>(R.id.retryInternetConnection)
                    retryButton.setOnClickListener {
                        if (isNetworkAvailable(context)){
                            dialog!!.cancel()
                        }
                    }

          }

        if (isNetworkAvailable(context)) {
            Log.d("sachin","network connected")
           if (dialog?.isShowing!!){
               dialog?.cancel()
           }

        } else {
            dialog?.show()
        }

    }

    private fun isNetworkAvailable(context: Context?): Boolean {
        val connectivityManager =
            context?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetworkInfo = connectivityManager.activeNetworkInfo
        return activeNetworkInfo != null && activeNetworkInfo.isConnected
    }
}