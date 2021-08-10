package com.proxy.location.huawei.tasks

import com.proxy.location.LocationAvailability
import com.proxy.location.OnCompleteListener
import com.proxy.location.Task
import com.proxy.location.huawei.toLocationAvailability
import com.huawei.hmf.tasks.Task as HuaweiTask
import com.huawei.hms.location.LocationAvailability as HuaweiLocationAvailability

internal fun HuaweiTask<HuaweiLocationAvailability>.toTask(): Task<LocationAvailability> {
    val huaweiTask = this
    return object : Task<LocationAvailability> {

        override val result: LocationAvailability
            get() = huaweiTask.result.toLocationAvailability()

        override val isSuccessful: Boolean
            get() = huaweiTask.isSuccessful

        override fun addOnCompleteListener(listener: OnCompleteListener<LocationAvailability>) {
            huaweiTask.addOnCompleteListener {
                listener.onComplete(task = it.toTask())
            }
        }

        override fun addOnCompleteListener(task: (Task<LocationAvailability>) -> Unit) {
            huaweiTask.addOnCompleteListener {
                task(it.toTask())
            }
        }
    }
}
