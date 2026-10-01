package com.example.util

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.widget.Toast
import androidx.core.content.FileProvider
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream

object PdfHelper {
    const val PDF_FILE_NAME = "guidanaspicompilazione2026.pdf"

    /**
     * Extracts the PDF from assets into the app's cache directory and returns the File object.
     */
    fun getOrExtractPdfFile(context: Context): File {
        val cacheFile = File(context.cacheDir, PDF_FILE_NAME)
        try {
            // Always refresh from assets to ensure the latest version is used
            context.assets.open(PDF_FILE_NAME).use { inputStream ->
                FileOutputStream(cacheFile).use { outputStream ->
                    inputStream.copyTo(outputStream)
                    outputStream.flush()
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return cacheFile
    }

    /**
     * Gets a content Uri for the PDF file via FileProvider.
     */
    fun getPdfContentUri(context: Context): Uri? {
        val file = getOrExtractPdfFile(context)
        return try {
            val authority = "${context.packageName}.fileprovider"
            FileProvider.getUriForFile(context, authority, file)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    /**
     * Opens the PDF with an external viewer app (Google PDF Viewer, Drive, Adobe, etc.)
     */
    fun openPdfWithExternalApp(context: Context) {
        val uri = getPdfContentUri(context) ?: return
        val intent = Intent(Intent.ACTION_VIEW).apply {
            setDataAndType(uri, "application/pdf")
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        try {
            val chooser = Intent.createChooser(intent, "Apri Guida NASpI PDF").apply {
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            context.startActivity(chooser)
        } catch (e: Exception) {
            Toast.makeText(context, "Nessuna app trovata per visualizzare i PDF", Toast.LENGTH_LONG).show()
        }
    }

    /**
     * Shares the PDF with other apps or users (WhatsApp, Email, Telegram, Drive, etc.)
     */
    fun sharePdf(context: Context) {
        val uri = getPdfContentUri(context) ?: return
        val intent = Intent(Intent.ACTION_SEND).apply {
            type = "application/pdf"
            putExtra(Intent.EXTRA_STREAM, uri)
            putExtra(Intent.EXTRA_SUBJECT, "Guida Compilazione Domanda NASpI 2026")
            putExtra(Intent.EXTRA_TEXT, "Guida ufficiale e completa alla compilazione della domanda NASpI 2026 (23 pagine).")
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }
        try {
            val chooser = Intent.createChooser(intent, "Condividi o Salva Guida PDF").apply {
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            context.startActivity(chooser)
        } catch (e: Exception) {
            Toast.makeText(context, "Errore nella condivisione del file", Toast.LENGTH_SHORT).show()
        }
    }
}
