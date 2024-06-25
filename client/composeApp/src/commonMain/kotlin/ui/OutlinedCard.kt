package ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun OutlinedCard(label: String?, content: @Composable () -> Unit) {
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
            Box(modifier = Modifier.padding(10.dp).fillMaxWidth()) {
                content()
            }
        }
    }
}
