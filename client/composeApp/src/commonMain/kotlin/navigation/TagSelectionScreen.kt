package navigation

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
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Done
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import kotlinx.coroutines.launch
import model.Tag
import network.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TagSelectionScreen(private val isOnRegistration: Boolean = false) : Screen {

    @OptIn(ExperimentalLayoutApi::class)
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.current
        val tags = remember { mutableStateListOf<Tag>() }
//        val chosenTags = remember { mutableStateListOf<Boolean>() }
        if (tags.isEmpty()) {
            val coroutineScope = rememberCoroutineScope()
            coroutineScope.launch {
                load {
                    tags.addAll(it)
                }
            }
        }
        else {
            val scrollState = rememberScrollState()
            Box(
                modifier = Modifier.fillMaxWidth().fillMaxHeight().verticalScroll(scrollState)
                    .padding(horizontal = 20.dp, vertical = 30.dp)
            ) {
                Column(Modifier.align(Alignment.TopCenter).widthIn(max = 500.dp).fillMaxWidth()) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            modifier = Modifier.weight(1f),
                            text = "Choose your interests",
                            style = MaterialTheme.typography.h5,
                            fontWeight = FontWeight.SemiBold
                        )
                        if (navigator != null) {
                            if (isOnRegistration) {
                                Button(
                                    onClick = {
                                    }
                                ) {
                                    Text("Done")
                                }
                            } else {
                                BackButton(navigator)
                            }
                        }
                    }
                    Spacer(Modifier.size(15.dp))
                    FlowRow(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(10.dp),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        tags.forEachIndexed { index, tag ->
                            TagUi(tag, false)
                        }
                    }
                }
            }
        }
    }

    private fun load(onSuccess: (tags: List<Tag>) -> Unit) {
//        val retrofitCall = RetrofitClient.retrofit.create(ApiInterface::class.java)
        val retrofitCall = RetrofitClient.retrofitCall
        retrofitCall.getTags().enqueue(object: Callback<List<Tag>> {
            override fun onResponse(call: Call<List<Tag>>, response: Response<List<Tag>>) {
                if (response.code() == 200) {
                    response.body()?.let { 
                        onSuccess(it)
                    }
                } else {
                    println("wrong code while getting all tags")
                }
            }

            override fun onFailure(call: Call<List<Tag>>, t: Throwable) {
                println("failure while getting all tags")
            }
        })
    }

    @Composable
    private fun TagUi(tag: Tag, checked: Boolean) {
        val clicked = remember { mutableStateOf(checked) }
        Row(
            modifier = Modifier
                .clip(RoundedCornerShape(10.dp))
                .clickable { 
                    clicked.value = !clicked.value 
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
}
