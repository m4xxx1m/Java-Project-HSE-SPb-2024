package model

data class User(
    val id: Int, 
    val name: String, 
    val imageUrl: String
) {
    public fun getProfile(): UserProfile {
        return UserProfile(
            this,
            "Tg: @user\nEmail: user@gmail.com", 
            "Lorem ipsum dolor sit amet",
            listOf(
                User(0, "User name", ""),
                User(0, "User name", ""),
                User(0, "User name", ""),
                User(0, "User name", ""),
                User(0, "User name", ""),
                User(0, "User name", "")
            ),
            listOf(
                Tag(0, "asdfghjkl"), Tag(1, "qwerty"), Tag(2, "zxcvbnm"),
                Tag(0, "asdfghjkl"), Tag(1, "qwerty"), Tag(2, "zxcvbnm"),
                Tag(0, "asdfghjkl"), Tag(1, "qwerty"), Tag(2, "zxcvbnm"),
                Tag(0, "asdfghjkl"), Tag(1, "qwerty"), Tag(2, "zxcvbnm")
            ),
            0
        )
    }
}
