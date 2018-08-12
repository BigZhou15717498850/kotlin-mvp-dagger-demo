package com.zhou.life.todo.tasks

import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.*
import android.widget.CheckBox
import android.widget.LinearLayout
import android.widget.TextView
import com.zhou.life.todo.R
import com.zhou.life.todo.data.Task
import com.zhou.life.todo.di.FragmentScope
import javax.inject.Inject

/**
 * 作者 LY309313
 * 日期 2018/8/12
 * 描述
 */
@FragmentScope
class TasksFragment @Inject constructor():Fragment(),TasksContract.View {
    override var isActive: Boolean=false
        get() = isAdded

    @Inject var presenter:TasksContract.Presenter?=null
    var tasksFilerLabel:TextView?=null
    var llTasks:LinearLayout?=null
    var llNoTasks:LinearLayout?=null
    internal var onItemClickListener = object : OnItemClickListener{
        override fun onTaskClick(task: Task) {
            presenter?.openTaskDetailUi(task)
        }

        override fun onCompleteTask(task: Task) {
            presenter?.completeTask(task)
        }

        override fun onActiveTask(task: Task) {
            presenter?.activeTask(task)
        }

    }

    val mAdapter = TasksAdapter(listOf(),onItemClickListener)

    companion object {
        fun newInstance() = TasksFragment()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var root = inflater.inflate(R.layout.tasks_frag, container, false)
        with(root){
            val recyclerView = root.findViewById<RecyclerView>(R.id.list_tasks).apply {
                layoutManager = LinearLayoutManager(activity,LinearLayoutManager.VERTICAL,false)
                adapter =mAdapter
                hasFixedSize()
            }
            findViewById<ScrollChildSwipeRereshLayout>(R.id.refreshlayout).apply {
                setColorSchemeColors(
                        ContextCompat.getColor(activity, R.color.colorPrimary),
                        ContextCompat.getColor(activity, R.color.colorAccent),
                        ContextCompat.getColor(activity, R.color.colorPrimaryDark)
                )
                childView = recyclerView
                setOnRefreshListener { presenter?.loadTasks(false) }
            }

             tasksFilerLabel = findViewById<TextView>(R.id.tv_filter_label)
             llTasks = findViewById<LinearLayout>(R.id.ll_tasks)
            llNoTasks = findViewById<LinearLayout>(R.id.ll_notasks)

            activity.findViewById<FloatingActionButton>(R.id.fab_add).apply {
                setImageResource(R.drawable.ic_add)
                setOnClickListener { presenter?.addNewTask() }
            }
           setHasOptionsMenu(true)

        }
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return super.onOptionsItemSelected(item)

    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        super.onCreateOptionsMenu(menu, inflater)

    }

    override fun onResume() {
        super.onResume()
        presenter?.takeView(this)
    }

    override fun onPause() {
        super.onPause()
        presenter?.dropView()
    }

    override fun setloadingIndicator(show: Boolean) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun showTasks(tasks: List<Task>) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun showAddTask() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun showTaskDetailUi(taskid: String) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun showTaskMarkedActive() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun showTaskMarkedCompleted() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun showActiveFilterLabel() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun showCompletedFilterLabel() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun showAllFilterLabel() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun showCompletedTasksCleared() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun showLoadingTasksError() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun showNoTasks() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun showNoActiveTasks() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun showNoCompletedTasks() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun showSuccessfullySavedMessage() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun showFilteringPopUpMenu() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }


    class TasksAdapter(tasks: List<Task>,
                       val onItemClickListener: OnItemClickListener) :RecyclerView.Adapter<TasksAdapter.TasksViewHolder>(){

        var tasks:List<Task> = tasks
            set(value) {
                field = value
                notifyDataSetChanged()
            }
        override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): TasksViewHolder {
            val root = LayoutInflater.from(parent?.context).inflate(R.layout.tasks_item, parent, false)
            return TasksViewHolder(root)
        }

        override fun getItemCount(): Int {
            return tasks.size
        }

        override fun onBindViewHolder(holder: TasksViewHolder?, position: Int) {
            holder?.bind(tasks[position],onItemClickListener)
        }


        class TasksViewHolder(val view:View):RecyclerView.ViewHolder(view){
            val checkBox = view.findViewById<CheckBox>(R.id.checkbox)
            val tvTitle = view.findViewById<TextView>(R.id.titile)

            fun bind(task: Task,onItemClickListener: OnItemClickListener){
                checkBox.isChecked = task.isCompleted
                tvTitle.text = task.titleForList
                checkBox.setOnCheckedChangeListener { buttonView, isChecked ->
                    if(isChecked)onItemClickListener.onCompleteTask(task) else onItemClickListener.onActiveTask(task)
                 }
                view.setOnClickListener { onItemClickListener.onTaskClick(task) }
            }
        }
    }

    interface OnItemClickListener{
        fun onTaskClick(task: Task)

        fun onCompleteTask(task: Task)

        fun onActiveTask(task: Task)
    }
}