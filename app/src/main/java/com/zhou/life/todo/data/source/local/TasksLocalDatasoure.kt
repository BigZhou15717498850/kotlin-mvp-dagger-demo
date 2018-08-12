package com.zhou.life.todo.data.source.local

import com.zhou.life.todo.data.Task
import com.zhou.life.todo.data.source.TasksDatasource
import com.zhou.life.todo.utils.AppExecutors
import javax.inject.Inject
import javax.inject.Singleton

/**
 * 作者 LY309313
 * 日期 2018/8/7
 * 描述
 */
@Singleton
class TasksLocalDatasoure @Inject constructor(
        val appEx:AppExecutors,
        val taskDao: TaskDao
): TasksDatasource {
    override fun getTasks(callBack: TasksDatasource.GetTasksCallBack) {
        //无参可以省略
        back {
            val tasks = taskDao.getTasks()
            ui {
                if(tasks.isEmpty()){
                    callBack.onDataNotAvailable()
                }else
                    callBack.onTasksLoaded(tasks)
            }
        }

    }

    override fun getTask(id: String, callBack: TasksDatasource.GetTaskCallBack) {
        back {
            val task = taskDao.getTaskById(id)
            ui {
                if(task==null){
                    callBack.onDataNotAvailable()
                }else{
                    callBack.onTaskLoaded(task)
                }
            }
        }

    }


    override fun saveTask(task: Task) {
       back { taskDao.insert(task) }
    }

    override fun completeTask(task: Task) {
        this.completeTask(task.id)

    }

    override fun completeTask(id: String) {
        back { taskDao.updateTaskComplete(id,1) }
    }

    override fun activeTask(task: Task) {
        this.activeTask(task.id)
    }

    override fun activeTask(id: String) {
        back { taskDao.updateTaskComplete(id,0) }
    }

    override fun clearCompleteTasks() {
        back { taskDao.clearCompeletedTasks() }
    }

    override fun refreshTasks() {
        //
    }

    override fun deleteAllTasks() {
        back { taskDao.deleteAllTasks() }
    }

    override fun deleteTask(id: String) {
        back { taskDao.deleteTasksById(id) }
    }

    fun  back(f:()->Unit){
        appEx.diskIO.execute{f()}
    }

    fun  ui(f:()->Unit){
        appEx.uiThread.execute{f()}
    }
}

