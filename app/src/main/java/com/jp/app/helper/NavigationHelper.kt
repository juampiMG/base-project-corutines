package com.jp.app.helper

import android.os.Bundle
import androidx.fragment.app.FragmentActivity
import com.jp.app.R
import com.jp.app.ui.basicSample.activity.view.SampleActivity
import com.jp.app.utils.NavigationUtils

class NavigationHelper constructor(var mActivity: FragmentActivity, var mExtras: Bundle) {

    fun navigateToSampleActivity() {
        NavigationUtils.navigateToActivityClearStack(
                mActivity,
                SampleActivity::class.java,
                mExtras,
                R.anim.slide_in_up,
                R.anim.stay
        )
    }

}
