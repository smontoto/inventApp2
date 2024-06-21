package com.montoto.inventapp.di

import android.app.Application
import com.google.gson.FieldNamingPolicy
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.montoto.inventapp.BuildConfig
import com.montoto.inventapp.data.repository.ArticlesRepositoryImpl
import com.montoto.inventapp.data.repository.FilesImageRepositoryImpl
import com.montoto.inventapp.domain.*
import com.montoto.inventapp.network.InventoryService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(ActivityRetainedComponent::class)
class UseCaseModule {

    @Provides
    fun provideGetArticlesUseCase(api: InventoryService): GetArticlesUseCase = GetArticlesUseCase(ArticlesRepositoryImpl(api))

    @Provides
    fun provideGetFilesImageUseCase(api: InventoryService, context: Application) = GetFilesImageUseCase(FilesImageRepositoryImpl(api, context))

    @Provides
    fun provideSendFilesImageUseCase(api: InventoryService, context: Application) = SendImageFilesUseCase(FilesImageRepositoryImpl(api, context))

    @Provides
    fun provideSendNewArticleUseCase(api: InventoryService) = SendNewArticleUseCase(ArticlesRepositoryImpl(api))

    @Provides
    fun provideMarkArticleAsDeliveredUseCase(api: InventoryService) = MarkArticleAsDelivered(ArticlesRepositoryImpl(api))

}

@Module
@InstallIn(SingletonComponent::class)
class NetworkModule {

    @Singleton
    @Provides
    fun provideRetrofitInstance(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(provideGsonInstance()))
            .client(provideOkHttpInstance(provideHttpInterceptor()))
            .build()
    }

    @Provides
    fun provideHttpInterceptor() = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    @Provides
    fun provideOkHttpInstance(interceptor: HttpLoggingInterceptor) =
        OkHttpClient.Builder()
            .addInterceptor(interceptor)
            .build()

    @Singleton
    @Provides
    fun provideGsonInstance(): Gson {
        return GsonBuilder()
            .enableComplexMapKeySerialization()
            .serializeNulls()
            .setPrettyPrinting()
            .setLenient()
            .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
            .setVersion(1.0)
            .create()
    }
}

@Module
@InstallIn(SingletonComponent::class)
class ServiceModule {
    @Singleton
    @Provides
    fun provideInventoryService(): InventoryService {
        return NetworkModule().provideRetrofitInstance().create(InventoryService::class.java)
    }

}