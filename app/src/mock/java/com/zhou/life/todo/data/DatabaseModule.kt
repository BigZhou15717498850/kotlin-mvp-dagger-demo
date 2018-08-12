package com.zhou.life.todo.data

import android.app.Application
import android.arch.persistence.room.Room
import com.zhou.life.todo.data.source.local.TaskDao
import com.zhou.life.todo.data.source.local.TodoDatabase
import com.zhou.life.todo.utils.AppExecutors
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

/**
 * 作者 LY309313
 * 日期 2018/8/8
 * 描述
 */
@Module(includes = arrayOf(TasksRepositoryModule::class))
class DatabaseModule {

    @Singleton
    @Provides
    fun provideRoomData(context: Application): TodoDatabase =  Room.databaseBuilder(context.applicationContext, TodoDatabase::class.java,"Tasks.db").build()

    @Singleton
    @Provides
    fun provideDao(todoDatabase: TodoDatabase): TaskDao {
        return todoDatabase.tasksDao()
    }

    @Singleton
    @Provides
    fun provideAppExecutor(): AppExecutors {
        return AppExecutors()
    }
}