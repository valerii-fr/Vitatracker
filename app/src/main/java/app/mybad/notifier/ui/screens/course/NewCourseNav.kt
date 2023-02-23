package app.mybad.notifier.ui.screens.course

import android.util.Log
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import app.mybad.domain.models.course.CourseDomainModel
import app.mybad.domain.models.med.MedDomainModel
import app.mybad.domain.models.usages.UsagesDomainModel

@Composable
fun NewCourseNav(
    modifier: Modifier = Modifier,
    userId: String = "userid",
    navController: NavHostController,
    onDismiss: () -> Unit = {},
    onFinish: (Triple<MedDomainModel, CourseDomainModel, UsagesDomainModel>) -> Unit = {},
) {

    var newMed by remember { mutableStateOf(MedDomainModel()) }
    var newCourse by remember { mutableStateOf(CourseDomainModel()) }
    var newUsages by remember { mutableStateOf(UsagesDomainModel()) }

    NavHost(
        navController = navController,
        startDestination = NavItemCourse.AddMed.route
    ) {
        composable(NavItemCourse.AddMed.route) {
            AddMedScreen(
                modifier = modifier.fillMaxSize(),
                userId = userId,
                onNext = {
                    navController.navigate(NavItemCourse.AddCourse.route)
                    newMed = it
                    Log.w("NCN_", "$newMed")
                },
                onBack = {
                    onDismiss()
                    navController.popBackStack()
                },
            )
        }
        composable(NavItemCourse.AddCourse.route) {
            AddCourse(
                modifier = modifier.fillMaxSize(),
                userId = userId,
                medId = newMed.id,
                onNext = {
                    navController.navigate(NavItemCourse.NextCourse.route)
                    newCourse = it.first
                    newUsages = it.second
                    Log.w("NCN_", "$newCourse")
                    Log.w("NCN_", "$newUsages")
                },
                onBack = { navController.popBackStack() },
            )
        }
        composable(NavItemCourse.NextCourse.route) {
            NextCourse(
                modifier = modifier.fillMaxSize(),
                previousEndDate = newCourse.endTime,
                onNext = {
                    navController.navigate(NavItemCourse.CourseCreated.route)
                    onFinish(Triple(newMed, newCourse, newUsages))
                },
                onBack = { navController.popBackStack() },
            )
        }
        composable(NavItemCourse.CourseCreated.route) {
            CourseCreated { }
        }
    }
}