package com.venkatasudha.portfolio.android.di

import com.google.firebase.auth.FirebaseAuth
import com.venkatasudha.portfolio.android.data.LoginDataSource
import com.venkatasudha.portfolio.android.data.LoginRepository
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
    fun provideLoginDataSource(auth: FirebaseAuth): LoginDataSource {
        return LoginDataSource(auth)
    }
}