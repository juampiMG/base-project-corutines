package com.jp.app.ui.basicSample.viewModel

import androidx.lifecycle.MutableLiveData
import com.jp.app.common.viewModel.IBaseActivityViewModel

interface ISampleFragmentViewModel : IBaseActivityViewModel {
    fun loadData()
    fun loadGame (): MutableLiveData <Boolean>
}
