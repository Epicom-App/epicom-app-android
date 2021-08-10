package com.proxy.location.google.tasks

import com.google.android.gms.tasks.Task as GoogleTask
import com.google.android.gms.location.LocationSettingsResponse as GoogleLocationSettingsResponse
import com.proxy.location.LocationSettingsResponse
import com.proxy.location.OnCompleteListener
import com.proxy.location.Task
import com.proxy.location.google.toLocationSettingsResponse

internal fun GoogleTask<GoogleLocationSettingsResponse>.toTask(): Task<LocationSettingsResponse> {
   val googleTask = this
   return object : Task<LocationSettingsResponse> {

       override val result: LocationSettingsResponse
           get() = googleTask.result.toLocationSettingsResponse()

       override val isSuccessful: Boolean
           get() = googleTask.isSuccessful

       override fun addOnCompleteListener(listener: OnCompleteListener<LocationSettingsResponse>) {
           googleTask.addOnCompleteListener {
               listener.onComplete(it.toTask())
           }
       }

       override fun addOnCompleteListener(task: (Task<LocationSettingsResponse>) -> Unit) {
           googleTask.addOnCompleteListener {
               task(it.toTask())
           }
       }


   }
}
