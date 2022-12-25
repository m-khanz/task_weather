package com.momin.task.repository

import com.momin.task.data.remote.WebApi
import okhttp3.ResponseBody
import retrofit2.Response
import javax.inject.Inject

class NetworkRepository @Inject constructor(private val api: WebApi) {
    //Get Json from Remote server
    suspend fun getData(): Response<ResponseBody> {
        return api.getWeathers()
    }

}