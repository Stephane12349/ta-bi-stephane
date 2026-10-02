package com.example.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val LightColorScheme = lightColorScheme(
    primary = AdjameGreenPrimary,
    onPrimary = Color.White,
    primaryContainer = AdjameGreenContainer,
    onPrimaryContainer = AdjameGreenDark,
    secondary = AdjameOrangeSecondary,
    onSecondary = Color.White,
    secondaryContainer = AdjameOrangeContainer,
    onSecondaryContainer = AdjameOrangeSecondary,
    tertiary = AdjameSuccess,
    onTertiary = Color.White,
    background = AdjameBackground,
    onBackground = AdjameTextPrimary,
    surface = AdjameSurface,
    onSurface = AdjameTextPrimary,
    surfaceVariant = AdjameSurfaceVariant,
    onSurfaceVariant = AdjameTextSecondary,
    outline = AdjameBorder,
    error = AdjameError,
    onError = Color.White
)

private val DarkColorScheme = darkColorScheme(
    primary = AdjameGreenLight,
    onPrimary = Color.Black,
    primaryContainer = AdjameGreenDark,
    onPrimaryContainer = AdjameGreenContainer,
    secondary = AdjameOrangeAccent,
    onSecondary = Color.Black,
    secondaryContainer = AdjameOrangeSecondary,
    onSecondaryContainer = Color.White,
    tertiary = AdjameSuccess,
    background = AdjameDarkBackground,
    onBackground = AdjameDarkTextPrimary,
    surface = AdjameDarkSurface,
    onSurface = AdjameDarkTextPrimary,
    surfaceVariant = AdjameDarkSurfaceVariant,
    onSurfaceVariant = AdjameDarkTextSecondary,
    outline = Color(0xFF2C3E50),
    error = AdjameError,
    onError = Color.White
)

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false, // Préférer la cohérence de marque Adjamé Market
    content: @Composable () -> Unit,
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
