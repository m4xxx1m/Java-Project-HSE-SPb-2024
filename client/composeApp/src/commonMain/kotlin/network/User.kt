package network

data class User(
    val userId: Int,
    val username: String,
    val email: String,
    val password: String,
    val profilePictureUrl: String?,
    val contacts: String,
    val bio: String,
    val resumeUrl: String?,
    val tags: String
) {
    fun convertUser() : model.User {
        return model.User(
            userId,
            username,
            profilePictureUrl
        )
    }
}
