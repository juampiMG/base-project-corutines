package com.jp.app.common.activity

import com.jp.app.model.AlertDialogModel


interface IBaseActivityCallback {
    fun showLoading(loading: Boolean)
    fun showAlertDialogTwoButtons(alertDialogModel: AlertDialogModel)
    fun showAlertDialogOneButton(alertDialogModel: AlertDialogModel)
    fun showErrorMessageDialog(descriptionError: Int)
    fun showErrorMessageDialog(descriptionError: String?)
    fun showErrorServerMessageToast()
}
