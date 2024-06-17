package model

import java.time.LocalDateTime

data class Post(
    val id: Int,
    val userId: Int,
    val dateTime: LocalDateTime,
    val tags: List<String>,
    val title: String,
    val text: String,
    val imagesUrl: List<String>,
    val likesCount: Int,
    val commentsCount: Int,
    val fileName: String?
) {
    var user: User? = null
}















