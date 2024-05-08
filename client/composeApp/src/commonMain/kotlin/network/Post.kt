package network

import model.Post
import model.Tag
import java.time.ZoneId
import java.util.Date

data class Post(
    val id: Int,
    val authorId: Int,
    val creationTime: Date,
    val title: String,
    val content: String,
    val rating: Int,
    val tags: String,
    val commentsCount: Int
) {
    fun convertPost(): Post {
        val tagsList = ArrayList<String>()
        tags.forEachIndexed() { index, c ->
            if (c == '1') {
                tagsList.add(Tag.tags[index])
            }
        }
        return Post(
            id,
            authorId,
            creationTime.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime(),
            tagsList,
            title,
            content,
            emptyList(),
            rating,
            commentsCount
        )
    }
}
