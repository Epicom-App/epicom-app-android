package org.ebolapp.features.permissions.controller

import android.Manifest.permission.*
import android.app.Activity
import android.app.Application
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import androidx.core.app.ActivityCompat
import androidx.core.content.PermissionChecker
import kotlinx.coroutines.flow.MutableStateFlow
import org.ebolapp.features.permissions.models.Permission
import org.ebolapp.logging.Logging
import java.lang.ref.WeakReference

private const val DEFAULT_REQUEST_CODE = 1001

internal class AndroidPermissionController(
    private val context: Context,
    private val resourceConfiguration: ResourceConfiguration
) : PermissionController, ActivityCompat.OnRequestPermissionsResultCallback, Logging {
    override val availablePermissions = MutableStateFlow(loadAvailablePermissions())

    var activityHolder: WeakReference<Activity>? = null

    // This needs to be called before the first activity is started!
    override fun init() {
        (context.applicationContext as Application).registerActivityLifecycleCallbacks(
            MyActivityLifecycleCallbacks()
        )
    }

    override suspend fun requestPermissions(vararg permissions: Permission) {
        val activity = activityHolder?.get() ?: return

        val requiredAndroidPermissions =
            permissions.filter { !hasPermission(it) }.map { it.toAndroidPermission() }.flatten()
                .distinct().toMutableSet()
        if (requiredAndroidPermissions.isEmpty()) return

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.R &&
            requiredAndroidPermissions.contains(ACCESS_BACKGROUND_LOCATION)
        ) {
            // TODO we need to change the flow and also ask for the other permissions later.
            // maybe requesting this permission should be part of another user flow?
            activity.showOpenPermissionSettingsDialog(
                resourceConfiguration = resourceConfiguration,
                onCancel = {},
                onSettings = {
                    val intent = Intent(
                        Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                        Uri.fromParts("package", context.packageName, null)
                    )
                    if (intent.resolveActivity(context.packageManager) != null) {
                        activity.startActivity(intent)
                    } else {
                        error("Could not open Settings")
                    }
                }
            )
            requiredAndroidPermissions.remove(ACCESS_BACKGROUND_LOCATION)
        } else {

            val anyPermissionNeedsRational = requiredAndroidPermissions.any {
                ActivityCompat.shouldShowRequestPermissionRationale(activity, it)
            }

            if (anyPermissionNeedsRational) {
                activity.showPermissionRationaleDialog(
                    resourceConfiguration = resourceConfiguration,
                    onOk = { requestAndroidPermissions(activity, requiredAndroidPermissions) }
                )
            } else {
                requestAndroidPermissions(activity, requiredAndroidPermissions)
            }
        }
    }

    private fun hasPermission(permission: Permission): Boolean {
        return permission.toAndroidPermission().all {
            PermissionChecker.checkSelfPermission(context, it) ==
                    PermissionChecker.PERMISSION_GRANTED
        }
    }

    private fun requestAndroidPermissions(activity: Activity, permissions: Collection<String>) {
        ActivityCompat.requestPermissions(
            activity,
            permissions.toTypedArray(),
            DEFAULT_REQUEST_CODE
        )
    }


    private fun loadAvailablePermissions(): Set<Permission> {
        return Permission.values().filter { hasPermission(it) }.toSet()
    }

    // ActivityCompat.OnRequestPermissionsResultCallback

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        // we can ignore the request and just refresh the permissions
        availablePermissions.value = loadAvailablePermissions()
    }

    private inner class MyActivityLifecycleCallbacks : Application.ActivityLifecycleCallbacks {
        // Application.ActivityLifecycleCallbacks

        override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {}

        override fun onActivityStarted(activity: Activity) {
            activityHolder = WeakReference(activity)
        }

        override fun onActivityResumed(activity: Activity) {
            availablePermissions.value = loadAvailablePermissions()
        }

        override fun onActivityPaused(activity: Activity) {}

        override fun onActivityStopped(activity: Activity) {
            if (activityHolder?.get() != activity) return

            activityHolder = null
        }

        override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {}

        override fun onActivityDestroyed(activity: Activity) {}
    }
}

internal fun Permission.toAndroidPermission(): List<String> = when (this) {
    Permission.FOREGROUND_LOCATION -> listOf(ACCESS_FINE_LOCATION, ACCESS_COARSE_LOCATION)
    Permission.BACKGROUND_LOCATION -> if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q) {
        listOf(ACCESS_FINE_LOCATION, ACCESS_COARSE_LOCATION, ACCESS_BACKGROUND_LOCATION)
    } else {
        listOf(ACCESS_COARSE_LOCATION, ACCESS_FINE_LOCATION)
    }
}
