package com.momin.task.di

import com.momin.task.common.Constants.TIME_OUT
import com.momin.task.common.Constants.BASE_URL
import com.momin.task.data.remote.WebApi
import com.momin.task.data.remote.GzipInterceptor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Singleton
    @Provides
    fun provideRetrofit(): Retrofit {
        val builder = OkHttpClient.Builder()
        val okClient = builder.readTimeout(TIME_OUT, TimeUnit.SECONDS)
            .writeTimeout(TIME_OUT, TimeUnit.SECONDS)
            .connectTimeout(TIME_OUT, TimeUnit.SECONDS)
            .addInterceptor(GzipInterceptor())
            .build()
        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(BASE_URL)
            .client(okClient).build()

    }

    @Provides
    fun providesGzipApi(retrofit: Retrofit): WebApi {
        return retrofit.create(WebApi::class.java)
    }


}