package com.example.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val EditorialColorScheme = lightColorScheme(
  primary = CharcoalPrimary,
  onPrimary = Color.White,
  primaryContainer = SageSurfaceVariant,
  onPrimaryContainer = CharcoalPrimary,
  secondary = CharcoalSecondary,
  onSecondary = Color.White,
  secondaryContainer = SageSurface,
  onSecondaryContainer = CharcoalPrimary,
  tertiary = AccentGreen,
  onTertiary = Color.White,
  background = SageBackground,
  onBackground = CharcoalPrimary,
  surface = SageSurface,
  onSurface = CharcoalPrimary,
  surfaceVariant = SageSurfaceVariant,
  onSurfaceVariant = CharcoalSecondary,
  outline = SageBorder,
  outlineVariant = SageBorderDark,
  error = AccentError,
  onError = Color.White
)

@Composable
fun MyApplicationTheme(
  content: @Composable () -> Unit,
) {
  MaterialTheme(
    colorScheme = EditorialColorScheme,
    typography = Typography,
    content = content
  )
}

