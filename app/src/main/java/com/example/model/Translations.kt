package com.example.model

object AppText {
    fun isEnglish(lang: AppLanguage) = lang == AppLanguage.EN

    // App Bar & Brand
    fun appTitle(lang: AppLanguage) = if (isEnglish(lang)) "NASpI Guide" else "Assistente NASpI"
    fun menu(lang: AppLanguage) = if (isEnglish(lang)) "Menu" else "Menu"
    fun legalNotice(lang: AppLanguage) = if (isEnglish(lang)) "Legal Notice" else "Informativa Legale"
    fun back(lang: AppLanguage) = if (isEnglish(lang)) "Back" else "Torna indietro"
    fun backToMenu(lang: AppLanguage) = if (isEnglish(lang)) "← Back to Menu" else "← Torna al Menu"
    fun openSection(lang: AppLanguage) = if (isEnglish(lang)) "Open section →" else "Apri sezione →"

    // Section Titles (TopAppBar)
    fun reqTitle(lang: AppLanguage) = if (isEnglish(lang)) "Requirement Simulator" else "Simulatore dei Requisiti"
    fun amountsTitle(lang: AppLanguage) = if (isEnglish(lang)) "Duration & Amount Calculation" else "Calcolo Durata e Importi"
    fun guideTitle(lang: AppLanguage) = if (isEnglish(lang)) "Step-by-Step Guide" else "Guida Passo Passo"
    fun docsTitle(lang: AppLanguage) = if (isEnglish(lang)) "Required Documents" else "Documenti Necessari"

    // Footer
    fun footerNormative(lang: AppLanguage) = if (isEnglish(lang)) "INPS Regulatory Database • Legislative Decree 22/2015" else "Banca Dati Normativa INPS • D.Lgs. 22/2015"
    fun createdBy() = "Created by Croz Design®"

    // Initial Screen
    fun initialBadge(lang: AppLanguage) = "NASpiFacile"
    fun initialTitle(lang: AppLanguage) = if (isEnglish(lang)) "NASpI Unemployment\nApplication Assistant" else "Assistente Domanda\nDisoccupazione NASpI"
    fun initialSubtitle(lang: AppLanguage) = if (isEnglish(lang)) "Informative database and technical simulator based on official INPS sources" else "Banca dati informativa e simulatore tecnico basato su fonti ufficiali INPS"
    fun initialSources(lang: AppLanguage) = if (isEnglish(lang)) "Official Sources: INPS (www.inps.it) • Legislative Decree 22/2015 • Circulars 94/2015 & 142/2015 • Revaluations updated to 2026" else "Fonti ufficiali: www.inps.it • D.Lgs. 22/2015 • Circolari 94/2015, 142/2015 e Rivalutazioni aggiornate al 2026"
    fun exclusiveDbTitle(lang: AppLanguage) = if (isEnglish(lang)) "Exclusive Regulatory Database: National Social Security Institute" else "Banca Dati Normativa Esclusiva: Istituto Nazionale Previdenza Sociale"
    fun exclusiveDbSubtitle(lang: AppLanguage) = if (isEnglish(lang)) "www.inps.it • Leg. Decree 22/2015 • Framework Circulars 94/2015, 142/2015 & INPS Circulars 2026" else "www.inps.it • D.Lgs. 22/2015 • Circolari Quadro n. 94/2015, 142/2015 e Circolari INPS 2026"
    fun featuresSectionTitle(lang: AppLanguage) = if (isEnglish(lang)) "APPLICATION FEATURES" else "FUNZIONALITÀ DELL'APPLICATIVO"

    fun feature1Title(lang: AppLanguage) = if (isEnglish(lang)) "1. Requirement Simulator" else "1. Simulatore dei Requisiti"
    fun feature1Desc(lang: AppLanguage) = if (isEnglish(lang)) "Preliminary check of involuntary unemployment status and contribution record (at least 13 weeks in the past 4-year period)." else "Verifica preliminare dello stato di disoccupazione involontaria e del monte contributivo (almeno 13 settimane nel quadriennio precedente)."

    fun feature2Title(lang: AppLanguage) = if (isEnglish(lang)) "2. Duration & Amount Calculation" else "2. Calcolo Durata e Importi"
    fun feature2Desc(lang: AppLanguage) = if (isEnglish(lang)) "Accurate estimate of entitled weeks (weeks / 2 up to 24 months), 75% gross monthly allowance and 3% monthly reduction schedule." else "Stima precisa delle settimane spettanti (settimane / 2 fino a 24 mesi), importo mensile lordo al 75% e applicazione del décalage al 3%."

    fun feature3Title(lang: AppLanguage) = if (isEnglish(lang)) "3. Step-by-Step Guide" else "3. Guida Passo Passo"
    fun feature3Desc(lang: AppLanguage) = if (isEnglish(lang)) "Screen-by-screen instructions on the official www.inps.it portal, from SPID/CIE login to submission and receipt." else "Istruzioni schermata per schermata sul portale telematico www.inps.it, dall'accesso SPID/CIE fino alla trasmissione e ricevuta."

    fun feature4Title(lang: AppLanguage) = if (isEnglish(lang)) "4. Required Documents Checklist" else "4. Checklist Documenti Necessari"
    fun feature4Desc(lang: AppLanguage) = if (isEnglish(lang)) "Complete list of personal, contractual, and banking documents to prepare in order to avoid delays or claim suspensions." else "Elenco completo dei documenti anagrafici, contrattuali e bancari da preparare per evitare ritardi o sospensioni della pratica."

    fun privacySectionTitle(lang: AppLanguage) = if (isEnglish(lang)) "TERMS OF USE, PRIVACY & LEGAL DISCLAIMER" else "CONDIZIONI DI UTILIZZO, PRIVACY E DISCLAIMER LEGALE"
    fun nonReplacementTitle(lang: AppLanguage) = if (isEnglish(lang)) "Not a Replacement for Professional Entities" else "Non Sostituzione a Enti Professionali"
    fun nonReplacementSubtitle(lang: AppLanguage) = if (isEnglish(lang)) "This application is an independent informational and technical guidance tool. It does NOT in any way replace:" else "Il presente applicativo è un supporto informativo e divulgativo indipendente. Esso NON si sostituisce in alcun modo a:"

    fun bulletCaf(lang: AppLanguage) = if (isEnglish(lang)) "Tax Assistance Centers (CAF)" else "Centri di Assistenza Fiscale (CAF)"
    fun bulletPatronato(lang: AppLanguage) = if (isEnglish(lang)) "Legally recognized Labor Patronati and Social Security Institutes" else "Istituti di Patronato riconosciuti dalla legge"
    fun bulletConsultants(lang: AppLanguage) = if (isEnglish(lang)) "Certified Labor Consultants or qualified legal professionals" else "Consulenti del Lavoro o professionisti abilitati"
    fun bulletInps(lang: AppLanguage) = if (isEnglish(lang)) "Official INPS public service desks or Contact Center channels" else "Sportelli ufficiali dell'INPS o canali Contact Center"

    // Nuovi punti Policy riformulati (Gratuità, Nessuna Iscrizione, Sostenibilità Pubblicitaria, Privacy Locale, Inoltro INPS)
    fun freeServiceTitle(lang: AppLanguage) = if (isEnglish(lang)) "100% Free & No Registration Required" else "Totalmente Gratuito & Nessuna Iscrizione Richiesta"
    fun freeServiceDesc(lang: AppLanguage) = if (isEnglish(lang)) "Access to all features, simulation calculators, and step-by-step guides is completely free for every citizen. The service does not require any account creation, login, or personal registration." else "L'accesso a tutte le funzionalità, ai simulatori di calcolo e alle guide operative è completamente gratuito per ogni cittadino. Il servizio non richiede alcuna iscrizione, creazione di account, né rilascio di credenziali o email."

    fun adSustainabilityTitle(lang: AppLanguage) = if (isEnglish(lang)) "Sustainability through Advertising" else "Sostenibilità del Progetto tramite Pubblicità"
    fun adSustainabilityDesc(lang: AppLanguage) = if (isEnglish(lang)) "This independent platform is supported and maintained through non-intrusive standard advertising banners and short sponsored videos (Google Ads/AdMob), ensuring that all statutory databases and guides remain freely available without any fee." else "Questo progetto indipendente è sostenuto e mantenuto attivo grazie alla presenza di banner pubblicitari non invasivi e brevi sponsorizzazioni video (Google Ads/AdMob). Tale modello permette di garantire l'accesso aperto e l'aggiornamento costante delle banche dati normative senza alcun costo per gli utenti."

    fun simulationDisclaimer(lang: AppLanguage) = if (isEnglish(lang)) "Simulations are for indicative purposes only based on standard current statutory parameters (D.Lgs. 22/2015 and INPS revaluations updated to 2026). Actual benefit amounts, entitlement, and final approval remain at the sole discretion of INPS based on the insured person's verified official contribution statement." else "Le simulazioni hanno valore puramente indicativo basato sui parametri standard vigenti (D.Lgs. 22/2015 e rivalutazioni INPS aggiornate al 2026). L'importo effettivo, il diritto e l'accoglimento definitivo restano ad esclusivo giudizio dell'INPS in base all'estratto conto contributivo reale dell'assicurato."
    
    fun onlineFilingTitle(lang: AppLanguage) = if (isEnglish(lang)) "Exclusive Electronic Filing on Official INPS Portal" else "Inoltro Ufficiale Telematico Esclusivo su INPS.it"
    fun onlineFilingDesc(lang: AppLanguage) = if (isEnglish(lang)) "This application does not submit applications to INPS. Filing must be completed directly and personally by the user on the official portal www.inps.it using SPID, CIE, or CNS credentials within the mandatory 68-day statutory deadline." else "Il presente applicativo non inoltra pratiche all'INPS. La trasmissione della domanda di disoccupazione deve essere effettuata personalmente dall'utente sul portale ufficiale www.inps.it accedendo con SPID, CIE o CNS entro il termine perentorio di 68 giorni."

    fun privacyTitle(lang: AppLanguage) = if (isEnglish(lang)) "Privacy & Local Device Processing" else "Tutela della Privacy & Elaborazione Locale"
    fun privacyDesc(lang: AppLanguage) = if (isEnglish(lang)) "No personal data, tax code, salary details, or contractual information entered into the application is recorded, stored on cloud servers, or profiled with third parties. All processing takes place strictly inside your device's local memory for maximum privacy." else "Nessun dato personale, codice fiscale, stipendio o informazione contrattuale inserita nell'applicazione viene registrato, conservato su cloud server o profilato con terzi. Tutte le elaborazioni avvengono all'interno della memoria del tuo dispositivo per garantire la totale riservatezza."

    fun deadlineReminder(lang: AppLanguage) = if (isEnglish(lang)) "Remember the mandatory deadline:" else "Ricorda il termine perentorio:"
    fun deadlineTitle(lang: AppLanguage) = if (isEnglish(lang)) "MANDATORY DEADLINE: 68 DAYS" else "TERMINE PERENTORIO: 68 GIORNI"
    fun deadlineDesc(lang: AppLanguage) = if (isEnglish(lang)) "The application must be transmitted to INPS within 68 days from the employment termination date under penalty of forfeiture." else "La domanda deve essere trasmessa all'INPS entro 68 giorni dalla data di cessazione a pena di decadenza."
    fun decorrenzaTitle(lang: AppLanguage) = if (isEnglish(lang)) "Effective Date of Payment (Art. 6 D.Lgs. 22/2015)" else "Decorrenza del pagamento (Art. 6 D.Lgs. 22/2015)"
    fun decorrenzaDesc(lang: AppLanguage) = if (isEnglish(lang)) "NASpI is payable starting from the eighth day following employment termination, or from the day following application submission if filed after the eighth day." else "La NASpI spetta a partire dall'ottavo giorno successivo alla cessazione del rapporto di lavoro, oppure dal giorno successivo alla presentazione se la domanda viene inoltrata dopo l'ottavo giorno."
    fun enterApp(lang: AppLanguage) = if (isEnglish(lang)) "ACCESS NASPI ASSISTANT →" else "ACCEDI ALL'ASSISTENTE NASPI →"

    // Main Screen
    fun mainHeaderTitle(lang: AppLanguage) = if (isEnglish(lang)) "Application Assistance Services" else "Servizi di Assistenza alla Domanda"
    fun mainHeaderSubtitle(lang: AppLanguage) = if (isEnglish(lang)) "INPS Regulatory Database: D.Lgs. 22/2015 & Implementing Circulars" else "Banca Dati Normativa INPS: D.Lgs. 22/2015 e Circolari Applicative"
    fun selectOperation(lang: AppLanguage) = if (isEnglish(lang)) "SELECT DESIRED OPERATION" else "SCEGLI L'OPERAZIONE DA EFFETTUARE"
    fun operationalArea(lang: AppLanguage) = if (isEnglish(lang)) "Operational Area" else "Area Operativa"
    fun card1Title(lang: AppLanguage) = if (isEnglish(lang)) "Simulate Eligibility" else "Simulare i Requisiti"
    fun card1Desc(lang: AppLanguage) = if (isEnglish(lang)) "Check involuntary unemployment requirements and at least 13 contribution weeks in the 4-year period." else "Verifica la sussistenza della disoccupazione involontaria e il possesso di almeno 13 settimane contributive nel quadriennio."
    fun card2Title(lang: AppLanguage) = if (isEnglish(lang)) "Simulate Duration & Amounts" else "Simulare Durata e Importi"
    fun card2Desc(lang: AppLanguage) = if (isEnglish(lang)) "Calculate indemnity weeks (weeks / 2), 75% monthly allowance, and monthly reduction table (-3%)." else "Calcola le settimane di indennità (settimane / 2), l'assegno mensile al 75% e la tabella temporale di décalage (-3%)."
    fun card3Title(lang: AppLanguage) = if (isEnglish(lang)) "Step-by-Step Filing Guide" else "Guida Passo Passo alla Compilazione"
    fun card3Desc(lang: AppLanguage) = if (isEnglish(lang)) "Screen-by-screen instructions to submit your application online on www.inps.it with SPID or CIE." else "Istruzioni schermata per schermata per trasmettere la domanda online sul portale www.inps.it con SPID o CIE."
    fun card4Title(lang: AppLanguage) = if (isEnglish(lang)) "Required Documents" else "Documenti Necessari"
    fun card4Desc(lang: AppLanguage) = if (isEnglish(lang)) "Interactive checklist with ID documents, payslips, contract termination letters, and certified IBAN." else "Checklist interattiva con documenti d'identità, buste paga, lettere contrattuali e codice IBAN certificato."
    fun badgeParams2026(lang: AppLanguage) = if (isEnglish(lang)) "INPS 2026 Parameters" else "Parametri INPS 2026"
    fun badge8Phases(lang: AppLanguage) = if (isEnglish(lang)) "8 Guided Phases" else "8 Fasi Guidate"
    fun badgeChecklist(lang: AppLanguage) = if (isEnglish(lang)) "Checklist" else "Checklist"
    fun faqSectionTitle(lang: AppLanguage) = if (isEnglish(lang)) "FREQUENTLY ASKED QUESTIONS & INPS CIRCULARS" else "DOMANDE FREQUENTI & CIRCOLARI INPS"
    fun inpsSourcesLabel(lang: AppLanguage) = if (isEnglish(lang)) "INPS Sources" else "Fonti INPS"

    // Requirements Screen
    fun reqHeaderTitle(lang: AppLanguage) = if (isEnglish(lang)) "Check If You Qualify for NASpI" else "Verifica se hai diritto alla NASpI"
    fun reqHeaderSource(lang: AppLanguage) = if (isEnglish(lang)) "Source: Leg. Decree 22/2015 • INPS Circular 94/2015" else "Fonte: D.Lgs. 4 marzo 2015 n. 22 • Circolare INPS n. 94/2015"
    fun reqSatisfied(lang: AppLanguage) = if (isEnglish(lang)) "ELIGIBILITY REQUIREMENTS MET" else "REQUISITI SODDISFATTI"
    fun reqNotSatisfied(lang: AppLanguage) = if (isEnglish(lang)) "WARNING: REQUIREMENTS NOT MET" else "ATTENZIONE: REQUISITI NON SODDISFATTI"
    fun reqEligibleExplanation(lang: AppLanguage) = if (isEnglish(lang)) "Based on entered parameters (involuntary status, at least 13 weeks, and DID availability), statutory requirements under Art. 3 of Leg. Decree 22/2015 are satisfied." else "In base ai parametri inseriti (stato involontario, almeno 13 settimane e disponibilità DID), sussistono i presupposti normativi previsti dall'art. 3 del D.Lgs. 22/2015."
    fun reqNotEligiblePrefix(lang: AppLanguage) = if (isEnglish(lang)) "Not eligible because: " else "Non idoneo perché: "
    fun reqReasonNotValid(lang: AppLanguage) = if (isEnglish(lang)) "the termination reason is not considered involuntary unemployment" else "la motivazione di cessazione non è considerata disoccupazione involontaria"
    fun reqWeeksNotValid(lang: AppLanguage) = if (isEnglish(lang)) "you have fewer than 13 contribution weeks in the 4-year period (at least 13 required)" else "hai meno di 13 settimane di contribuzione nel quadriennio (ne servono almeno 13)"
    fun reqDidNotValid(lang: AppLanguage) = if (isEnglish(lang)) "the immediate availability declaration (DID) is required" else "è necessaria la dichiarazione di immediata disponibilità (DID)"
    fun reqPensionNotValid(lang: AppLanguage) = if (isEnglish(lang)) "recipients of direct retirement pensions cannot claim NASpI" else "i titolari di pensione diretta non possono accedere alla NASpI"

    fun reqSection1Title(lang: AppLanguage) = if (isEnglish(lang)) "1. REASON FOR EMPLOYMENT TERMINATION" else "1. MOTIVO DELLA CESSAZIONE DEL RAPPORTO"
    fun reqSection1Subtitle(lang: AppLanguage) = if (isEnglish(lang)) "NASpI is granted exclusively in cases of involuntary unemployment." else "La NASpI spetta esclusivamente in caso di disoccupazione involontaria."
    fun tagEligible(lang: AppLanguage) = if (isEnglish(lang)) "Eligible" else "Ammesso"
    fun tagExcluded(lang: AppLanguage) = if (isEnglish(lang)) "Excluded" else "Escluso"
    fun refLabel(lang: AppLanguage) = if (isEnglish(lang)) "Ref:" else "Rif:"

    fun reqSection2Title(lang: AppLanguage) = if (isEnglish(lang)) "2. CONTRIBUTION WEEKS (LAST 4 YEARS)" else "2. SETTIMANE DI CONTRIBUZIONE (ULTIMI 4 ANNI)"
    fun reqSection2Subtitle(lang: AppLanguage) = if (isEnglish(lang)) "Minimum INPS requirement: at least 13 paid weeks in the 4 years preceding unemployment." else "Requisito minimo INPS: almeno 13 settimane versate nei 4 anni precedenti l'evento di disoccupazione."
    fun weeksLabelCount(weeks: Int, months: Double, lang: AppLanguage) = if (isEnglish(lang)) "$weeks weeks (~${String.format("%.1f", months)} months)" else "$weeks settimane (~${String.format("%.1f", months)} mesi)"
    fun tagReqMet(lang: AppLanguage) = if (isEnglish(lang)) "Requirement Met (≥ 13)" else "Requisito Soddisfatto (≥ 13)"
    fun tagReqInsufficient(lang: AppLanguage) = if (isEnglish(lang)) "Insufficient (< 13)" else "Insufficiente (< 13)"

    fun reqSection3Title(lang: AppLanguage) = if (isEnglish(lang)) "3. IMMEDIATE AVAILABILITY DECLARATION (DID)" else "3. DICHIARAZIONE IMMEDIATA DISPONIBILITÀ (DID)"
    fun reqSection3Subtitle(lang: AppLanguage) = if (isEnglish(lang)) "Art. 19 Leg. Decree 150/2015: formal commitment to seek new employment and attend Job Center training programs." else "Art. 19 D.Lgs. 150/2015: disponibilità formale a cercare nuova occupazione e frequentare percorsi formativi del Centro per l'Impiego."
    fun didSwitchLabel(lang: AppLanguage) = if (isEnglish(lang)) "Willing to issue DID and sign Service Agreement with Job Center (CPI)" else "Disponibile a rilasciare la DID e sottoscrivere il Patto di Servizio con il Centro per l'Impiego"
    fun tagMandatory(lang: AppLanguage) = if (isEnglish(lang)) "Mandatory" else "Obbligatorio"

    fun reqSection4Title(lang: AppLanguage) = if (isEnglish(lang)) "4. PENSION INCOMPATIBILITY" else "4. INCOMPATIBILITÀ PENSIONISTICA"
    fun reqSection4Subtitle(lang: AppLanguage) = if (isEnglish(lang)) "NASpI is incompatible with direct retirement pension benefits (Old Age, Early Retirement, Quota 103, etc.)." else "La NASpI non è compatibile con trattamenti pensionistici diretti (Vecchiaia, Anticipata, Quota 103, etc.)."
    fun pensionSwitchLabel(lang: AppLanguage) = if (isEnglish(lang)) "I do not receive any direct retirement pension from INPS" else "Non percepisco alcuna pensione diretta a carico dell'INPS"
    fun proceedToAmountBtn(lang: AppLanguage) = if (isEnglish(lang)) "Proceed to Amount Calculation →" else "Procedi al Calcolo dell'Importo →"

    // Amounts Screen
    fun amountsHeaderTitle(lang: AppLanguage) = if (isEnglish(lang)) "Official Calculation by INPS Formula" else "Calcolo Ufficiale Secondo Formula INPS"
    fun amountsHeaderCitation(lang: AppLanguage) = if (isEnglish(lang)) "Reference threshold: € 1,456.72 • Maximum Cap: € 1,584.70 (INPS Parameters 2026)" else "Soglia di riferimento: € 1.456,72 • Tetto Massimo: € 1.584,70 (Parametri INPS 2026)"
    fun amountsDisclaimer(lang: AppLanguage) = if (isEnglish(lang))
        "The calculation of the new indemnity will be based exclusively on the contributions and salaries of your last employment relationship (or in any case on working periods subsequent to a previous application); otherwise, it is calculated on the average monthly gross salary up to four years back."
    else
        "Il calcolo della nuova indennità si baserà esclusivamente sui contributi e sugli stipendi del tuo ultimo rapporto di lavoro (o comunque sui periodi lavorativi successivi a una precedente domanda); altrimenti si va a calcolare sulla media dello stipendio mensile lordo fino a quattro anni indietro."
    fun initialGrossMonthlyTitle(lang: AppLanguage) = if (isEnglish(lang)) "INITIAL GROSS MONTHLY AMOUNT" else "IMPORTO INIZIALE MENSILE (LORDO)"
    fun amountInitialMonthlyGross(lang: AppLanguage) = initialGrossMonthlyTitle(lang)
    fun entitledDurationTitle(lang: AppLanguage) = if (isEnglish(lang)) "ENTITLED DURATION" else "DURATA SPETTANTE"
    fun amountDurationEntitled(lang: AppLanguage) = entitledDurationTitle(lang)
    fun entitledDurationText(weeks: Int, months: Double, lang: AppLanguage) = if (isEnglish(lang)) "$weeks weeks (~${String.format("%.1f", months)} months)" else "$weeks sett. (~${String.format("%.1f", months)} mesi)"
    fun durationFormatted(weeks: Int, months: Double, lang: AppLanguage) = entitledDurationText(weeks, months, lang)
    fun decalageTitle(lang: AppLanguage) = if (isEnglish(lang)) "3% REDUCTION" else "DÉCALAGE -3%"
    fun decalageFromMonth(month: Int, lang: AppLanguage) = if (isEnglish(lang)) "From month $month" else "Dal $month° mese"
    fun decalageStartFormatted(month: Int, lang: AppLanguage) = decalageFromMonth(month, lang)
    fun totalEstimatedLabel(lang: AppLanguage) = if (isEnglish(lang)) "Estimated Gross Total for Full Cycle:" else "Totale Lordo Stimato Ciclo Completo:"
    fun amountTotalEstimatedGross(lang: AppLanguage) = totalEstimatedLabel(lang)
    fun dataForEstimateTitle(lang: AppLanguage) = if (isEnglish(lang)) "Estimation Parameters" else "Dati per la Stima"
    fun enterSalaryData(lang: AppLanguage) = if (isEnglish(lang)) "ENTER SALARY PARAMETERS" else "INSERISCI I DATI RETRIBUTIVI"
    fun averageMonthlySalaryLabel(lang: AppLanguage) = if (isEnglish(lang)) "Average Gross Monthly Salary (€):" else "Retribuzione Mensile Media Lorda (€):"
    fun averageMonthlySalaryHelper(lang: AppLanguage) = if (isEnglish(lang)) "Taxable social security average over the last 4 years divided by weeks worked and multiplied by 4.33 (INPS Circular 94/2015)." else "Media imponibile previdenziale degli ultimi 4 anni diviso le settimane lavorate moltiplicato per 4,33 (Circolare INPS 94/2015)."
    fun contributionWeeksWorkedLabel(lang: AppLanguage) = if (isEnglish(lang)) "Contribution Weeks (last 4 years):" else "Settimane di Contribuzione (ultimi 4 anni):"
    fun workedWeeksLabel(lang: AppLanguage) = contributionWeeksWorkedLabel(lang)
    fun over55SwitchLabel(lang: AppLanguage) = if (isEnglish(lang)) "Worker aged 55 or older at employment termination" else "Lavoratore di età pari o superiore a 55 anni alla cessazione"
    fun over55Helper(lang: AppLanguage) = if (isEnglish(lang)) "Under Budget Law 234/2021, for workers aged 55 or older the 3% reduction begins from the 8th month (instead of the 6th)." else "In base alla Legge di Bilancio n. 234/2021, per gli over 55 il décalage del 3% si applica a partire dall'8° mese (invece che dal 6°)."
    fun scheduleTableTitle(lang: AppLanguage) = if (isEnglish(lang)) "Monthly Payment Schedule & Progressive Reduction" else "Piano Mensile di Erogazione e Décalage Progressivo"
    fun colMonth(lang: AppLanguage) = if (isEnglish(lang)) "MONTH" else "MESE"
    fun colAmount(lang: AppLanguage) = if (isEnglish(lang)) "MONTHLY AMOUNT" else "IMPORTO MENSILE"
    fun colStatus(lang: AppLanguage) = if (isEnglish(lang)) "STATUS" else "STATUS"
    fun rowMonth(m: Int, lang: AppLanguage) = if (isEnglish(lang)) "Month $m" else "Mese $m"
    fun fullAmountTag(lang: AppLanguage) = if (isEnglish(lang)) "Full Amount" else "Importo Pieno"
    fun decalageTag(lang: AppLanguage) = if (isEnglish(lang)) "-3% Reduction" else "-3% Décalage"
    fun decorrenzaPaymentTitle(lang: AppLanguage) = if (isEnglish(lang)) "Effective Payment Date:" else "Decorrenza del pagamento:"
    fun decorrenzaPaymentText(lang: AppLanguage) = if (isEnglish(lang)) "NASpI is payable starting from the eighth day following employment termination, or from the day following application submission if filed after the eighth day." else "La NASpI spetta a partire dall'ottavo giorno successivo alla cessazione del rapporto di lavoro, oppure dal giorno successivo alla presentazione se la domanda viene inoltrata dopo l'ottavo giorno."

    // Guide Screen
    fun guideHeaderTitle(lang: AppLanguage) = if (isEnglish(lang)) "Claim Submission Guide on www.inps.it" else "Compilazione Domanda su www.inps.it"
    fun guideHeaderCitation(lang: AppLanguage) = if (isEnglish(lang)) "Source: INPS Portal User Manual • Leg. Decree 150/2015" else "Fonte: Manuale utente Portale INPS • D.Lgs. 150/2015"
    fun guideProgressTitle(lang: AppLanguage) = if (isEnglish(lang)) "Application Progress" else "Avanzamento Compilazione"
    fun guideProgressCount(completed: Int, total: Int, lang: AppLanguage) = if (isEnglish(lang)) "$completed of $total steps completed" else "$completed di $total passaggi completati"
    fun guideDeadlineTitle(lang: AppLanguage) = if (isEnglish(lang)) "68-day deadline from employment termination" else "Termine di 68 giorni dalla cessazione"
    fun guideDeadlineDesc(lang: AppLanguage) = if (isEnglish(lang)) "Failure to submit within 68 days results in permanent forfeiture of entitlement (Art. 6 Leg. Decree 22/2015)." else "La mancata presentazione entro 68 giorni comporta la decadenza definitiva dal diritto alla prestazione (art. 6 D.Lgs. 22/2015)."
    fun phaseChip(n: Int, lang: AppLanguage) = if (isEnglish(lang)) "Phase $n" else "Fase $n"
    fun portalPathLabel(lang: AppLanguage) = if (isEnglish(lang)) "Official Portal Path:" else "Percorso Ufficiale Portale:"
    fun operationalTipsLabel(lang: AppLanguage) = if (isEnglish(lang)) "Key Operational Instructions:" else "Punti Chiave ed Istruzioni Operative:"
    fun officialNoticeLabel(lang: AppLanguage) = if (isEnglish(lang)) "Official INPS Regulatory Notice:" else "Nota Normativa Ufficiale INPS:"
    fun markCompletedBtn(lang: AppLanguage) = if (isEnglish(lang)) "Mark as Completed" else "Segna come completato"
    fun completedCheckedBtn(lang: AppLanguage) = if (isEnglish(lang)) "Completed ✓" else "Completato ✓"
    fun previousBtn(lang: AppLanguage) = if (isEnglish(lang)) "← Previous" else "← Precedente"
    fun nextBtn(lang: AppLanguage) = if (isEnglish(lang)) "Next →" else "Successivo →"
    fun cpiWarningTitle(lang: AppLanguage) = if (isEnglish(lang)) "⚠ Penalties:" else "⚠ Sanzioni:"
    fun cpiWarningDesc(lang: AppLanguage) = if (isEnglish(lang)) "⚠ Penalties: If you do not complete the online procedure or fail to attend the Job Center (CPI) without justified grounds, you risk suspension or loss of NASpI." else "⚠ Sanzioni: Se non completi la procedura online o non ti presenti al CPI senza giustificato motivo rischi la sospensione o la perdita della NASpI."

    // Documents Screen
    fun docsHeaderTitle(lang: AppLanguage) = if (isEnglish(lang)) "Checklist Before You Begin" else "Checklist Prima di Iniziare"
    fun docsHeaderCitation(lang: AppLanguage) = if (isEnglish(lang)) "Preliminary checks for self-submission or through CAF / Patronato" else "Verifiche preliminari per trasmissione autonoma o presso CAF / Patronato"
    fun docsStatusTitle(lang: AppLanguage) = if (isEnglish(lang)) "Document Status" else "Stato Documenti"
    fun docsReadyCount(checked: Int, total: Int, lang: AppLanguage) = if (isEnglish(lang)) "$checked of $total documents ready" else "$checked di $total documenti pronti"
    fun mandatoryReadyTag(lang: AppLanguage) = if (isEnglish(lang)) "Mandatory Ready" else "Obbligatori OK"
    fun incompleteTag(lang: AppLanguage) = if (isEnglish(lang)) "Incomplete" else "Incompleti"
    fun filterAll(count: Int, lang: AppLanguage) = if (isEnglish(lang)) "All ($count)" else "Tutti ($count)"
    fun filterMandatory(lang: AppLanguage) = if (isEnglish(lang)) "Mandatory Only" else "Solo Obbligatori"
    fun filterMissing(lang: AppLanguage) = if (isEnglish(lang)) "To Obtain" else "Da Reperire"
    fun docMandatoryBadge(lang: AppLanguage) = if (isEnglish(lang)) "Mandatory" else "Obbligatorio"
    fun docOptionalBadge(lang: AppLanguage) = if (isEnglish(lang)) "Optional / If Applicable" else "Facoltativo / Se Applicabile"
    fun docInpsNoteLabel(lang: AppLanguage) = if (isEnglish(lang)) "INPS Reference & Verification:" else "Riferimento e Controllo INPS:"
    fun selectAllBtn(lang: AppLanguage) = if (isEnglish(lang)) "Select All" else "Seleziona Tutti"
    fun clearAllBtn(lang: AppLanguage) = if (isEnglish(lang)) "Clear All" else "Azzera Spunte"

    // Ads & Rewarded Video
    fun adSponsoredBadge(lang: AppLanguage) = if (isEnglish(lang)) "SPONSORED" else "SPONSORIZZATO"
    fun adNotice(lang: AppLanguage) = if (isEnglish(lang)) "Advertisement" else "Annuncio Pubblicitario"
    fun adBannerPlaceholderText(lang: AppLanguage) = if (isEnglish(lang)) "Ad Space • Standard Banner (Google AdMob / Ads)" else "Spazio Pubblicitario • Banner Standard (Google AdMob / Ads)"
    fun videoAdDialogTitle(lang: AppLanguage) = if (isEnglish(lang)) "Sponsored Video" else "Video Sponsorizzato"
    fun videoAdDialogSubtitle(lang: AppLanguage) = if (isEnglish(lang)) "Watch the short video to unlock the Official INPS PDF Guide (23 Pages)" else "Guarda il breve video per sbloccare la Guida PDF Ufficiale INPS (23 Pagine)"
    fun videoAdUnlockIn(seconds: Int, lang: AppLanguage) = if (isEnglish(lang)) "Unlocking resource in: ${seconds}s" else "Sblocco risorsa tra: ${seconds}s"
    fun videoAdSkip(lang: AppLanguage) = if (isEnglish(lang)) "Skip / Close" else "Salta / Chiudi"
    fun videoAdUnlocked(lang: AppLanguage) = if (isEnglish(lang)) "Resource Unlocked!" else "Risorsa Sbloccata con Successo!"
    fun videoAdDownloadNow(lang: AppLanguage) = if (isEnglish(lang)) "Download and Open PDF →" else "Scarica e Apri PDF →"

    // Policy & Privacy Window and Footer Link
    fun policyAndPrivacy(lang: AppLanguage) = if (isEnglish(lang)) "Policy e Privacy" else "Policy e Privacy"
    fun closePolicy(lang: AppLanguage) = if (isEnglish(lang)) "Close" else "Chiudi"
    fun openPolicy(lang: AppLanguage) = if (isEnglish(lang)) "Open Terms of Use & Privacy" else "Condizioni di Utilizzo, Privacy & Disclaimer (Apri Finestra)"
    fun closeDialogBtn(lang: AppLanguage) = if (isEnglish(lang)) "I Have Read & Understood • Close Window" else "Ho Letto e Compreso • Chiudi Finestra"
}
