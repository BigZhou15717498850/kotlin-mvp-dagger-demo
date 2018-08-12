package com.zhou.life.todo.di

import android.app.Application
import android.content.Context
import dagger.Binds
import dagger.Module
import javax.inject.Singleton

/**
 * 作者 LY309313
 * 日期 2018/8/8
 * 描述
 */
@Module
abstract class ApplicationModule {
    @Singleton
    @Binds
    abstract fun bindContext(application: Application):Context
}