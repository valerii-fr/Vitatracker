package app.mybad.notifier.ui.screens.mycourses

import androidx.lifecycle.ViewModel
import app.mybad.domain.models.course.CourseDomainModel
import app.mybad.domain.models.med.MedDetailsDomainModel
import app.mybad.domain.models.med.MedDomainModel
import app.mybad.notifier.R
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class MyCoursesViewModel @Inject constructor(

) : ViewModel() {

    private val coursesList = listOf(
        CourseDomainModel(id=1L, medId = 1L, startDate = 0L, endDate = 11000000L),
        CourseDomainModel(id=2L, medId = 2L, startDate = 0L, endDate = 12000000L),
        CourseDomainModel(id=3L, medId = 3L, startDate = 0L, endDate = 13000000L),
    )

    private val medsList = listOf(
        MedDomainModel(id=1L, name = "Doliprane",   details = MedDetailsDomainModel(type = 1, dose = 500, measureUnit = 1, icon = R.drawable.pill)),
        MedDomainModel(id=2L, name = "Dexedrine",   details = MedDetailsDomainModel(type = 1, dose = 30,  measureUnit = 1, icon = R.drawable.pill)),
        MedDomainModel(id=3L, name = "Prozac",      details = MedDetailsDomainModel(type = 1, dose = 120, measureUnit = 1, icon = R.drawable.pill)),
    )

    private val _state = MutableStateFlow(MyCoursesState(coursesList, medsList))
    val state get() = _state.asStateFlow()

    fun reduce(intent: MyCoursesIntent) {

    }

}