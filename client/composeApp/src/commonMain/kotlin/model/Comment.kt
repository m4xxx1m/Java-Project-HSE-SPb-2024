package model

import java.time.LocalDateTime

data class Comment(
    val id: Int,
    val authorId: Int,
    val dateTime: LocalDateTime,
    val text: String,
    val imagesUrl: List<String>,
    val likesCount: Int,
    val postId: Int,
    val replyToCommentId: Int?
) {
    var user: User? = null
}
