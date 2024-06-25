package navigation

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.KeyboardArrowLeft
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.navigator.Navigator
import ui.AppTheme

@Composable
fun BackButton(navigator: Navigator) {
    Row(verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(10.dp).clip(RoundedCornerShape(3.dp))
            .clickable {
                navigator.pop()
            }.padding(5.dp)
    ) {
        Icon(
            Icons.Rounded.KeyboardArrowLeft, contentDescription = "Back button",
            modifier = Modifier.size(20.dp),
            tint = AppTheme.black
        )
        Spacer(Modifier.size(4.dp))
        Text(
            "Назад",
            fontSize = 14.sp,
            modifier = Modifier.padding(end = 4.dp),
            color = AppTheme.black
        )
    }
}
