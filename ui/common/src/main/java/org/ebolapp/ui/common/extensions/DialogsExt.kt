package org.ebolapp.ui.map.views

import android.content.Context
import androidx.annotation.StringRes
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import org.ebolapp.ui.common.R


fun Context.showErrorDialog(
    @StringRes titleResId: Int,
    @StringRes messageResId: Int,
    @StringRes positiveResId: Int = R.string.ok_button,
    @StringRes negativeResId: Int = R.string.refresh_button,
    onPositive: () -> Unit = {},
    onNegative: () -> Unit = {}
) {
    MaterialAlertDialogBuilder(this)
        .setTitle(titleResId)
        .setMessage(messageResId)
        .setCancelable(false)
        .setPositiveButton(positiveResId) { dialog, _ ->
            onPositive()
            dialog.dismiss()
        }.setNegativeButton(negativeResId) { dialog, _ ->
            onNegative()
            dialog.dismiss()
        }.show()
}