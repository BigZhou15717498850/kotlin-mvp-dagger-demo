package com.zhou.life.todo.data.source.remote

import android.os.Handler
import com.google.common.collect.Lists
import com.zhou.life.todo.data.Task
import com.zhou.life.todo.data.source.TasksDatasource
import javax.inject.Inject
import javax.inject.Singleton

/**
 * 作者 LY309313
 * 日期 2018/8/8
 * 描述
 */
@Singleton
class TasksRemoteDataSource @Inject constructor(): TasksDatasource {

    private val SERVICE_LATENCY_IN_MILIIS = 5000L
    private var DATAS = LinkedHashMap<String,Task>()

    init {
        addTask("标题1","第一个任务得")
        addTask("标题2","第二个任务得")
    }

    private fun addTask(title: String, desciption: String) {
        val task = Task(title,desciption)
        DATAS.put(task.id,task)
    }

    override fun getTasks(callBack: TasksDatasource.GetTasksCallBack) {
        val tasks = Lists.newArrayList(DATAS.values)
        delay {  callBack.onTasksLoaded(tasks) }
    }

    override fun getTask(id: String, callBack: TasksDatasource.GetTaskCallBack) {
        val task = DATAS[id]
        delay {
            if(task==null){
                callBack.onDataNotAvailable()
            }else{
                callBack.onTaskLoaded(task)
            }
        }
    }


    override fun saveTask(task: Task) {
       DATAS.put(task.id,task)

    }

    override fun completeTask(task: Task) {
       val newTask = with(task){
           Task(title,description,id).apply {
               isCompleted=true
           }
       }
        DATAS.put(newTask.id,newTask)
    }

    override fun completeTask(id: String) {
        val task = DATAS[id]
        if(task!=null)
        this.completeTask(task)
    }

    override fun activeTask(task: Task) {
        val newTask = with(task){
            Task(title,description,id).apply {
                isCompleted=false
            }
        }
        DATAS.put(newTask.id,newTask)
    }

    override fun activeTask(id: String) {
        val task = DATAS[id]
        if(task!=null)
            this.activeTask(task)
    }

    override fun clearCompleteTasks() {
        DATAS = DATAS.filterValues { !it.isCompleted } as LinkedHashMap
    }

    override fun refreshTasks() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun deleteAllTasks() {
        DATAS.clear()
    }

    override fun deleteTask(id: String) {
        DATAS.remove(id)
    }

    fun delay(f:()->Unit){
        Handler().postDelayed({f()},SERVICE_LATENCY_IN_MILIIS)
    }
}