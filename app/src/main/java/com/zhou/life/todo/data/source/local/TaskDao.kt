package com.zhou.life.todo.data.source.local

import android.arch.persistence.room.*
import com.zhou.life.todo.data.Task

/**
 * 作者 LY309313
 * 日期 2018/8/7
 * 描述
 */

@Dao interface TaskDao {

    @Query("SELECT * FROM Tasks") fun getTasks():List<Task>

    @Query("SELECT * FROM Tasks WHERE entryid = :tasksId") fun getTaskById(tasksId:String):Task?

    @Insert(onConflict = OnConflictStrategy.REPLACE) fun insert(task:Task)

    @Update fun update(task: Task):Int

    @Query("UPDATE Tasks SET isCompleted = :complete WHERE entryid=:id") fun updateTaskComplete(id:String,complete:Int)

    @Query("DELETE FROM tasks WHERE entryid=:tasksId") fun deleteTasksById(tasksId: String)

    @Query("DELETE FROM tasks") fun deleteAllTasks()

    @Query("DELETE FROM tasks WHERE isCompleted=1") fun clearCompeletedTasks()
}