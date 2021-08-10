package com.proxy.location.huawei.tasks

import com.proxy.location.OnCompleteListener
import com.proxy.location.Task
import com.huawei.hmf.tasks.Task as HuaweiTask

internal fun HuaweiTask<Void>.toTask(): Task<Void> {
    val huaweiTask = this
    return object : Task<Void> {

        override val result: Void
            get() = huaweiTask.result

        override val isSuccessful: Boolean
            get() = huaweiTask.isSuccessful

        override fun addOnCompleteListener(listener: OnCompleteListener<Void>) {
            huaweiTask.addOnCompleteListener {
                listener.onComplete(it.toTask())
            }
        }

        override fun addOnCompleteListener(task: (Task<Void>) -> Unit) {
            huaweiTask.addOnCompleteListener {
                task(it.toTask())
            }
        }
    }
}
