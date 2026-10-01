package com.example.model

/**
 * Riferimenti normativi e parametri ufficiali INPS:
 * - D.Lgs. 4 marzo 2015, n. 22 (Istituzione NASpI)
 * - Circolare INPS n. 94 del 12 maggio 2015 (Disposizioni applicative e calcolo)
 * - Circolare INPS n. 142 del 29 luglio 2015 (Chiarimenti su requisiti e contribuzione)
 * - Circolare INPS n. 25 del 29 gennaio 2024 (Valori e massimali rivalutati)
 * - Legge 30 dicembre 2021, n. 234 (Décalage dal 6° mese, o dall'8° mese per over 55)
 */
object InpsConstants {
    // Valori ufficiali rivalutati INPS (Parametri 2026)
    const val SOGLIA_RETRIBUZIONE_RIFERIMENTO = 1456.72
    const val MASSIMALE_MENSILE_LORDO = 1584.70
    const val COEFFICIENTE_MESE = 4.33
    const val PERCENTUALE_BASE = 0.75
    const val PERCENTUALE_ECCEDENZA = 0.25
    const val PERCENTUALE_DECALAGE = 0.03

    // Termini e requisiti
    const val TERMINE_PRESENTAZIONE_GIORNI = 68
    const val SETTIMANE_MINIME_QUADRIENNIO = 13
    const val SETTIMANE_MASSIME_CALCOLO = 208 // 4 anni = 208 settimane
    const val SETTIMANE_MASSIME_DURATA = 104 // 24 mesi di NASpI
}

/**
 * Motivo di cessazione del rapporto di lavoro
 */
enum class CessationReason(
    val label: String,
    val labelEn: String,
    val isEligible: Boolean,
    val note: String,
    val noteEn: String,
    val legalRef: String
) {
    LICENZIAMENTO_GIUSTIFICATO_OGGETTIVO(
        label = "Licenziamento per Giustificato Motivo Oggettivo / Economico",
        labelEn = "Dismissal for Objective / Economic Justified Reason",
        isEligible = true,
        note = "Idoneo. Cessazione involontaria ammessa a pieno titolo.",
        noteEn = "Eligible. Involuntary termination fully qualifying for benefit.",
        legalRef = "D.Lgs. 22/2015, art. 3 c. 1"
    ),
    LICENZIAMENTO_DISCIPLINARE(
        label = "Licenziamento Disciplinare (Giusta Causa / Soggettivo)",
        labelEn = "Disciplinary Dismissal (Just Cause / Subjective)",
        isEligible = true,
        note = "Idoneo. Anche in caso di licenziamento disciplinare lo stato di disoccupazione è considerato involontario.",
        noteEn = "Eligible. Even disciplinary dismissal is deemed involuntary unemployment by law.",
        legalRef = "Circolare INPS n. 94/2015, par. 2.1"
    ),
    SCADENZA_TERMINE(
        label = "Scadenza naturale di Contratto a Termine",
        labelEn = "Natural Expiry of Fixed-Term Contract",
        isEligible = true,
        note = "Idoneo. La mancata proroga o rinnovo costituisce cessazione involontaria.",
        noteEn = "Eligible. Non-renewal or contract expiration is recognized as involuntary.",
        legalRef = "Circolare INPS n. 94/2015, par. 2.1"
    ),
    DIMISSIONI_GIUSTA_CAUSA(
        label = "Dimissioni per Giusta Causa (es. mancati stipendi, mobbing, molestie)",
        labelEn = "Resignation for Just Cause (e.g. unpaid wages, mobbing, harassment)",
        isEligible = true,
        note = "Idoneo. Richiede allegazione documentale (diffida, vertenza o dichiarazione sostitutiva).",
        noteEn = "Eligible. Requires supporting evidence (formal warning, legal dispute or affidavit).",
        legalRef = "Circolare INPS n. 94/2015, par. 2.2"
    ),
    RISOLUZIONE_CONSENSUALE_ITL(
        label = "Risoluzione Consensuale presso Ispettorato del Lavoro (ITL)",
        labelEn = "Mutual Termination at Territorial Labor Inspectorate (ITL)",
        isEligible = true,
        note = "Idoneo se intervenuta nell'ambito della procedura di conciliazione obbligatoria ex art. 7 L. 604/1966.",
        noteEn = "Eligible if agreed through mandatory conciliation under Art. 7 Law 604/1966.",
        legalRef = "D.Lgs. 22/2015, art. 3 c. 2"
    ),
    DIMISSIONI_PERIODO_MATERNITA(
        label = "Dimissioni durante periodo di tutela maternità/paternità",
        labelEn = "Resignation during Maternity/Paternity Protection Period",
        isEligible = true,
        note = "Idoneo (da 300 giorni prima della data presunta parto fino al compimento di 1 anno di età del figlio).",
        noteEn = "Eligible (from 300 days before expected birth date until child turns 1 year old).",
        legalRef = "D.Lgs. 151/2001, art. 55"
    ),
    DIMISSIONI_VOLONTARIE_ORDINARIE(
        label = "Dimissioni Volontarie Ordinarie (senza giusta causa)",
        labelEn = "Ordinary Voluntary Resignation (without just cause)",
        isEligible = false,
        note = "NON IDONEO. Le dimissioni volontarie ordinarie non danno diritto alla NASpI in quanto cessazione volontaria.",
        noteEn = "NOT ELIGIBLE. Standard voluntary resignation does not qualify as it is a voluntary termination.",
        legalRef = "D.Lgs. 22/2015, art. 3"
    ),
    RISOLUZIONE_CONSENSUALE_ORDINARIA(
        label = "Risoluzione Consensuale Ordinaria (privata, senza procedura ITL)",
        labelEn = "Ordinary Mutual Termination (private, without ITL conciliation)",
        isEligible = false,
        note = "NON IDONEO, salvo specifico rifiuto del lavoratore al trasferimento d'azienda oltre 50 km.",
        noteEn = "NOT ELIGIBLE, except when refusing company transfer over 50 km away.",
        legalRef = "Circolare INPS n. 94/2015, par. 2.3"
    );

    fun getLocalizedLabel(lang: AppLanguage): String = if (lang == AppLanguage.EN) labelEn else label
    fun getLocalizedNote(lang: AppLanguage): String = if (lang == AppLanguage.EN) noteEn else note
}

/**
 * Step per la guida passo passo
 */
data class ApplicationStep(
    val stepNumber: Int,
    val title: String,
    val officialCategory: String,
    val description: String,
    val keyTips: List<String>,
    val inpsPortalPath: String,
    val officialNotice: String
)

/**
 * Elemento checklist documenti
 */
data class DocumentItem(
    val id: String,
    val title: String,
    val mandatory: Boolean,
    val description: String,
    val inpsNote: String
)

/**
 * Risultato simulazione importi e durata
 */
data class SimulationResult(
    val weeksWorked: Int,
    val durationWeeks: Int,
    val durationMonths: Double,
    val averageMonthlySalary: Double,
    val initialGrossMonthly: Double,
    val isOver55: Boolean,
    val decalageStartMonth: Int,
    val monthlySchedule: List<MonthlyPaymentProjection>,
    val totalEstimatedGross: Double
)

data class MonthlyPaymentProjection(
    val monthNumber: Int,
    val grossAmount: Double,
    val note: String,
    val noteEn: String = ""
) {
    fun getLocalizedNote(lang: AppLanguage): String = if (lang == AppLanguage.EN && noteEn.isNotBlank()) noteEn else note
}

/**
 * Elemento FAQ per ottimizzazione SEO e chiarezza normativa E-E-A-T
 */
data class FaqItem(
    val id: String,
    val question: String,
    val answer: String,
    val legalCitation: String,
    val tag: String
)
