package com.momin.task.utils

sealed class Resource<T>(
    val data: T? = null,
    val message: String? = null
) {
    class SUCCESS<T>(data: T) : Resource<T>(data)
    class ERROR<T>(data: T? = null, message: String) : Resource<T>(data, message)
    class LOADING<T> : Resource<T>()
}
