package ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Search
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun SearchWidget() {
    Column(Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
            modifier = Modifier
                .widthIn(max = 500.dp)
                .fillMaxWidth()
                .padding(10.dp)
                .clip(RoundedCornerShape(5.dp))
                .background(MaterialTheme.colors.primaryVariant)
                .clickable { } 
                .padding(5.dp)
        ) {
            Image(
                Icons.Rounded.Search, contentDescription = "Search",
                modifier = Modifier.size(30.dp)
            )
            Text("Поиск", modifier = Modifier.align(Alignment.Center),
                fontSize = 18.sp)
        }
    }

}
