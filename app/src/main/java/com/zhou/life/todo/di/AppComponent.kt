package com.zhou.life.todo.di

import android.app.Application
import com.zhou.life.todo.TodoApp
import com.zhou.life.todo.data.DatabaseModule
import com.zhou.life.todo.data.source.TasksRepository
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton

/**
 * 作者 LY309313
 * 日期 2018/8/8
 * 描述
 */
@Singleton
@Component(modules = arrayOf(
        ApplicationModule::class,
        DatabaseModule::class,
        AndroidSupportInjectionModule::class)
)
interface AppComponent:AndroidInjector<TodoApp> {

    fun tasksRepository(): TasksRepository

    @Component.Builder
    interface Builder{
        @BindsInstance
        fun application(app:Application):Builder

        fun build():AppComponent
    }
}