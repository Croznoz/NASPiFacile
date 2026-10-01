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
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Schedule
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
import com.example.ui.components.AppFooter
import com.example.ui.components.EditorialButton
import com.example.ui.components.EditorialCard
import com.example.ui.components.LanguageToggleAction
import com.example.ui.components.PdfDownloadBanner
import com.example.ui.components.RewardedAdVideoDialog
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
 * MODULO 3: GUIDA PASSO PASSO ALLA COMPILAZIONE
 * Navigazione pratica dettagliata sul portale telematico www.inps.it.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StepByStepGuideScreen(
    onBack: () -> Unit,
    onNavigateToPdf: () -> Unit = {},
    modifier: Modifier = Modifier,
    currentLanguage: AppLanguage = AppLanguage.IT,
    onToggleLanguage: () -> Unit = {}
) {
    val steps = InpsRepository.getApplicationSteps(currentLanguage)
    // Tracciamento stato completato per ogni step
    val completedSteps = remember { mutableStateMapOf<Int, Boolean>() }
    var expandedStepNumber by remember { mutableStateOf(1) }
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

    val completedCount = completedSteps.values.count { it }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = SageBackground,
        topBar = {
            TopAppBar(
                title = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.testTag("app_logo_guide")
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.img_naspifacile_logo),
                            contentDescription = "NASpiFacile Logo",
                            modifier = Modifier
                                .height(32.dp)
                                .clip(RoundedCornerShape(6.dp))
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = AppText.guideTitle(currentLanguage),
                            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                            color = CharcoalPrimary
                        )
                    }
                },
                navigationIcon = {
                    IconButton(
                        onClick = onBack,
                        modifier = Modifier.testTag("back_button_guide")
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = AppText.back(currentLanguage),
                            tint = CharcoalPrimary
                        )
                    }
                },
                actions = {
                    LanguageToggleAction(
                        currentLanguage = currentLanguage.name,
                        onToggleLanguage = onToggleLanguage
                    )
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
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp, vertical = 10.dp)
        ) {
            // Header e citazione
            Text(
                text = if (AppText.isEnglish(currentLanguage)) "Claim Application on www.inps.it" else "Compilazione Domanda su www.inps.it",
                style = MaterialTheme.typography.headlineMedium,
                color = CharcoalPrimary
            )
            Spacer(modifier = Modifier.height(6.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .width(2.dp)
                        .height(20.dp)
                        .background(CharcoalPrimary)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = if (AppText.isEnglish(currentLanguage))
                        "Source: INPS Portal User Manual • Leg. Decree 150/2015"
                    else
                        "Fonte: Manuale utente Portale INPS • D.Lgs. 150/2015",
                    style = MaterialTheme.typography.bodySmall.copy(fontStyle = FontStyle.Italic),
                    color = CharcoalSecondary
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Riquadro Scritta e Pulsante Scarica / Visualizza Guida PDF (23 Pagine)
            PdfDownloadBanner(
                onClick = { showVideoAdForPdf = true },
                currentLanguage = currentLanguage,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Box Progresso
            EditorialCard(
                backgroundColor = SageCardLight,
                borderColor = CharcoalPrimary
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = if (AppText.isEnglish(currentLanguage)) "Application Progress" else "Avanzamento Compilazione",
                            style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                            color = CharcoalPrimary
                        )
                        Text(
                            text = if (AppText.isEnglish(currentLanguage))
                                "$completedCount of ${steps.size} steps completed"
                            else
                                "$completedCount di ${steps.size} passaggi completati",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                            color = CharcoalPrimary
                        )
                    }
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(4.dp))
                            .background(if (completedCount == steps.size) AccentGreen else CharcoalPrimary)
                            .padding(horizontal = 10.dp, vertical = 6.dp)
                    ) {
                        Text(
                            text = "${(completedCount * 100) / steps.size}%",
                            style = MaterialTheme.typography.labelLarge.copy(
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Box Avvertenza sui 68 giorni e decorrenza
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(6.dp))
                    .background(SageSurface)
                    .border(1.dp, SageBorder, RoundedCornerShape(6.dp))
                    .padding(14.dp)
            ) {
                Row(verticalAlignment = Alignment.Top) {
                    Icon(
                        imageVector = Icons.Default.Schedule,
                        contentDescription = null,
                        tint = CharcoalPrimary,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = if (AppText.isEnglish(currentLanguage)) "Effective Dates & Deadlines:" else "Decorrenza e Scadenze:",
                            style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold),
                            color = CharcoalPrimary
                        )
                        Text(
                            text = if (AppText.isEnglish(currentLanguage))
                                "• If submitted within 8 days of job termination: benefit starts on the 8th day following termination.\n• If submitted between the 9th and 68th day: benefit starts on the day following submission of the claim."
                            else
                                "• Se inviata entro 8 giorni dalla fine del lavoro: l'indennità decorre dall'8° giorno successivo alla cessazione.\n• Se inviata tra il 9° e il 68° giorno: l'indennità decorre dal giorno successivo alla data di invio della domanda.",
                            style = MaterialTheme.typography.bodySmall.copy(lineHeight = 17.sp),
                            color = CharcoalSecondary
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = if (AppText.isEnglish(currentLanguage)) "PROCEDURE STEPS" else "I PASSAGGI DELLA PROCEDURA",
                style = MaterialTheme.typography.labelLarge.copy(
                    letterSpacing = 0.8.sp,
                    fontWeight = FontWeight.Bold
                ),
                color = CharcoalPrimary
            )

            Spacer(modifier = Modifier.height(10.dp))

            // Lista Step Espandibili
            steps.forEach { step ->
                val isExpanded = expandedStepNumber == step.stepNumber
                val isDone = completedSteps[step.stepNumber] == true

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 6.dp)
                        .clip(RoundedCornerShape(6.dp))
                        .background(if (isDone) SageSurface else SageCardLight)
                        .border(
                            width = if (isExpanded) 1.5.dp else 1.dp,
                            color = if (isExpanded) CharcoalPrimary else SageBorder,
                            shape = RoundedCornerShape(6.dp)
                        )
                ) {
                    Column {
                        // Header dello Step cliccabile
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    expandedStepNumber = if (isExpanded) 0 else step.stepNumber
                                }
                                .padding(14.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.weight(1f)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(32.dp)
                                        .clip(RoundedCornerShape(4.dp))
                                        .background(if (isDone) AccentGreen else CharcoalPrimary),
                                    contentAlignment = Alignment.Center
                                ) {
                                    if (isDone) {
                                        Icon(
                                            imageVector = Icons.Default.Check,
                                            contentDescription = if (AppText.isEnglish(currentLanguage)) "Completed" else "Completato",
                                            tint = Color.White,
                                            modifier = Modifier.size(18.dp)
                                        )
                                    } else {
                                        Text(
                                            text = "${step.stepNumber}",
                                            style = MaterialTheme.typography.labelLarge.copy(
                                                fontWeight = FontWeight.Bold,
                                                color = Color.White
                                            )
                                        )
                                    }
                                }

                                Spacer(modifier = Modifier.width(12.dp))

                                Column {
                                    Text(
                                        text = step.title,
                                        style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                                        color = CharcoalPrimary
                                    )
                                    Text(
                                        text = step.officialCategory,
                                        style = MaterialTheme.typography.labelSmall,
                                        color = CharcoalSecondary
                                    )
                                }
                            }

                            Icon(
                                imageVector = if (isExpanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                                contentDescription = if (isExpanded)
                                    (if (AppText.isEnglish(currentLanguage)) "Collapse" else "Comprimi")
                                else
                                    (if (AppText.isEnglish(currentLanguage)) "Expand" else "Espandi"),
                                tint = CharcoalPrimary
                            )
                        }

                        // Dettagli espansi dello Step
                        AnimatedVisibility(visible = isExpanded) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 14.dp, vertical = 8.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(1.dp)
                                        .background(SageBorder)
                                )

                                Spacer(modifier = Modifier.height(12.dp))

                                Text(
                                    text = step.description,
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = CharcoalPrimary
                                )

                                Spacer(modifier = Modifier.height(12.dp))

                                // Percorso portale
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clip(RoundedCornerShape(4.dp))
                                        .background(SageSurface)
                                        .padding(8.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Language,
                                        contentDescription = null,
                                        tint = CharcoalPrimary,
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = step.inpsPortalPath,
                                        style = MaterialTheme.typography.labelSmall.copy(
                                            fontWeight = FontWeight.Bold,
                                            fontFamily = androidx.compose.ui.text.font.FontFamily.Monospace
                                        ),
                                        color = CharcoalPrimary
                                    )
                                }

                                Spacer(modifier = Modifier.height(12.dp))

                                Text(
                                    text = if (AppText.isEnglish(currentLanguage)) "Operational Tips:" else "Consigli Operativi:",
                                    style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                                    color = CharcoalPrimary
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                step.keyTips.forEach { tip ->
                                    Row(
                                        modifier = Modifier.padding(vertical = 2.dp),
                                        verticalAlignment = Alignment.Top
                                    ) {
                                        Text(text = "• ", color = CharcoalPrimary, fontWeight = FontWeight.Bold)
                                        Text(
                                            text = tip,
                                            style = MaterialTheme.typography.bodySmall,
                                            color = CharcoalSecondary
                                        )
                                    }
                                }

                                Spacer(modifier = Modifier.height(10.dp))

                                // Nota ufficiale
                                Text(
                                    text = if (AppText.isEnglish(currentLanguage))
                                        "Official INPS Note: ${step.officialNotice}"
                                    else
                                        "Nota Ufficiale INPS: ${step.officialNotice}",
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        fontStyle = FontStyle.Italic,
                                        lineHeight = 15.sp
                                    ),
                                    color = CharcoalMuted
                                )

                                Spacer(modifier = Modifier.height(14.dp))

                                // Bottone per segnare come completato
                                EditorialButton(
                                    text = if (isDone)
                                        (if (AppText.isEnglish(currentLanguage)) "Mark as To Do" else "Contrassegna come Da Fare")
                                    else
                                        (if (AppText.isEnglish(currentLanguage)) "Mark Step ${step.stepNumber} as Completed ✓" else "Segna Passo ${step.stepNumber} come Completato ✓"),
                                    onClick = {
                                        completedSteps[step.stepNumber] = !isDone
                                        if (!isDone && step.stepNumber < steps.size) {
                                            expandedStepNumber = step.stepNumber + 1
                                        }
                                    },
                                    isPrimary = !isDone,
                                    testTag = "toggle_step_${step.stepNumber}"
                                )

                                Spacer(modifier = Modifier.height(6.dp))
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // NOTA INFORMATIVA SIISL
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp))
                    .background(SageSurface)
                    .border(1.5.dp, CharcoalPrimary, RoundedCornerShape(8.dp))
                    .padding(16.dp)
            ) {
                Column {
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
                                text = if (AppText.isEnglish(currentLanguage)) "INFORMATION NOTE" else "NOTA INFORMATIVA",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White,
                                    letterSpacing = 0.5.sp
                                )
                            )
                        }
                        Text(
                            text = if (AppText.isEnglish(currentLanguage)) "SIISL Platform" else "Piattaforma SIISL",
                            style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                            color = CharcoalPrimary
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = if (AppText.isEnglish(currentLanguage))
                            "Mandatory NASpI Obligations on the SIISL Platform"
                        else
                            "Adempimenti obbligatori NASpI su piattaforma SIISL",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        color = CharcoalPrimary
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = if (AppText.isEnglish(currentLanguage))
                            "• What it is: Enrollment on the SIISL platform is mandatory to maintain entitlement to NASpI unemployment benefit."
                        else
                            "• Cos'è: L'iscrizione alla piattaforma SIISL è obbligatoria per mantenere il diritto alla disoccupazione NASpI.",
                        style = MaterialTheme.typography.bodySmall.copy(lineHeight = 18.sp),
                        color = CharcoalPrimary
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = if (AppText.isEnglish(currentLanguage))
                            "• What happens initially: Upon approval of your claim, INPS automatically registers you on the platform and sends a notification (SMS or email). You cannot and should not do this at the time of claim filing."
                        else
                            "• Cosa succede all'inizio: Al momento dell'accoglimento della domanda, l'INPS ti iscrive automaticamente alla piattaforma e ti invia una notifica (SMS o e-mail). Non devi e non puoi farlo al momento dell'inoltro della domanda.",
                        style = MaterialTheme.typography.bodySmall.copy(lineHeight = 18.sp),
                        color = CharcoalSecondary
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = if (AppText.isEnglish(currentLanguage))
                            "• What you must do (Within 15 days): The 15-day deadline begins from the date of approval/effective start of NASpI (not from submission). Within this period you must log in to the SIISL site via SPID or CIE to:\n  1. Sign the PAD (Digital Activation Agreement).\n  2. Enter or update your Curriculum Vitae (CV)."
                        else
                            "• Cosa devi fare (Entro 15 giorni): Il termine di 15 giorni scatta dalla data di accoglimento/decorrenza della NASpI (non dall'inoltro). Entro questo termine devi accedere al sito SIISL tramite SPID o CIE per:\n  1. Sottoscrivere il PAD (Patto di Attivazione Digitale).\n  2. Inserire o aggiornare il tuo Curriculum Vitae (CV).",
                        style = MaterialTheme.typography.bodySmall.copy(lineHeight = 18.sp),
                        color = CharcoalPrimary
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = if (AppText.isEnglish(currentLanguage))
                            "• Final stage: You will subsequently be contacted by your local Job Center (CPI) to sign the Personalized Service Agreement."
                        else
                            "• Fase finale: Successivamente verrai contattato dal tuo Centro per l'Impiego (CPI) per firmare il Patto di Servizio Personalizzato.",
                        style = MaterialTheme.typography.bodySmall.copy(lineHeight = 18.sp),
                        color = CharcoalSecondary
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(6.dp))
                            .background(Color(0xFFFDE8E8))
                            .border(1.dp, Color(0xFFC53030), RoundedCornerShape(6.dp))
                            .padding(12.dp)
                    ) {
                        Text(
                            text = if (AppText.isEnglish(currentLanguage))
                                "⚠ Penalties: If you fail to complete the online procedure or fail to attend the Job Center without justified cause, you risk suspension or forfeiture of NASpI."
                            else
                                "⚠ Sanzioni: Se non completi la procedura online o non ti presenti al CPI senza giustificato motivo rischi la sospensione o la perdita della NASpI.",
                            style = MaterialTheme.typography.bodySmall.copy(
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF9B1C1C),
                                lineHeight = 17.sp
                            )
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            AppFooter(currentLanguage = currentLanguage)

            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}
