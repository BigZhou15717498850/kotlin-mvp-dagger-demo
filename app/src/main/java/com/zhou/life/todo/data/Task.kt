package com.zhou.life.todo.data

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import java.util.*

/**
 * 作者 LY309313
 * 日期 2018/8/7
 * 描述
 */
@Entity(tableName = "tasks")
data class Task @JvmOverloads constructor(
        @ColumnInfo(name = "title") var title:String="",
        @ColumnInfo(name = "description") var description:String="",
        @PrimaryKey @ColumnInfo(name = "entryid") var id:String=UUID.randomUUID().toString()
) {
    @ColumnInfo
    var isCompleted:Boolean=false

    val titleForList:String
        get() = if(title.isNotEmpty())title else description

    val isActive:Boolean
        get() = !isCompleted

    val isEmpty:Boolean
        get() = title.isEmpty() && description.isEmpty()
}