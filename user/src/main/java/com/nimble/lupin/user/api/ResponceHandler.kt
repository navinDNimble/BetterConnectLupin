package com.nimble.lupin.user.api

sealed class ResponseHandler<out T> {
    object Loading : ResponseHandler<Nothing>()
    object OnEmptyData : ResponseHandler<Nothing>()
    class OnFailed(val code: Int, val message: String?, val messageCode: String) : ResponseHandler<Nothing>()
    class OnSuccessResponse<T>(val response: T) : ResponseHandler<T>()
}