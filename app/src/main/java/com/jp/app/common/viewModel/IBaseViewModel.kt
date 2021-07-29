package com.jp.app.common.viewModel

import androidx.lifecycle.MutableLiveData
import com.jp.app.model.AlertDialogModel

interface IBaseViewModel {
    fun onDestroy ()
    fun showIsLoading(): MutableLiveData<Boolean>
    fun showAlertDialogTwoButtons (): MutableLiveData<AlertDialogModel>
    fun showAlertDialogOneButton(): MutableLiveData<AlertDialogModel>
    fun showErrorMessageDialog(): MutableLiveData<Int>
    fun showErrorMessageDialogString(): MutableLiveData<String?>
    fun showDisplayServerErrorToast(): MutableLiveData<Boolean?>
}