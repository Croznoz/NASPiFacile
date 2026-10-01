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
import androidx.compose.material.icons.filled.CheckBox
import androidx.compose.material.icons.filled.CheckBoxOutlineBlank
import androidx.compose.material.icons.filled.FolderSpecial
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
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
import com.example.ui.components.StatusTag
import com.example.ui.components.StatusType
import com.example.ui.theme.AccentGreen
import com.example.ui.theme.CharcoalMuted
import com.example.ui.theme.CharcoalPrimary
import com.example.ui.theme.CharcoalSecondary
import com.example.ui.theme.SageBackground
import com.example.ui.theme.SageBorder
import com.example.ui.theme.SageCardLight
import com.example.ui.theme.SageSurface

/**
 * MODULO 4: DOCUMENTI NECESSARI
 * Checklist interattiva con requisiti documentali conformi a circolari INPS.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DocumentsChecklistScreen(
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
    currentLanguage: AppLanguage = AppLanguage.IT,
    onToggleLanguage: () -> Unit = {}
) {
    val documents = InpsRepository.getRequiredDocuments(currentLanguage)
    val checkedDocuments = remember { mutableStateMapOf<String, Boolean>() }
    var filterType by remember { mutableStateOf("ALL") } // ALL, MANDATORY, MISSING

    val totalCount = documents.size
    val checkedCount = checkedDocuments.values.count { it }
    val mandatoryTotal = documents.count { it.mandatory }
    val mandatoryChecked = documents.count { it.mandatory && checkedDocuments[it.id] == true }

    val filteredDocs = when (filterType) {
        "MANDATORY" -> documents.filter { it.mandatory }
        "MISSING" -> documents.filter { checkedDocuments[it.id] != true }
        else -> documents
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = SageBackground,
        topBar = {
            TopAppBar(
                title = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.testTag("app_logo_documents")
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
                            text = AppText.docsTitle(currentLanguage),
                            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                            color = CharcoalPrimary
                        )
                    }
                },
                navigationIcon = {
                    IconButton(
                        onClick = onBack,
                        modifier = Modifier.testTag("back_button_documents")
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
            // Intestazione con citazione
            Text(
                text = if (AppText.isEnglish(currentLanguage)) "Checklist Before You Start" else "Checklist Prima di Iniziare",
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
                        "Preliminary checks for self-filing or via CAF / Trade Union Patronato"
                    else
                        "Verifiche preliminari per trasmissione autonoma o presso CAF / Patronato",
                    style = MaterialTheme.typography.bodySmall.copy(fontStyle = FontStyle.Italic),
                    color = CharcoalSecondary
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Box Progresso Documentale
            EditorialCard(
                backgroundColor = SageCardLight,
                borderColor = CharcoalPrimary
            ) {
                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = if (AppText.isEnglish(currentLanguage)) "Documents Status" else "Stato Documenti",
                                style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                                color = CharcoalPrimary
                            )
                            Text(
                                text = if (AppText.isEnglish(currentLanguage))
                                    "$checkedCount of $totalCount documents ready"
                                else
                                    "$checkedCount di $totalCount documenti pronti",
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                color = CharcoalPrimary
                            )
                        }

                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(4.dp))
                                .background(if (mandatoryChecked == mandatoryTotal) AccentGreen else CharcoalPrimary)
                                .padding(horizontal = 10.dp, vertical = 6.dp)
                        ) {
                            Text(
                                text = if (mandatoryChecked == mandatoryTotal)
                                    (if (AppText.isEnglish(currentLanguage)) "Mandatory OK" else "Obbligatori OK")
                                else
                                    (if (AppText.isEnglish(currentLanguage)) "Incomplete" else "Incompleti"),
                                style = MaterialTheme.typography.labelMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = if (AppText.isEnglish(currentLanguage))
                            "Mandatory documents ready: $mandatoryChecked of $mandatoryTotal"
                        else
                            "Documenti obbligatori pronti: $mandatoryChecked su $mandatoryTotal",
                        style = MaterialTheme.typography.bodySmall,
                        color = CharcoalSecondary
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Filtri veloci
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                FilterChip(
                    selected = filterType == "ALL",
                    onClick = { filterType = "ALL" },
                    label = {
                        Text(if (AppText.isEnglish(currentLanguage)) "All ($totalCount)" else "Tutti ($totalCount)")
                    },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = CharcoalPrimary,
                        selectedLabelColor = Color.White,
                        containerColor = SageSurface,
                        labelColor = CharcoalPrimary
                    )
                )

                FilterChip(
                    selected = filterType == "MANDATORY",
                    onClick = { filterType = "MANDATORY" },
                    label = {
                        Text(if (AppText.isEnglish(currentLanguage)) "Mandatory Only ($mandatoryTotal)" else "Solo Obbligatori ($mandatoryTotal)")
                    },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = CharcoalPrimary,
                        selectedLabelColor = Color.White,
                        containerColor = SageSurface,
                        labelColor = CharcoalPrimary
                    )
                )

                FilterChip(
                    selected = filterType == "MISSING",
                    onClick = { filterType = "MISSING" },
                    label = {
                        Text(if (AppText.isEnglish(currentLanguage)) "Missing (${totalCount - checkedCount})" else "Mancanti (${totalCount - checkedCount})")
                    },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = CharcoalPrimary,
                        selectedLabelColor = Color.White,
                        containerColor = SageSurface,
                        labelColor = CharcoalPrimary
                    )
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Lista Documenti con Caselle di Spunta
            filteredDocs.forEach { doc ->
                val isChecked = checkedDocuments[doc.id] == true
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 5.dp)
                        .clip(RoundedCornerShape(6.dp))
                        .background(if (isChecked) SageSurface else SageCardLight)
                        .border(
                            width = if (isChecked) 1.5.dp else 1.dp,
                            color = if (isChecked) AccentGreen else SageBorder,
                            shape = RoundedCornerShape(6.dp)
                        )
                        .clickable {
                            checkedDocuments[doc.id] = !isChecked
                        }
                        .padding(14.dp)
                        .testTag("doc_item_${doc.id}")
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.Top
                    ) {
                        Checkbox(
                            checked = isChecked,
                            onCheckedChange = { checkedDocuments[doc.id] = it },
                            colors = CheckboxDefaults.colors(
                                checkedColor = CharcoalPrimary,
                                uncheckedColor = CharcoalSecondary,
                                checkmarkColor = Color.White
                            )
                        )

                        Spacer(modifier = Modifier.width(8.dp))

                        Column(modifier = Modifier.weight(1f)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = doc.title,
                                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                                    color = CharcoalPrimary,
                                    modifier = Modifier.weight(1f)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                StatusTag(
                                    text = if (doc.mandatory)
                                        (if (AppText.isEnglish(currentLanguage)) "Mandatory" else "Obbligatorio")
                                    else
                                        (if (AppText.isEnglish(currentLanguage)) "If requested" else "Se richiesto"),
                                    status = if (doc.mandatory) StatusType.NEUTRAL else StatusType.WARNING
                                )
                            }

                            Spacer(modifier = Modifier.height(4.dp))

                            Text(
                                text = doc.description,
                                style = MaterialTheme.typography.bodySmall,
                                color = CharcoalSecondary
                            )

                            Spacer(modifier = Modifier.height(4.dp))

                            Text(
                                text = if (AppText.isEnglish(currentLanguage))
                                    "INPS Circular / Practice: ${doc.inpsNote}"
                                else
                                    "Circolare / Prassi INPS: ${doc.inpsNote}",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontStyle = FontStyle.Italic,
                                    lineHeight = 15.sp
                                ),
                                color = CharcoalMuted
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Azione rapida seleziona tutti / reset
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                EditorialButton(
                    text = if (AppText.isEnglish(currentLanguage)) "Select All" else "Seleziona Tutti",
                    onClick = {
                        documents.forEach { checkedDocuments[it.id] = true }
                    },
                    isPrimary = false,
                    modifier = Modifier.weight(1f),
                    testTag = "select_all_docs_btn"
                )
                Spacer(modifier = Modifier.width(12.dp))
                EditorialButton(
                    text = if (AppText.isEnglish(currentLanguage)) "Reset Checklist" else "Azzera Spunte",
                    onClick = {
                        checkedDocuments.clear()
                    },
                    isPrimary = false,
                    modifier = Modifier.weight(1f),
                    testTag = "clear_docs_btn"
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            AppFooter(currentLanguage = currentLanguage)

            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}
