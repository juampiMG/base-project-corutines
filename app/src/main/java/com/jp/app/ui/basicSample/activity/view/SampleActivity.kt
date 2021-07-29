package com.jp.app.ui.basicSample.activity.view

import android.os.Bundle
import com.jp.app.R
import com.jp.app.common.activity.BaseActivity
import com.jp.app.ui.basicSample.activity.viewModel.ISampleActivityViewModel
import com.jp.app.ui.basicSample.view.SampleFragment
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

/**
 * Remember to add activity to the AndroidManifest.xml and to the InjectorModule.kt
 */
@AndroidEntryPoint
class SampleActivity @Inject constructor(): BaseActivity<ISampleActivityViewModel>(),
        SampleFragment.FragmentCallback {

    override fun getLayoutId(): Int {
        return R.layout.generic_activity
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        savedInstanceState?.let {
            supportFragmentManager.findFragmentById(R.id.content)?.let { fr ->
                mCurrentFragment = fr
                loadFragment(addToBackStack = false)
            }
        }?: run {
            mCurrentFragment = SampleFragment.newInstance(mExtras)
            loadFragment(addToBackStack = false)
        }

    }

    override fun subscribeToLiveData() {
        mActivityViewModel.showToast().observe(this,  {
            showErrorMessageDialog(getString (R.string.hello_activity))
        })
    }

    override fun loadFromActivityGame() {
        mActivityViewModel.loadServerGame()
    }

}