package model

import java.time.LocalDateTime

data class Comment(
    val id: Int,
    val user: User,
    val dateTime: LocalDateTime,
    val text: String,
    val imagesUrl: List<String>,
    val likesCount: Int
)