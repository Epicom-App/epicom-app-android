package org.ebolapp.features.permissions.controller

import android.app.Activity
import com.google.android.material.dialog.MaterialAlertDialogBuilder

fun Activity.showPermissionRationaleDialog(
    resourceConfiguration: ResourceConfiguration,
    onOk: () -> Unit = {},
    onCancel: () -> Unit = {}
) {
    MaterialAlertDialogBuilder(this)
        .setTitle(resourceConfiguration.rationalTitleResId)
        .setMessage(resourceConfiguration.rationalMessageResId)
        .setCancelable(true)
        .setPositiveButton(resourceConfiguration.okButton) { dialog, _ ->
            onOk()
            dialog.dismiss()
        }
        .setOnCancelListener { dialog ->
            onCancel()
            dialog.dismiss()
        }
        .create()
        .show()
}


fun Activity.showOpenPermissionSettingsDialog(
    resourceConfiguration: ResourceConfiguration,
    onOk: () -> Unit = {},
    onSettings: () -> Unit = {},
    onCancel: () -> Unit
) {
    MaterialAlertDialogBuilder(this)
        .setTitle(resourceConfiguration.settingsTitleResId)
        .setMessage(resourceConfiguration.settingsMessageResId)
        .setCancelable(true)
        .setPositiveButton(resourceConfiguration.okButton) { dialog, _ ->
            onOk()
            dialog.dismiss()
        }
        .setNegativeButton(
            resourceConfiguration.openSettingsButton
        ) { dialog, _ ->
            onSettings()
            dialog.dismiss()
        }
        .setOnCancelListener { dialog ->
            onCancel()
            dialog.dismiss()
        }
        .create()
        .show()
}