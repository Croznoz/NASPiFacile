package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Gavel
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.VerifiedUser
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.model.AppLanguage
import com.example.model.AppText
import com.example.ui.theme.AccentGreen
import com.example.ui.theme.CharcoalMuted
import com.example.ui.theme.CharcoalPrimary
import com.example.ui.theme.CharcoalSecondary
import com.example.ui.theme.SageBackground
import com.example.ui.theme.SageBorder
import com.example.ui.theme.SageCardLight

/**
 * CompositionLocal per consentire a qualsiasi componente (incluso AppFooter) di aprire il Dialog della Policy
 */
val LocalOpenPolicy = compositionLocalOf<() -> Unit> { {} }

@Composable
fun PolicyTermsDialog(
    onDismiss: () -> Unit,
    currentLanguage: AppLanguage,
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(
            usePlatformDefaultWidth = false,
            dismissOnClickOutside = true,
            dismissOnBackPress = true
        )
    ) {
        Surface(
            modifier = modifier
                .padding(16.dp)
                .widthIn(max = 620.dp)
                .clip(RoundedCornerShape(12.dp))
                .border(1.5.dp, SageBorder, RoundedCornerShape(12.dp)),
            color = SageCardLight,
            shadowElevation = 8.dp
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(max = 680.dp)
                    .padding(20.dp)
            ) {
                // Header della Finestra con titolo e tasto Chiudi (✕)
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
                                .size(32.dp)
                                .clip(RoundedCornerShape(6.dp))
                                .background(CharcoalPrimary),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Gavel,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                text = AppText.privacySectionTitle(currentLanguage),
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    letterSpacing = 0.4.sp,
                                    fontSize = 14.sp
                                ),
                                color = CharcoalPrimary
                            )
                            Text(
                                text = if (currentLanguage == AppLanguage.EN) "Terms of Service, Free Access & Privacy" else "Condizioni d'Uso, Gratuità & Privacy",
                                style = MaterialTheme.typography.labelSmall.copy(fontSize = 11.sp),
                                color = CharcoalMuted
                            )
                        }
                    }

                    IconButton(
                        onClick = onDismiss,
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(CharcoalPrimary.copy(alpha = 0.08f))
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = AppText.closePolicy(currentLanguage),
                            tint = CharcoalPrimary,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(1.dp)
                        .background(SageBorder)
                )
                Spacer(modifier = Modifier.height(12.dp))

                // Contenuto scrollabile con tutti i punti della Policy
                Column(
                    modifier = Modifier
                        .weight(1f, fill = false)
                        .verticalScroll(scrollState)
                        .padding(end = 4.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    // Punto 1: Totalmente Gratuito & Nessuna Iscrizione Richiesta
                    PolicyDetailRow(
                        icon = Icons.Default.CheckCircle,
                        iconTint = AccentGreen,
                        title = AppText.freeServiceTitle(currentLanguage),
                        description = AppText.freeServiceDesc(currentLanguage)
                    )

                    // Punto 2: Sostenibilità del Progetto tramite Pubblicità
                    PolicyDetailRow(
                        icon = Icons.Default.VerifiedUser,
                        iconTint = CharcoalPrimary,
                        title = AppText.adSustainabilityTitle(currentLanguage),
                        description = AppText.adSustainabilityDesc(currentLanguage)
                    )

                    // Punto 3: Tutela della Privacy & Elaborazione Locale
                    PolicyDetailRow(
                        icon = Icons.Default.Lock,
                        iconTint = AccentGreen,
                        title = AppText.privacyTitle(currentLanguage),
                        description = AppText.privacyDesc(currentLanguage)
                    )

                    // Punto 4: Simulazioni Indicative e Non Vincolanti
                    PolicyDetailRow(
                        icon = Icons.Default.Calculate,
                        iconTint = CharcoalSecondary,
                        title = if (currentLanguage == AppLanguage.EN) "Indicative & Non-Binding Simulations" else "Simulazioni Indicative e Non Vincolanti",
                        description = AppText.simulationDisclaimer(currentLanguage)
                    )

                    // Punto 6: Inoltro Ufficiale Telematico Esclusivo su INPS.it
                    PolicyDetailRow(
                        icon = Icons.AutoMirrored.Filled.ArrowForward,
                        iconTint = CharcoalPrimary,
                        title = AppText.onlineFilingTitle(currentLanguage),
                        description = AppText.onlineFilingDesc(currentLanguage)
                    )
                }

                Spacer(modifier = Modifier.height(14.dp))
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(1.dp)
                        .background(SageBorder)
                )
                Spacer(modifier = Modifier.height(12.dp))

                // Pulsante inferiore di chiusura
                Button(
                    onClick = onDismiss,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(46.dp),
                    shape = RoundedCornerShape(6.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = CharcoalPrimary,
                        contentColor = Color.White
                    )
                ) {
                    Text(
                        text = AppText.closeDialogBtn(currentLanguage),
                        style = MaterialTheme.typography.labelLarge.copy(
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 0.5.sp
                        )
                    )
                }
            }
        }
    }
}

@Composable
private fun PolicyDetailRow(
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

@Composable
private fun BulletItem(text: String) {
    Row(verticalAlignment = Alignment.Top) {
        Text(
            text = "• ",
            style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold),
            color = CharcoalPrimary
        )
        Text(
            text = text,
            style = MaterialTheme.typography.bodySmall,
            color = CharcoalSecondary
        )
    }
}
