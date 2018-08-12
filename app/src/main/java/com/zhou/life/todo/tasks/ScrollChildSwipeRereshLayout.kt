package com.zhou.life.todo.tasks

import android.content.Context
import android.support.v4.widget.SwipeRefreshLayout
import android.util.AttributeSet
import android.view.View
import java.util.jar.Attributes

/**
 * 作者 LY309313
 * 日期 2018/8/12
 * 描述
 */
class ScrollChildSwipeRereshLayout @JvmOverloads constructor(context: Context,
                                                             attrs: AttributeSet?=null):SwipeRefreshLayout(context,attrs){

    var childView:View?=null

    override fun canChildScrollUp(): Boolean {
        return childView?.canScrollVertically(-1)?:super.canChildScrollUp()
    }
}