package app.mybad.notifier.ui.screens.newcourse.common

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.integerArrayResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import app.mybad.notifier.R

@Composable
fun IconSelector(
    modifier: Modifier = Modifier,
    selected: Int,
    onSelect: (Int) -> Unit
) {
    val icons = integerArrayResource(R.array.icons)
    val r = LocalContext.current.resources.obtainTypedArray(R.array.icons)
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = modifier.fillMaxWidth()
    ) {
        icons.forEachIndexed { index, i ->
            Surface(
                shape = CircleShape,
                color = if(index == selected) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.background,
                border = BorderStroke(1.dp, if(index == selected) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.outline),
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .clickable { onSelect(index) }
            ) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier.fillMaxSize()
                ) {
                    Icon(
                        painter = painterResource(r.getResourceId(index,0)),
                        contentDescription = null,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }
        }
    }
}