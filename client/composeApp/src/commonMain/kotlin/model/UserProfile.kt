package model

data class UserProfile(
    val user: User,
    val contacts: String,
    val about: String,
    val subscriptions: List<User>,
    val tags: List<Tag>,
    val cvUrl: String
)
