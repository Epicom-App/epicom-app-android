package com.proxy.location.google.tasks

import com.proxy.location.LocationAvailability
import com.proxy.location.OnCompleteListener
import com.proxy.location.Task
import com.proxy.location.google.toLocationAvailability
import com.google.android.gms.location.LocationAvailability as GoogleLocationAvailability
import com.google.android.gms.tasks.Task as GoogleTask

internal fun GoogleTask<GoogleLocationAvailability>.toTask(): Task<LocationAvailability> {
    val googleTask = this
    return object : Task<LocationAvailability> {

        override val result: LocationAvailability
            get() = googleTask.result.toLocationAvailability()

        override val isSuccessful: Boolean
            get() = googleTask.isSuccessful

        override fun addOnCompleteListener(listener: OnCompleteListener<LocationAvailability>) {
            googleTask.addOnCompleteListener {
                listener.onComplete(task = it.toTask())
            }
        }

        override fun addOnCompleteListener(task: (Task<LocationAvailability>) -> Unit) {
            googleTask.addOnCompleteListener {
                task(it.toTask())
            }
        }
    }
}
