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

private val DarkColorScheme = darkColorScheme(
  primary = CrimsonPrimaryDark,
  onPrimary = OnCrimsonContainer,
  primaryContainer = CrimsonPrimary,
  onPrimaryContainer = Color.White,
  secondary = SaffronAmberDark,
  onSecondary = OnSaffronContainer,
  secondaryContainer = SaffronAmber,
  onSecondaryContainer = Color.White,
  tertiary = SpiceGreenDark,
  background = DarkBackground,
  onBackground = DarkText,
  surface = DarkSurface,
  onSurface = DarkText,
  surfaceVariant = DarkSurfaceVariant,
  onSurfaceVariant = DarkMutedText
)

private val LightColorScheme = lightColorScheme(
  primary = CrimsonPrimary,
  onPrimary = Color.White,
  primaryContainer = CrimsonContainer,
  onPrimaryContainer = OnCrimsonContainer,
  secondary = SaffronAmber,
  onSecondary = Color.White,
  secondaryContainer = SaffronContainer,
  onSecondaryContainer = OnSaffronContainer,
  tertiary = SpiceGreen,
  background = CreamBackground,
  onBackground = CharcoalText,
  surface = CreamSurface,
  onSurface = CharcoalText,
  surfaceVariant = CreamSurfaceVariant,
  onSurfaceVariant = MutedText
)

@Composable
fun MyApplicationTheme(
  darkTheme: Boolean = isSystemInDarkTheme(),
  dynamicColor: Boolean = false, // Preserve brand identity by default
  content: @Composable () -> Unit
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
