package com.dscvit.keats.utils

import android.util.Patterns

fun validateEmail(emailForValidation: String): Boolean {
    return Patterns.EMAIL_ADDRESS.matcher(emailForValidation).matches()
}
