package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.model.AppLanguage
import com.example.model.AppText
import com.example.ui.theme.*
import kotlinx.coroutines.delay

/**
 * Componente Banner Pubblicitario Standard (Display Ad / AdMob AdView).
 * Progettato per rispettare le linee guida di Google AdMob / AdSense:
 * - Etichetta esplicita "SPONSORIZZATO / ANNUNCIO"
 * - Spaziature definite per prevenire clic accidentali
 * - Layout non invasivo integrato con la palette grafica
 *
 * NOTA PER L'INTEGRAZIONE ADMOB IN PRODUZIONE:
 * Sostituire il layout segnaposto con la vista nativa AdMob:
 * ```kotlin
 * AndroidView(
 *     modifier = modifier.fillMaxWidth(),
 *     factory = { context ->
 *         AdView(context).apply {
 *             setAdSize(AdSize.BANNER)
 *             this.adUnitId = adUnitId
 *             loadAd(AdRequest.Builder().build())
 *         }
 *     }
 * )
 * ```
 */
@Composable
fun AdBannerPlaceholder(
    adUnitId: String,
    currentLanguage: AppLanguage,
    modifier: Modifier = Modifier
) {
    val isEn = currentLanguage == AppLanguage.EN

    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(6.dp))
            .background(SageCardLight)
            .border(1.dp, SageBorder, RoundedCornerShape(6.dp))
            .padding(vertical = 10.dp, horizontal = 12.dp)
            .testTag("ad_banner_slot_${adUnitId.takeLast(6)}")
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Badge Sponsor / Annuncio Pubblicitario
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(3.dp))
                        .background(CharcoalPrimary.copy(alpha = 0.08f))
                        .padding(horizontal = 6.dp, vertical = 2.dp)
                ) {
                    Text(
                        text = AppText.adSponsoredBadge(currentLanguage),
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontSize = 9.5.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 0.8.sp
                        ),
                        color = CharcoalSecondary
                    )
                }

                Spacer(modifier = Modifier.width(6.dp))

                Text(
                    text = "•  " + AppText.adNotice(currentLanguage),
                    style = MaterialTheme.typography.labelSmall.copy(
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Normal
                    ),
                    color = CharcoalMuted
                )
            }

            Spacer(modifier = Modifier.height(6.dp))

            // Riquadro Banner Contenuto
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(54.dp)
                    .clip(RoundedCornerShape(4.dp))
                    .background(SageSurface)
                    .border(1.dp, SageBorder.copy(alpha = 0.6f), RoundedCornerShape(4.dp))
                    .padding(horizontal = 10.dp),
                contentAlignment = Alignment.Center
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Star,
                        contentDescription = null,
                        tint = CharcoalMuted.copy(alpha = 0.6f),
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = AppText.adBannerPlaceholderText(currentLanguage),
                        style = MaterialTheme.typography.bodySmall.copy(
                            fontSize = 11.5.sp,
                            fontWeight = FontWeight.Medium
                        ),
                        color = CharcoalSecondary,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}

/**
 * Modale Video Sponsorizzato (Rewarded Video Ad).
 * Si apre prima di scaricare la Guida PDF Ufficiale INPS:
 * - Timer di countdown di 5 secondi con progress bar
 * - Possibilità di chiudere/saltare in qualunque momento
 * - Al termine sblocca il download e l'apertura del documento
 *
 * NOTA PER L'INTEGRAZIONE ADMOB REWARDED IN PRODUZIONE:
 * È possibile agganciare questo componente a:
 * ```kotlin
 * RewardedAd.load(context, adUnitId, adRequest, object : RewardedAdLoadCallback() {
 *     override fun onAdLoaded(ad: RewardedAd) {
 *         ad.show(activity) { rewardItem -> onRewardUnlocked() }
 *     }
 * })
 * ```
 */
@Composable
fun RewardedAdVideoDialog(
    onDismiss: () -> Unit,
    onRewardUnlocked: () -> Unit,
    currentLanguage: AppLanguage
) {
    var secondsLeft by remember { mutableIntStateOf(5) }
    var isUnlocked by remember { mutableStateOf(false) }

    // Timer decrescente di 5 secondi per simulare la visione del video sponsorizzato
    LaunchedEffect(Unit) {
        while (secondsLeft > 0) {
            delay(1000L)
            secondsLeft -= 1
        }
        isUnlocked = true
    }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(
            dismissOnBackPress = true,
            dismissOnClickOutside = false
        )
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .testTag("rewarded_video_dialog"),
            shape = RoundedCornerShape(12.dp),
            color = SageBackground,
            border = androidx.compose.foundation.BorderStroke(1.5.dp, CharcoalPrimary),
            shadowElevation = 8.dp
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Header con Badge e Tasto Chiudi/Salta
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(4.dp))
                            .background(CharcoalPrimary)
                            .padding(horizontal = 8.dp, vertical = 3.dp)
                    ) {
                        Text(
                            text = AppText.adSponsoredBadge(currentLanguage) + " • VIDEO",
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 0.8.sp
                            ),
                            color = Color.White
                        )
                    }

                    IconButton(
                        onClick = onDismiss,
                        modifier = Modifier
                            .size(32.dp)
                            .testTag("close_video_ad_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = AppText.videoAdSkip(currentLanguage),
                            tint = CharcoalPrimary,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                Text(
                    text = AppText.videoAdDialogTitle(currentLanguage),
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                    color = CharcoalPrimary,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = AppText.videoAdDialogSubtitle(currentLanguage),
                    style = MaterialTheme.typography.bodySmall.copy(lineHeight = 18.sp),
                    color = CharcoalSecondary,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Simulazione Schermo Video Player (16:9 Aspect)
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(150.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(CharcoalPrimary)
                        .border(1.dp, CharcoalSecondary, RoundedCornerShape(8.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Box(
                            modifier = Modifier
                                .size(48.dp)
                                .clip(CircleShape)
                                .background(Color.White.copy(alpha = 0.18f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = if (isUnlocked) Icons.Default.CheckCircle else Icons.Default.PlayArrow,
                                contentDescription = null,
                                tint = if (isUnlocked) AccentGreen else Color.White,
                                modifier = Modifier.size(28.dp)
                            )
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        Text(
                            text = if (isUnlocked)
                                AppText.videoAdUnlocked(currentLanguage)
                            else
                                "Partner Ufficiale Servizi Lavoro • Video Spot",
                            style = MaterialTheme.typography.labelMedium.copy(
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 12.sp
                            ),
                            color = Color.White
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Countdown e Progress Bar
                if (!isUnlocked) {
                    Text(
                        text = AppText.videoAdUnlockIn(secondsLeft, currentLanguage),
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp
                        ),
                        color = CharcoalPrimary
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    val progress = (5 - secondsLeft) / 5f
                    LinearProgressIndicator(
                        progress = { progress },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(6.dp)
                            .clip(RoundedCornerShape(3.dp)),
                        color = CharcoalPrimary,
                        trackColor = SageBorder
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    TextButton(
                        onClick = onDismiss,
                        modifier = Modifier.testTag("skip_video_ad_button")
                    ) {
                        Text(
                            text = AppText.videoAdSkip(currentLanguage),
                            style = MaterialTheme.typography.labelMedium,
                            color = CharcoalMuted
                        )
                    }
                } else {
                    // Risorsa sbloccata
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(6.dp))
                            .background(AccentGreen.copy(alpha = 0.12f))
                            .border(1.dp, AccentGreen.copy(alpha = 0.4f), RoundedCornerShape(6.dp))
                            .padding(10.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "✓ " + AppText.videoAdUnlocked(currentLanguage),
                            style = MaterialTheme.typography.bodySmall.copy(
                                fontWeight = FontWeight.Bold,
                                color = AccentGreen
                            )
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Button(
                        onClick = {
                            onDismiss()
                            onRewardUnlocked()
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp)
                            .testTag("unlocked_pdf_download_button"),
                        shape = RoundedCornerShape(6.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = CharcoalPrimary,
                            contentColor = Color.White
                        )
                    ) {
                        Text(
                            text = AppText.videoAdDownloadNow(currentLanguage),
                            style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold)
                        )
                    }
                }
            }
        }
    }
}
