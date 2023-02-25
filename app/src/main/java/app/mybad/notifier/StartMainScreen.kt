package app.mybad.notifier

import android.content.res.Resources
import android.icu.util.Calendar
import android.os.Build
import android.util.DisplayMetrics
import androidx.annotation.RequiresApi
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PageSize
import androidx.compose.foundation.pager.PageSize.Fill.calculateMainAxisPageSize
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.internal.StabilityInferred
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import app.mybad.notifier.ui.screens.authorization.SurfaceSignInWith
import app.mybad.notifier.ui.screens.authorization.login.*
import app.mybad.notifier.ui.screens.authorization.navigation.AuthorizationNavItem
import app.mybad.notifier.ui.screens.navigation.NavItemMain
import kotlinx.coroutines.launch
import java.text.DateFormatSymbols
import java.time.*
import kotlin.io.path.ExperimentalPathApi
import kotlin.math.absoluteValue
import kotlin.time.Duration.Companion.days

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StartMainScreen(navController: NavHostController) {

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = stringResource(id = R.string.main_screen_top_bar_name)) },
                navigationIcon = {
                    IconButton(onClick = { /*navController.navigate(route = AuthorizationNavItem.Authorization.route)*/ }) {
//                        Icon(imageVector = Icons.Filled.ArrowBack, contentDescription = "Go Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
            )
        },
        content = { contentPadding ->
            Surface(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(contentPadding)
            ) {
                MainScreen(navController = navController)
            }
        })

}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MainScreen(navController: NavHostController) {

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        MainScreenBackgroundImage()
        Column(
            modifier = Modifier, horizontalAlignment = Alignment.CenterHorizontally
        ) {
            MainScreenMonthPager()
            MainScreenWeekPager()
        }
    }

}

@Composable
fun MainScreenBackgroundImage() {

}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MainScreenMonthPager() {

    val paddingStart = 10.dp
    val paddingEnd = 10.dp

    val state = rememberPagerState(LocalDate.now().month.ordinal)
    val scope = rememberCoroutineScope()

    HorizontalPager(
        pageCount = Month.values().size,
        state = state,
        pageSpacing = 13.dp,
        pageSize = PageSize.Fixed(40.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 14.dp, start = paddingStart, end = paddingEnd),
        verticalAlignment = Alignment.CenterVertically,
        contentPadding = PaddingValues(
            horizontal = (Resources.getSystem().configuration.screenWidthDp.dp - paddingStart * 3) / 2
        )
    ) { month ->
        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(5.dp)
        ) {
            Text(
                text = AnnotatedString(Month.values()[month].toString().substring(0, 3)),
                color = if (state.currentPage == month) {
                    MaterialTheme.colorScheme.primaryContainer
                } else if (state.currentPage == month + 1) {
                    Color.Black
                } else if (state.currentPage == month - 1) {
                    Color.Black
                } else if (state.currentPage == month + 2) {
                    Color.Gray
                } else if (state.currentPage == month - 2) {
                    Color.Gray
                } else {
                    Color.LightGray
                },
                modifier = Modifier
                    .padding(1.dp)
                    .clickable {
                        scope.launch { state.animateScrollToPage(month) }
                    },
                fontWeight = if (state.currentPage == month) {
                    FontWeight.Bold
                } else if (state.currentPage == month + 1) {
                    FontWeight.Normal
                } else if (state.currentPage == month - 1) {
                    FontWeight.Normal
                } else {
                    FontWeight.Normal
                },
                maxLines = 1,
                textAlign = TextAlign.Center
            )
        }
    }

}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MainScreenWeekPager() {

    val paddingStart = 10.dp
    val paddingEnd = 10.dp

    val dayOfWeek = DayOfWeek.values()
    val shortWeekDays = DateFormatSymbols.getInstance().shortWeekdays

    val state = rememberPagerState(LocalDate.now().dayOfMonth)
    val scope = rememberCoroutineScope()

    HorizontalPager(
        pageCount = LocalDate.now().month.maxLength(),
        state = state,
        pageSpacing = 13.dp,
        pageSize = PageSize.Fixed(40.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 20.dp, start = paddingStart, end = paddingEnd),
        verticalAlignment = Alignment.CenterVertically,
        contentPadding = PaddingValues(
            horizontal = (Resources.getSystem().configuration.screenWidthDp.dp - paddingStart * 3) / 2
        )
    ) { days ->
        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(5.dp)
        ) {
            Box(modifier = Modifier) {
                Text(
                    text = AnnotatedString(days.toString()),
                    color = if (state.currentPage == days) {
                        MaterialTheme.colorScheme.primaryContainer
                    } else {
                        Color.LightGray
                    },
                    modifier = Modifier
                        .padding(1.dp)
                        .clickable {
                            scope.launch { state.animateScrollToPage(days) }
                        },
                    fontWeight = if (state.currentPage == days) {
                        FontWeight.Bold
                    } else {
                        FontWeight.Normal
                    },
                    maxLines = 1,
                    textAlign = TextAlign.Center
                )

                Text(
                    text = AnnotatedString(days.toString()),
                    color = if (state.currentPage == days) {
                        MaterialTheme.colorScheme.primaryContainer
                    } else {
                        Color.LightGray
                    },
                    modifier = Modifier
                        .padding(1.dp)
                        .clickable {
                            scope.launch { state.animateScrollToPage(days) }
                        },
                    fontWeight = if (state.currentPage == days) {
                        FontWeight.Bold
                    } else {
                        FontWeight.Normal
                    },
                    maxLines = 1,
                    textAlign = TextAlign.Center
                )
            }

        }
    }

}