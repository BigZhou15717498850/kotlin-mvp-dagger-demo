package com.zhou.life.todo.data.source

import com.google.common.collect.Lists
import com.zhou.life.todo.data.Task
import com.zhou.life.todo.di.Local
import com.zhou.life.todo.di.Remote
import javax.inject.Inject
import javax.inject.Singleton

/**
 * 作者 LY309313
 * 日期 2018/8/8
 * 描述
 */
@Singleton
class TasksRepository @Inject constructor(
        @Local val localDatasource: TasksDatasource,
        @Remote val remoteDatasource: TasksDatasource): TasksDatasource {

    var taskCaches = LinkedHashMap<String, Task>()

    var isCacheDirty = false

    override fun getTasks(callBack: TasksDatasource.GetTasksCallBack) {
        if(taskCaches.isNotEmpty()&&!isCacheDirty){
            callBack.onTasksLoaded(Lists.newArrayList(taskCaches.values))
            return
        }

        if(isCacheDirty){
            getTasksFromRemote(callBack)
        }else{
            getTasksFromLocal(callBack)
        }
    }

    private fun getTasksFromLocal(callBack: TasksDatasource.GetTasksCallBack) {
        localDatasource.getTasks(object : TasksDatasource.GetTasksCallBack {
            override fun onDataNotAvailable() {
                getTasksFromRemote(callBack)
            }

            override fun onTasksLoaded(tasks: List<Task>) {
                refreshCache(tasks)
                callBack.onTasksLoaded(tasks)
            }
        })
    }

    private fun getTasksFromRemote(callBack: TasksDatasource.GetTasksCallBack) {
        remoteDatasource.getTasks(object : TasksDatasource.GetTasksCallBack {
            override fun onDataNotAvailable() {
                callBack.onDataNotAvailable()
            }

            override fun onTasksLoaded(tasks: List<Task>) {
                //获取到了数据，要存到缓存里面
                refreshCache(tasks)
                refreshLocalCache(tasks)
                callBack.onTasksLoaded(tasks)
            }
        })
    }

    private fun refreshLocalCache(tasks: List<Task>) {
        tasks.forEach {
            localDatasource.saveTask(it)
        }
    }

    private fun refreshCache(tasks: List<Task>) {
        taskCaches.clear()
        tasks.forEach {
            cachePerfom(it){}
        }
        isCacheDirty=false
    }

    private fun cachePerfom(task: Task, perform:(Task)->Unit) {
        val newtask = Task(task.title, task.description, task.id).apply { isCompleted= task.isCompleted}
        taskCaches.put(newtask.id,newtask)
        perform(newtask)
    }

    override fun getTask(id: String, callBack: TasksDatasource.GetTaskCallBack) {
       //第一步看缓存里面有没有
        val taskInCache = getTaskWithId(id)
        if(taskInCache!=null){
            callBack.onTaskLoaded(taskInCache)
            return
        }

        localDatasource.getTask(id,object : TasksDatasource.GetTaskCallBack {
            override fun onTaskLoaded(task: Task) {
                cachePerfom(task){
                    callBack.onTaskLoaded(it)
                }
            }

            override fun onDataNotAvailable() {
                remoteDatasource.getTask(id,object : TasksDatasource.GetTaskCallBack {
                    override fun onDataNotAvailable() {
                        callBack.onDataNotAvailable()
                    }

                    override fun onTaskLoaded(task: Task) {
                        localDatasource.saveTask(task)
                        cachePerfom(task){
                            callBack.onTaskLoaded(it)
                        }
                    }
                })
            }
        })
    }

    private fun getTaskWithId(taskId: String): Task? {
        return taskCaches[taskId]
    }


    override fun saveTask(task: Task) {
        val newTask = Task(task.title, task.description, task.id).apply {
            isCompleted=task.isCompleted
        }
        remoteDatasource.saveTask(newTask)
        localDatasource.saveTask(newTask)
        taskCaches.put(newTask.id,newTask)
    }

    override fun completeTask(task: Task) {
        cachePerfom(task){
            it.isCompleted=true
            remoteDatasource.completeTask(task)
            localDatasource.completeTask(task)
        }
    }

    override fun completeTask(id: String) {
       getTaskWithId(id)?.let {
           completeTask(it)
       }

    }

    override fun activeTask(task: Task) {
        cachePerfom(task){
            it.isCompleted=false
            remoteDatasource.activeTask(it)
            localDatasource.activeTask(it)
        }
    }

    override fun activeTask(id: String) {
       getTaskWithId(id)?.let {
           activeTask(it)
       }
    }

    override fun clearCompleteTasks() {
        taskCaches =  taskCaches.filterValues { !it.isCompleted } as LinkedHashMap
        remoteDatasource.clearCompleteTasks()
        localDatasource.clearCompleteTasks()
    }

    override fun refreshTasks() {
        isCacheDirty=true
    }

    override fun deleteAllTasks() {
        taskCaches.clear()
        remoteDatasource.deleteAllTasks()
        localDatasource.deleteAllTasks()
    }

    override fun deleteTask(id: String) {
        taskCaches.remove(id)
        remoteDatasource.deleteTask(id)
        localDatasource.deleteTask(id)
    }


}