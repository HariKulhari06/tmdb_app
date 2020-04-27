package com.hari.tmdb.binding

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.annotation.SuppressLint
import android.view.View
import android.widget.TextView
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import androidx.databinding.BindingAdapter
import com.hari.tmdb.R
import com.hari.tmdb.util.AndroidRInteger
import java.text.NumberFormat
import java.util.*

@BindingAdapter("isVisible")
fun View.showGone(show: Boolean) {
    isVisible = show
}

@BindingAdapter("isInvisible")
fun View.showHide(invisible: Boolean) {
    isInvisible = invisible
}

@BindingAdapter("isVisibleWithAnimation")
fun View.showGoneWithAnimation(show: Boolean) {
    val animationDuration = resources.getInteger(AndroidRInteger.config_longAnimTime)
    val endOpacity = if (show) 100f else 0f
    val endVisible = if (show) View.VISIBLE else View.GONE
    animate()
        .alpha(endOpacity)
        .setDuration(animationDuration.toLong())
        .setListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                visibility = endVisible
            }
        })
}

@BindingAdapter("isHideWithAnimation")
fun View.showHideWithAnimation(show: Boolean) {
    val animationDuration = resources.getInteger(AndroidRInteger.config_longAnimTime)
    val endOpacity = if (show) 100f else 0f
    val endVisible = if (show) View.VISIBLE else View.INVISIBLE
    animate()
        .alpha(endOpacity)
        .setDuration(animationDuration.toLong())
        .setListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                visibility = endVisible
            }
        })
}

@BindingAdapter("currency")
fun TextView.currency(currency: Long) {
    text = NumberFormat.getCurrencyInstance(Locale.US).format(currency)
}


@SuppressLint("SetTextI18n")
@BindingAdapter("runtime")
fun TextView.runtime(runtime: Int) {
    val hours: Int = runtime / 60 //since both are ints, you get an int
    val minutes: Int = runtime % 60
    text = "$hours" + "h " + minutes + "m"
}

@BindingAdapter("gender")
fun TextView.gender(gender: Int) {
    text =
        if (gender == 1) context.getString(R.string.female) else if (gender == 2) context.getString(
            R.string.male
        ) else context.getString(R.string.not_specified)
}