package com.dscvit.keats.network

import android.content.Context
import com.dscvit.keats.utils.Constants
import com.dscvit.keats.utils.PreferenceHelper
import com.squareup.moshi.Moshi
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import timber.log.Timber

object ApiService {
    fun createRetrofit(moshi: Moshi, context: Context): ApiInterface {
        val retrofit = Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .client(getOkHttpClient(context))
            .build()

        return retrofit.create(ApiInterface::class.java)
    }

    private fun getOkHttpClient(context: Context): OkHttpClient {
        val sharedPreferences = PreferenceHelper.customPrefs(context, Constants.PREF_NAME)
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
                .addHeader(
                    "Authorization",
                    "Bearer ${sharedPreferences.getString(Constants.PREF_AUTH_KEY, "")}"
                )
            val request = requestBuilder.build()
            return@addInterceptor chain.proceed(request)
        }
            .addInterceptor(logging)
        return httpClient.build()
    }
}
