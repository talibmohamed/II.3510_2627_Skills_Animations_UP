package com.example.ii3510_2627_animations.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColors = lightColorScheme(
    primary = Color(0xFF245B4B), onPrimary = Color.White,
    primaryContainer = Color(0xFFDAEBDF), onPrimaryContainer = Color(0xFF173D32),
    secondary = Color(0xFF876044),
    background = Color(0xFFF5F3ED), surface = Color(0xFFFFFEFA),
    onBackground = Color(0xFF252C28), onSurface = Color(0xFF252C28),
    onSurfaceVariant = Color(0xFF58635C), outline = Color(0xFF77857B)
)
private val DarkColors = darkColorScheme(
    primary = Color(0xFFA7D7BB), onPrimary = Color(0xFF123827),
    primaryContainer = Color(0xFF315342), onPrimaryContainer = Color(0xFFD2EEDB),
    background = Color(0xFF18201B), surface = Color(0xFF222B25),
    onBackground = Color(0xFFE7ECE5), onSurface = Color(0xFFE7ECE5)
)

@Composable
fun II3510_2627_AnimationsTheme(darkTheme: Boolean = isSystemInDarkTheme(), content: @Composable () -> Unit) {
    MaterialTheme(colorScheme = if (darkTheme) DarkColors else LightColors,
        typography = Typography, content = content)
}
