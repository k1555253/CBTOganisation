package com.fyp.kweku.cbtoganisation.tasks.presentation.home


import android.view.View
import com.fyp.kweku.cbtoganisation.tasks.presentation.TaskActivity
import com.fyp.kweku.cbtoganisation.tasks.presentation.TaskViewModel


interface HomeViewClassInterface {

    interface HomeListener{
        fun onGoToCreateNewTaskFragmentButtonClicked(taskActivity: TaskActivity)
        fun onGoToMonthCalendarFragmentButtonClicked(taskActivity: TaskActivity)
    }

    fun getRootView(): View


    fun setListener(listener: HomeListener)

    fun getTaskActivity(): TaskActivity

    fun setTaskActivity(taskActivity: TaskActivity)

    fun setGoToCreateNewTaskFragmentButtonOnClickListener()

    fun setGoToMonthCalendarFragmentButtonOnClickListener()

    fun bindTaskViewModel(taskViewModel: TaskViewModel)


}