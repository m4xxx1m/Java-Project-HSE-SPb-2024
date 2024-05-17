package ui

import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

object AppTheme {
    
    private val primaryColor = Color(0xff086ca2)
    private val primaryVariantColor = Color(0xffc6cfdb)
    private val backgroundColor = Color(0xffebf4f8)
    private val secondaryColor = Color(0xff84caf0)
    private val secondaryVariantColor = Color(0xff477895)
    
    @Composable
    fun apply(
        content: @Composable () -> Unit
    ) {
        val colorScheme = MaterialTheme.colors.copy(
            primary = primaryColor,
            primaryVariant = primaryVariantColor,
            background = backgroundColor,
            secondary = secondaryColor,
            secondaryVariant = secondaryVariantColor
        )

        MaterialTheme(
            colors = colorScheme,
            content = content
        )
    }
}
