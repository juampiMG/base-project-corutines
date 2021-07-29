package com.jp.app.helper

import android.content.DialogInterface
import android.util.Log
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import com.jp.app.R
import com.jp.app.common.view.BaseBottomSheetDialogFragment
import com.jp.app.common.view.BaseFragment


/**
 * Helper to show dialogs
 */
class DialogHelper(private val mActivity: FragmentActivity, private val mFragmentManager: FragmentManager) {

    /**
     * Show Bottom Sheet Dialog
     */
    fun navigateToBottomSheetDialog(
        bottomDialog: BaseBottomSheetDialogFragment<*>
    ) {
        bottomDialog.show(mFragmentManager, getFragmentTag(bottomDialog))
    }

    /**
     * Show a dialog with two options
     * if one of the two button or both, have not set the listener will have the close behaviour
     */

    fun alertDialogTwoButtons(
        title: String,
        message: String?,
        leftButtonTittle: String,
        leftClickListener: DialogInterface.OnClickListener?,
        rightButtonTittle: String,
        rightClickListener: DialogInterface.OnClickListener?,
        cancelable: Boolean
    ) {
        val rightListener = DialogInterface.OnClickListener { dialog, which ->
            rightClickListener?.onClick(dialog, which)
            dialog.dismiss()
        }

        val leftListener = DialogInterface.OnClickListener { dialog, which ->
            leftClickListener?.onClick(dialog, which)
            dialog.dismiss()
        }

        AlertDialog.Builder(mActivity, R.style.AlertDialogTheme)
            .setTitle(title)
            .setMessage(message)
            .setPositiveButton(rightButtonTittle, rightListener)
            .setNegativeButton(leftButtonTittle, leftListener)
            .setCancelable(cancelable)
            .show()
    }

    /**
     * Show a dialog with three options
     * if one of the three button or both, have not set the listener will have the close behaviour
     */
    fun alertDialogThreeButtons(
        title: String,
        message: String?,
        leftButtonTittle: String,
        leftClickListener: DialogInterface.OnClickListener?,
        rightButtonTittle: String,
        rightClickListener: DialogInterface.OnClickListener?,
        neutralButtonTittle: String,
        neuralClickListener: DialogInterface.OnClickListener?,
        cancelable: Boolean
    ) {
        val rightListener = DialogInterface.OnClickListener { dialog, which ->
            rightClickListener?.onClick(dialog, which)
            dialog.dismiss()
        }

        val leftListener = DialogInterface.OnClickListener { dialog, which ->
            leftClickListener?.onClick(dialog, which)
            dialog.dismiss()
        }

        AlertDialog.Builder(mActivity, R.style.AlertDialogTheme)
            .setTitle(title)
            .setMessage(message)
            .setPositiveButton(rightButtonTittle, rightListener)
            .setNegativeButton(leftButtonTittle, leftListener)
            .setCancelable(cancelable)
            .show()
    }

    /**
     * Show a dialog with one option
     * if the button has not set the listener will have the close behaviour
     */
    fun alertDialogOneButton(
        title: String,
        message: String?,
        buttonTittle: String,
        clickListener: DialogInterface.OnClickListener?,
        cancelable: Boolean
    ) {
        val listener = DialogInterface.OnClickListener { dialog, which ->
            clickListener?.onClick(dialog, which)
            dialog.dismiss()
        }

        AlertDialog.Builder(mActivity, R.style.AlertDialogTheme)
            .setTitle(title)
            .setMessage(message)
            .setPositiveButton(buttonTittle, listener)
            .setCancelable(cancelable)
            .show()
    }

    /**
     * Show a dialog with one option
     * this is specific for errors, has the Titles set and always has the same behaviour for the button -> close
     */
    fun errorMessageEventDialog(descriptionError: String) {
        val rightListener = DialogInterface.OnClickListener { dialog, _ ->
            dialog.dismiss()
        }

        AlertDialog.Builder(mActivity, R.style.AlertDialogTheme)
            .setTitle(mActivity.resources.getString(R.string.error_title_oops))
            .setMessage(descriptionError)
            .setPositiveButton(R.string.ok, rightListener)
            .show()
    }


    /**
     * get the fragment tag to use it for the transitions
     */
    private fun getFragmentTag(fragment: Fragment): String {
        return (fragment as? BaseFragment<*, *>)?.getFragmentId()
            ?: (fragment as Any).javaClass.name
    }

    fun showDialog(dialog: DialogFragment, tag: String) {
        try {
            dialog.show(mActivity.supportFragmentManager, tag)
        } catch (e: IllegalStateException) {
            Log.e("DialogUtil", e.localizedMessage)
        }
    }

}
