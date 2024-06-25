package model

data class Notification(
    val id: Int,
    val originalComment: Comment,
    val replyComment: Comment
)
