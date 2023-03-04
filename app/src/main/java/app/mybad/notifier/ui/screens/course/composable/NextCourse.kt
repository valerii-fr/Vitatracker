package app.mybad.notifier.ui.screens.course

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import app.mybad.domain.models.course.CourseDomainModel
import app.mybad.notifier.R
import app.mybad.notifier.ui.screens.common.*
import app.mybad.notifier.ui.theme.Typography

@Composable
@Preview
fun NextCourse(
    modifier: Modifier = Modifier,
    previousEndDate: Long = 1677366000L,
    onBack: () -> Unit = {},
    onNext: () -> Unit = {},
    onChange: () -> Unit = {}
) {

    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            DurationSelector(
                startDate = previousEndDate,
                label = stringResource(R.string.add_next_course_interval),
                onSelect = { }
            )
            Spacer(Modifier.height(16.dp))
            IterationsSelector(label = stringResource(R.string.add_next_course_remind_before)) {}
            Spacer(Modifier.height(16.dp))
            Text(
                text = stringResource(R.string.add_next_course_time),
                style = Typography.bodyLarge
            )
            Spacer(Modifier.height(4.dp))
            TimeSelector(initTime = 32400) {}
            Spacer(Modifier.height(16.dp))
            TextInput(label = stringResource(R.string.add_course_comment)) {}
        }
        NavigationRow(
            nextLabel = stringResource(R.string.navigation_finish),
            onBack = onBack::invoke,
            onNext = onNext::invoke
        )
    }

}