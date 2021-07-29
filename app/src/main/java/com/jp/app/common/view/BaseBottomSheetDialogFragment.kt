package com.jp.app.common.view

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.jp.app.R
import com.jp.app.utils.ViewUtils.isRTL

interface IBaseBottomSheetFragmentCallback {}
/**
 * Base for the BottomSheetDialogFragment of the project
 */
abstract class BaseBottomSheetDialogFragment<TCallback : IBaseBottomSheetFragmentCallback> : BottomSheetDialogFragment() {

    protected var mCallback: TCallback? = null
    private var mFragmentId: String? = null
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
        setStyle(STYLE_NORMAL, R.style.BottomSheetDialog)
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

    /**
     * Used to add a fragment tag when the fragment is loaded into fragmentManager
     */
    fun getFragmentId(): String? {
        val fragmentClass = (this as Any).javaClass
        mFragmentId = fragmentClass.name
        return mFragmentId
    }

    @LayoutRes
    abstract fun getLayoutId(): Int

    abstract fun setUpViews()

    abstract fun applyRTLChanges()

}