package com.jp.app.common.viewModel

import android.content.DialogInterface
import androidx.lifecycle.MutableLiveData
import com.jp.app.model.AlertDialogModel

interface IBaseActivityViewModel : IBaseViewModel {}

abstract class BaseActivityViewModel : BaseViewModel(), IBaseActivityViewModel {

    private val mIsLoading = MutableLiveData<Boolean>()

    private val mAlertDialogModelTwoButtons = MutableLiveData<AlertDialogModel>()
    private val mAlertDialogModelOneButton = MutableLiveData<AlertDialogModel>()
    private val mAlertDialogError = MutableLiveData<Int>()
    private val mAlertDialogErrorString = MutableLiveData<String?>()
    private val mDisplayServerErrorToast = MutableLiveData<Boolean?>()


    // =============== IBaseViewModel ==============================================================

    /**
     * on view is destroy clear all the pending calls
     */
    override fun onDestroy() {

    }

    override fun showAlertDialogTwoButtons(): MutableLiveData<AlertDialogModel> {
        return mAlertDialogModelTwoButtons
    }

    override fun showAlertDialogOneButton(): MutableLiveData<AlertDialogModel> {
        return mAlertDialogModelOneButton
    }

    override fun showErrorMessageDialog(): MutableLiveData<Int> {
        return mAlertDialogError
    }


    override fun showErrorMessageDialogString(): MutableLiveData<String?> {
        return mAlertDialogErrorString
    }

    override fun showDisplayServerErrorToast(): MutableLiveData<Boolean?> {
        return mDisplayServerErrorToast
    }

    override fun showIsLoading(): MutableLiveData<Boolean> {
        return mIsLoading
    }

    fun isLoading(visibility: Boolean) {
        mIsLoading.value = visibility
    }

    fun setAlertDialogModelTwoButtons(
        title: Int,
        message: Int,
        leftButtonTitle: Int,
        leftButtonListener: DialogInterface.OnClickListener?,
        rightButtonTitle: Int,
        rightButtonListener: DialogInterface.OnClickListener?
    ) {
        val alert = AlertDialogModel(
            title,
            message,
            ArrayList(),
            leftButtonTitle,
            leftButtonListener,
            rightButtonTitle,
            rightButtonListener
        )
        mAlertDialogModelTwoButtons.value = alert
    }


    fun setAlertDialogModelTwoButtons(
        title: Int,
        message: Int,
        messageArg: List<Pair<Int, String>>,
        leftButtonTitle: Int,
        leftButtonListener: DialogInterface.OnClickListener?,
        rightButtonTitle: Int,
        rightButtonListener: DialogInterface.OnClickListener?
    ) {
        val alert = AlertDialogModel(
            title,
            message,
            messageArg,
            leftButtonTitle,
            leftButtonListener,
            rightButtonTitle,
            rightButtonListener
        )
        mAlertDialogModelTwoButtons.value = alert
    }

    fun setAlertDialogModelOneButton(
        title: Int,
        message: Int,
        messageArg: List<Pair<Int, String?>>,
        rightButtonTitle: Int,
        rightButtonListener: DialogInterface.OnClickListener?
    ) {
        val alert = AlertDialogModel(title, message, messageArg, 0, null, rightButtonTitle, rightButtonListener)
        mAlertDialogModelOneButton.value = alert
    }

    fun setAlertDialogModelOneButton(
        title: Int,
        message: Int,
        rightButtonTitle: Int,
        rightButtonListener: DialogInterface.OnClickListener?
    ) {
        val alert = AlertDialogModel(title, message, ArrayList(), 0, null, rightButtonTitle, rightButtonListener)
        mAlertDialogModelOneButton.value = alert
    }

    fun setAlertDialogError(error: Int) {
        mAlertDialogError.value = error
    }

    fun setAlertDialogError(error: String?) {
        mAlertDialogErrorString.value = error
    }

    fun setDisplayServerErrorToast() {
        mDisplayServerErrorToast.value = true
        mDisplayServerErrorToast.postValue(null)
    }

}
