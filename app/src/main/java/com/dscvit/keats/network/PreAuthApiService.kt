package com.dscvit.keats.network

import com.dscvit.keats.utils.Constants
import com.squareup.moshi.Moshi
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import timber.log.Timber

object PreAuthApiService {
    fun createRetrofit(moshi: Moshi): ApiInterface {
        val retrofit = Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .client(getOkHttpClient())
            .build()

        return retrofit.create(ApiInterface::class.java)
    }

    private fun getOkHttpClient(): OkHttpClient {
        val httpClient = OkHttpClient.Builder()
        val logging = HttpLoggingInterceptor { message -> Timber.tag("OkHttp").d(message) }
        logging.setLevel(HttpLoggingInterceptor.Level.BODY)
        httpClient.addInterceptor { chain ->
            val original = chain.request()
            val requestBuilder = original.newBuilder()
                .addHeader(
                    "Content-Type",
                    "application/json"
                )
            val request = requestBuilder.build()
            return@addInterceptor chain.proceed(request)
        }
            .addInterceptor(logging)
        return httpClient.build()
    }
}
