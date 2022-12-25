package com.momin.task.data.remote

import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Headers

interface WebApi {
    @Headers(
        "Content-Type: application/json;charset=utf-8",
        "Content-Encoding: gzip",
        "Accept: application/json"
    )
    @GET("sample/weather_14.json.gz")
    suspend fun getWeathers(): Response<ResponseBody>
}