package com.montoto.inventapp.util

import android.content.Context
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.montoto.inventapp.R

// Activity Extensions
fun AppCompatActivity.replaceFragment(fragment: Fragment, addToStack: Boolean? = null) {
    if (addToStack == false){
        supportFragmentManager.beginTransaction()
            .replace(R.id.container, fragment)
            .commit()
    } else {
        supportFragmentManager.beginTransaction()
            .replace(R.id.container, fragment)
            .addToBackStack(null)
            .commit()
    }
}


//View Extensions
fun View.visibilityBy(condition: Boolean) {
    if (condition) visibility = View.VISIBLE
    else visibility = View.INVISIBLE
}

fun Context.toast(text: String){
    Toast.makeText(this, text, Toast.LENGTH_SHORT).show()
}

fun Context.toastLong(text: String){
    Toast.makeText(this, text, Toast.LENGTH_SHORT).show()
}