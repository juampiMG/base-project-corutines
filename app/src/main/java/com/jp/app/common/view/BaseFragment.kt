package com.jp.app.common.view

import android.app.Activity
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.annotation.VisibleForTesting
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.jp.app.common.activity.IBaseActivityCallback
import com.jp.app.common.viewModel.IBaseFragmentViewModel
import com.jp.app.helper.DialogHelper
import com.jp.app.helper.NavigationHelper
import com.jp.app.utils.GeneralUtils
import com.jp.app.utils.ViewUtils.isRTL
import kotlinx.android.synthetic.main.generic_loading.*
import java.util.*
import javax.inject.Inject

interface IBaseFragmentCallback : IBaseActivityCallback {}

abstract class BaseFragment<TViewModel : IBaseFragmentViewModel, TCallback : IBaseFragmentCallback> :
    Fragment() {

    @Inject
    lateinit var mCallback: TCallback

    @Inject
    lateinit var mViewModel: TViewModel

    @Inject
    lateinit var mNavigationHelper: NavigationHelper

    @Inject
    lateinit var mDialogHelper: DialogHelper

    @Inject
    lateinit var mExtras: Bundle

    @Inject
    lateinit var mActivity: FragmentActivity

    var currentChildFragment: Fragment? = null

    private var mFragmentId: String? = null
    private var mLayoutId: Int = 0
    protected var mSearchBackButtonPressed = false
    private var destroyed: Boolean = false
    var mIsCurrentChildFragmentLoad = false


    /**
     * set the LayoutID and the ViewModel for the target fragment
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        mLayoutId = getLayoutId()
    }


    /**
     * Call to viewModel.onDestroy() to clear all the rx pending calls
     */
    override fun onDestroy() {
        destroyed = true
        super.onDestroy()
        mViewModel.onDestroy()
    }


    /**
     * Inflate the target layout
     */
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(mLayoutId, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        subscribers()
    }

    /**
     * Subscribe to loading progressbar for show or hide on demand from the viewModel
     * Subscribe to the otres live datas that the target fragment require
     */
    private fun subscribers() {
        subscribeLoading()
        subscribeToAlertDialogs()
        subscribeToLiveData()
    }


    /**
     * Subscribe to the loading live data to wait for a signal and show or hide the progressBar
     * generic_loading comes from the generic_loading.xml view. To use the progress bar just include it
     * to the fragment view <include layout="@layout/generic_loading" />
     */
    private fun subscribeLoading() {
        mViewModel.showIsLoading().observe(viewLifecycleOwner, { isLoading ->
            if (isLoading != null) isLoadingAtFragment(isLoading)
        })
    }

    private fun isLoadingAtFragment(load: Boolean) {
        generic_loading?.visibility = if (load) VISIBLE else GONE
    }

    /**
     * Subscribe to show the Alert Dialogs
     */
    private fun subscribeToAlertDialogs() {
        mViewModel.showAlertDialogTwoButtons().observe(viewLifecycleOwner, { alertDialogModel ->
            if (alertDialogModel != null) mCallback.showAlertDialogTwoButtons(alertDialogModel)
        })

        mViewModel.showAlertDialogOneButton().observe(viewLifecycleOwner, { alertDialogModel ->
            if (alertDialogModel != null) mCallback.showAlertDialogOneButton(alertDialogModel)
        })

        mViewModel.showErrorMessageDialog().observe(viewLifecycleOwner, { error ->
            if (error != null) mCallback.showErrorMessageDialog(error)
        })

        mViewModel.showErrorMessageDialogString().observe(viewLifecycleOwner, { error ->
            mCallback.showErrorMessageDialog(error)
        })

        mViewModel.showDisplayServerErrorToast().observe(viewLifecycleOwner, {
            it?.let {
                mCallback.showErrorServerMessageToast()
            }
        })

    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        onViewLoaded(savedInstanceState, view)
    }

    /**
     * Once the view is get inflate properly after the onViewStateRestored() set the views
     */
    open fun onViewLoaded(savedInstanceState: Bundle?, view: View?) {
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

    @VisibleForTesting
    fun getParentActivity(): Activity? {
        return mActivity
    }

    /**
     * Convenience method you can use instead of the original [.requestPermissions].
     * This method only calls [.requestPermissions] when necessary.
     *
     * You should also override [.onRequestPermissionsResult] to check
     * the permissions were granted and, if so, do the action (like use the camera or such).
     */
    fun requestPermissions(requestCode: Int, vararg requiredPermissions: String) {
        val missingPermissions = ArrayList<String>()
        for (permission in requiredPermissions) {
            if (!hasPermission(permission)) {
                missingPermissions.add(permission)
            }
        }

        if (missingPermissions.isNotEmpty()) {
            val permissions = missingPermissions.toTypedArray()
            requestPermissions(permissions, requestCode)
        } else {
            onRequestPermissionsResult(requestCode, emptyList<String>())
        }
    }

    fun hasPermission(permission: String): Boolean {
        val permissionCheck = ContextCompat.checkSelfPermission(mActivity, permission)
        return PackageManager.PERMISSION_GRANTED == permissionCheck
    }


    /**
     * By default calls [.onRequestPermissionsResult]. You may override this.
     */
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        val deniedPermissions = ArrayList<String>()

        for (i in permissions.indices) {
            if (grantResults[i] == PackageManager.PERMISSION_DENIED) {
                deniedPermissions.add(permissions[i])
            }
        }
        onRequestPermissionsResult(requestCode, deniedPermissions)
    }

    /**
     * If you don't override [.onRequestPermissionsResult] this
     * method is called with result of permission request, containing only the permissions denied.
     *
     * Override this method when necessary. You usually want to check that deniedPermissions is empty.
     */
    open fun onRequestPermissionsResult(requestCode: Int, deniedPermissions: List<String>) {
    }

    /**
     * Used to display the navigation button, usually used in forms.
     */
    fun showNavigationHome(enabled: Boolean) {
        (mActivity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(!enabled)
        (mActivity as AppCompatActivity).supportActionBar?.setDisplayShowHomeEnabled(!enabled)
    }

    fun hideKeyboard() {
        GeneralUtils.hideSoftKeyboard(mActivity)
    }

    fun showKeyboard() {
        GeneralUtils.showSoftKeyboard(mActivity)
    }

    @LayoutRes
    abstract fun getLayoutId(): Int

    abstract fun getFragmentTitle(): String?

    abstract fun subscribeToLiveData()

    abstract fun setUpViews()

    abstract fun applyRTLChanges()


}
