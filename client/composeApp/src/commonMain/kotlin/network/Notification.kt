package network

data class Notification(
    val id: Int,
    val originalComment: Comment,
    val replyComment: Comment
) {
    fun convertNotification(): model.Notification {
        return model.Notification(
            id,
            originalComment.convertComment(),
            replyComment.convertComment()
        )
    }
}