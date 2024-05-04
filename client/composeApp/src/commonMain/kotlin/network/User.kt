package network

import java.util.Date

data class User(
    val userId: Int,
    val username: String,
    val email: String,
    val password: String,
    val profilePictureUrl: String,
    val firstName: String,
    val secondName: String,
    val dateOfBirth: Date,
    val country: String,
    val city: String,
    val education: String,
    val bio: String,
    val resumeUrl: String
) {
    fun convertUser() : model.User {
        return model.User(
            userId,
            username,
            profilePictureUrl
        )
    }
}
