package com.jp.app.utils

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.text.Html
import android.view.inputmethod.InputMethodManager
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.jp.app.BuildConfig
import java.io.File
import java.util.regex.Pattern

object GeneralUtils {

    @JvmStatic
    fun hideSoftKeyboard(activity: Activity) {

        // http://stackoverflow.com/a/7696791/1121497
        val view = activity.currentFocus
        if (view != null) {
            val imm = activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }


    @JvmStatic
    fun showSoftKeyboard(activity: Activity) {

        val imm = activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0)
    }

    @JvmStatic
    fun isValid(target: CharSequence?, pattern: Pattern): Boolean {
        return if (target == null) {
            false
        } else {
            pattern.matcher(target).matches()
        }
    }


    @JvmStatic
    fun isDebug(): Boolean {
        return BuildConfig.BUILD_TYPE === "debug"
    }
}