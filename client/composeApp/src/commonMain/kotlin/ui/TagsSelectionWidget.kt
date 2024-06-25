package ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import model.Tag

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun TagsSelectionWidget(isClickedByIndex: (Int) -> Boolean, onTagClick: (Int) -> Unit) {
    FlowRow(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(10.dp),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Tag.tags.forEachIndexed { index, tag ->
            TagUi(
                Tag(index, tag),
                isClickedByIndex(index)
            ) {
                onTagClick(index)
            }
        }
    }
}
