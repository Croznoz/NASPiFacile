package com.example.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Gavel
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.VerifiedUser
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.model.AppLanguage
import com.example.model.AppText
import com.example.ui.components.AdBannerPlaceholder
import com.example.ui.components.AppFooter
import com.example.ui.components.EditorialButton
import com.example.ui.components.EditorialCard
import com.example.ui.components.LanguageToggleAction
import com.example.ui.theme.AccentGreen
import com.example.ui.theme.CharcoalMuted
import com.example.ui.theme.CharcoalPrimary
import com.example.ui.theme.CharcoalSecondary
import com.example.ui.theme.SageBackground
import com.example.ui.theme.SageBorder
import com.example.ui.theme.SageCardLight
import com.example.ui.theme.SageSurface

/**
 * PAGINA INIZIALE:
 * Spiegazione delle funzionalità, privacy e policy (disclaimer di non sostituzione a CAF/patronato).
 */
@Composable
fun InitialScreen(
    onEnterMainApp: () -> Unit,
    onNavigateToPdf: () -> Unit = {},
    modifier: Modifier = Modifier,
    currentLanguage: AppLanguage = AppLanguage.IT,
    onToggleLanguage: () -> Unit = {}
) {
    val scrollState = rememberScrollState()
    var isPolicyVisible by remember { mutableStateOf(true) }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = SageBackground,
        bottomBar = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(SageBackground)
                    .padding(horizontal = 20.dp, vertical = 16.dp)
            ) {
                EditorialButton(
                    text = AppText.enterApp(currentLanguage),
                    onClick = onEnterMainApp,
                    icon = Icons.AutoMirrored.Filled.ArrowForward,
                    isPrimary = true,
                    testTag = "enter_app_button"
                )
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(scrollState)
                .padding(horizontal = 20.dp, vertical = 16.dp)
        ) {
            // Header Top Row with Badge on Left and Language Toggle on Right
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.testTag("app_logo_initial")
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.img_naspifacile_logo),
                        contentDescription = "NASpiFacile Logo",
                        modifier = Modifier
                            .height(44.dp)
                            .clip(RoundedCornerShape(8.dp))
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = "NASpiFacile",
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Bold,
                            letterSpacing = (-0.5).sp
                        ),
                        color = CharcoalPrimary
                    )
                }

                LanguageToggleAction(
                    currentLanguage = currentLanguage.name,
                    onToggleLanguage = onToggleLanguage
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Main Display Headline
            Text(
                text = AppText.initialTitle(currentLanguage),
                style = MaterialTheme.typography.displayLarge,
                color = CharcoalPrimary
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Minimalist author / source line (con barra verticale stile TypeSafe)
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(start = 2.dp)
            ) {
                Box(
                    modifier = Modifier
                        .width(2.5.dp)
                        .height(28.dp)
                        .background(CharcoalPrimary)
                )
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(
                        text = AppText.exclusiveDbTitle(currentLanguage),
                        style = MaterialTheme.typography.bodySmall.copy(
                            fontWeight = FontWeight.Bold,
                            color = CharcoalPrimary
                        )
                    )
                    Text(
                        text = AppText.exclusiveDbSubtitle(currentLanguage),
                        style = MaterialTheme.typography.bodySmall.copy(
                            fontStyle = FontStyle.Italic,
                            color = CharcoalSecondary
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Sezione 1: Panoramica funzionalità
            Text(
                text = AppText.featuresSectionTitle(currentLanguage),
                style = MaterialTheme.typography.labelLarge.copy(
                    letterSpacing = 0.8.sp,
                    fontWeight = FontWeight.Bold
                ),
                color = CharcoalPrimary
            )

            Spacer(modifier = Modifier.height(10.dp))

            EditorialCard(
                backgroundColor = SageCardLight,
                borderColor = CharcoalPrimary
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
                    FeatureOverviewRow(
                        icon = Icons.Default.CheckCircle,
                        title = AppText.feature1Title(currentLanguage),
                        description = AppText.feature1Desc(currentLanguage)
                    )

                    FeatureOverviewRow(
                        icon = Icons.Default.Calculate,
                        title = AppText.feature2Title(currentLanguage),
                        description = AppText.feature2Desc(currentLanguage)
                    )

                    FeatureOverviewRow(
                        icon = Icons.Default.MenuBook,
                        title = AppText.feature3Title(currentLanguage),
                        description = AppText.feature3Desc(currentLanguage)
                    )

                    FeatureOverviewRow(
                        icon = Icons.Default.Description,
                        title = AppText.feature4Title(currentLanguage),
                        description = AppText.feature4Desc(currentLanguage)
                    )
                }
            }

            Spacer(modifier = Modifier.height(22.dp))

            // Sezione 2: Privacy, Policy e Disclaimer Legale (Finestra chiudibile e richiamabile)
            if (isPolicyVisible) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = AppText.privacySectionTitle(currentLanguage),
                        style = MaterialTheme.typography.labelLarge.copy(
                            letterSpacing = 0.8.sp,
                            fontWeight = FontWeight.Bold
                        ),
                        color = CharcoalPrimary,
                        modifier = Modifier.weight(1f)
                    )

                    IconButton(
                        onClick = { isPolicyVisible = false },
                        modifier = Modifier
                            .size(32.dp)
                            .clip(CircleShape)
                            .background(CharcoalPrimary.copy(alpha = 0.08f))
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = AppText.closePolicy(currentLanguage),
                            tint = CharcoalPrimary,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(8.dp))
                        .background(SageSurface)
                        .border(1.2.dp, CharcoalPrimary, RoundedCornerShape(8.dp))
                        .padding(16.dp)
                ) {
                    Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
                        // Punto 1: Totalmente Gratuito & Nessuna Iscrizione
                        PolicyPointRow(
                            icon = Icons.Default.CheckCircle,
                            iconTint = AccentGreen,
                            title = AppText.freeServiceTitle(currentLanguage),
                            description = AppText.freeServiceDesc(currentLanguage)
                        )

                        // Punto 2: Sostenibilità tramite Pubblicità
                        PolicyPointRow(
                            icon = Icons.Default.VerifiedUser,
                            iconTint = CharcoalPrimary,
                            title = AppText.adSustainabilityTitle(currentLanguage),
                            description = AppText.adSustainabilityDesc(currentLanguage)
                        )

                        // Punto 3: Tutela della Privacy & Elaborazione Locale
                        PolicyPointRow(
                            icon = Icons.Default.Lock,
                            iconTint = AccentGreen,
                            title = AppText.privacyTitle(currentLanguage),
                            description = AppText.privacyDesc(currentLanguage)
                        )

                        // Punto 4: Simulazioni Non Vincolanti
                        PolicyPointRow(
                            icon = Icons.Default.Calculate,
                            iconTint = CharcoalSecondary,
                            title = if (currentLanguage == AppLanguage.EN) "Indicative & Non-Binding Simulations" else "Simulazioni Indicative e Non Vincolanti",
                            description = AppText.simulationDisclaimer(currentLanguage)
                        )

                        // Punto 6: Inoltro Esclusivo su INPS.it
                        PolicyPointRow(
                            icon = Icons.AutoMirrored.Filled.ArrowForward,
                            iconTint = CharcoalPrimary,
                            title = AppText.onlineFilingTitle(currentLanguage),
                            description = AppText.onlineFilingDesc(currentLanguage)
                        )

                        // Pulsante inferiore per chiudere la finestra informativa
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 10.dp, bottom = 4.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            EditorialButton(
                                text = if (currentLanguage == AppLanguage.EN) "Close This Window ✕" else "Chiudi Questa Finestra ✕",
                                onClick = { isPolicyVisible = false },
                                isPrimary = false,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(48.dp)
                            )
                        }
                    }
                }
            } else {
                // Card compatta cliccabile per riaprire le condizioni
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(8.dp))
                        .background(SageSurface)
                        .border(1.2.dp, SageBorder, RoundedCornerShape(8.dp))
                        .clickable { isPolicyVisible = true }
                        .padding(14.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(
                            modifier = Modifier.weight(1f),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(28.dp)
                                    .clip(RoundedCornerShape(4.dp))
                                    .background(CharcoalPrimary.copy(alpha = 0.1f)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.VerifiedUser,
                                    contentDescription = null,
                                    tint = CharcoalPrimary,
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(10.dp))
                            Column {
                                Text(
                                    text = AppText.privacySectionTitle(currentLanguage),
                                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                                    color = CharcoalPrimary
                                )
                                Text(
                                    text = if (currentLanguage == AppLanguage.EN) "100% Free • Ad Supported • Local Privacy (Click to open)" else "100% Gratuito • Sostenuto da Ads • Privacy Locale (Clicca per aprire)",
                                    style = MaterialTheme.typography.labelSmall.copy(fontSize = 11.sp),
                                    color = CharcoalMuted
                                )
                            }
                        }
                        Text(
                            text = if (currentLanguage == AppLanguage.EN) "Open Window →" else "Apri Finestra →",
                            style = MaterialTheme.typography.labelMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = AccentGreen
                            )
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Banner tempistiche ufficiali
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(6.dp))
                    .background(SageCardLight)
                    .border(1.dp, SageBorder, RoundedCornerShape(6.dp))
                    .padding(14.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.VerifiedUser,
                        contentDescription = AppText.deadlineTitle(currentLanguage),
                        tint = CharcoalPrimary,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = AppText.deadlineReminder(currentLanguage),
                            style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold),
                            color = CharcoalPrimary
                        )
                        Text(
                            text = AppText.deadlineDesc(currentLanguage),
                            style = MaterialTheme.typography.bodySmall,
                            color = CharcoalSecondary
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            // Banner Pubblicitario Schermata Iniziale (Non invasivo, integrato prima del footer)
            AdBannerPlaceholder(
                adUnitId = "ca-app-pub-placeholder/initial-screen-banner",
                currentLanguage = currentLanguage,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            val openGlobalPolicy = com.example.ui.components.LocalOpenPolicy.current
            AppFooter(
                currentLanguage = currentLanguage,
                onOpenPolicy = {
                    isPolicyVisible = true
                    openGlobalPolicy()
                }
            )

            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}

@Composable
private fun FeatureOverviewRow(
    icon: ImageVector,
    title: String,
    description: String
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.Top
    ) {
        Box(
            modifier = Modifier
                .size(34.dp)
                .clip(RoundedCornerShape(4.dp))
                .background(CharcoalPrimary),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(18.dp)
            )
        }
        Spacer(modifier = Modifier.width(12.dp))
        Column {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                color = CharcoalPrimary
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = description,
                style = MaterialTheme.typography.bodySmall.copy(lineHeight = 18.sp),
                color = CharcoalSecondary
            )
        }
    }
}

@Composable
private fun BulletPoint(text: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(vertical = 1.dp)
    ) {
        Box(
            modifier = Modifier
                .size(5.dp)
                .background(CharcoalPrimary)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = text,
            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Medium),
            color = CharcoalPrimary
        )
    }
}

@Composable
private fun PolicyPointRow(
    icon: ImageVector,
    iconTint: Color,
    title: String,
    description: String,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.Top
    ) {
        Box(
            modifier = Modifier
                .padding(top = 2.dp)
                .size(24.dp)
                .clip(RoundedCornerShape(4.dp))
                .background(iconTint.copy(alpha = 0.12f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = iconTint,
                modifier = Modifier.size(16.dp)
            )
        }
        Spacer(modifier = Modifier.width(10.dp))
        Column {
            Text(
                text = title,
                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                color = CharcoalPrimary
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = description,
                style = MaterialTheme.typography.bodySmall.copy(lineHeight = 18.sp),
                color = CharcoalSecondary
            )
        }
    }
}
