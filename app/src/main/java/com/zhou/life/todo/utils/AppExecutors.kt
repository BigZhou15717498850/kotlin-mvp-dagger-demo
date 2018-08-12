package com.zhou.life.todo.utils

import android.os.Handler
import android.os.Looper
import java.util.concurrent.Executor
import java.util.concurrent.Executors

/**
 * 作者 LY309313
 * 日期 2018/8/7
 * 描述
 */
const val THREAD_COUNT:Int=3

class AppExecutors(
        val diskIO:Executor=DiskIOExecutor(),
        val networkIO:Executor=Executors.newFixedThreadPool(THREAD_COUNT),
        val uiThread:Executor=MainThread()
){
    private class MainThread:Executor{
        private val mHandler = Handler(Looper.getMainLooper())
        override fun execute(r: Runnable?) {
            mHandler.post(r)
        }
    }
}