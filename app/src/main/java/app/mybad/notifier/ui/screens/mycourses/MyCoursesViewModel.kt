package app.mybad.notifier.ui.screens.mycourses

import android.util.Log
import androidx.lifecycle.viewModelScope
import app.mybad.domain.models.CourseDisplayDomainModel
import app.mybad.domain.usecases.courses.GetCoursesUseCase
import app.mybad.notifier.ui.base.BaseViewModel
import app.mybad.utils.atEndOfDay
import app.mybad.utils.atStartOfDay
import app.mybad.utils.betweenDaysPlus
import app.mybad.utils.currentDateTimeSystem
import app.mybad.utils.plusDays
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MyCoursesViewModel @Inject constructor(
    private val getCoursesUseCase: GetCoursesUseCase,
) : BaseViewModel<MyCoursesContract.Event, MyCoursesContract.State, MyCoursesContract.Effect>() {


    override fun setInitialState() = MyCoursesContract.State()

    override fun handleEvents(event: MyCoursesContract.Event) {
        viewModelScope.launch {
            when (event) {
                is MyCoursesContract.Event.CourseEditing -> {
                    setEffect { MyCoursesContract.Effect.Navigation.ToCourseEditing(event.courseId) }
                }

                MyCoursesContract.Event.ActionBack -> setEffect { MyCoursesContract.Effect.Navigation.Back }
            }
        }
    }

    private val currentDateTime = MutableStateFlow(currentDateTimeSystem()) // с учетом часового пояса

    init {
        Log.w("VTTAG", "MyCoursesViewModel: init")
        uploadCoursesInState()
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    private fun uploadCoursesInState() {
        viewModelScope.launch {
            currentDateTime.flatMapLatest {
                getCoursesUseCase()
                    .distinctUntilChanged()
                    .map(::addRemindCourse)
            }
                .distinctUntilChanged()
                .collect(::changeState)
        }
    }

    private fun changeState(courses: List<CourseDisplayDomainModel>) {
        viewModelScope.launch {
            setState {
                copy(
                    courses = courses,
                )
            }
        }
    }

    private fun addRemindCourse(courses: List<CourseDisplayDomainModel>): List<CourseDisplayDomainModel> {
        val currentDate = currentDateTimeSystem()
        val newCourses = mutableListOf<CourseDisplayDomainModel>()
        courses.forEach { newCourse ->
            if (newCourse.remindDate != null && newCourse.interval >= 0) {

                val startDate = newCourse.endDate.plusDays(newCourse.interval).atStartOfDay()
                if (startDate > currentDate) {
                    val endDate = startDate.plusDays(
                        newCourse.endDate.atStartOfDay().betweenDaysPlus(newCourse.startDate)
                    ).atEndOfDay()
                    newCourses.add(
                        newCourse.copy(
                            id = 1000000 + newCourse.id,
                            idn = 0,
                            startDate = startDate,
                            endDate = endDate,
                            remindDate = null,
                            interval = startDate.betweenDaysPlus(currentDate.atEndOfDay()), // старт курса через ... дней
                        )
                    )
                }
            }
        }
        return courses.plus(newCourses)
    }

}
