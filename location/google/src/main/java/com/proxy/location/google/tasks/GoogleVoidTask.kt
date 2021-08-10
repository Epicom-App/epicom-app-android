package com.proxy.location.google.tasks

import com.proxy.location.OnCompleteListener
import com.proxy.location.Task
import com.google.android.gms.tasks.Task as GoogleTask

internal fun GoogleTask<Void>.toTask(): Task<Void> {
    val googleTask = this
    return object : Task<Void> {

        override val result: Void
            get() = googleTask.result

        override val isSuccessful: Boolean
            get() = googleTask.isSuccessful

        override fun addOnCompleteListener(listener: OnCompleteListener<Void>) {
            googleTask.addOnCompleteListener {
                listener.onComplete(it.toTask())
            }
        }

        override fun addOnCompleteListener(task: (Task<Void>) -> Unit) {
            googleTask.addOnCompleteListener {
                task(it.toTask())
            }
        }
    }
}
