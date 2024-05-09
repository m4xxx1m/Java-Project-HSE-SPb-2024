package ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Checkbox
import androidx.compose.material.IconButton
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.ArrowDropDown
import androidx.compose.material.icons.rounded.Done
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import model.Tag
import navigation.CreatePostManager

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun NewPostForm() {
    val textMode = remember { mutableStateOf(true) }
    Column(
        modifier = Modifier.fillMaxWidth().fillMaxHeight(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Column(
            modifier = Modifier.widthIn(max = 500.dp).fillMaxWidth()
                .fillMaxHeight().padding(10.dp)
        ) {
            val title = remember { mutableStateOf("") }
            val postText = remember { mutableStateOf("") }
            val checkedState = remember { mutableStateOf(false) }
            val postTags = remember { StringBuffer(Tag.defaultTags) }
            if (textMode.value) {
                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = title.value,
                    onValueChange = { title.value = it },
                    label = { Text("Заголовок поста", fontWeight = FontWeight.SemiBold) },
                    maxLines = 3
                )
                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth().weight(1f),
                    value = postText.value,
                    onValueChange = { postText.value = it },
                    label = { Text("Текст поста") }
                )
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Checkbox(
                        checked = checkedState.value,
                        onCheckedChange = { checkedState.value = it },
                    )
                    Text("Прикрепить резюме из профиля", fontWeight = FontWeight.Thin)
                }
            } else {
                FlowRow(
                    modifier = Modifier.fillMaxWidth().weight(1f),
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Tag.tags.forEachIndexed { index, tag ->
                        TagUi(
                            Tag(index, tag),
                            postTags[index] == '1'
                        ) {
                            postTags.setCharAt(index, if (postTags[index] == '1') '0' else '1')
                        }
                    }
                }
            }
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = {}) {
                    Image(
                        Icons.Rounded.Add, contentDescription = "Attach images and files",
                        modifier = Modifier.size(35.dp)
                    )
                }
                Spacer(Modifier.weight(1f))
                Button(
                    onClick = {
                        textMode.value = !textMode.value
                    }, 
                    colors = ButtonDefaults.buttonColors(backgroundColor = Color.LightGray),
                    modifier = Modifier.width(150.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Spacer(Modifier.width(20.dp))
                        Spacer(Modifier.weight(1f))
                        Text("Теги")
                        Spacer(Modifier.weight(1f))
                        Image(
                            Icons.Rounded.ArrowDropDown, contentDescription = "Edit post tags",
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
                Spacer(Modifier.weight(1f))
                IconButton(onClick = {
                    CreatePostManager().createPost(
                        title.value,
                        postText.value,
                        postTags.toString()
                    ) {
                        title.value = ""
                        postText.value = ""
                        postTags.replace(0, postTags.length, Tag.defaultTags)
                        textMode.value = true
                    }
                }) {
                    Image(
                        Icons.Rounded.Done, contentDescription = "Post is done",
                        modifier = Modifier.size(35.dp)
                    )
                }
            }
        }
    }
}
