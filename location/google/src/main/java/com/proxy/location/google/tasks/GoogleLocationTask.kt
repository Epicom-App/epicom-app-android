package com.proxy.location.google.tasks

import com.proxy.location.OnCompleteListener
import com.proxy.location.Task
import android.location.Location as AndroidLocation
import com.google.android.gms.tasks.Task as GoogleTask

internal fun GoogleTask<AndroidLocation?>.toTask(): Task<AndroidLocation?> {
    val googleTask = this
    return object : Task<AndroidLocation?> {

        override val result: AndroidLocation?
            get() = googleTask.result

        override val isSuccessful: Boolean
            get() = googleTask.isSuccessful

        override fun addOnCompleteListener(listener: OnCompleteListener<AndroidLocation?>) {
            googleTask.addOnCompleteListener {
                listener.onComplete(task = it.toTask())
            }
        }

        override fun addOnCompleteListener(task: (Task<AndroidLocation?>) -> Unit) {
            googleTask.addOnCompleteListener {
                task(it.toTask())
            }
        }
    }
}
