package com.fyp.kweku.cbtoganisation.tasks.presentation.monthviewpager

import android.view.View
import com.fyp.kweku.cbtoganisation.tasks.presentation.TaskActivity
import org.threeten.bp.YearMonth

interface MonthViewPagerViewClassInterface {


    interface MonthViewPagerViewClassListener{

    }


    fun getRoot(): View
    fun setListener(monthViewPagerViewClassListener: MonthViewPagerViewClassListener)
    fun initViewPager(months: List<YearMonth>,currentMonthIndex: Int)
    fun setToolbar(taskActivity: TaskActivity)

}