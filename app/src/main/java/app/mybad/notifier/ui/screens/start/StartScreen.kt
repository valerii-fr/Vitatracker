package app.mybad.notifier.ui.screens.start

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import app.mybad.notifier.R

@Composable
fun StartScreenApp() {
    Box(
        modifier = Modifier,
        contentAlignment = Alignment.BottomCenter
    ) {
        StartScreenBackgroundImage()
        Column(
            modifier = Modifier,
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            StartScreenImage()
            StartScreenText()
        }
    }
}

@Composable
fun StartScreenBackgroundImage() {
    Image(
        imageVector = ImageVector.vectorResource(id = R.drawable.ic_background_start_screen),
        contentDescription = null,
        modifier = Modifier.fillMaxSize(),
        alignment = Alignment.TopCenter,
        contentScale = ContentScale.FillWidth
    )
}

@Composable
fun StartScreenImage() {
    Image(
        modifier = Modifier
            .fillMaxWidth()
            .padding(36.dp),
        imageVector = ImageVector.vectorResource(id = R.drawable.ic_frau_doctor),
        contentDescription = null,
        contentScale = ContentScale.FillWidth
    )
}

@Composable
fun StartScreenText() {
    Column(
        modifier = Modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        TextWelcome(Modifier)
        TextRecommendation(Modifier)
        ButtonStartAuthorization(Modifier)
    }
}

@Composable
fun TextWelcome(modifier: Modifier = Modifier) {
    Text(
        text = stringResource(id = R.string.start_screen_welcome),
        modifier = modifier,
        fontSize = 32.sp,
        textAlign = TextAlign.Justify
    )
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = stringResource(id = R.string.start_screen_to), fontSize = 32.sp)
        Spacer(modifier = modifier.height(4.dp))
        Text(
            text = stringResource(id = R.string.app_name),
            fontSize = 32.sp,
            textAlign = TextAlign.Justify,
            color = MaterialTheme.colorScheme.primary
        )
    }
}

@Composable
fun TextRecommendation(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.padding(top = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = stringResource(id = R.string.start_screen_recomendation_1),
            fontSize = 16.sp,
            textAlign = TextAlign.Justify, modifier = modifier
        )
        Text(
            text = stringResource(id = R.string.start_screen_recomendation_2),
            fontSize = 16.sp,
            textAlign = TextAlign.Justify,
            modifier = modifier
        )
    }
}

@Composable
fun ButtonStartAuthorization(modifier: Modifier = Modifier) {
//    val mContext = LocalContext.current

    Button(
        modifier = modifier
            .padding(top = 16.dp, start = 8.dp, end = 8.dp, bottom = 16.dp)
            .fillMaxWidth(),
        onClick = {

//            mContext.startActivity(
//                Intent(
//                    mContext,
//                    AuthorizationScreen::class.java
//                )
//            )
        },
        shape = MaterialTheme.shapes.small,
        contentPadding = PaddingValues(top = 15.dp, bottom = 15.dp)
    ) {
        Text(
            text = stringResource(id = R.string.start_screen_go),
            modifier = modifier,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    StartScreenApp()
}