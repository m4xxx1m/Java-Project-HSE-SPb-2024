package platform_depended

expect fun getPlatform(): Platform;

enum class Platform {
    ANDROID,
    DESKTOP
}