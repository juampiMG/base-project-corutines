package com.jp.app.utils

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import com.jp.app.R
import com.jp.app.common.view.BaseFragment
import com.jp.app.utils.ViewUtils.isRTL

object NavigationUtils {
    enum class Animation {
        IN,
        OUT,
        NONE
    }

    @JvmStatic
    fun navigateToActivity(currentActivity: FragmentActivity, destActivity: Class<*>, bundle: Bundle?) {
        val intent = Intent(currentActivity, destActivity)
        if (bundle != null) intent.putExtras(bundle)
        currentActivity.startActivity(intent)
    }

    @JvmStatic
    fun navigateToActivityNotAddStack(currentActivity: FragmentActivity, destActivity: Class<*>, bundle: Bundle?) {
        val intent = Intent(currentActivity, destActivity)
        if (bundle != null) intent.putExtras(bundle)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        currentActivity.startActivity(intent)
    }

    @JvmStatic
    fun navigateToActivityClearStack(currentActivity: FragmentActivity, destActivity: Class<*>, bundle: Bundle?, enterAnim: Int, outAnim: Int) {
        val intent = Intent(currentActivity, destActivity)
        if (bundle != null) intent.putExtras(bundle)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        currentActivity.startActivity(intent)
        currentActivity.overridePendingTransition(enterAnim, outAnim)
    }


    @JvmStatic
    fun navigateToActivityAnimated(currentActivity: FragmentActivity, destActivity: Class<*>, bundle: Bundle?, enterAnim: Int, outAnim: Int) {
        val intent = Intent(currentActivity, destActivity)
        if (bundle != null) intent.putExtras(bundle)
        currentActivity.startActivity(intent)
        currentActivity.overridePendingTransition(enterAnim, outAnim)
    }

    @JvmStatic
    fun navigateToActivityAnimatedWithResult(currentActivity: FragmentActivity, destActivity: Class<*>, bundle: Bundle?, enterAnim: Int, outAnim: Int, resultCode: Int) {
        val intent = Intent(currentActivity, destActivity)
        if (bundle != null) intent.putExtras(bundle)
        currentActivity.startActivityForResult(intent, resultCode)
        currentActivity.overridePendingTransition(enterAnim, outAnim)
    }


    @JvmStatic
    fun navigateToFragment(activity: FragmentActivity?, supportFragmentManager: FragmentManager, fragment: Fragment?, contentFrame: Int, addToBackStack: Boolean) {
        pushFragment(activity, fragment, supportFragmentManager, contentFrame, addToBackStack, Animation.NONE, true)
    }

    @JvmStatic
    fun navigateToFragmentWithAnimationIn(activity: FragmentActivity?, supportFragmentManager: FragmentManager, fragment: Fragment?, contentFrame: Int, addToBackStack: Boolean) {
        pushFragment(activity, fragment, supportFragmentManager, contentFrame, addToBackStack, Animation.IN, true)
    }

    @JvmStatic
    fun navigateToAddFragmentWithAnimationIn(activity: FragmentActivity?, supportFragmentManager: FragmentManager, fragment: Fragment?, contentFrame: Int, addToBackStack: Boolean) {
        pushFragment(activity, fragment, supportFragmentManager, contentFrame, addToBackStack, Animation.IN, false)
    }

    @JvmStatic
    fun navigateToFragmentWithAnimationOut(activity: FragmentActivity?, supportFragmentManager: FragmentManager, fragment: Fragment?, contentFrame: Int, addToBackStack: Boolean) {
        pushFragment(activity, fragment, supportFragmentManager, contentFrame, addToBackStack, Animation.OUT, true)
    }

    private fun getFragmentTag(fragment: Fragment): String {
        return (fragment as? BaseFragment<*, *>)?.getFragmentId()
                ?: (fragment as Any).javaClass.name
    }


    private fun pushFragment(
            activity: FragmentActivity?,
            fragment: Fragment?,
            supportFragmentManager: FragmentManager,
            contentFrame: Int,
            addToBackStack: Boolean,
            animation: Animation,
            replace: Boolean
    ) {
        if (fragment == null) {
            return
        }

        val fragmentTransaction = supportFragmentManager.beginTransaction()

        val tag = getFragmentTag(fragment)

        if (isRTL()) {
            when (animation) {
                Animation.IN -> fragmentTransaction.setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_right, R.anim.slide_in_right, R.anim.slide_out_left)
                Animation.OUT -> fragmentTransaction.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left, R.anim.slide_in_left, R.anim.slide_out_right)
                Animation.NONE -> true
            }
        } else {
            when (animation) {
                Animation.IN -> fragmentTransaction.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left, R.anim.slide_in_left, R.anim.slide_out_right)
                Animation.OUT -> fragmentTransaction.setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_right, R.anim.slide_in_right, R.anim.slide_out_left)
                Animation.NONE -> true
            }
        }


        if (replace) {
            fragmentTransaction.replace(contentFrame, fragment, tag)
        } else {
            fragmentTransaction.add(contentFrame, fragment, tag)
        }

        if (addToBackStack) {
            fragmentTransaction.addToBackStack(tag)
        }

        fragmentTransaction.commit()

        // Only calling executePendingTransactions() if no nested Fragment call
        if (contentFrame <= 0) {
            activity?.supportFragmentManager?.executePendingTransactions()
        }

    }
}