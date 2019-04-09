package com.fyp.kweku.cbtoganisation.tasks.presentation.home.horizontalrecyclerview.mvc


import com.fyp.kweku.cbtoganisation.R
import com.fyp.kweku.cbtoganisation.common.ProjectDateTimeUtils
import com.fyp.kweku.cbtoganisation.tasks.domain.interactors.GetTasksInteractorInterface
import com.fyp.kweku.cbtoganisation.tasks.presentation.home.horizontalrecyclerview.HorizontalCalendarItem
import com.fyp.kweku.cbtoganisation.tasks.presentation.home.horizontalrecyclerview.HorizontalCalendarProperties
import com.fyp.kweku.cbtoganisation.tasks.presentation.home.horizontalrecyclerview.HorizontalCalendarUtils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import org.threeten.bp.LocalDate
import timber.log.Timber
import kotlin.coroutines.CoroutineContext

//swap TaskOutput for getTasks Interactor Interface
class HorizontalCalendarController(val getTasksInteractorInterface: GetTasksInteractorInterface): HorizontalCalendarViewClassInterface.HorizontalCalendarViewClassListener {

    private lateinit var horizontalCalendarViewClassInterface: HorizontalCalendarViewClassInterface
    private lateinit var calendarProperties: HorizontalCalendarProperties
    private var calendar = LocalDate.now() // get instacne of calendar
    private var parentJob = Job()
    private val coroutineContext: CoroutineContext
        get() = parentJob + Dispatchers.Main
    private val scope = CoroutineScope(coroutineContext)


    override fun onDaySelected(date: String){
        updateEntries(date)
        Timber.i("update")
    }

    override fun onEndReached() {
        val monthAtEnd: Int = calendarProperties.monthAtEnd
        if (monthAtEnd == 0) {
            calendarProperties.monthAtEnd = 11
            calendarProperties.yearAtEnd -= 1
        } else {
            calendarProperties.monthAtEnd = monthAtEnd - 1
        }

    }

     override fun fetchHorizontalCalenderItemMutableListForOnEndReached():MutableList<HorizontalCalendarItem>{
        return getCalendarItems(
             calendarProperties.monthAtEnd,
             calendarProperties.yearAtEnd
         )
     }

    override fun onStartReached(){
        val monthAtStart: Int = calendarProperties.monthAtStart
        if (monthAtStart == 11) {
            calendarProperties.monthAtStart = 0
            calendarProperties.yearAtStart +=1
        } else {
            calendarProperties.monthAtStart = monthAtStart + 1
        }
    }

    override fun horizontalCalenderItemMutableListForOnStartReached():MutableList<HorizontalCalendarItem>{
        return getCalendarItems( calendarProperties.monthAtStart, calendarProperties.yearAtStart)
    }

    override fun SmoothScrollToPosionParameters(): Int{
        return calendar.dayOfMonth // day of month as Integer
    }

    fun bindView(horizontalCalendarViewClassInterface: HorizontalCalendarViewClassInterface){
       this.horizontalCalendarViewClassInterface = horizontalCalendarViewClassInterface
    }

    override fun CalenderAdapterSetDataParameters(): MutableList<HorizontalCalendarItem> {
        return getCalendarItems(calendarProperties.currentMonth, calendarProperties.currentYear)
    }

    fun initHorizontalCalendar() {
         //set current date to the set time of callendar in millseconds
        setCalenderProperties(calendar.monthValue, calendar.year) // get month and year

       horizontalCalendarViewClassInterface.initHorizontalCalendar()
    }

    fun getCalendarItems(month: Int, year: Int): MutableList<HorizontalCalendarItem> {
        val items = ArrayList<HorizontalCalendarItem>()
        for (day in 0 until HorizontalCalendarUtils.calculateMonthLength(month)) {
            items.add(HorizontalCalendarItem(day + 1, month, R.color.colorPrimary, year))
        }
        return items
    }

    fun setCalenderProperties(currentMonth: Int, currentYear: Int) {
        calendarProperties = HorizontalCalendarProperties(currentMonth, currentYear)
    }

     fun updateEntries(date: String)= scope.launch(Dispatchers.IO){getTasksInteractorInterface.getTasksByDay(LocalDate.parse(date,ProjectDateTimeUtils.getCustomDateFormatter()))

    }


    fun setControllerAsHorizontalCalendarViewClassListener(){horizontalCalendarViewClassInterface.setListener(this)}
}