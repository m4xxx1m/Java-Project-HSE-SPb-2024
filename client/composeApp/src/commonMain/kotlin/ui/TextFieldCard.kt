package ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Card
import androidx.compose.material.IconButton
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Done
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun TextFieldCard(label: String?, initText: String = "", onButtonClick: (String) -> Unit) {
    val text = remember { mutableStateOf(initText) }
    Column {
        if (label != null) {
            Text(
                label, fontWeight = FontWeight.Light,
                fontSize = 12.sp
            )
        }
        Card(
            modifier = Modifier.fillMaxWidth(),
            elevation = 0.dp,
            border = BorderStroke(0.5.dp, Color(0x77777777))
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    modifier = Modifier.weight(1f),
                    value = text.value,
                    onValueChange = {
                        text.value = it
                    },
                    label = null,
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        backgroundColor = Color.Transparent,
                        disabledBorderColor = Color.Transparent,
                        focusedBorderColor = Color.Transparent,
                        unfocusedBorderColor = Color.Transparent,
                        errorBorderColor = Color.Transparent
                    )
                )
                IconButton(onClick = {
                    onButtonClick(text.value)
                }) {
                    Image(Icons.Rounded.Done, contentDescription = "Text field done")
                }
            }
        }
    }
}
