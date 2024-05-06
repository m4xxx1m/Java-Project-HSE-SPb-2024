package model

data class UserProfile(
    val user: User,
    val contacts: String,
    val about: String,
    val tags: List<Tag>,
    val cvUrl: String
)
