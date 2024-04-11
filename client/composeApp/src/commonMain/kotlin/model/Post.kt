package model

import java.time.LocalDateTime

data class Post(
    val id: Int,
    val userId: Int,
    val dateTime: LocalDateTime,
    val tags: List<Tag>,
    val title: String,
    val text: String,
    val imagesUrl: List<String>,
    val likesCount: Int,
    val commentsCount: Int
) {
    var user: User? = null
}
