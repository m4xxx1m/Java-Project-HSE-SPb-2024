package model

data class Notification(
    val id: Int,
    val answerPost: Post,
    val answerTo: Post
)
