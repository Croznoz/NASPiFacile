package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.model.AppLanguage
import com.example.ui.components.LocalOpenPolicy
import com.example.ui.components.PolicyTermsDialog
import com.example.ui.screens.AmountSimulatorScreen
import com.example.ui.screens.DocumentsChecklistScreen
import com.example.ui.screens.InitialScreen
import com.example.ui.screens.MainScreen
import com.example.ui.screens.PdfViewerScreen
import com.example.ui.screens.RequirementSimulatorScreen
import com.example.ui.screens.StepByStepGuideScreen
import com.example.ui.theme.MyApplicationTheme

enum class AppNavDestination {
  INITIAL,
  MAIN,
  REQUIREMENTS,
  AMOUNTS,
  GUIDE,
  DOCUMENTS,
  PDF_VIEWER
}

class MainActivity : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    enableEdgeToEdge()
    setContent {
      MyApplicationTheme {
        NaspiAppRoot()
      }
    }
  }
}

@Composable
fun NaspiAppRoot() {
  var currentDestination by remember { mutableStateOf(AppNavDestination.INITIAL) }
  var previousDestination by remember { mutableStateOf(AppNavDestination.MAIN) }
  var currentLanguage by remember { mutableStateOf(AppLanguage.IT) }
  var showPolicyDialog by remember { mutableStateOf(false) }

  val onToggleLanguage = {
    currentLanguage = if (currentLanguage == AppLanguage.IT) AppLanguage.EN else AppLanguage.IT
  }

  // Gestione tasto indietro di sistema
  BackHandler(enabled = currentDestination != AppNavDestination.INITIAL) {
    currentDestination = when (currentDestination) {
      AppNavDestination.REQUIREMENTS,
      AppNavDestination.AMOUNTS,
      AppNavDestination.GUIDE,
      AppNavDestination.DOCUMENTS -> AppNavDestination.MAIN
      AppNavDestination.PDF_VIEWER -> previousDestination
      AppNavDestination.MAIN -> AppNavDestination.INITIAL
      AppNavDestination.INITIAL -> AppNavDestination.INITIAL
    }
  }

  CompositionLocalProvider(LocalOpenPolicy provides { showPolicyDialog = true }) {
    Crossfade(targetState = currentDestination, label = "AppNavigation") { destination ->
      when (destination) {
        AppNavDestination.INITIAL -> {
          InitialScreen(
            onEnterMainApp = { currentDestination = AppNavDestination.MAIN },
            onNavigateToPdf = {
              previousDestination = AppNavDestination.INITIAL
              currentDestination = AppNavDestination.PDF_VIEWER
            },
            currentLanguage = currentLanguage,
            onToggleLanguage = onToggleLanguage,
            modifier = Modifier.fillMaxSize()
          )
        }
        AppNavDestination.MAIN -> {
          MainScreen(
            onNavigateToRequirements = { currentDestination = AppNavDestination.REQUIREMENTS },
            onNavigateToAmounts = { currentDestination = AppNavDestination.AMOUNTS },
            onNavigateToGuide = { currentDestination = AppNavDestination.GUIDE },
            onNavigateToDocuments = { currentDestination = AppNavDestination.DOCUMENTS },
            onViewPrivacyPolicy = { showPolicyDialog = true },
            onNavigateToPdf = {
              previousDestination = AppNavDestination.MAIN
              currentDestination = AppNavDestination.PDF_VIEWER
            },
            currentLanguage = currentLanguage,
            onToggleLanguage = onToggleLanguage,
            modifier = Modifier.fillMaxSize()
          )
        }
        AppNavDestination.REQUIREMENTS -> {
          RequirementSimulatorScreen(
            onBack = { currentDestination = AppNavDestination.MAIN },
            currentLanguage = currentLanguage,
            onToggleLanguage = onToggleLanguage,
            modifier = Modifier.fillMaxSize()
          )
        }
        AppNavDestination.AMOUNTS -> {
          AmountSimulatorScreen(
            onBack = { currentDestination = AppNavDestination.MAIN },
            currentLanguage = currentLanguage,
            onToggleLanguage = onToggleLanguage,
            modifier = Modifier.fillMaxSize()
          )
        }
        AppNavDestination.GUIDE -> {
          StepByStepGuideScreen(
            onBack = { currentDestination = AppNavDestination.MAIN },
            onNavigateToPdf = {
              previousDestination = AppNavDestination.GUIDE
              currentDestination = AppNavDestination.PDF_VIEWER
            },
            currentLanguage = currentLanguage,
            onToggleLanguage = onToggleLanguage,
            modifier = Modifier.fillMaxSize()
          )
        }
        AppNavDestination.DOCUMENTS -> {
          DocumentsChecklistScreen(
            onBack = { currentDestination = AppNavDestination.MAIN },
            currentLanguage = currentLanguage,
            onToggleLanguage = onToggleLanguage,
            modifier = Modifier.fillMaxSize()
          )
        }
        AppNavDestination.PDF_VIEWER -> {
          PdfViewerScreen(
            onBack = { currentDestination = previousDestination },
            currentLanguage = currentLanguage,
            modifier = Modifier.fillMaxSize()
          )
        }
      }
    }

    if (showPolicyDialog) {
      PolicyTermsDialog(
        onDismiss = { showPolicyDialog = false },
        currentLanguage = currentLanguage
      )
    }
  }
}

/**
 * Kept for test compatibility
 */
@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
  Text(text = "Hello $name!", modifier = modifier)
}

@Preview(showBackground = true)
@Composable
fun NaspiAppPreview() {
  MyApplicationTheme {
    NaspiAppRoot()
  }
}

