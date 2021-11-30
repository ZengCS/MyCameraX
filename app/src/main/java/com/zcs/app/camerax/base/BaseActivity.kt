package com.zcs.app.camerax.base

import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.tbruyelle.rxpermissions2.RxPermissions

abstract class BaseActivity : AppCompatActivity() {
    val rxPermissions: RxPermissions by lazy(LazyThreadSafetyMode.NONE) {
        RxPermissions(this)
    }

    fun showToast(msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show()
    }

    fun showMessage(msg: String, withExit: Boolean = false) {
        val builder = AlertDialog.Builder(this)
        builder.setMessage(msg)
        builder.setPositiveButton(if (withExit) "退出" else "我知道了") { dialog, _ ->
            dialog.dismiss()
            if (withExit)
                onBackPressed()
        }
        builder.show()
    }

}