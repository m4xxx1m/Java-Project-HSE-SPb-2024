package ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.staggeredgrid.LazyHorizontalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun ChooseInterests() {
    MaterialTheme {
        Column(modifier = Modifier.padding(35.dp).fillMaxWidth().fillMaxHeight()) {
            Text(
                "Choose your interests",
                style = MaterialTheme.typography.h5,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}
