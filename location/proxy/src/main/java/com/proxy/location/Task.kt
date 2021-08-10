package com.proxy.location

interface Task<T> {

    val result: T
    val isSuccessful: Boolean

    fun addOnCompleteListener(listener: OnCompleteListener<T>)
    fun addOnCompleteListener(task: (Task<T>) -> Unit)
}

interface OnCompleteListener<T> {
    fun onComplete(task: Task<T>)
}
