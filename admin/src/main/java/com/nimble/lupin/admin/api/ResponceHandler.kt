package com.nimble.lupin.admin.api

data class ResponseHandler<out T> (val code: Int, val message: String, val response: T , val isLastPage :Boolean)