package com.jp.app.common.viewModel

import androidx.lifecycle.ViewModel

abstract class BaseViewModel : ViewModel(), IBaseViewModel {


    /**
     * on view is destroy clear all the pending calls
     */
    override fun onDestroy() {

    }
}