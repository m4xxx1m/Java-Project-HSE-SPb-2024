package network

import model.Comment
import java.time.ZoneId
import java.util.Date

data class Comment(
    val id: Int,
    val authorId: Int,
    val creationTime: Date,
    val content: String,
    val rating: Int,
    val postId: Int,
    val replyToCommentId: Int?
) {
    fun convertComment(): Comment {
        return Comment(
            id,
            authorId,
            creationTime.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime(),
            content,
            emptyList(),
            rating,
            postId,
            replyToCommentId
        )
    }
}
