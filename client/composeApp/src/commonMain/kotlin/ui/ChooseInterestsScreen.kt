package ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.MaterialTheme
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import model.Tag

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ChooseInterestsForm(tags: List<Tag>) {
    MaterialTheme {
        val scrollState = rememberScrollState()
        Box(
            modifier = Modifier.fillMaxWidth().fillMaxHeight().verticalScroll(scrollState)
                .padding(35.dp)
        ) {
            Column(Modifier.align(Alignment.TopCenter).widthIn(max = 500.dp).fillMaxWidth()) {
                Text(
                    "Choose your interests",
                    style = MaterialTheme.typography.h5,
                    fontWeight = FontWeight.SemiBold
                )
                Spacer(Modifier.size(15.dp))
                FlowRow(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    tags.forEach { tag ->
                        TagUi(tag, false)
                    }
                }
            }
        }
    }
}

@Composable
fun TagUi(tag: Tag, checked: Boolean) {
    val clicked = remember { mutableStateOf(checked) }
    Row(
        modifier = Modifier.clip(RoundedCornerShape(10.dp))
            .clickable { clicked.value = !clicked.value }.background(Color.LightGray).padding(7.dp),
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
