package com.eyeque.eyequeconnect.di
import com.eyeque.enterprisedatamodule.repository.EnterpriseDataRepository
import com.eyeque.enterprisedatamodule.service.EnterpriseService
import com.eyeque.eyequeconnect.BuildConfig
import com.eyeque.eyequeconnect.utils.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    @Provides
    @Singleton
    fun provideOKHttpClient() = if(BuildConfig.DEBUG){
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
        OkHttpClient.Builder().addInterceptor(loggingInterceptor).build()
    }else{
        OkHttpClient.Builder().build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient) =
        Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .baseUrl(Constants.EYEQUE_CONNECT_DEV_DOMAIN)
            .build()

    @Provides
    @Singleton
    fun provideEnterpriseService(retrofit: Retrofit) = retrofit.create(EnterpriseService::class.java)

    @Provides
    @Singleton
    fun provideEnterpriseDataRepository(enterpriseService: EnterpriseService) = EnterpriseDataRepository(enterpriseService)

}