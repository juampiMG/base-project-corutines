package com.jp.app.ui.basicSample.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.jp.app.common.viewModel.BaseFragmentViewModel
import com.jp.domain.interactor.IGetSampleBooleanUseCase
import kotlinx.coroutines.launch
import retrofitCallLaunch
import javax.inject.Inject

class SampleFragmentViewModel
@Inject
constructor() : BaseFragmentViewModel(), ISampleFragmentViewModel {

    private var mLoadGame = MutableLiveData<Boolean>()

    @Inject
    lateinit var mGetSampleBooleanUseCase: IGetSampleBooleanUseCase

    override fun loadData() {
        viewModelScope.launch {
            retrofitCallLaunch(
                    asyncCall = {
                        mGetSampleBooleanUseCase.execute()
                    },
                    onStart = {
                        isLoading(true)
                    },
                    onError = {
                        isLoading(false)
                        setAlertDialogError("Ha fallado Fragment")
                    }
            ) {
                isLoading(false)
                mLoadGame.value = it
            }
        }
    }

    override fun loadGame(): MutableLiveData<Boolean> {
        return mLoadGame
    }

}
