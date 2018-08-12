package com.zhou.life.todo.tasks

import com.zhou.life.todo.data.Task
import com.zhou.life.todo.data.source.TasksDatasource
import com.zhou.life.todo.data.source.TasksRepository
import com.zhou.life.todo.di.ActivityScope
import javax.inject.Inject

/**
 * 作者 LY309313
 * 日期 2018/8/12
 * 描述
 */
@ActivityScope
class TasksPresenter @Inject constructor(val tasksRepository: TasksRepository):TasksContract.Presenter {


    override var tasksFilterLabel: TasksFilterLabel=TasksFilterLabel.ALL_TASKS

    private var tasksView:TasksContract.View?=null

    private var fristload:Boolean=true

    override fun loadTasks(forceUpdate: Boolean) {
        loadTasks(forceUpdate||fristload,true)
        fristload=false
    }

    private fun loadTasks(update:Boolean,showLoadingUi:Boolean){
        if(showLoadingUi)tasksView?.setloadingIndicator(true)
        if(update)tasksRepository.refreshTasks()
        tasksRepository.getTasks(object :TasksDatasource.GetTasksCallBack{
            override fun onDataNotAvailable() {
                tasksView?.setloadingIndicator(false)
                tasksView?.showLoadingTasksError()
            }

            override fun onTasksLoaded(tasks: List<Task>) {
                val showdTasks = ArrayList<Task>()
                for (task in tasks){
                    when(tasksFilterLabel){
                        TasksFilterLabel.ACTIVE_TASKS->if(task.isActive)showdTasks.add(task)
                        TasksFilterLabel.COMPLETE_TASKS->if(task.isCompleted)showdTasks.add(task)
                        else->showdTasks.add(task)
                    }
                }
                if(tasksView==null||!tasksView!!.isActive)return
                if(showLoadingUi)tasksView!!.setloadingIndicator(false)
                processTasks(tasks)

            }

        })
    }

    private fun processTasks(tasks: List<Task>) {
        if(tasks.isEmpty())processEmptyTasks()
        else{
            tasksView?.showTasks(tasks)
            showFilterLabel()
        }
    }

    private fun showFilterLabel() {
        when(tasksFilterLabel){
            TasksFilterLabel.ACTIVE_TASKS->tasksView?.showActiveFilterLabel()
            TasksFilterLabel.COMPLETE_TASKS->tasksView?.showCompletedFilterLabel()
            else->tasksView?.showAllFilterLabel()
        }
    }

    private fun processEmptyTasks() {
        when(tasksFilterLabel){
            TasksFilterLabel.ACTIVE_TASKS->tasksView?.showNoActiveTasks()
            TasksFilterLabel.COMPLETE_TASKS->tasksView?.showNoCompletedTasks()
            else->tasksView?.showNoTasks()
        }
    }

    override fun addNewTask() {
        tasksView?.showAddTask()
    }

    override fun openTaskDetailUi(task: Task) {
        tasksView?.showTaskDetailUi(taskid = task.id)
    }

    override fun activeTask(task: Task) {
        tasksRepository.activeTask(task)
        tasksView?.showTaskMarkedActive()
    }

    override fun completeTask(task: Task) {
        tasksRepository.completeTask(task)
        tasksView?.showTaskMarkedCompleted()
    }

    override fun result(requestCode: Int, response: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun clearCompletedTasks() {
       tasksRepository.clearCompleteTasks()
        tasksView?.showCompletedTasksCleared()
    }



    override fun takeView(view: TasksContract.View) {
        this.tasksView=view
        loadTasks(false)
    }

    override fun dropView() {
        this.tasksView = null
    }
}