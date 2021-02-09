package com.venkatasudha.portfolio.di

import com.venkatasudha.portfolio.data.LoginDataSource
import com.venkatasudha.portfolio.data.LoginRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent

@InstallIn(ActivityComponent::class)
@Module
object LoginModule {
    @Provides
    fun provideLoginRepository(dataSource: LoginDataSource): LoginRepository {
        return LoginRepository(dataSource)
    }

    @Provides
    fun provideLoginDataSource(): LoginDataSource {
        return LoginDataSource()
    }
}