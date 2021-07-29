package com.jp.app.ui.basicSample.activity.viewModel

import androidx.lifecycle.MutableLiveData
import com.jp.app.common.viewModel.IBaseActivityViewModel


interface ISampleActivityViewModel : IBaseActivityViewModel {
    fun loadServerGame()
    fun showToast(): MutableLiveData<Boolean>
}