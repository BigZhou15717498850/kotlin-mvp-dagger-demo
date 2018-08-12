package com.zhou.life.todo.data.source

import com.google.common.collect.Lists
import com.zhou.life.todo.any
import com.zhou.life.todo.capture
import com.zhou.life.todo.data.Task
import com.zhou.life.todo.data.source.TasksDatasource
import com.zhou.life.todo.eq
import junit.framework.Assert.assertNotNull
import junit.framework.Assert.assertTrue
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.`is`
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.mockito.ArgumentCaptor

import org.mockito.Captor
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations
import java.sql.SQLOutput

/**
 * 作者 LY309313
 * 日期 2018/8/8
 * 描述
 */

class TasksRepositoryTest {

    private val TITLE1 = "title1"
    private val TITLE2 = "title2"
    private val TITLE3 = "title3"
    private val DESCRIPTION = "any description"
    private val DATAS = Lists.newArrayList(Task(TITLE1,"desc1"),Task(TITLE2,"desc2"))
    private lateinit var tasksRepository:TasksRepository
    @Mock private lateinit var localDatasource: TasksDatasource

    @Mock private lateinit var remoteDatasource: TasksDatasource

    @Mock private lateinit var getTaskCallBack: TasksDatasource.GetTaskCallBack

    @Mock private lateinit var  getTasksCallBack: TasksDatasource.GetTasksCallBack

    @Captor private lateinit var taskscallCaptor:ArgumentCaptor<TasksDatasource.GetTasksCallBack>

    @Captor private lateinit var taskcallCaptor:ArgumentCaptor<TasksDatasource.GetTaskCallBack>

    @Before fun setupTasksRepository() {
        MockitoAnnotations.initMocks(this)

        tasksRepository = TasksRepository(localDatasource, remoteDatasource)

    }

    @Test fun getTasks_repositoryCachesAfterFristApiCall(){
        twoTasksLoadCallsToRepository(getTasksCallBack)

        verify(remoteDatasource).getTasks(any<TasksDatasource.GetTasksCallBack>())
//        //执行获取所有数据
//        tasksRepository.getTasks(getTasksCallBack)
//        //第一次执行时，会先从本地加载，所有只能时localDatasource先加载
//        setTasksNotAvailable(localDatasource)
//        //找不到数据，紧接着去远程找
//        setTasksAvailable(remoteDatasource,DATAS)
//        //远程回馈数据
//        verify(getTasksCallBack).onTasksLoaded(DATAS)
    }


    @Test fun getTasks_requestAllTasksFromLocal(){
        tasksRepository.getTasks(getTasksCallBack)

        verify(localDatasource).getTasks(any<TasksDatasource.GetTasksCallBack>())
    }

    @Test fun getTasks_requestAllTasksFromRemote(){
        tasksRepository.getTasks(getTasksCallBack)
        setTasksNotAvailable(localDatasource)
        verify(remoteDatasource).getTasks(any<TasksDatasource.GetTasksCallBack>())
    }

    @Test fun getTask_requestSingleTask(){
        tasksRepository.getTask(TITLE1,getTaskCallBack)

        verify(localDatasource).getTask(eq(TITLE1), any<TasksDatasource.GetTaskCallBack>())
    }

    @Test fun saveTask_saveTaskToServiceApi(){
        //创建新task,存入，检测缓存
        val task = Task(TITLE3,DESCRIPTION)
        tasksRepository.saveTask(task)

        verify(remoteDatasource).saveTask(task)
        verify(localDatasource).saveTask(task)

        assertThat(tasksRepository.taskCaches.size,`is`(1))
    }

    @Test fun completeTask_CompleteTaskToServiceApiUpdateCache(){
        with(tasksRepository){
            val task = Task(TITLE3,DESCRIPTION)
            saveTask(task)

            completeTask(task)

            verify(remoteDatasource).completeTask(task)
            verify(localDatasource).completeTask(task)

            val cacheTask = tasksRepository.taskCaches[task.id]
            assertNotNull(cacheTask as Task)
            assertThat(cacheTask.isCompleted,`is`(true))
        }
    }

    @Test fun activeTask_activeTaskToServiceApiUpdateCache(){
        with(tasksRepository){
            val task = Task(TITLE1,DESCRIPTION).apply { isCompleted = true }
            saveTask(task)

            activeTask(task)
            verify(remoteDatasource).activeTask(task)
            verify((localDatasource)).activeTask(task)

            val cacheTask = tasksRepository.taskCaches[task.id]
            assertNotNull(cacheTask as Task)
            assertThat(cacheTask.isCompleted,`is`(false))
        }
    }

    @Test fun clearCompletedTasks_clearCompletedTasksToServiceApi(){
        with(tasksRepository){
            val task1 = Task(TITLE1,DESCRIPTION).apply { isCompleted=true }
            saveTask(task1)
            val task2 = Task(TITLE2,DESCRIPTION)
            saveTask(task2)
            val task3 = Task(TITLE3, DESCRIPTION).apply { isCompleted = true }
            saveTask(task3)

            clearCompleteTasks()

            verify(remoteDatasource).clearCompleteTasks()
            verify(localDatasource).clearCompleteTasks()

            val task = tasksRepository.taskCaches[task2.id]
            assertNotNull(task as Task)
            assertTrue(task.isActive)
            assertThat(task.title,`is`(TITLE2))
        }
    }

    @Test fun deleteAllTasks_deleteAllTasksToServiceApi(){
        with(tasksRepository){
            val task1 = Task(TITLE1,DESCRIPTION).apply { isCompleted=true }
            saveTask(task1)
            val task2 = Task(TITLE2,DESCRIPTION)
            saveTask(task2)
            val task3 = Task(TITLE3, DESCRIPTION).apply { isCompleted = true }
            saveTask(task3)
            assertThat(tasksRepository.taskCaches.size,`is`(3))
            deleteAllTasks()

            verify(remoteDatasource).deleteAllTasks()
            verify(localDatasource).deleteAllTasks()

            assertThat(tasksRepository.taskCaches.size,`is`(0))
        }
    }

    @Test fun deleteTaskId_deleteTaskIdToServiceApi(){
        with(tasksRepository){
            val task1 = Task(TITLE1,DESCRIPTION).apply { isCompleted=true }
            saveTask(task1)

            assertThat(tasksRepository.taskCaches.size,`is`(1))
            deleteTask(task1.id)

            verify(remoteDatasource).deleteTask(task1.id)
            verify(localDatasource).deleteTask(task1.id)

            assertThat(tasksRepository.taskCaches.size,`is`(0))
        }
    }

    @Test fun getTasksWithDirtyCache_refreshTasksFromRemotesource(){
        with(tasksRepository){
            refreshTasks()
            getTasks(getTasksCallBack)
        }

        setTasksAvailable(remoteDatasource,DATAS)

        verify(localDatasource, never()).getTasks(any<TasksDatasource.GetTasksCallBack>())

        verify(getTasksCallBack).onTasksLoaded(DATAS)
    }
    @Test fun getTasks_localDataNotAvailable(){
        tasksRepository.getTasks(getTasksCallBack)
        setTasksNotAvailable(localDatasource)
        setTasksAvailable(remoteDatasource,DATAS)
        verify(getTasksCallBack).onTasksLoaded(DATAS)
    }

    @Test fun getTasksTwoSourcesUnavailable_firesOnDataUnavailable(){
        tasksRepository.getTasks(getTasksCallBack)

        setTasksNotAvailable(localDatasource)
        setTasksNotAvailable(remoteDatasource)

        verify(getTasksCallBack).onDataNotAvailable()
    }

    @Test fun getTaskWithBothSourcesUnavailable_firesOnDataUnavailable(){
        val taskId = TITLE1
        tasksRepository.getTask(taskId,getTaskCallBack)
        setTaskNoAvailable(taskId,localDatasource)
        setTaskNoAvailable(taskId,remoteDatasource)

        verify(getTaskCallBack).onDataNotAvailable()
    }

    @Test fun getTasksRefreshlocalDatasource_updateLocal(){
        with(tasksRepository){
            refreshTasks()
            getTasks(getTasksCallBack)
        }

        setTasksAvailable(remoteDatasource,DATAS)

        verify(localDatasource, times(DATAS.size)).saveTask(any<Task>())
    }
    private fun setTaskNoAvailable(taskId:String,tasksDatasource: TasksDatasource) {
        verify(tasksDatasource).getTask(eq(taskId), capture(taskcallCaptor))
        taskcallCaptor.value.onDataNotAvailable()
    }

    private fun twoTasksLoadCallsToRepository(tasksCallBack: TasksDatasource.GetTasksCallBack) {
        //从仓库获取，本地和缓存是肯定没有得
        tasksRepository.getTasks(tasksCallBack)
        verify(localDatasource).getTasks(capture(taskscallCaptor))
        taskscallCaptor.value.onDataNotAvailable()

        verify(remoteDatasource).getTasks(capture(taskscallCaptor))

        taskscallCaptor.value.onTasksLoaded(DATAS)

        tasksRepository.getTasks(tasksCallBack)
    }


    private fun setTasksAvailable(datasource: TasksDatasource,tasks:List<Task>){
        verify(datasource).getTasks(capture(taskscallCaptor))
        taskscallCaptor.value.onTasksLoaded(tasks)
    }

    private fun setTasksNotAvailable(datasource: TasksDatasource){
        verify(datasource).getTasks(capture(taskscallCaptor))
        taskscallCaptor.value.onDataNotAvailable()
    }
}