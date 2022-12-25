package com.momin.task.data.remote

import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import okhttp3.ResponseBody
import okio.GzipSource
import okio.buffer
import java.io.BufferedReader
import java.io.IOException
import java.io.StringReader

/**
 * This file is an interceptor for Retrofit
 * We are getting .gz file from API, we are using interceptor to
 * extract json from .gz file in a proper format
 * or else, getting data may cause errors and crashes
 */
class GzipInterceptor : Interceptor {
    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val newRequest: Request.Builder = chain.request().newBuilder()
        newRequest.addHeader("Accept-Encoding", "gzip")
        val response: Response = chain.proceed(newRequest.build())
        return unzip(response)
    }

    private fun unzip(response: Response): Response {
        if (response.body == null) {
            return response
        }
        val gzipSource = GzipSource(response.body!!.source())
        val bodyString = gzipSource.buffer().readUtf8()
        val jsonString = toJson(bodyString)
        val responseBody = ResponseBody.create(response.body!!.contentType(), jsonString)
        val strippedHeaders = response.headers.newBuilder()
            .removeAll("Content-Encoding")
            .removeAll("Content-Length")
            .build()
        return response.newBuilder()
            .headers(strippedHeaders)
            .body(responseBody)
            .message(response.message)
            .build()
    }

    private fun toJson(input: String?): String {
        val reader = BufferedReader(StringReader(input))
        val out = StringBuilder()
        out.append("[")
        var line: String?
        while (reader.readLine().also { line = it } != null) {
            out.append(line)
            out.append(",")
        }
        out.append("]")
        return out.toString()

    }
}