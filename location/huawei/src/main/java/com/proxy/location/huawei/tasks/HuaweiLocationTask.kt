package com.proxy.location.huawei.tasks

import android.location.Location
import com.proxy.location.OnCompleteListener
import com.proxy.location.Task
import com.huawei.hmf.tasks.Task as HuaweiTask

internal fun HuaweiTask<Location?>.toTask(): Task<Location?> {
    val huaweiTask = this
    return object : Task<Location?> {

        override val result: Location?
            get() = huaweiTask.result

        override val isSuccessful: Boolean
            get() = huaweiTask.isSuccessful

        override fun addOnCompleteListener(listener: OnCompleteListener<Location?>) {
            huaweiTask.addOnCompleteListener {
                listener.onComplete(task = it.toTask())
            }
        }

        override fun addOnCompleteListener(task: (Task<Location?>) -> Unit) {
            huaweiTask.addOnCompleteListener {
                task(it.toTask())
            }
        }
    }
}
