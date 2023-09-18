package app.mybad.notifier.ui.common

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.RemoveCircle
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import app.mybad.notifier.ui.theme.Typography
import app.mybad.notifier.utils.toText
import app.mybad.utils.timeInMinutesToDisplay

@Composable
fun NotificationItem(
    modifier: Modifier = Modifier,
    time: Int, // UTC время в минутах
    quantity: Float,
    form: Int,
    forms: Array<String>,
    onDelete: () -> Unit = {},
    onTimeClick: () -> Unit = {},
    onDoseChange: (Float) -> Unit = {}
) {
    val fm = LocalFocusManager.current
    var field by remember { mutableStateOf(TextFieldValue(quantity.toText())) }
    LaunchedEffect(quantity) {
        val q = quantity.toText()
        field = field.copy(
            text = q,
            selection = if (quantity == 0f) TextRange(0, 1) else TextRange(q.length, q.length),
        )
    }

    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(10.dp),
        color = MaterialTheme.colorScheme.background,
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.tertiaryContainer),
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 12.dp)
        ) {
            Icon(
                imageVector = Icons.Default.RemoveCircle,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.error,
                modifier = Modifier
                    .size(24.dp)
                    .clickable(
                        onClick = onDelete
                    )
            )
            Spacer(modifier = Modifier.width(16.dp))
            Text(
                modifier = Modifier
                    .clickable(onClick = onTimeClick),
                text = time.timeInMinutesToDisplay(), // отображает время с учетом часового пояса
                style = Typography.bodyLarge,
            )
            Row(modifier = Modifier.weight(0.4f), horizontalArrangement = Arrangement.End) {
                BasicTextField(
                    modifier = Modifier
                        .clickable(
                            onClick = onTimeClick
                        )
                        .width(25.dp)
                        .onFocusChanged {
                            if (it.hasFocus || it.isFocused) {
                                field = field.copy(selection = TextRange(0, field.text.length))
                            }
                        },
                    value = field,
                    onValueChange = {
                        val countValue = it.text.toFloatOrNull() ?: 0f
                        field = if (countValue > 10f) field else it
                        onDoseChange(if (countValue > 10f) 10f else countValue)
                    },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Decimal,
                        imeAction = ImeAction.Done
                    ),
                    keyboardActions = KeyboardActions(
                        onDone = {
                            fm.clearFocus()
                            field = field.copy(selection = TextRange.Zero)
                        }
                    )

                )
                Text(
                    text = forms[form],
                    style = Typography.bodyMedium,
                    modifier = Modifier.padding(start = 4.dp)
                )
            }
        }
    }
}

@Preview
@Composable
fun NotificationItemPreview() {
    NotificationItem(
        time = 140,
        quantity = 1f,
        form = 0,
        forms = arrayOf("Таблетка"),
    )
}