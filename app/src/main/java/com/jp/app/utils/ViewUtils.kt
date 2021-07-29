package com.jp.app.utils

import android.app.Activity
import android.content.Context
import android.content.res.Resources
import android.os.Build
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import androidx.core.content.ContextCompat
import java.util.*

object ViewUtils {
    /**
     * Sets a listener to the `editText` so when the action key performs a
     * [EditorInfo.IME_ACTION_DONE], the `runnable` is run.
     *
     *
     * This method adds the necessary `android:imeOptions="actionDone"` to the editText.
     */
    fun setOnActionDone(editText: EditText, unit: () -> Unit) {
        editText.imeOptions = EditorInfo.IME_ACTION_DONE

        editText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                unit.invoke()
                true
            } else {
                false
            }
        }
    }

    /**
     * Make the full screen available for the target activity
     * * make the status bar transparent
     *
     * @param activity        where is gonna be applied the new style
     * @param backgroundColor color of the status bar
     */
    fun manageStatusBarStyle(activity: Activity, backgroundColor: Int) {

        //make translucent statusBar on kitkat devices
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT && Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            setWindowFlag(activity, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, true)
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            activity.window.decorView.systemUiVisibility =
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        }
        //make fully Android Transparent Status bar
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            setWindowFlag(activity, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, false)
            activity.window.statusBarColor = ContextCompat.getColor(activity, backgroundColor)
        }
    }

    fun resetStatusBarStyle(activity: Activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            activity.window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        } else {
            activity.window.decorView.systemUiVisibility = 0
        }
    }

    fun setWindowFlag(activity: Activity, bits: Int, on: Boolean) {
        val win = activity.window
        val winParams = win.attributes
        if (on) {
            winParams.flags = winParams.flags or bits
        } else {
            winParams.flags = winParams.flags and bits.inv()
        }
        win.attributes = winParams
    }

    /**
     * Converts Density Independent Pixels (dip, dp) to Pixels (px).
     * Maybe it is preferable to use [Resources.getDimensionPixelSize].
     */
    fun dpToPx(context: Context, dips: Float): Int {
        return (dips * context.resources.displayMetrics.density).toInt()
    }

    fun isRTL(): Boolean {
        return isRTL(Locale.getDefault())
    }

    fun isRTL(locale: Locale): Boolean {
        val directionality = Character.getDirectionality(locale.displayName[0]).toInt()
        return directionality == Character.DIRECTIONALITY_RIGHT_TO_LEFT.toInt() || directionality == Character.DIRECTIONALITY_RIGHT_TO_LEFT_ARABIC.toInt()
    }

}