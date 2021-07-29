package com.jp.app.model

import android.content.DialogInterface
import java.io.Serializable

data class AlertDialogModel(
        var title: Int,
        var message: Int,
        var messageArg: List<Pair<Int, String?>>,
        var leftButtonTitle: Int,
        var leftButtonListener: DialogInterface.OnClickListener?,
        var rightButtonTitle: Int,
        var rightButtonListener: DialogInterface.OnClickListener?
) : Serializable