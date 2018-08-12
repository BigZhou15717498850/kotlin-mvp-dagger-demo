package com.zhou.life.todo

/**
 * 作者 LY309313
 * 日期 2018/8/7
 * 描述
 */
interface BasePresenter<T> {

    fun takeView(view:T)

    fun dropView()
}