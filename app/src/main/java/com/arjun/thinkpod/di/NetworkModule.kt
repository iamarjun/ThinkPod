package com.arjun.thinkpod.di

import com.arjun.thinkpod.BuildConfig
import com.arjun.thinkpod.network.ITunesApi
import com.arjun.thinkpod.network.XmlOrJsonConverterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@Module
@InstallIn(ApplicationComponent::class)
object NetworkModule {

    @Provides
    fun provideXmlOrJsonConverterFactory(): XmlOrJsonConverterFactory {
        return XmlOrJsonConverterFactory()
    }

    @Provides
    fun provideRetrofitBuilder(
        xjf: XmlOrJsonConverterFactory
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .addConverterFactory(xjf)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    fun provideRestApi(retrofit: Retrofit): ITunesApi {
        return retrofit.create(ITunesApi::class.java)
    }

}