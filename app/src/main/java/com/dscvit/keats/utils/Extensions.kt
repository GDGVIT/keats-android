package com.dscvit.keats.utils

import android.content.Context
import android.view.View
import android.widget.Toast

fun Context.shortToast(string: String) {
    Toast.makeText(this, string, Toast.LENGTH_SHORT).show()
}

fun Context.longToast(string: String) {
    Toast.makeText(this, string, Toast.LENGTH_LONG).show()
}

fun View.hide() {
    this.visibility = View.GONE
}

fun View.show() {
    this.visibility = View.VISIBLE
}

fun View.enable() {
    this.isEnabled = true
}

fun View.disable() {
    this.isEnabled = false
}
