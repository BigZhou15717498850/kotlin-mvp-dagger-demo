package com.zhou.life.todo.data.source

import com.zhou.life.todo.data.Task

/**
 * 作者 LY309313
 * 日期 2018/8/7
 * 描述
 */
interface TasksDatasource {

    //两个接口，返回所有数据的接口，和返回一个数据的接口，可用不可用两个反馈
    interface GetTasksCallBack{

        fun onDataNotAvailable()

        fun onTasksLoaded(tasks:List<Task>)
    }

    interface GetTaskCallBack{
        fun onTaskLoaded(task: Task)

        fun onDataNotAvailable()
    }

    fun getTasks(callBack: GetTasksCallBack)

    fun getTask(id:String,callBack: GetTaskCallBack)

    fun saveTask(task: Task)

    fun completeTask(task: Task)

    fun completeTask(id: String)

    fun activeTask(task: Task)

    fun activeTask(id: String)

    fun clearCompleteTasks()

    fun refreshTasks()

    fun deleteAllTasks()

    fun deleteTask(id: String)
}