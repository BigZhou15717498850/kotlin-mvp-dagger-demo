package com.zhou.life.todo

import com.zhou.life.todo.data.Task
import com.zhou.life.todo.data.source.TasksRepository
import com.zhou.life.todo.di.DaggerAppComponent
import dagger.android.AndroidInjector
import dagger.android.support.DaggerApplication
import javax.inject.Inject

/**
 * 作者 LY309313
 * 日期 2018/8/8
 * 描述
 */
class TodoApp:DaggerApplication() {

    var tasksRepository: TasksRepository?=null
        @Inject set

    override fun applicationInjector(): AndroidInjector<out DaggerApplication> =
            DaggerAppComponent.builder().application(this).build()

    override fun onCreate() {
        super.onCreate()
        tasksRepository?.saveTask(Task())
    }
}