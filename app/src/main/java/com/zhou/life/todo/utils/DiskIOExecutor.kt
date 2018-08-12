package com.zhou.life.todo.utils

import java.util.concurrent.Executor
import java.util.concurrent.Executors

/**
 * 作者 LY309313
 * 日期 2018/8/7
 * 描述
 */
class DiskIOExecutor:Executor {
    private val diskio = Executors.newSingleThreadExecutor()
    override fun execute(r: Runnable?) {
        diskio.execute(r)
    }

}