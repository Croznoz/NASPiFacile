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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
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
import com.example.model.CessationReason
import com.example.model.InpsConstants
import com.example.ui.components.AppFooter
import com.example.ui.components.EditorialCard
import com.example.ui.components.LanguageToggleAction
import com.example.ui.components.StatusTag
import com.example.ui.components.StatusType
import com.example.ui.theme.AccentError
import com.example.ui.theme.AccentGreen
import com.example.ui.theme.CharcoalMuted
import com.example.ui.theme.CharcoalPrimary
import com.example.ui.theme.CharcoalSecondary
import com.example.ui.theme.SageBackground
import com.example.ui.theme.SageBorder
import com.example.ui.theme.SageCardLight
import com.example.ui.theme.SageSurface
import kotlin.math.roundToInt

/**
 * MODULO 1: SIMULATORE DEI REQUISITI NASpI
 * Fonti: D.Lgs. 22/2015 art. 3; Circolare INPS n. 94/2015; L. 234/2021.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RequirementSimulatorScreen(
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
    currentLanguage: AppLanguage = AppLanguage.IT,
    onToggleLanguage: () -> Unit = {}
) {
    var selectedReason by remember { mutableStateOf(CessationReason.LICENZIAMENTO_GIUSTIFICATO_OGGETTIVO) }
    var contributionWeeks by remember { mutableFloatStateOf(52f) }
    var isReadyForWork by remember { mutableStateOf(true) }
    var hasRetirementPension by remember { mutableStateOf(false) }

    val weeksInt = contributionWeeks.roundToInt()
    val hasEnoughWeeks = weeksInt >= InpsConstants.SETTIMANE_MINIME_QUADRIENNIO
    val isReasonValid = selectedReason.isEligible
    val isDidValid = isReadyForWork
    val isPensionValid = !hasRetirementPension

    val isEligible = isReasonValid && hasEnoughWeeks && isDidValid && isPensionValid

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = SageBackground,
        topBar = {
            TopAppBar(
                title = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.testTag("app_logo_requirements")
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
                            text = AppText.reqTitle(currentLanguage),
                            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                            color = CharcoalPrimary
                        )
                    }
                },
                navigationIcon = {
                    IconButton(
                        onClick = onBack,
                        modifier = Modifier.testTag("back_button_requirements")
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
            // Header con citazione
            Text(
                text = AppText.reqHeaderTitle(currentLanguage),
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
                    text = AppText.reqHeaderSource(currentLanguage),
                    style = MaterialTheme.typography.bodySmall.copy(fontStyle = FontStyle.Italic),
                    color = CharcoalSecondary
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Box Risultato Live
            val outcomeBg = if (isEligible) AccentGreen.copy(alpha = 0.12f) else AccentError.copy(alpha = 0.12f)
            val outcomeBorder = if (isEligible) AccentGreen else AccentError
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(6.dp))
                    .background(outcomeBg)
                    .border(1.5.dp, outcomeBorder, RoundedCornerShape(6.dp))
                    .padding(16.dp)
                    .testTag("requirements_outcome_box")
            ) {
                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = if (isEligible) AppText.reqSatisfied(currentLanguage) else AppText.reqNotSatisfied(currentLanguage),
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                            color = if (isEligible) AccentGreen else AccentError
                        )
                        Icon(
                            imageVector = if (isEligible) Icons.Default.CheckCircle else Icons.Default.Error,
                            contentDescription = null,
                            tint = if (isEligible) AccentGreen else AccentError,
                            modifier = Modifier.size(24.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = if (isEligible) {
                            AppText.reqEligibleExplanation(currentLanguage)
                        } else {
                            val reasons = mutableListOf<String>()
                            if (!isReasonValid) reasons.add(AppText.reqReasonNotValid(currentLanguage))
                            if (!hasEnoughWeeks) reasons.add(AppText.reqWeeksNotValid(currentLanguage))
                            if (!isDidValid) reasons.add(AppText.reqDidNotValid(currentLanguage))
                            if (!isPensionValid) reasons.add(AppText.reqPensionNotValid(currentLanguage))
                            AppText.reqNotEligiblePrefix(currentLanguage) + reasons.joinToString(", ") + "."
                        },
                        style = MaterialTheme.typography.bodyMedium,
                        color = CharcoalPrimary
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Domanda 1: Motivo della cessazione
            Text(
                text = AppText.reqSection1Title(currentLanguage),
                style = MaterialTheme.typography.labelLarge.copy(
                    letterSpacing = 0.8.sp,
                    fontWeight = FontWeight.Bold
                ),
                color = CharcoalPrimary
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = AppText.reqSection1Subtitle(currentLanguage),
                style = MaterialTheme.typography.bodySmall,
                color = CharcoalSecondary
            )

            Spacer(modifier = Modifier.height(10.dp))

            CessationReason.values().forEach { reason ->
                val isSelected = selectedReason == reason
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                        .clip(RoundedCornerShape(6.dp))
                        .background(if (isSelected) SageCardLight else SageSurface)
                        .border(
                            width = if (isSelected) 1.5.dp else 1.dp,
                            color = if (isSelected) CharcoalPrimary else SageBorder,
                            shape = RoundedCornerShape(6.dp)
                        )
                        .clickable { selectedReason = reason }
                        .padding(12.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.Top
                    ) {
                        RadioButton(
                            selected = isSelected,
                            onClick = { selectedReason = reason },
                            colors = RadioButtonDefaults.colors(
                                selectedColor = CharcoalPrimary,
                                unselectedColor = CharcoalSecondary
                            )
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Column {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = reason.getLocalizedLabel(currentLanguage),
                                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold),
                                    color = CharcoalPrimary,
                                    modifier = Modifier.weight(1f)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                StatusTag(
                                    text = if (reason.isEligible) AppText.tagEligible(currentLanguage) else AppText.tagExcluded(currentLanguage),
                                    status = if (reason.isEligible) StatusType.SUCCESS else StatusType.ERROR
                                )
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = reason.getLocalizedNote(currentLanguage),
                                style = MaterialTheme.typography.bodySmall,
                                color = CharcoalSecondary
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = "${AppText.refLabel(currentLanguage)} ${reason.legalRef}",
                                style = MaterialTheme.typography.labelSmall.copy(fontStyle = FontStyle.Italic),
                                color = CharcoalMuted
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Domanda 2: Settimane di contribuzione nel quadriennio
            Text(
                text = AppText.reqSection2Title(currentLanguage),
                style = MaterialTheme.typography.labelLarge.copy(
                    letterSpacing = 0.8.sp,
                    fontWeight = FontWeight.Bold
                ),
                color = CharcoalPrimary
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = AppText.reqSection2Subtitle(currentLanguage),
                style = MaterialTheme.typography.bodySmall,
                color = CharcoalSecondary
            )

            Spacer(modifier = Modifier.height(10.dp))

            EditorialCard {
                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = if (AppText.isEnglish(currentLanguage)) "Declared weeks:" else "Settimane dichiarate:",
                            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Medium),
                            color = CharcoalPrimary
                        )
                        Text(
                            text = AppText.weeksLabelCount(weeksInt, weeksInt / 4.33, currentLanguage),
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                            color = CharcoalPrimary
                        )
                    }

                    Slider(
                        value = contributionWeeks,
                        onValueChange = { contributionWeeks = it },
                        valueRange = 0f..208f,
                        steps = 207,
                        colors = SliderDefaults.colors(
                            thumbColor = CharcoalPrimary,
                            activeTrackColor = CharcoalPrimary,
                            inactiveTrackColor = SageBorder
                        ),
                        modifier = Modifier.testTag("contribution_weeks_slider")
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = if (AppText.isEnglish(currentLanguage)) "0 wks" else "0 sett.",
                            style = MaterialTheme.typography.labelSmall,
                            color = CharcoalMuted
                        )
                        Text(
                            text = if (AppText.isEnglish(currentLanguage)) "Minimum: 13 wks" else "Minimo: 13 sett.",
                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                            color = if (hasEnoughWeeks) AccentGreen else AccentError
                        )
                        Text(
                            text = if (AppText.isEnglish(currentLanguage)) "Max: 208 wks (4 yrs)" else "Max: 208 sett. (4 anni)",
                            style = MaterialTheme.typography.labelSmall,
                            color = CharcoalMuted
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // Nota abolizione 30 giorni
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(4.dp))
                            .background(SageSurface)
                            .padding(10.dp)
                    ) {
                        Row(verticalAlignment = Alignment.Top) {
                            Icon(
                                imageVector = Icons.Default.Info,
                                contentDescription = null,
                                tint = CharcoalPrimary,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = if (AppText.isEnglish(currentLanguage))
                                    "Statutory update: From January 1, 2022 (Law 234/2021), the requirement of 30 days of actual work in the preceding 12 months was ABOLISHED."
                                else
                                    "Novità normativa: Dal 1° gennaio 2022 (Legge n. 234/2021) è stato ABOLITO il vecchio requisito delle 30 giornate di lavoro effettivo nei 12 mesi precedenti.",
                                style = MaterialTheme.typography.bodySmall.copy(lineHeight = 16.sp),
                                color = CharcoalSecondary
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Domanda 3: Condizioni accessorie (DID e Pensione)
            Text(
                text = if (AppText.isEnglish(currentLanguage)) "3. MANDATORY ADDITIONAL CONDITIONS" else "3. CONDIZIONI ACCESSORIE OBBLIGATORIE",
                style = MaterialTheme.typography.labelLarge.copy(
                    letterSpacing = 0.8.sp,
                    fontWeight = FontWeight.Bold
                ),
                color = CharcoalPrimary
            )

            Spacer(modifier = Modifier.height(10.dp))

            EditorialCard {
                Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = if (AppText.isEnglish(currentLanguage)) "Availability for Work (DID)" else "Disponibilità al Lavoro (DID)",
                                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                                color = CharcoalPrimary
                            )
                            Text(
                                text = if (AppText.isEnglish(currentLanguage))
                                    "You declare immediate availability for new employment and commitment to sign the Service Agreement with the Job Center."
                                else
                                    "Dichiari di essere immediatamente disponibile a nuove occupazioni e a stipulare il Patto di Servizio col CPI.",
                                style = MaterialTheme.typography.bodySmall,
                                color = CharcoalSecondary
                            )
                        }
                        Switch(
                            checked = isReadyForWork,
                            onCheckedChange = { isReadyForWork = it },
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = Color.White,
                                checkedTrackColor = CharcoalPrimary
                            ),
                            modifier = Modifier.testTag("did_switch")
                        )
                    }

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(1.dp)
                            .background(SageBorder)
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = if (AppText.isEnglish(currentLanguage)) "Direct Retirement Pension Recipient" else "Titolare di Pensione Diretta",
                                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                                color = CharcoalPrimary
                            )
                            Text(
                                text = if (AppText.isEnglish(currentLanguage))
                                    "Recipients of old-age or early retirement pensions are not entitled to NASpI (INPS Circular 94/2015 sec. 2.10)."
                                else
                                    "Chi percepisce pensione di vecchiaia o anticipata non ha diritto alla NASpI (Circolare INPS 94/2015 par. 2.10).",
                                style = MaterialTheme.typography.bodySmall,
                                color = CharcoalSecondary
                            )
                        }
                        Switch(
                            checked = hasRetirementPension,
                            onCheckedChange = { hasRetirementPension = it },
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = Color.White,
                                checkedTrackColor = AccentError
                            ),
                            modifier = Modifier.testTag("pension_switch")
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
