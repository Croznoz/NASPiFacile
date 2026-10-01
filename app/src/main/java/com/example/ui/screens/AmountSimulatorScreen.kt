package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.TrendingDown
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.model.AppLanguage
import com.example.model.AppText
import com.example.model.InpsConstants
import com.example.model.NaspiCalculator
import com.example.ui.components.AppFooter
import com.example.ui.components.EditorialButton
import com.example.ui.components.EditorialCard
import com.example.ui.components.LanguageToggleAction
import com.example.ui.theme.AccentGreen
import com.example.ui.theme.AccentWarning
import com.example.ui.theme.CharcoalMuted
import com.example.ui.theme.CharcoalPrimary
import com.example.ui.theme.CharcoalSecondary
import com.example.ui.theme.SageBackground
import com.example.ui.theme.SageBorder
import com.example.ui.theme.SageCardLight
import com.example.ui.theme.SageSurface
import kotlin.math.roundToInt

/**
 * MODULO 2: SIMULATORE DURATA E IMPORTI NASpI
 * Fonti: D.Lgs. 22/2015 artt. 4-5; Circolare INPS n. 94/2015; Circolare INPS n. 25/2024; L. 234/2021.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AmountSimulatorScreen(
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
    currentLanguage: AppLanguage = AppLanguage.IT,
    onToggleLanguage: () -> Unit = {}
) {
    var contributionWeeks by remember { mutableFloatStateOf(104f) } // default 2 anni
    var monthlyGrossSalaryInput by remember { mutableStateOf("1650") }
    var isOver55 by remember { mutableStateOf(false) }
    var showFullSchedule by remember { mutableStateOf(false) }

    val weeksInt = contributionWeeks.roundToInt().coerceAtLeast(1)
    val monthlyGross = monthlyGrossSalaryInput.toDoubleOrNull() ?: 1650.0

    // Calcolo del monte retributivo quadriennale stimato: (stipendio mensile / 4.33) * settimane
    val estimated4YearsSalary by remember(weeksInt, monthlyGross) {
        derivedStateOf {
            (monthlyGross / InpsConstants.COEFFICIENTE_MESE) * weeksInt
        }
    }

    val result by remember(weeksInt, estimated4YearsSalary, isOver55) {
        derivedStateOf {
            NaspiCalculator.calculate(
                weeksWorked = weeksInt,
                totalTaxableSalary4Years = estimated4YearsSalary,
                isOver55 = isOver55
            )
        }
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = SageBackground,
        topBar = {
            TopAppBar(
                title = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.testTag("app_logo_amount")
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
                            text = AppText.amountsTitle(currentLanguage),
                            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                            color = CharcoalPrimary
                        )
                    }
                },
                navigationIcon = {
                    IconButton(
                        onClick = onBack,
                        modifier = Modifier.testTag("back_button_amount")
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
            // Intestazione con parametri ufficiali INPS
            Text(
                text = if (AppText.isEnglish(currentLanguage)) "Official Calculation According to INPS Formula" else "Calcolo Ufficiale Secondo Formula INPS",
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
                        "Reference threshold: € 1,456.72 • Maximum Cap: € 1,584.70 (INPS Parameters 2026)"
                    else
                        "Soglia di riferimento: € 1.456,72 • Tetto Massimo: € 1.584,70 (Parametri INPS 2026)",
                    style = MaterialTheme.typography.bodySmall.copy(fontStyle = FontStyle.Italic),
                    color = CharcoalSecondary
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            // SCHEDA RISULTATO PRINCIPALE
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(6.dp))
                    .background(CharcoalPrimary)
                    .padding(20.dp)
                    .testTag("simulation_summary_box")
            ) {
                Column {
                    Text(
                        text = AppText.amountInitialMonthlyGross(currentLanguage),
                        style = MaterialTheme.typography.labelMedium.copy(
                            color = SageSurface,
                            letterSpacing = 1.sp,
                            fontWeight = FontWeight.Bold
                        )
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = NaspiCalculator.formatCurrency(result.initialGrossMonthly),
                        style = MaterialTheme.typography.displayMedium.copy(
                            color = Color.White,
                            fontWeight = FontWeight.Bold
                        )
                    )

                    Spacer(modifier = Modifier.height(16.dp))
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(1.dp)
                            .background(CharcoalSecondary)
                    )
                    Spacer(modifier = Modifier.height(16.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text(
                                text = AppText.amountDurationEntitled(currentLanguage),
                                style = MaterialTheme.typography.labelSmall.copy(color = SageSurface),
                            )
                            Text(
                                text = AppText.durationFormatted(result.durationWeeks, result.durationMonths, currentLanguage),
                                style = MaterialTheme.typography.titleMedium.copy(
                                    color = Color.White,
                                    fontWeight = FontWeight.Bold
                                )
                            )
                        }

                        Column(horizontalAlignment = Alignment.End) {
                            Text(
                                text = "DÉCALAGE -3%",
                                style = MaterialTheme.typography.labelSmall.copy(color = SageSurface)
                            )
                            Text(
                                text = AppText.decalageStartFormatted(result.decalageStartMonth, currentLanguage),
                                style = MaterialTheme.typography.titleMedium.copy(
                                    color = Color.White,
                                    fontWeight = FontWeight.Bold
                                )
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = AppText.amountTotalEstimatedGross(currentLanguage),
                            style = MaterialTheme.typography.bodySmall.copy(color = SageSurface)
                        )
                        Text(
                            text = NaspiCalculator.formatCurrency(result.totalEstimatedGross),
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = Color.White,
                                fontWeight = FontWeight.Bold
                            )
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // DISCLAIMER CALCOLO INDENNITÀ
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(6.dp))
                    .background(SageSurface)
                    .border(1.dp, SageBorder, RoundedCornerShape(6.dp))
                    .padding(14.dp)
                    .testTag("amounts_disclaimer_box")
            ) {
                Row(verticalAlignment = Alignment.Top) {
                    Icon(
                        imageVector = Icons.Default.Info,
                        contentDescription = null,
                        tint = CharcoalPrimary,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = AppText.amountsDisclaimer(currentLanguage),
                        style = MaterialTheme.typography.bodySmall.copy(
                            fontWeight = FontWeight.Medium,
                            lineHeight = 17.sp
                        ),
                        color = CharcoalPrimary
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Box Principio Ufficiale di Calcolo Importo 2026
            EditorialCard(
                backgroundColor = SageCardLight,
                borderColor = CharcoalPrimary
            ) {
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(24.dp)
                                .clip(RoundedCornerShape(4.dp))
                                .background(CharcoalPrimary),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Calculate,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = if (AppText.isEnglish(currentLanguage))
                                "How the Amount is Calculated (2026 INPS Rule)"
                            else
                                "Come si calcola l'importo (Regola Ufficiale 2026)",
                            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                            color = CharcoalPrimary
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = if (AppText.isEnglish(currentLanguage))
                            "The NASpI amount equals 75% of the average monthly gross salary over the past four years if the average salary is below € 1,456.72 (reference benchmark for 2026), up to a maximum ceiling of € 1,584.70 per month."
                        else
                            "L'importo della NASpI è pari al 75% della retribuzione media mensile degli ultimi quattro anni se lo stipendio medio è inferiore a 1.456,72 euro (cifra di riferimento per il 2026), fino a un massimo di 1.584,70 euro al mese.",
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontWeight = FontWeight.SemiBold,
                            lineHeight = 20.sp
                        ),
                        color = CharcoalPrimary
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = if (AppText.isEnglish(currentLanguage))
                            "The calculation varies depending on your average earnings over the past 4 years:"
                        else
                            "Il calcolo cambia in base alla media dei tuoi stipendi degli ultimi 4 anni:",
                        style = MaterialTheme.typography.bodySmall.copy(fontStyle = FontStyle.Italic),
                        color = CharcoalSecondary
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        Row(verticalAlignment = Alignment.Top) {
                            Text("• ", color = CharcoalPrimary, fontWeight = FontWeight.Bold)
                            Text(
                                text = if (AppText.isEnglish(currentLanguage))
                                    "Low or medium salary (up to € 1,456.72): You get 75% of your average salary."
                                else
                                    "Stipendio basso o medio (fino a 1.456,72 €): Prendi il 75% della tua retribuzione media.",
                                style = MaterialTheme.typography.bodySmall.copy(lineHeight = 18.sp),
                                color = CharcoalPrimary
                            )
                        }
                        Row(verticalAlignment = Alignment.Top) {
                            Text("• ", color = CharcoalPrimary, fontWeight = FontWeight.Bold)
                            Text(
                                text = if (AppText.isEnglish(currentLanguage))
                                    "High salary (over € 1,456.72): You get 75% of € 1,456.72 plus 25% of the difference between your average salary and € 1,456.72."
                                else
                                    "Stipendio alto (oltre 1.456,72 €): Prendi il 75% di 1.456,72 euro più il 25% della differenza tra il tuo stipendio medio e 1.456,72 euro.",
                                style = MaterialTheme.typography.bodySmall.copy(lineHeight = 18.sp),
                                color = CharcoalPrimary
                            )
                        }
                        Row(verticalAlignment = Alignment.Top) {
                            Text("• ", color = CharcoalPrimary, fontWeight = FontWeight.Bold)
                            Text(
                                text = if (AppText.isEnglish(currentLanguage))
                                    "Maximum ceiling: In any case, the benefit cannot exceed the maximum cap of € 1,584.70 per month for 2026."
                                else
                                    "Limite massimo: L'assegno non può comunque superare il massimale di 1.584,70 euro al mese per il 2026.",
                                style = MaterialTheme.typography.bodySmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    lineHeight = 18.sp
                                ),
                                color = CharcoalPrimary
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            // INPUT PARAMETRI
            Text(
                text = AppText.enterSalaryData(currentLanguage),
                style = MaterialTheme.typography.labelLarge.copy(
                    letterSpacing = 0.8.sp,
                    fontWeight = FontWeight.Bold
                ),
                color = CharcoalPrimary
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Parametro 1: Settimane lavorate nel quadriennio
            EditorialCard {
                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = AppText.workedWeeksLabel(currentLanguage),
                            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                            color = CharcoalPrimary
                        )
                        Text(
                            text = if (AppText.isEnglish(currentLanguage)) "$weeksInt weeks" else "$weeksInt settimane",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                            color = CharcoalPrimary
                        )
                    }

                    Slider(
                        value = contributionWeeks,
                        onValueChange = { contributionWeeks = it },
                        valueRange = 13f..208f,
                        steps = 194,
                        colors = SliderDefaults.colors(
                            thumbColor = CharcoalPrimary,
                            activeTrackColor = CharcoalPrimary,
                            inactiveTrackColor = SageBorder
                        ),
                        modifier = Modifier.testTag("weeks_slider_calculator")
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = if (AppText.isEnglish(currentLanguage)) "Minimum: 13 wks" else "Minimo: 13 sett.",
                            style = MaterialTheme.typography.labelSmall,
                            color = CharcoalMuted
                        )
                        Text(
                            text = if (AppText.isEnglish(currentLanguage)) "Duration = Weeks ÷ 2" else "Durata = Settimane ÷ 2",
                            style = MaterialTheme.typography.labelSmall.copy(fontStyle = FontStyle.Italic),
                            color = CharcoalSecondary
                        )
                        Text(
                            text = if (AppText.isEnglish(currentLanguage)) "Max: 208 wks (4 yrs)" else "Max: 208 sett. (4 anni)",
                            style = MaterialTheme.typography.labelSmall,
                            color = CharcoalMuted
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Parametro 2: Retribuzione lorda media mensile
            EditorialCard {
                Column {
                    Text(
                        text = if (AppText.isEnglish(currentLanguage)) "Average Monthly Gross Salary (€):" else "Stipendio Medio Mensile Lordo Ordinario (€):",
                        style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                        color = CharcoalPrimary
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = if (AppText.isEnglish(currentLanguage))
                            "Enter the average monthly gross social security taxable base reported on pay slips."
                        else
                            "Inserisci l'imponibile previdenziale medio mensile riportato sulle buste paga.",
                        style = MaterialTheme.typography.bodySmall,
                        color = CharcoalSecondary
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    OutlinedTextField(
                        value = monthlyGrossSalaryInput,
                        onValueChange = { monthlyGrossSalaryInput = it },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("salary_input_field"),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = CharcoalPrimary,
                            unfocusedBorderColor = SageBorder,
                            focusedTextColor = CharcoalPrimary,
                            unfocusedTextColor = CharcoalPrimary,
                            focusedContainerColor = SageCardLight,
                            unfocusedContainerColor = SageCardLight
                        ),
                        singleLine = true,
                        prefix = { Text("€ ", color = CharcoalPrimary) }
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    // Spiegazione della formula del 75%
                    val isAboveThreshold = result.averageMonthlySalary > InpsConstants.SOGLIA_RETRIBUZIONE_RIFERIMENTO
                    val formulaDesc = if (!isAboveThreshold) {
                        if (AppText.isEnglish(currentLanguage))
                            "Low or medium salary (up to € 1,456.72): You get 75% of your average salary."
                        else
                            "Stipendio basso o medio (fino a 1.456,72 €): Prendi il 75% della tua retribuzione media (${NaspiCalculator.formatCurrency(result.averageMonthlySalary)} × 75% = ${NaspiCalculator.formatCurrency(result.initialGrossMonthly)})."
                    } else {
                        if (AppText.isEnglish(currentLanguage))
                            "High salary (over € 1,456.72): You get 75% of € 1,456.72 plus 25% of the difference between your average salary and € 1,456.72."
                        else
                            "Stipendio alto (oltre 1.456,72 €): Prendi il 75% di 1.456,72 euro più il 25% della differenza tra il tuo stipendio medio e 1.456,72 euro (75% di € 1.456,72 = € 1.092,54 + 25% quota eccedente, con tetto massimo di € 1.584,70)."
                    }

                    Text(
                        text = formulaDesc,
                        style = MaterialTheme.typography.bodySmall.copy(lineHeight = 16.sp),
                        color = CharcoalSecondary
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Parametro 3: Toggle Over 55
            EditorialCard {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = if (AppText.isEnglish(currentLanguage)) "Age 55 or older" else "Età pari o superiore a 55 anni",
                            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                            color = CharcoalPrimary
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = if (AppText.isEnglish(currentLanguage))
                                "Workers aged 55 and older benefit from postponing the decalage (-3% monthly reduction) to the 8th month instead of the 6th month (Law 234/2021)."
                            else
                                "I lavoratori con 55 anni compiuti beneficiano del rinvio del décalage (-3% mensile) all'8° mese anziché al 6° mese (L. 234/2021).",
                            style = MaterialTheme.typography.bodySmall,
                            color = CharcoalSecondary
                        )
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Switch(
                        checked = isOver55,
                        onCheckedChange = { isOver55 = it },
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = Color.White,
                            checkedTrackColor = CharcoalPrimary
                        ),
                        modifier = Modifier.testTag("over55_switch")
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // PROIEZIONE MENSILE DÉCALAGE
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = if (AppText.isEnglish(currentLanguage)) "MONTHLY PROJECTION WITH DÉCALAGE" else "PROIEZIONE MENSILE CON DÉCALAGE",
                    style = MaterialTheme.typography.labelLarge.copy(
                        letterSpacing = 0.8.sp,
                        fontWeight = FontWeight.Bold
                    ),
                    color = CharcoalPrimary
                )
                Text(
                    text = if (showFullSchedule)
                        (if (AppText.isEnglish(currentLanguage)) "Show less" else "Mostra meno")
                    else
                        (if (AppText.isEnglish(currentLanguage)) "View all months" else "Vedi tutti i mesi"),
                    style = MaterialTheme.typography.labelMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = CharcoalPrimary
                    ),
                    modifier = Modifier.testTag("toggle_schedule_button")
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            val displayedSchedule = if (showFullSchedule) {
                result.monthlySchedule
            } else {
                result.monthlySchedule.take(6)
            }

            EditorialCard {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    displayedSchedule.forEach { item ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(4.dp))
                                .background(if (item.monthNumber >= result.decalageStartMonth) SageSurface else SageCardLight)
                                .padding(horizontal = 10.dp, vertical = 8.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(
                                    text = if (AppText.isEnglish(currentLanguage)) "Month ${item.monthNumber}" else "Mese ${item.monthNumber}",
                                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                                    color = CharcoalPrimary
                                )
                                Text(
                                    text = item.getLocalizedNote(currentLanguage),
                                    style = MaterialTheme.typography.labelSmall,
                                    color = CharcoalMuted
                                )
                            }
                            Text(
                                text = NaspiCalculator.formatCurrency(item.grossAmount),
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = if (item.monthNumber >= result.decalageStartMonth) AccentWarning else CharcoalPrimary
                                )
                            )
                        }
                    }

                    if (result.monthlySchedule.size > 6) {
                        Spacer(modifier = Modifier.height(4.dp))
                        EditorialButton(
                            text = if (showFullSchedule)
                                (if (AppText.isEnglish(currentLanguage)) "Collapse Schedule" else "Comprimi Prospetto")
                            else
                                (if (AppText.isEnglish(currentLanguage)) "View All ${result.monthlySchedule.size} Months" else "Visualizza Tutti i ${result.monthlySchedule.size} Mesi"),
                            onClick = { showFullSchedule = !showFullSchedule },
                            isPrimary = false,
                            testTag = "expand_schedule_btn"
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Nota Fiscale Ufficiale
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
                        imageVector = Icons.Default.Info,
                        contentDescription = null,
                        tint = CharcoalPrimary,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = if (AppText.isEnglish(currentLanguage))
                            "Tax Note (IRPEF): Simulated amounts are GROSS of income taxes. INPS withholds income tax (IRPEF) in brackets applying employment tax credits and issues the annual Single Certification (CU) tax statement."
                        else
                            "Attenzione Fiscale (IRPEF): Gli importi simulati sono al LORDO delle ritenute fiscali. L'INPS trattiene l'IRPEF a scaglioni applicando le detrazioni da lavoro dipendente e rilascia a fine anno la Certificazione Unica (CU).",
                        style = MaterialTheme.typography.bodySmall.copy(lineHeight = 17.sp),
                        color = CharcoalSecondary
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Box Decorrenza Pagamento
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
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = if (AppText.isEnglish(currentLanguage)) "Payment Effective Date:" else "Decorrenza del pagamento:",
                            style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                            color = CharcoalPrimary
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = if (AppText.isEnglish(currentLanguage))
                                "NASpI starts from the 8th day following the termination of employment, or from the day following application if submitted after the 8th day."
                            else
                                "La NASpI spetta a partire dall'ottavo giorno successivo alla cessazione del rapporto di lavoro, oppure dal giorno successivo alla presentazione se la domanda viene inoltrata dopo l'ottavo giorno.",
                            style = MaterialTheme.typography.bodySmall.copy(lineHeight = 17.sp),
                            color = CharcoalSecondary
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
