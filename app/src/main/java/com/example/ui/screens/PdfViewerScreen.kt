package com.example.ui.screens

import android.graphics.Bitmap
import android.graphics.pdf.PdfRenderer
import android.os.ParcelFileDescriptor
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.OpenInNew
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.model.AppLanguage
import com.example.model.AppText
import com.example.ui.components.AppFooter
import com.example.ui.components.EditorialButton
import com.example.ui.theme.CharcoalPrimary
import com.example.ui.theme.CharcoalSecondary
import com.example.ui.theme.SageBackground
import com.example.ui.theme.SageBorder
import com.example.ui.theme.SageCardLight
import com.example.ui.theme.SageSurface
import com.example.util.PdfHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PdfViewerScreen(
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
    currentLanguage: AppLanguage = AppLanguage.IT
) {
    val context = LocalContext.current
    var pageCount by remember { mutableIntStateOf(0) }
    val renderedBitmaps = remember { mutableStateMapOf<Int, Bitmap>() }

    DisposableEffect(Unit) {
        var renderer: PdfRenderer? = null
        var pfd: ParcelFileDescriptor? = null
        try {
            val file = PdfHelper.getOrExtractPdfFile(context)
            pfd = ParcelFileDescriptor.open(file, ParcelFileDescriptor.MODE_READ_ONLY)
            renderer = PdfRenderer(pfd)
            pageCount = renderer.pageCount
        } catch (e: Exception) {
            e.printStackTrace()
        }

        onDispose {
            try {
                renderer?.close()
                pfd?.close()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    LaunchedEffect(pageCount) {
        if (pageCount > 0) {
            withContext(Dispatchers.IO) {
                try {
                    val file = PdfHelper.getOrExtractPdfFile(context)
                    val pfd = ParcelFileDescriptor.open(file, ParcelFileDescriptor.MODE_READ_ONLY)
                    val renderer = PdfRenderer(pfd)
                    for (i in 0 until renderer.pageCount) {
                        val page = renderer.openPage(i)
                        // Render with high quality (2x scale)
                        val width = page.width * 2
                        val height = page.height * 2
                        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
                        page.render(bitmap, null, null, PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY)
                        page.close()
                        withContext(Dispatchers.Main) {
                            renderedBitmaps[i] = bitmap
                        }
                    }
                    renderer.close()
                    pfd.close()
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
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
                        modifier = Modifier.testTag("app_logo_pdf")
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.img_naspifacile_logo),
                            contentDescription = "NASpiFacile Logo",
                            modifier = Modifier
                                .height(32.dp)
                                .clip(RoundedCornerShape(6.dp))
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Column {
                            Text(
                                text = if (AppText.isEnglish(currentLanguage)) "Application Guide PDF" else "Guida Domanda PDF",
                                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                                color = CharcoalPrimary
                            )
                            Text(
                                text = if (AppText.isEnglish(currentLanguage)) "23 Pages • Editorial 2026" else "23 Pagine • Edizione 2026",
                                style = MaterialTheme.typography.labelSmall,
                                color = CharcoalSecondary
                            )
                        }
                    }
                },
                navigationIcon = {
                    IconButton(
                        onClick = onBack,
                        modifier = Modifier.testTag("back_button_pdf")
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = AppText.back(currentLanguage),
                            tint = CharcoalPrimary
                        )
                    }
                },
                actions = {
                    IconButton(
                        onClick = { PdfHelper.sharePdf(context) },
                        modifier = Modifier.testTag("share_pdf_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Share,
                            contentDescription = "Condividi PDF",
                            tint = CharcoalPrimary
                        )
                    }
                    IconButton(
                        onClick = { PdfHelper.openPdfWithExternalApp(context) },
                        modifier = Modifier.testTag("open_external_pdf_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.OpenInNew,
                            contentDescription = "Apri con lettore esterno",
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
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                // Header Banner con azioni veloci
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(8.dp))
                        .background(SageCardLight)
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
                                    .padding(horizontal = 8.dp, vertical = 4.dp)
                            ) {
                                Text(
                                    text = if (AppText.isEnglish(currentLanguage)) "OFFICIAL DOSSIER" else "FASCICOLO UFFICIALE",
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        fontWeight = FontWeight.Bold,
                                        color = Color.White
                                    )
                                )
                            }
                            Text(
                                text = "23 Pagine / Pages",
                                style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                                color = CharcoalPrimary
                            )
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        Text(
                            text = if (AppText.isEnglish(currentLanguage))
                                "Complete NASpI Application Manual"
                            else
                                "Manuale Completo Compilazione NASpI 2026",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                            color = CharcoalPrimary
                        )

                        Spacer(modifier = Modifier.height(4.dp))

                        Text(
                            text = if (AppText.isEnglish(currentLanguage))
                                "Reconstruction of all 16 INPS portal screens, contribution verification, IBAN setup and protocolled receipt."
                            else
                                "Ricostruzione di tutte le 16 schermate del portale INPS, verifica contributiva, gestione coordinate bancarie e rilascio ricevuta.",
                            style = MaterialTheme.typography.bodySmall,
                            color = CharcoalSecondary
                        )

                        Spacer(modifier = Modifier.height(14.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Box(modifier = Modifier.weight(1f)) {
                                EditorialButton(
                                    text = if (AppText.isEnglish(currentLanguage)) "Open External App" else "Apri con App Esterna",
                                    icon = Icons.Default.OpenInNew,
                                    onClick = { PdfHelper.openPdfWithExternalApp(context) },
                                    isPrimary = true,
                                    testTag = "btn_open_external"
                                )
                            }
                            Box(modifier = Modifier.weight(1f)) {
                                EditorialButton(
                                    text = if (AppText.isEnglish(currentLanguage)) "Share / Save PDF" else "Condividi / Salva PDF",
                                    icon = Icons.Default.Share,
                                    onClick = { PdfHelper.sharePdf(context) },
                                    isPrimary = false,
                                    testTag = "btn_share_pdf"
                                )
                            }
                        }
                    }
                }
            }

            if (pageCount == 0) {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(color = CharcoalPrimary)
                    }
                }
            } else {
                items(pageCount) { index ->
                    val bitmap = renderedBitmaps[index]
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(6.dp))
                            .background(Color.White)
                            .border(1.dp, SageBorder, RoundedCornerShape(6.dp))
                            .padding(10.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = if (AppText.isEnglish(currentLanguage)) "Page ${index + 1} of $pageCount" else "Pagina ${index + 1} di $pageCount",
                                style = MaterialTheme.typography.labelMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = CharcoalPrimary
                                )
                            )
                            Text(
                                text = "Croz Design®",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    color = CharcoalSecondary,
                                    fontSize = 10.sp
                                )
                            )
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        if (bitmap != null) {
                            Image(
                                bitmap = bitmap.asImageBitmap(),
                                contentDescription = "Pagina ${index + 1}",
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(4.dp))
                                    .border(0.5.dp, SageBorder, RoundedCornerShape(4.dp))
                            )
                        } else {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(350.dp)
                                    .background(SageSurface),
                                contentAlignment = Alignment.Center
                            ) {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(32.dp),
                                    color = CharcoalPrimary,
                                    strokeWidth = 2.dp
                                )
                            }
                        }
                    }
                }
            }

            item {
                Spacer(modifier = Modifier.height(10.dp))
                AppFooter(currentLanguage = currentLanguage)
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}
