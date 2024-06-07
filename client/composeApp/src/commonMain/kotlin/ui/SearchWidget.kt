package ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Search
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.navigator.Navigator
import navigation.SearchScreen

@Composable
fun SearchWidget(
    navigator: Navigator?,
    default: String = "",
    onSearch: ((String) -> Unit)? = null
) {
    val searchQuery = remember { mutableStateOf(default) }
    BasicTextField(
        modifier = Modifier
            .widthIn(max = 500.dp)
            .fillMaxWidth()
            .padding(10.dp)
            .clip(RoundedCornerShape(5.dp))
            .background(MaterialTheme.colors.primaryVariant),
        value = searchQuery.value,
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
        keyboardActions = KeyboardActions(
            onSearch = {
                if (searchQuery.value.isNotEmpty()) {
                    if (onSearch == null) {
                        navigator?.push(SearchScreen(searchQuery.value))
                        searchQuery.value = ""
                    } else {
                        onSearch(searchQuery.value)
                    }
                }
            }
        ),
        textStyle = TextStyle(fontSize = 18.sp, color = AppTheme.black),
        onValueChange = {
            searchQuery.value = it
        },
        singleLine = true
    ) { innerTextField ->
        Row(
            modifier = Modifier.fillMaxWidth().padding(5.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                Icons.Rounded.Search, contentDescription = "Search",
                modifier = Modifier.size(30.dp),
                tint = AppTheme.black
            )
            Spacer(Modifier.width(5.dp))
            Box {
                if (searchQuery.value.isEmpty()) {
                    Text("Поиск", fontSize = 18.sp, color = AppTheme.black)
                }
                innerTextField()
            }
        }
    }
}
