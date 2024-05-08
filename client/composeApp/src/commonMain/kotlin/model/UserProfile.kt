package model

import androidx.compose.runtime.MutableState

data class UserProfile(
    val user: User,
    val contacts: String,
    val about: String,
    val tags: String,
    val cvUrl: String?
) {
    fun getTagsList(list: MutableState<ArrayList<String>?>) {
        val newList = ArrayList<String>()
        tags.forEachIndexed { index, c ->
            if (c == '1') {
                newList.add(Tag.tags[index])
            }
        }
        list.value = newList
    }
}
