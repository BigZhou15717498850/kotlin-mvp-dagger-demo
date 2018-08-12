package com.zhou.life.todo.tasks

import com.zhou.life.todo.BasePresenter
import com.zhou.life.todo.BaseView
import com.zhou.life.todo.data.Task

/**
 * 作者 LY309313
 * 日期 2018/8/12
 * 描述
 */
interface TasksContract {
    interface Presenter:BasePresenter<View>{
        var tasksFilterLabel:TasksFilterLabel

        fun loadTasks(forceUpdate:Boolean)

        fun addNewTask()

        fun openTaskDetailUi(task:Task)

        fun activeTask(task: Task)

        fun completeTask(task: Task)

        fun result(requestCode:Int,response:Int)

        fun clearCompletedTasks()

    }
    interface View:BaseView{
        var isActive:Boolean

        fun setloadingIndicator(show:Boolean)

        fun showTasks(tasks:List<Task>)

        fun showAddTask()

        fun showTaskDetailUi(taskid:String)

        fun showTaskMarkedActive()

        fun showTaskMarkedCompleted()

        fun showActiveFilterLabel()

        fun showCompletedFilterLabel()

        fun showAllFilterLabel()

        fun showCompletedTasksCleared()

        fun showLoadingTasksError()

        fun showNoTasks()

        fun showNoActiveTasks()

        fun showNoCompletedTasks()

        fun showSuccessfullySavedMessage()

        fun showFilteringPopUpMenu()

    }
}