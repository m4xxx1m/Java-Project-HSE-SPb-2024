package network

data class CommentCreate(
    val authorId: Int,
    val content: String,
    val replyToCommentId: Int = -1
)
