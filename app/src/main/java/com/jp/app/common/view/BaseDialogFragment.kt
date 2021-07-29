package com.jp.app.common.view

import android.app.Activity
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.fragment.app.DialogFragment
import com.jp.app.utils.ViewUtils.isRTL

interface IBaseDialogFragmentCallback {}

abstract class BaseDialogFragment<TCallback : IBaseDialogFragmentCallback> : DialogFragment() {

    protected var mCallback: TCallback? = null
    private var mLayoutId = -1

    /**
     * Check if the Callback has set properly the implementation to the target dialog
     * This onAttach is for old versions
     */
    override fun onAttach(activity: Activity) {
        super.onAttach(activity)
        if (activity is IBaseFragmentCallback) {
            mCallback = activity as TCallback
        } else {
            throw IllegalStateException(activity::class.java.simpleName + " must implements " + IBaseFragmentCallback::class.java.simpleName)
        }
    }


    /**
     * Check if the Callback has set properly the implementation to the target dialog
     */
    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is IBaseFragmentCallback) {
            mCallback = context as TCallback
        } else {
            throw IllegalStateException(context::class.java.simpleName + " must implements " + IBaseFragmentCallback::class.java.simpleName)
        }
    }

    /**
     * On Create the bottom dialog set the background to transparent
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mLayoutId = getLayoutId()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(mLayoutId, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpViews()
        if (isRTL()) applyRTLChanges()
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        if (activity is DialogInterface.OnDismissListener) {
            (activity as DialogInterface.OnDismissListener).onDismiss(dialog)
        }
    }

    @LayoutRes
    abstract fun getLayoutId(): Int

    abstract fun setUpViews()

    abstract fun applyRTLChanges()

}