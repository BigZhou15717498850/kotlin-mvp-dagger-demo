package com.zhou.life.todo.data

import com.zhou.life.todo.data.source.TasksDatasource
import com.zhou.life.todo.data.source.local.TasksLocalDatasoure
import com.zhou.life.todo.data.source.remote.TasksRemoteDataSource
import com.zhou.life.todo.di.Local
import com.zhou.life.todo.di.Remote
import dagger.Binds
import dagger.Module
import javax.inject.Singleton

/**
 * 作者 LY309313
 * 日期 2018/8/8
 * 描述
 */
@Module
abstract class TasksRepositoryModule {

    @Singleton
    @Binds
    @Local
    abstract fun provideLocalDatasource(datasource:TasksLocalDatasoure): TasksDatasource

    @Singleton
    @Binds
    @Remote
    abstract fun provideRemoteDatasource(datasource:TasksRemoteDataSource): TasksDatasource

}