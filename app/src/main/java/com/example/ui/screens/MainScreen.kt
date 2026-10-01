package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.HelpOutline
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material.icons.filled.PrivacyTip
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.model.AppLanguage
import com.example.model.AppText
import com.example.model.InpsRepository
import com.example.ui.components.AdBannerPlaceholder
import com.example.ui.components.AppFooter
import com.example.ui.components.EditorialCard
import com.example.ui.components.EditorialHeader
import com.example.ui.components.LanguageToggleAction
import com.example.ui.components.MainFeatureCardButton
import com.example.ui.components.OfficialDisclaimerBox
import com.example.ui.components.PdfDownloadBanner
import com.example.ui.components.RewardedAdVideoDialog
import com.example.ui.theme.AccentError
import com.example.ui.theme.AccentGreen
import com.example.ui.theme.AccentWarning
import com.example.ui.theme.CharcoalMuted
import com.example.ui.theme.CharcoalPrimary
import com.example.ui.theme.CharcoalSecondary
import com.example.ui.theme.SageBackground
import com.example.ui.theme.SageBorder
import com.example.ui.theme.SageCardLight
import com.example.ui.theme.SageSurface

/**
 * PAGINA PRINCIPALE:
 * Presenta i 4 pulsanti card centrali per simulazione requisiti, durata/importi, guida passo passo e documenti.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    onNavigateToRequirements: () -> Unit,
    onNavigateToAmounts: () -> Unit,
    onNavigateToGuide: () -> Unit,
    onNavigateToDocuments: () -> Unit,
    onViewPrivacyPolicy: () -> Unit,
    onNavigateToPdf: () -> Unit = {},
    modifier: Modifier = Modifier,
    currentLanguage: AppLanguage = AppLanguage.IT,
    onToggleLanguage: () -> Unit = {}
) {
    val scrollState = rememberScrollState()
    val expandedFaqs = remember { mutableStateMapOf<String, Boolean>() }
    var showVideoAdForPdf by remember { mutableStateOf(false) }

    if (showVideoAdForPdf) {
        RewardedAdVideoDialog(
            onDismiss = { showVideoAdForPdf = false },
            onRewardUnlocked = {
                showVideoAdForPdf = false
                onNavigateToPdf()
            },
            currentLanguage = currentLanguage
        )
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = SageBackground,
        topBar = {
            TopAppBar(
                title = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.testTag("app_logo_main")
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.img_naspifacile_logo),
                            contentDescription = "NASpiFacile Logo",
                            modifier = Modifier
                                .height(38.dp)
                                .clip(RoundedCornerShape(6.dp))
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
                },
                actions = {
                    LanguageToggleAction(
                        currentLanguage = currentLanguage.name,
                        onToggleLanguage = onToggleLanguage
                    )
                    IconButton(
                        onClick = onViewPrivacyPolicy,
                        modifier = Modifier.testTag("open_policy_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.PrivacyTip,
                            contentDescription = AppText.legalNotice(currentLanguage),
                            tint = CharcoalPrimary
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = SageBackground
                )
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(scrollState)
                .padding(horizontal = 20.dp, vertical = 8.dp)
        ) {
            // Intestazione Editoriale di Pagina
            EditorialHeader(
                title = AppText.mainHeaderTitle(currentLanguage),
                subtitle = AppText.mainHeaderSubtitle(currentLanguage),
                sourceCitation = if (AppText.isEnglish(currentLanguage))
                    "Official sources: www.inps.it • Framework Circulars 94/2015, 142/2015 & INPS Circulars 2026"
                else
                    "Fonti ufficiali: www.inps.it • Circolari Quadro n. 94/2015, n. 142/2015 e Circolari INPS 2026"
            )

            Spacer(modifier = Modifier.height(14.dp))

            // Banner Scadenza 68 Giorni
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(6.dp))
                    .background(CharcoalPrimary)
                    .padding(14.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(RoundedCornerShape(4.dp))
                            .background(SageSurface),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.NotificationsActive,
                            contentDescription = null,
                            tint = CharcoalPrimary,
                            modifier = Modifier.size(20.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Column {
                        Text(
                            text = AppText.deadlineTitle(currentLanguage),
                            style = MaterialTheme.typography.labelMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = Color.White,
                                letterSpacing = 0.6.sp
                            )
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = AppText.deadlineDesc(currentLanguage),
                            style = MaterialTheme.typography.bodySmall.copy(color = SageSurface)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Riquadro Scritta e Pulsante Scarica / Visualizza Guida PDF (23 Pagine)
            PdfDownloadBanner(
                onClick = { showVideoAdForPdf = true },
                currentLanguage = currentLanguage,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Banner Pubblicitario Pagina Successiva (AdMob Display / Responsive Banner)
            AdBannerPlaceholder(
                adUnitId = "ca-app-pub-placeholder/main-screen-banner",
                currentLanguage = currentLanguage,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(18.dp))

            Text(
                text = AppText.selectOperation(currentLanguage),
                style = MaterialTheme.typography.labelLarge.copy(
                    letterSpacing = 0.8.sp,
                    fontWeight = FontWeight.Bold
                ),
                color = CharcoalPrimary
            )

            Spacer(modifier = Modifier.height(12.dp))

            // I QUATTRO PULSANTI PRINCIPALI (MANDATORY PER USER PROMPT)
            Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
                // 1. Simulare i Requisiti
                MainFeatureCardButton(
                    title = AppText.card1Title(currentLanguage),
                    subtitle = AppText.card1Desc(currentLanguage),
                    badgeText = "Art. 3 D.Lgs. 22/2015",
                    icon = Icons.Default.CheckCircle,
                    onClick = onNavigateToRequirements,
                    testTag = "btn_simulate_requirements",
                    actionText = AppText.openSection(currentLanguage)
                )

                // 2. Simulare Durata e Importi
                MainFeatureCardButton(
                    title = AppText.card2Title(currentLanguage),
                    subtitle = AppText.card2Desc(currentLanguage),
                    badgeText = AppText.badgeParams2026(currentLanguage),
                    icon = Icons.Default.Calculate,
                    onClick = onNavigateToAmounts,
                    testTag = "btn_simulate_amounts",
                    actionText = AppText.openSection(currentLanguage)
                )

                // 3. Guida Passo Passo alla Compilazione
                MainFeatureCardButton(
                    title = AppText.card3Title(currentLanguage),
                    subtitle = AppText.card3Desc(currentLanguage),
                    badgeText = AppText.badge8Phases(currentLanguage),
                    icon = Icons.Default.MenuBook,
                    onClick = onNavigateToGuide,
                    testTag = "btn_step_guide",
                    actionText = AppText.openSection(currentLanguage)
                )

                // 4. Documenti Necessari
                MainFeatureCardButton(
                    title = AppText.card4Title(currentLanguage),
                    subtitle = AppText.card4Desc(currentLanguage),
                    badgeText = AppText.badgeChecklist(currentLanguage),
                    icon = Icons.Default.Description,
                    onClick = onNavigateToDocuments,
                    testTag = "btn_required_documents",
                    actionText = AppText.openSection(currentLanguage)
                )
            }

            Spacer(modifier = Modifier.height(26.dp))

            // SEZIONE DOMANDE FREQUENTI & CHIARIMENTI NORMATIVI (SEO & E-E-A-T)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = AppText.faqSectionTitle(currentLanguage),
                    style = MaterialTheme.typography.labelLarge.copy(
                        letterSpacing = 0.8.sp,
                        fontWeight = FontWeight.Bold
                    ),
                    color = CharcoalPrimary
                )
                Text(
                    text = AppText.inpsSourcesLabel(currentLanguage),
                    style = MaterialTheme.typography.labelSmall.copy(
                        color = CharcoalMuted,
                        fontStyle = FontStyle.Italic
                    )
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                InpsRepository.getFaqList(currentLanguage).forEach { faq ->
                    val isExpanded = expandedFaqs[faq.id] ?: false

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(6.dp))
                            .background(SageCardLight)
                            .border(1.dp, SageBorder, RoundedCornerShape(6.dp))
                            .clickable { expandedFaqs[faq.id] = !isExpanded }
                            .padding(14.dp)
                    ) {
                        Column {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.Top
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Box(
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(3.dp))
                                            .background(SageSurface)
                                            .border(0.8.dp, CharcoalPrimary, RoundedCornerShape(3.dp))
                                            .padding(horizontal = 6.dp, vertical = 2.dp)
                                    ) {
                                        Text(
                                            text = faq.tag.uppercase(),
                                            style = MaterialTheme.typography.labelSmall.copy(
                                                fontSize = 9.sp,
                                                fontWeight = FontWeight.Bold,
                                                letterSpacing = 0.5.sp,
                                                color = CharcoalPrimary
                                            )
                                        )
                                    }
                                    Spacer(modifier = Modifier.height(6.dp))
                                    Text(
                                        text = faq.question,
                                        style = MaterialTheme.typography.titleSmall.copy(
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 14.sp,
                                            lineHeight = 18.sp
                                        ),
                                        color = CharcoalPrimary
                                    )
                                }

                                Icon(
                                    imageVector = if (isExpanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                                    contentDescription = if (isExpanded) "Comprimi" else "Espandi",
                                    tint = CharcoalPrimary,
                                    modifier = Modifier.padding(start = 8.dp)
                                )
                            }

                            AnimatedVisibility(visible = isExpanded) {
                                Column(modifier = Modifier.padding(top = 10.dp)) {
                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(1.dp)
                                            .background(SageBorder)
                                    )
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Text(
                                        text = faq.answer,
                                        style = MaterialTheme.typography.bodySmall.copy(
                                            color = CharcoalSecondary,
                                            lineHeight = 18.sp
                                        )
                                    )
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Box(
                                            modifier = Modifier
                                                .size(3.dp, 14.dp)
                                                .background(CharcoalPrimary)
                                        )
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Text(
                                            text = faq.legalCitation,
                                            style = MaterialTheme.typography.labelSmall.copy(
                                                fontStyle = FontStyle.Italic,
                                                color = CharcoalMuted,
                                                fontSize = 10.sp
                                            )
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Disclaimer Box Permanente
            OfficialDisclaimerBox(currentLanguage = currentLanguage)

            Spacer(modifier = Modifier.height(16.dp))

            AppFooter(currentLanguage = currentLanguage)

            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}
