package ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Done
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import model.Tag

@Composable
fun TagUi(
    tag: Tag,
    clickedInit: Boolean,
    onClick: () -> Unit
) {
    val clicked = remember { mutableStateOf(clickedInit) }
    Row(
        modifier = Modifier
            .clip(RoundedCornerShape(10.dp))
            .clickable {
                clicked.value = !clicked.value
                onClick()
            }
            .background(if (clicked.value) Color.Gray else Color.LightGray)
            .padding(7.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(tag.tagName, fontSize = TextUnit(18f, TextUnitType.Sp))
        if (!clicked.value) {
            Image(
                Icons.Rounded.Add, contentDescription = "Add tag", modifier = Modifier
                    .size(20.dp)
            )
        } else {
            Image(Icons.Rounded.Done, contentDescription = "Remove tag")
        }
    }
}
