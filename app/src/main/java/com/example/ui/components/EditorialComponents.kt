package com.example.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Gavel
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.PictureAsPdf
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
 * Intestazione editoriale in stile Typesafe / Minimal Swiss:
 * Titolo bold, tracking compatto, e barra verticale di citazione per le fonti ufficiali INPS.
 */
@Composable
fun EditorialHeader(
    title: String,
    subtitle: String? = null,
    sourceCitation: String = "Fonti ufficiali: www.inps.it • Circolari Quadro n. 94/2015, n. 142/2015 e Circolari INPS 2026",
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp)
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.displayMedium,
            color = CharcoalPrimary
        )

        if (subtitle != null) {
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = subtitle,
                style = MaterialTheme.typography.titleMedium,
                color = CharcoalSecondary
            )
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Barra verticale editoriale (stile immagine allegata)
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(start = 2.dp)
        ) {
            Box(
                modifier = Modifier
                    .width(2.5.dp)
                    .height(22.dp)
                    .background(CharcoalPrimary)
            )
            Spacer(modifier = Modifier.width(10.dp))
            Text(
                text = sourceCitation,
                style = MaterialTheme.typography.bodySmall.copy(
                    fontStyle = FontStyle.Italic,
                    letterSpacing = 0.2.sp
                ),
                color = CharcoalSecondary
            )
        }
    }
}

/**
 * Scheda con bordo nero minimal e sfondo leggero o a contrasto
 */
@Composable
fun EditorialCard(
    modifier: Modifier = Modifier,
    backgroundColor: Color = SageCardLight,
    borderColor: Color = CharcoalPrimary,
    content: @Composable () -> Unit
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(6.dp))
            .background(backgroundColor)
            .border(1.dp, borderColor, RoundedCornerShape(6.dp))
            .padding(16.dp)
    ) {
        content()
    }
}

/**
 * Pulsante primario ad alto contrasto (stile minimal dark o outlined)
 */
@Composable
fun EditorialButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    icon: ImageVector? = null,
    isPrimary: Boolean = true,
    testTag: String = "editorial_button"
) {
    val bg = if (isPrimary) CharcoalPrimary else Color.Transparent
    val textColor = if (isPrimary) Color.White else CharcoalPrimary
    val borderCol = if (isPrimary) CharcoalPrimary else CharcoalPrimary

    Surface(
        onClick = onClick,
        modifier = modifier
            .fillMaxWidth()
            .heightIn(min = 48.dp)
            .testTag(testTag),
        shape = RoundedCornerShape(6.dp),
        color = bg,
        border = androidx.compose.foundation.BorderStroke(1.5.dp, borderCol)
    ) {
        Row(
            modifier = Modifier
                .padding(horizontal = 16.dp, vertical = 10.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (icon != null) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = textColor,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(10.dp))
            }
            Text(
                text = text,
                style = MaterialTheme.typography.labelLarge.copy(
                    letterSpacing = 0.6.sp,
                    fontWeight = FontWeight.Bold
                ),
                color = textColor,
                maxLines = 1
            )
        }
    }
}

/**
 * Pulsante per la griglia delle 4 funzionalità della pagina principale
 */
@Composable
fun MainFeatureCardButton(
    title: String,
    subtitle: String,
    badgeText: String,
    icon: ImageVector,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    testTag: String,
    actionText: String = "Apri sezione →"
) {
    Surface(
        onClick = onClick,
        modifier = modifier
            .fillMaxWidth()
            .testTag(testTag),
        shape = RoundedCornerShape(6.dp),
        color = SageCardLight,
        border = androidx.compose.foundation.BorderStroke(1.2.dp, CharcoalPrimary),
        shadowElevation = 0.dp
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(18.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(42.dp)
                        .clip(RoundedCornerShape(6.dp))
                        .background(CharcoalPrimary),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(22.dp)
                    )
                }

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(4.dp))
                        .border(1.dp, CharcoalPrimary, RoundedCornerShape(4.dp))
                        .background(SageSurface)
                        .padding(horizontal = 8.dp, vertical = 3.dp)
                ) {
                    Text(
                        text = badgeText,
                        style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                        color = CharcoalPrimary
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            Text(
                text = title,
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                color = CharcoalPrimary
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodyMedium,
                color = CharcoalSecondary
            )

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = actionText,
                    style = MaterialTheme.typography.labelLarge.copy(
                        fontWeight = FontWeight.Bold,
                        color = CharcoalPrimary
                    )
                )
            }
        }
    }
}

/**
 * Banner riquadro con scritta e pulsante per visualizzare e scaricare la guida PDF da 23 pagine (come da layout ufficiale)
 */
@Composable
fun PdfDownloadBanner(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    currentLanguage: com.example.model.AppLanguage = com.example.model.AppLanguage.IT,
    isCompact: Boolean = false
) {
    val isEn = currentLanguage == com.example.model.AppLanguage.EN
    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(SageCardLight)
            .border(1.5.dp, CharcoalPrimary, RoundedCornerShape(8.dp))
            .padding(16.dp)
    ) {
        Column {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(4.dp))
                        .background(CharcoalPrimary)
                        .padding(horizontal = 7.dp, vertical = 3.dp)
                ) {
                    Text(
                        text = if (isEn) "PDF DOCUMENT" else "DOCUMENTO PDF",
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = FontWeight.Bold,
                            color = Color.White,
                            fontSize = 11.sp,
                            letterSpacing = 0.5.sp
                        )
                    )
                }

                Text(
                    text = if (isEn) "23 Pages • Editorial Format" else "23 Pagine • Formato Editoriale",
                    style = MaterialTheme.typography.labelMedium.copy(
                        fontWeight = FontWeight.SemiBold,
                        color = CharcoalSecondary,
                        fontSize = 12.5.sp
                    )
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = if (isEn)
                    "Official Manual Screen by Screen"
                else
                    "Manuale Ufficiale Schermata per Schermata",
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = if (isCompact) 17.sp else 19.sp
                ),
                color = CharcoalPrimary
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = if (isEn)
                    "View the guide in PDF format featuring graphical reconstructions of all INPS screens and operational guidelines."
                else
                    "Visualizza la guida in formato PDF impaginata con le ricostruzioni grafiche di tutte le schermate INPS e le note operative.",
                style = MaterialTheme.typography.bodyMedium.copy(
                    lineHeight = 20.sp,
                    fontSize = 13.5.sp
                ),
                color = CharcoalSecondary
            )

            Spacer(modifier = Modifier.height(14.dp))

            EditorialButton(
                text = if (isEn) "Apri PDF (23 Pagine)" else "Apri PDF (23 Pagine)",
                icon = Icons.Default.Download,
                onClick = onClick,
                isPrimary = true,
                testTag = "btn_download_view_pdf"
            )
        }
    }
}

/**
 * Banner disclaimer obbligatorio (normativa, privacy e assenza di sostituzione CAF/patronato)
 */
@Composable
fun OfficialDisclaimerBox(
    modifier: Modifier = Modifier,
    currentLanguage: com.example.model.AppLanguage = com.example.model.AppLanguage.IT
) {
    val isEn = currentLanguage == com.example.model.AppLanguage.EN
    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(6.dp))
            .background(SageSurface)
            .border(1.dp, SageBorder, RoundedCornerShape(6.dp))
            .padding(14.dp)
    ) {
        Row(
            verticalAlignment = Alignment.Top
        ) {
            Icon(
                imageVector = Icons.Default.Info,
                contentDescription = if (isEn) "Information and Privacy" else "Informativa e Privacy",
                tint = CharcoalPrimary,
                modifier = Modifier
                    .size(20.dp)
                    .padding(top = 2.dp)
            )
            Spacer(modifier = Modifier.width(10.dp))
            Column {
                Text(
                    text = if (isEn) "IMPORTANT NOTICE & POLICY" else "AVVISO IMPORTANTE E POLICY",
                    style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold),
                    color = CharcoalPrimary
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = if (isEn)
                        "This application serves an exclusively informative and technical simulation purpose based on official INPS circulars. It does not replace professional entities such as CAF, Patronati or labor consultants, nor formal claim filing on www.inps.it."
                    else
                        "Questo applicativo ha una funzione esclusivamente informativa e di simulazione tecnica basata su circolari ufficiali INPS. Non si sostituisce a enti professionali come CAF, Patronati o consulenti del lavoro, né alla presentazione sul portale www.inps.it.",
                    style = MaterialTheme.typography.bodySmall.copy(lineHeight = 18.sp),
                    color = CharcoalSecondary
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = if (isEn)
                        "Privacy: All calculations and simulations take place locally on the device. No personal data is saved or transmitted to external servers."
                    else
                        "Privacy: Tutti i calcoli e le simulazioni avvengono localmente sul dispositivo. Nessun dato personale viene salvato o trasmesso a server esterni.",
                    style = MaterialTheme.typography.bodySmall.copy(
                        fontStyle = FontStyle.Italic,
                        lineHeight = 16.sp
                    ),
                    color = CharcoalMuted
                )
            }
        }
    }
}

/**
 * Pill status
 */
@Composable
fun StatusTag(
    text: String,
    status: StatusType,
    modifier: Modifier = Modifier
) {
    val (bg, contentColor, borderCol) = when (status) {
        StatusType.SUCCESS -> Triple(AccentGreen.copy(alpha = 0.15f), AccentGreen, AccentGreen)
        StatusType.WARNING -> Triple(AccentWarning.copy(alpha = 0.15f), AccentWarning, AccentWarning)
        StatusType.ERROR -> Triple(AccentError.copy(alpha = 0.15f), AccentError, AccentError)
        StatusType.NEUTRAL -> Triple(CharcoalPrimary.copy(alpha = 0.08f), CharcoalPrimary, CharcoalPrimary)
    }

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(4.dp))
            .background(bg)
            .border(1.dp, borderCol, RoundedCornerShape(4.dp))
            .padding(horizontal = 8.dp, vertical = 3.dp)
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
            color = contentColor
        )
    }
}

enum class StatusType {
    SUCCESS, WARNING, ERROR, NEUTRAL
}

/**
 * Icona mappamondo vettoriale wireframe con meridiani e paralleli (stesso design della foto utente)
 */
@Composable
fun GlobeLanguageIcon(
    modifier: Modifier = Modifier,
    tint: Color = CharcoalPrimary
) {
    Canvas(modifier = modifier.size(19.dp)) {
        val strokeWidth = 1.6.dp.toPx()
        val radius = (size.minDimension / 2f) - strokeWidth
        val center = Offset(size.width / 2f, size.height / 2f)

        // Cerchio esterno
        drawCircle(
            color = tint,
            radius = radius,
            center = center,
            style = Stroke(width = strokeWidth)
        )

        // Parallelo orizzontale (equatore)
        drawLine(
            color = tint,
            start = Offset(center.x - radius, center.y),
            end = Offset(center.x + radius, center.y),
            strokeWidth = strokeWidth
        )

        // Meridiano ellittico verticale centrale
        val ellipseWidth = radius * 0.45f
        val rect = Rect(
            left = center.x - ellipseWidth,
            top = center.y - radius,
            right = center.x + ellipseWidth,
            bottom = center.y + radius
        )
        val path = Path().apply {
            addOval(rect)
        }
        drawPath(
            path = path,
            color = tint,
            style = Stroke(width = strokeWidth)
        )
    }
}

/**
 * Azione di commutazione lingua IT / EN con stile e font identici all'immagine di riferimento
 */
@Composable
fun LanguageToggleAction(
    currentLanguage: String, // "IT" o "EN"
    onToggleLanguage: () -> Unit,
    modifier: Modifier = Modifier
) {
    val isIt = currentLanguage == "IT"
    Row(
        modifier = modifier
            .clip(RoundedCornerShape(6.dp))
            .clickable(onClick = onToggleLanguage)
            .padding(horizontal = 8.dp, vertical = 6.dp)
            .testTag("language_toggle_button"),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        GlobeLanguageIcon(
            tint = CharcoalPrimary
        )
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "IT",
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontFamily = FontFamily.SansSerif,
                    fontWeight = if (isIt) FontWeight.Bold else FontWeight.Normal,
                    letterSpacing = 0.8.sp,
                    fontSize = 13.sp
                ),
                color = if (isIt) CharcoalPrimary else CharcoalMuted.copy(alpha = 0.55f)
            )
            Text(
                text = "  /  ",
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontFamily = FontFamily.SansSerif,
                    fontWeight = FontWeight.Light,
                    fontSize = 13.sp
                ),
                color = CharcoalMuted.copy(alpha = 0.45f)
            )
            Text(
                text = "EN",
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontFamily = FontFamily.SansSerif,
                    fontWeight = if (!isIt) FontWeight.Bold else FontWeight.Normal,
                    letterSpacing = 0.8.sp,
                    fontSize = 13.sp
                ),
                color = if (!isIt) CharcoalPrimary else CharcoalMuted.copy(alpha = 0.55f)
            )
        }
    }
}

/**
 * Footer condiviso presente in tutte le schermate dell'app
 */
@Composable
fun AppFooter(
    modifier: Modifier = Modifier,
    currentLanguage: com.example.model.AppLanguage = com.example.model.AppLanguage.IT,
    onOpenPolicy: (() -> Unit)? = null
) {
    val contextOpenPolicy = LocalOpenPolicy.current
    val actualOpenPolicy = onOpenPolicy ?: contextOpenPolicy

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(top = 16.dp, bottom = 12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Banner Fisso "Non Sostituzione a Enti Professionali" presente su ogni pagina
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(6.dp))
                .background(SageSurface)
                .border(1.2.dp, SageBorder, RoundedCornerShape(6.dp))
                .padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.Gavel,
                    contentDescription = null,
                    tint = CharcoalPrimary,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = com.example.model.AppText.nonReplacementTitle(currentLanguage),
                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                    color = CharcoalPrimary
                )
            }
            Text(
                text = com.example.model.AppText.nonReplacementSubtitle(currentLanguage),
                style = MaterialTheme.typography.bodySmall.copy(fontSize = 12.5.sp, lineHeight = 17.sp),
                color = CharcoalPrimary
            )
            Column(
                modifier = Modifier.padding(start = 4.dp),
                verticalArrangement = Arrangement.spacedBy(3.dp)
            ) {
                FooterBulletItem(text = com.example.model.AppText.bulletCaf(currentLanguage))
                FooterBulletItem(text = com.example.model.AppText.bulletPatronato(currentLanguage))
                FooterBulletItem(text = com.example.model.AppText.bulletConsultants(currentLanguage))
                FooterBulletItem(text = com.example.model.AppText.bulletInps(currentLanguage))
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(1.dp)
                .background(SageBorder)
        )
        Spacer(modifier = Modifier.height(12.dp))
        Text(
            text = com.example.model.AppText.footerNormative(currentLanguage),
            style = MaterialTheme.typography.labelSmall.copy(
                fontSize = 11.sp,
                fontWeight = FontWeight.Normal
            ),
            color = CharcoalMuted
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = com.example.model.AppText.createdBy(),
            style = MaterialTheme.typography.bodySmall.copy(
                fontWeight = FontWeight.Normal,
                letterSpacing = 0.4.sp,
                fontSize = 12.sp
            ),
            color = CharcoalSecondary
        )
        Spacer(modifier = Modifier.height(6.dp))
        // Ultima scritta nel footer di ogni pagina: Policy e Privacy
        Text(
            text = com.example.model.AppText.policyAndPrivacy(currentLanguage),
            style = MaterialTheme.typography.labelSmall.copy(
                fontWeight = FontWeight.SemiBold,
                textDecoration = TextDecoration.Underline,
                letterSpacing = 0.4.sp,
                fontSize = 11.5.sp
            ),
            color = CharcoalPrimary,
            modifier = Modifier
                .clip(RoundedCornerShape(4.dp))
                .clickable { actualOpenPolicy() }
                .padding(horizontal = 8.dp, vertical = 4.dp)
        )
    }
}

@Composable
private fun FooterBulletItem(text: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.Top
    ) {
        Text(
            text = "▪ ",
            style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold),
            color = CharcoalPrimary
        )
        Text(
            text = text,
            style = MaterialTheme.typography.bodySmall.copy(fontSize = 12.sp, lineHeight = 16.sp),
            color = CharcoalSecondary
        )
    }
}


