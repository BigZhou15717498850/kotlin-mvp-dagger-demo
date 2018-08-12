package com.zhou.life.todo.data.source.local

import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import android.content.Context
import android.support.v4.app.NavUtils
import com.zhou.life.todo.data.Task

/**
 * 作者 LY309313
 * 日期 2018/8/7
 * 描述
 */
@Database(entities = arrayOf(Task::class),version = 1)
abstract class TodoDatabase:RoomDatabase() {

    abstract fun tasksDao():TaskDao

//    companion object {
//        private var INSTANCE:TodoDatabase?=null
//
//        private val lock = Any()
//
//        fun getInstance(context: Context):TodoDatabase{
//            synchronized(lock){
//                if(INSTANCE==null){
//                    INSTANCE = Room.databaseBuilder(context.applicationContext,TodoDatabase::class.java,"Tasks.db").build()
//                }
//                return INSTANCE!!
//            }
//        }
   // }
}