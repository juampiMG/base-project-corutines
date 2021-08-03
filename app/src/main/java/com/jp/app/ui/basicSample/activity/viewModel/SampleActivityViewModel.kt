package com.jp.app.ui.basicSample.activity.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.jp.app.common.viewModel.BaseActivityViewModel
import com.jp.domain.interactor.IGetSampleBooleanUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import javax.inject.Inject


class SampleActivityViewModel
@Inject
constructor() : BaseActivityViewModel(), ISampleActivityViewModel {

    @Inject
    lateinit var mGetSampleBooleanUseCase: IGetSampleBooleanUseCase

    private var showToast = MutableLiveData<Boolean>()

    override fun loadServerGame() {
        showToast.value  = true
        viewModelScope.launch {
            val result = async(Dispatchers.Main) {
                mGetSampleBooleanUseCase.execute()
            }
            if (result.await().isSuccessful) {
                setAlertDialogError("Ha ido bien Activity")
            }
        }
    }

    override fun showToast(): MutableLiveData<Boolean> {
        return showToast
    }

}