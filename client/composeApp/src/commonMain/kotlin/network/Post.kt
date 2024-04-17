package network

import model.Post
import java.time.ZoneId
import java.util.Date

data class Post(
    val id: Int,
    val authorId: Int,
    val creationTime: Date,
    val title: String,
    val content: String,
    val rating: Int,
    val tagIds: List<Int>,
    val commentsCount: Int
) {
    fun convertPost(): Post {
        return Post(
            id,
            authorId,
            creationTime.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime(),
            emptyList(),
            title,
            content,
            emptyList(),
            rating,
            commentsCount
        )
    }
}
