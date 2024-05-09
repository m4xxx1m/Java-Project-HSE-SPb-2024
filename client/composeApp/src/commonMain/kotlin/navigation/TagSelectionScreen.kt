package navigation

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
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.Navigator
import model.AuthManager
import model.Tag
import network.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import ui.TagUi

class TagSelectionScreen(
    private val userTags: String? = null
) : Screen {

    @OptIn(ExperimentalLayoutApi::class)
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.current
        val chosenTags = remember {
            mutableStateOf(
                if (userTags == null) null else StringBuffer(userTags)
            )
        }
        val changed = mutableStateOf(false)
        if (chosenTags.value == null) {
            chosenTags.value = StringBuffer("0".repeat(Tag.tags.size))
        }
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
                        text = "Выберите свои интересы",
                        style = MaterialTheme.typography.h5,
                        fontWeight = FontWeight.SemiBold
                    )
                    if (navigator != null) {
                        Column {
                            Button(
                                onClick = {
                                    done(
                                        changed.value,
                                        chosenTags.value.toString(),
                                        navigator
                                    )
                                }
                            ) {
                                Text("Готово")
                            }
                        }
                    }
                }
                Spacer(Modifier.size(15.dp))
                FlowRow(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Tag.tags.forEachIndexed { index, tag ->
                        TagUi(
                            Tag(index, tag),
                            chosenTags.value?.getOrNull(index) == '1'
                        ) {
                            changed.value = true
                            chosenTags.value?.let {
                                if (it.length <= index) {
                                    it.append("0".repeat(index - it.length + 1))
                                }
                                it.setCharAt(index, if (it[index] == '1') '0' else '1')
                            }
                        }
                    }
                }
            }
        }
    }

//    private fun load(tags: MutableState<StringBuffer?>) {
//        RetrofitClient.retrofitCall.getUserTags(AuthManager.currentUser.id).enqueue(
//            object : Callback<String> {
//                override fun onResponse(call: Call<String>, response: Response<String>) {
//                    if (response.code() == 200) {
//                        tags.value = StringBuffer(response.body())
//                    } else {
//                        println("wrong code on getting user tags")
//                    }
//                }
//
//                override fun onFailure(call: Call<String>, t: Throwable) {
//                    println("failure on getting user tags")
//                }
//
//            }
//        )
//    }

    private fun done(isChanged: Boolean, chosenTags: String?, navigator: Navigator?) {
        if (isChanged) {
            RetrofitClient.retrofitCall.updateUserTags(
                AuthManager.currentUser.id,
                chosenTags!!
            ).enqueue(object : Callback<Void> {
                override fun onResponse(call: Call<Void>, response: Response<Void>) {
                    if (response.code() != 200) {
                        println("wrong code on updating user tags")
                    }
                }

                override fun onFailure(call: Call<Void>, t: Throwable) {
                    println("failure updating user tags")
                }

            })
        }
        if (userTags == null) {
            navigator?.popAll()
            navigator?.replace(MainNavigation())
        } else {
            navigator?.pop()
        }
    }
}
