package network

data class AuthenticationResponse(
    val token: String,
    val user: User
)
