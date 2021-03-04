package com.dscvit.keats.di.modules

import com.dscvit.keats.network.ApiInterface
import com.dscvit.keats.network.PreAuthApiService
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@InstallIn(SingletonComponent::class)
@Module
object AuthModule {
    @Provides
    fun moshiProvider(): Moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()

    @Provides
    fun preAuthApiProvider(moshi: Moshi): ApiInterface = PreAuthApiService.createRetrofit(moshi)
}
