package com.example.model

object InpsRepository {

    val applicationSteps: List<ApplicationStep> = listOf(
        ApplicationStep(
            stepNumber = 1,
            title = "Accesso all'Identità Digitale",
            officialCategory = "Autenticazione Portale www.inps.it",
            description = "Accedi al portale istituzionale www.inps.it utilizzando una delle identità digitali autorizzate dallo Stato Italiano.",
            keyTips = listOf(
                "SPID (Livello 2): PosteID, Aruba, Namirial, InfoCert, Sielte, etc.",
                "CIE (Carta di Identità Elettronica 3.0) con PIN e app CieID",
                "CNS (Carta Nazionale dei Servizi / Tessera Sanitaria abilitata con lettore smart card)"
            ),
            inpsPortalPath = "Home > Entra in MyINPS > Autenticazione con SPID/CIE/CNS",
            officialNotice = "Non sono più ammessi i vecchi PIN INPS per i cittadini privati (Circolare INPS n. 87/2020)."
        ),
        ApplicationStep(
            stepNumber = 2,
            title = "Ricerca del Servizio Ufficiale",
            officialCategory = "Navigazione e Apertura Pratica",
            description = "Dalla barra di ricerca in alto su www.inps.it, digita 'NASpI: indennità mensile di disoccupazione' e seleziona il servizio telematico.",
            keyTips = listOf(
                "Clicca sul pulsante 'Utilizza il servizio'",
                "Seleziona il profilo 'Cittadino'",
                "Nella schermata successiva, clicca su 'NASpI' nel menu laterale sinistro e poi su 'Nuova domanda'"
            ),
            inpsPortalPath = "Servizi e Prestazioni > NASpI: indennità mensile di disoccupazione > Nuova Domanda",
            officialNotice = "Verifica di essere nel servizio ordinario NASpI e non in Dis-Coll (riservata ai collaboratori coordinati e continuativi)."
        ),
        ApplicationStep(
            stepNumber = 3,
            title = "Verifica Anagrafica e Recapiti",
            officialCategory = "Dati del Richiedente",
            description = "Controlla l'esattezza dei tuoi dati anagrafici, residenza e domicilio. Inserisci i tuoi recapiti aggiornati per ricevere notifiche di accoglimento.",
            keyTips = listOf(
                "Indica un indirizzo email o PEC personale e attivo",
                "Inserisci il tuo numero di cellulare per ricevere gli SMS di notifica INPS relativi allo stato d'avanzamento della pratica",
                "Se l'indirizzo di residenza non è aggiornato, aggiornalo prima nella sezione Anagrafica MyINPS"
            ),
            inpsPortalPath = "Scheda 1: Dati Anagrafici e Recapiti",
            officialNotice = "I recapiti forniti saranno utilizzati per inviare il provvedimento di accoglimento o le richieste di documentazione integrativa (Circolare INPS 94/2015)."
        ),
        ApplicationStep(
            stepNumber = 4,
            title = "Dati Ultimo Lavoro e Motivo Cessazione",
            officialCategory = "Rapporto di Lavoro e Causa Fine",
            description = "Inserisci la data dell'ultimo giorno lavorato, la matricola aziendale del datore (se nota) e la motivazione esatta della cessazione del rapporto.",
            keyTips = listOf(
                "Data di cessazione: coincide con l'ultimo giorno di vigenza contrattuale",
                "Seleziona il motivo conforme ai documenti: Licenziamento, Scadenza termine o Dimissioni giusta causa",
                "Se si tratta di licenziamento economico, indica se hai percepito indennità sostitutiva del preavviso"
            ),
            inpsPortalPath = "Scheda 2: Ultimo Rapporto di Lavoro e Motivo Cessazione",
            officialNotice = "La data di decorrenza della prestazione parte dall'8° giorno successivo alla fine del preavviso o della cessazione se presentata entro 8 giorni (D.Lgs. 22/2015, art. 6)."
        ),
        ApplicationStep(
            stepNumber = 5,
            title = "Coordinate di Pagamento (IBAN)",
            officialCategory = "Modalità di Accredito",
            description = "Specifica la modalità di riscossione dell'indennità mensile. La modalità preferenziale è l'accredito su conto corrente bancario o postale con IBAN.",
            keyTips = listOf(
                "L'IBAN deve essere obbligatoriamente intestato o cointestato al richiedente della disoccupazione",
                "Non sono ammessi conti o carte intestate a coniugi, genitori o terzi soggetti",
                "Per le carte prepagate dotate di IBAN (es. Postepay Evolution), accertarsi che il circuito consenta bonifici SEPA continuativi"
            ),
            inpsPortalPath = "Scheda 3: Dati di Pagamento",
            officialNotice = "Dal 2020 il modello cartaceo SR163 è stato dematerializzato grazie alla verifica telematica automatizzata con Poste e sistema bancario (Circolare INPS 48/2020)."
        ),
        ApplicationStep(
            stepNumber = 6,
            title = "Dichiarazione DID e Lavoro Autonomo",
            officialCategory = "Dichiarazioni di Legge Obbligatorie",
            description = "Rilascia la Dichiarazione di Immediata Disponibilità al lavoro (DID) integrata nella domanda e dichiara se svolgi attività autonoma o societaria.",
            keyTips = listOf(
                "Spunta l'adesione alla DID: confermi la tua disponibilità a nuove attività lavorative e formative",
                "Se svolgi attività autonoma/partita IVA con reddito annuo presunto inferiore a 5.500€ (o 8.500€ parasubordinato), indicalo per la NASpI ridotta",
                "Se non svolgi alcuna altra attività, seleziona 'Nessuna attività in corso'"
            ),
            inpsPortalPath = "Scheda 4: Dichiarazioni e DID",
            officialNotice = "La presentazione della domanda di NASpI equivale per legge alla Dichiarazione di Immediata Disponibilità (D.Lgs. 150/2015, art. 19 c. 1)."
        ),
        ApplicationStep(
            stepNumber = 7,
            title = "Controllo Riepilogo e Invio Telematico",
            officialCategory = "Invio e Protocollazione",
            description = "Rileggi accuratamente il prospetto riassuntivo. Conferma l'invio e scarica immediatamente la ricevuta in formato PDF.",
            keyTips = listOf(
                "Verifica la correttezza del codice IBAN e della data di fine rapporto",
                "Clicca su 'Invia Domanda'",
                "Scarica e archivia sul tuo dispositivo la Ricevuta di Presentazione contenente il NUMERO DI PROTOCOLLO ufficiale INPS"
            ),
            inpsPortalPath = "Scheda 5: Riepilogo e Ricevuta Ufficiale",
            officialNotice = "Conserva il numero di protocollo: ti servirà per monitorare lo stato di avanzamento e per comunicarlo al Centro per l'Impiego."
        ),
        ApplicationStep(
            stepNumber = 8,
            title = "Piattaforma SIISL e Centro Impiego",
            officialCategory = "Adempimento Post-Accoglimento",
            description = "Adempimenti obbligatori per mantenere il diritto alla disoccupazione NASpI: accesso a SIISL, sottoscrizione PAD e Patto con il CPI.",
            keyTips = listOf(
                "L'iscrizione alla piattaforma SIISL è obbligatoria: l'INPS ti iscrive automaticamente solo all'accoglimento della domanda e ti notifica via SMS/email (non va fatto all'inoltro)",
                "Entro 15 giorni dalla data di accoglimento/decorrenza: accedi a SIISL con SPID o CIE, sottoscrivi il PAD (Patto di Attivazione Digitale) e compila/aggiorna il CV",
                "Successivamente verrai convocato dal tuo Centro per l'Impiego (CPI) per firmare il Patto di Servizio Personalizzato"
            ),
            inpsPortalPath = "Piattaforma SIISL (siisl.inps.it) & CPI Territoriale",
            officialNotice = "Sanzioni: Se non completi la procedura online o non ti presenti al CPI senza giustificato motivo rischi la sospensione o la perdita della NASpI."
        )
    )

    val requiredDocuments: List<DocumentItem> = listOf(
        DocumentItem(
            id = "doc_identity",
            title = "Documento d'Identità in corso di validità",
            mandatory = true,
            description = "Carta d'identità (cartacea o elettronica) oppure Passaporto italiano/UE in corso di validità.",
            inpsNote = "Indispensabile per il riconoscimento anagrafico e la presentazione allo sportello CPI."
        ),
        DocumentItem(
            id = "doc_fiscal",
            title = "Codice Fiscale o Tessera Sanitaria",
            mandatory = true,
            description = "Tessera sanitaria magnetica o certificato di attribuzione del codice fiscale rilasciato dall'Agenzia delle Entrate.",
            inpsNote = "Identificativo fiscale univoco su cui sono registrate le posizioni contributive negli archivi INPS."
        ),
        DocumentItem(
            id = "doc_spid",
            title = "Credenziali SPID (Livello 2) o CIE con PIN",
            mandatory = true,
            description = "Identità digitale attiva sul proprio smartphone per autenticarsi in autonomia sul portale www.inps.it.",
            inpsNote = "Obbligatorio per qualsiasi accesso telematico ai servizi dell'Istituto (Circolare INPS n. 87/2020)."
        ),
        DocumentItem(
            id = "doc_iban",
            title = "Coordinate Bancarie / Postali con codice IBAN",
            mandatory = true,
            description = "Codice IBAN di un conto corrente bancario, postale o carta conto con IBAN, tassativamente intestato o cointestato al richiedente.",
            inpsNote = "L'INPS esegue controlli automatici di titolarità sui database bancari interbancari prima di emettere i mandati di pagamento."
        ),
        DocumentItem(
            id = "doc_buste_paga",
            title = "Ultime Buste Paga (o Estratto Conto Contributivo)",
            mandatory = true,
            description = "Copia delle ultime 3 buste paga rilasciate dal datore di lavoro e, se possibile, estratto conto contributivo estratto dal cassetto previdenziale MyINPS.",
            inpsNote = "Utile per verificare la regolarità dei flussi Uniemens trasmessi dal datore e verificare le 13 settimane minime."
        ),
        DocumentItem(
            id = "doc_cessazione",
            title = "Lettera di Licenziamento o Contratto di Lavoro",
            mandatory = true,
            description = "Lettera di licenziamento notificata dal datore, copia del contratto a termine recante la data di scadenza, o verbale di conciliazione ITL.",
            inpsNote = "Comprova la cessazione involontaria del rapporto di lavoro secondo l'art. 3 del D.Lgs. 22/2015."
        ),
        DocumentItem(
            id = "doc_giusta_causa",
            title = "Atti comprovanti Dimissioni per Giusta Causa (se applicabile)",
            mandatory = false,
            description = "Diffida ad adempiere, copie di sollecito mancati stipendi, ricorso al tribunale o dichiarazione sostitutiva dell'atto di notorietà.",
            inpsNote = "Richiesto dalla Circolare INPS n. 94/2015 par. 2.2 per attestare la non volontarietà delle dimissioni."
        ),
        DocumentItem(
            id = "doc_permesso",
            title = "Permesso di Soggiorno (per cittadini Extra-UE)",
            mandatory = false,
            description = "Permesso di soggiorno per motivi di lavoro o CE per soggiornanti di lungo periodo, in corso di validità o con ricevuta di richiesta rinnovo.",
            inpsNote = "I lavoratori extracomunitari regolarmente assunti hanno pieno diritto alla NASpI a parità di requisiti contributivi."
        )
    )

    val faqList: List<FaqItem> = listOf(
        FaqItem(
            id = "faq_recess",
            question = "Ho dato le dimissioni volontarie: ho diritto alla NASpI?",
            answer = "No, per legge le dimissioni volontarie ordinarie non danno diritto alla prestazione perché lo stato di disoccupazione deve essere 'involontario'. Eccezioni ammesse: dimissioni per giusta causa (es. stipendi non pagati per 2 mesi, mobbing, molestie) oppure dimissioni intervenute durante il periodo di maternità/paternità.",
            legalCitation = "Art. 3 D.Lgs. 22/2015 • Circolare INPS n. 94/2015 par. 2.2",
            tag = "Requisiti"
        ),
        FaqItem(
            id = "faq_decalage",
            question = "Cos'è e come funziona la riduzione (décalage) del 3%?",
            answer = "La legge prevede che a partire dal 6° mese (91° giorno dalla decorrenza) l'importo della NASpI si riduca progressivamente del 3% ogni mese. Per i lavoratori con almeno 55 anni compiuti alla data di cessazione, il taglio scatta invece solo a partire dall'8° mese.",
            legalCitation = "Legge n. 234/2021 (Legge di Bilancio) • Art. 4 D.Lgs. 22/2015",
            tag = "Importi"
        ),
        FaqItem(
            id = "faq_anticipata",
            question = "Posso richiedere tutti i soldi della NASpI in un'unica soluzione?",
            answer = "Sì, richiedendo la 'NASpI Anticipata' per l'avvio di un'attività di lavoro autonomo, impresa individuale o sottoscrizione di capitale in una cooperativa. La domanda deve essere presentata telematicamente all'INPS entro 30 giorni dall'inizio dell'attività.",
            legalCitation = "Art. 8 D.Lgs. 22/2015 • Circolare INPS n. 174/2017",
            tag = "Liquidazione"
        ),
        FaqItem(
            id = "faq_nuovo_lavoro",
            question = "Cosa succede se trovo un nuovo lavoro durante la NASpI?",
            answer = "Se il nuovo contratto subordinato dura fino a 6 mesi e produce un reddito annuo inferiore a 8.500 € (o lavoro autonomo fino a 5.500 €), l'indennità viene ridotta dell'80% del reddito previsto previa comunicazione con modello NASpI-Com entro 30 giorni. Se dura oltre 6 mesi o supera il limite, la prestazione decade.",
            legalCitation = "Artt. 9 e 10 D.Lgs. 22/2015 • Circolare INPS n. 94/2015",
            tag = "Compatibilità"
        ),
        FaqItem(
            id = "faq_termini",
            question = "Cosa succede se presento la domanda dopo 68 giorni?",
            answer = "Il termine di 68 giorni è perentorio: se la domanda viene inviata anche con un solo giorno di ritardo (69° giorno), l'INPS rigetta la pratica per intervenuta decadenza del diritto senza possibilità di deroga.",
            legalCitation = "Art. 6 c. 1 D.Lgs. 22/2015",
            tag = "Scadenze"
        ),
        FaqItem(
            id = "faq_siisl",
            question = "Quali sono gli adempimenti obbligatori su piattaforma SIISL e PAD?",
            answer = "L'iscrizione alla piattaforma SIISL è obbligatoria per mantenere il diritto alla disoccupazione NASpI. Al momento dell'accoglimento della domanda, l'INPS ti iscrive automaticamente e ti invia una notifica (SMS o e-mail). Entro 15 giorni dall'accoglimento devi accedere a SIISL tramite SPID o CIE per: 1) Sottoscrivere il PAD (Patto di Attivazione Digitale); 2) Inserire o aggiornare il tuo Curriculum Vitae (CV). Successivamente verrai contattato dal tuo CPI per firmare il Patto di Servizio Personalizzato. Se non completi la procedura online o non ti presenti al CPI senza giustificato motivo rischi la sospensione o la perdita della NASpI.",
            legalCitation = "Decreto Lavoro (D.L. 48/2023) • Circolari INPS",
            tag = "SIISL & CPI"
        ),
        FaqItem(
            id = "faq_naspi_com",
            question = "Comunicazioni NASpI (NASpI-Com): quando inviarlo e regole sul reddito?",
            answer = "NOTA INFORMATIVA: Il modello NASpI-Com serve a comunicare all'INPS qualsiasi variazione durante la disoccupazione. Va inviato obbligatoriamente entro 30 giorni dall'evento.\n\nREGOLA ASSOLUTA: Il modello NASpI-Com va sempre e comunque inviato in caso di qualsiasi ripresa di attività lavorativa (anche per un solo giorno, anche se il reddito è minimo o pari a zero, e anche se l'azienda effettua già le comunicazioni obbligatorie).\n\nCasi di invio obbligatorio (Entro 30 giorni):\n• Nuovo lavoro dipendente o autonomo: Per comunicare l'inizio dell'attività e il reddito annuo presunto.\n• Variazione del reddito: Se cambia il reddito presunto già dichiarato in precedenza.\n• Eventi previdenziali o personali: Inizio di malattia, maternità, infortunio, espatrio o rinuncia alla NASpI.",
            legalCitation = "Artt. 9 e 10 D.Lgs. 22/2015 • Circolare INPS n. 94/2015",
            tag = "NASpI-Com"
        )
    )

    fun getApplicationSteps(lang: AppLanguage): List<ApplicationStep> {
        if (lang != AppLanguage.EN) return applicationSteps
        return listOf(
            ApplicationStep(
                stepNumber = 1,
                title = "Digital Identity Login",
                officialCategory = "Portal Authentication www.inps.it",
                description = "Log in to the official www.inps.it portal using an authorized Italian digital identity.",
                keyTips = listOf(
                    "SPID (Level 2): PosteID, Aruba, Namirial, InfoCert, Sielte, etc.",
                    "CIE (Electronic ID Card 3.0) with PIN and CieID app",
                    "CNS (National Services Card / Health Card with smart card reader)"
                ),
                inpsPortalPath = "Home > Enter MyINPS > SPID/CIE/CNS Authentication",
                officialNotice = "Legacy INPS PIN codes are no longer accepted for private citizens (INPS Circular 87/2020)."
            ),
            ApplicationStep(
                stepNumber = 2,
                title = "Official Service Search",
                officialCategory = "Navigation & Claim Opening",
                description = "From the top search bar on www.inps.it, type 'NASpI: monthly unemployment benefit' and select the online service.",
                keyTips = listOf(
                    "Click the 'Use Service' button",
                    "Select the 'Citizen' profile",
                    "In the left menu, click 'NASpI' and then 'New Application'"
                ),
                inpsPortalPath = "Services > NASpI: monthly unemployment benefit > New Application",
                officialNotice = "Ensure you choose standard NASpI and not Dis-Coll (which is reserved for quasi-subordinate collaborators)."
            ),
            ApplicationStep(
                stepNumber = 3,
                title = "Personal Data & Contact Verification",
                officialCategory = "Applicant Information",
                description = "Verify the accuracy of your personal data, residence, and address. Enter current contact details to receive approval notices.",
                keyTips = listOf(
                    "Provide an active personal email or certified email (PEC)",
                    "Enter your mobile phone number to receive official INPS SMS status updates",
                    "If residence is outdated, update it first in MyINPS Registry"
                ),
                inpsPortalPath = "Tab 1: Personal Data & Contacts",
                officialNotice = "Provided contacts will be used to send approval notices or requests for additional documents (INPS Circular 94/2015)."
            ),
            ApplicationStep(
                stepNumber = 4,
                title = "Last Employment & Reason for Termination",
                officialCategory = "Employment Record & Termination Cause",
                description = "Enter the last day worked, employer company registration code (if known), and exact reason for termination.",
                keyTips = listOf(
                    "Termination date coincides with the last day of contract validity",
                    "Select reason matching documents: Dismissal, Expiry or Just Cause Resignation",
                    "If redundancy dismissal, indicate whether pay in lieu of notice was received"
                ),
                inpsPortalPath = "Tab 2: Last Employment & Termination Reason",
                officialNotice = "Payment effective date begins from the 8th day following notice or termination if filed within 8 days (Leg. Decree 22/2015, Art. 6)."
            ),
            ApplicationStep(
                stepNumber = 5,
                title = "Payment Details (IBAN)",
                officialCategory = "Disbursement Method",
                description = "Specify how you wish to receive the monthly benefit. The preferred method is bank/postal wire transfer via IBAN.",
                keyTips = listOf(
                    "IBAN must strictly be in the name of or joint with the claimant",
                    "Accounts or cards under spouses, parents or third parties are not permitted",
                    "For prepaid cards with IBAN (e.g. Postepay Evolution), ensure incoming recurring SEPA transfers are enabled"
                ),
                inpsPortalPath = "Tab 3: Payment Details",
                officialNotice = "Since 2020, the paper SR163 form has been eliminated thanks to direct automated bank verification (INPS Circular 48/2020)."
            ),
            ApplicationStep(
                stepNumber = 6,
                title = "DID Declaration & Self-Employment",
                officialCategory = "Mandatory Statutory Declarations",
                description = "Issue the Immediate Availability Declaration (DID) integrated in the form and state whether you engage in freelance or business activity.",
                keyTips = listOf(
                    "Check the DID consent box: confirm availability for employment and training",
                    "If performing freelance activity with expected annual income below €5,500 (€8,500 coordinated), declare it for reduced NASpI",
                    "If not performing any other activity, select 'No ongoing activity'"
                ),
                inpsPortalPath = "Tab 4: Declarations & DID",
                officialNotice = "Submitting a NASpI application legally equals issuing the Immediate Availability Declaration (Leg. Decree 150/2015, Art. 19 c. 1)."
            ),
            ApplicationStep(
                stepNumber = 7,
                title = "Summary Review & Electronic Submission",
                officialCategory = "Submission & Protocol Registration",
                description = "Carefully review the summary statement. Confirm submission and immediately download the PDF receipt.",
                keyTips = listOf(
                    "Double-check accuracy of IBAN code and termination date",
                    "Click 'Submit Application'",
                    "Download and save the Submission Receipt containing your official INPS PROTOCOL NUMBER"
                ),
                inpsPortalPath = "Tab 5: Summary & Official Receipt",
                officialNotice = "Keep your protocol number safe: you will need it to track status and communicate with the Job Center."
            ),
            ApplicationStep(
                stepNumber = 8,
                title = "SIISL Platform & Job Center (CPI)",
                officialCategory = "Post-Approval Obligations",
                description = "Mandatory compliance to maintain NASpI entitlement: access SIISL, sign the Digital Activation Pact (PAD) and Job Center agreement.",
                keyTips = listOf(
                    "SIISL registration is mandatory: INPS registers you automatically upon claim approval and notifies you by SMS/email (not at filing)",
                    "Within 15 days of approval: log in to SIISL with SPID/CIE, sign PAD and update your CV",
                    "Next, you will be summoned by your local Job Center (CPI) to sign the Personalized Service Agreement"
                ),
                inpsPortalPath = "SIISL Platform (siisl.inps.it) & Local Job Center (CPI)",
                officialNotice = "Penalties: If you fail to complete the online procedure or miss Job Center appointments without valid grounds, you risk benefit suspension or termination."
            )
        )
    }

    fun getRequiredDocuments(lang: AppLanguage): List<DocumentItem> {
        if (lang != AppLanguage.EN) return requiredDocuments
        return listOf(
            DocumentItem(
                id = "doc_identity",
                title = "Valid Identity Document",
                mandatory = true,
                description = "Identity Card (paper or electronic) or valid Italian/EU passport.",
                inpsNote = "Essential for identity verification and presentation at the Job Center (CPI)."
            ),
            DocumentItem(
                id = "doc_fiscal",
                title = "Tax Code / Health Card (Codice Fiscale)",
                mandatory = true,
                description = "Magnetic health card or official tax code attribution certificate from the Revenue Agency.",
                inpsNote = "Unique fiscal identifier used to record social security records in INPS archives."
            ),
            DocumentItem(
                id = "doc_spid",
                title = "SPID Credentials (Level 2) or CIE with PIN",
                mandatory = true,
                description = "Active digital identity on your smartphone to independently authenticate on www.inps.it.",
                inpsNote = "Mandatory for all digital access to Institute services (INPS Circular 87/2020)."
            ),
            DocumentItem(
                id = "doc_iban",
                title = "Bank / Postal Details with IBAN Code",
                mandatory = true,
                description = "IBAN code of bank, postal account or card account, strictly in claimant's name or joint account.",
                inpsNote = "INPS performs automated account ownership verification with interbank systems before issuing payments."
            ),
            DocumentItem(
                id = "doc_buste_paga",
                title = "Recent Payslips (or Social Security Statement)",
                mandatory = true,
                description = "Copies of the last 3 employer payslips and, if possible, social security statement from MyINPS portal.",
                inpsNote = "Useful to verify employer Uniemens reporting regularity and verify minimum 13 contribution weeks."
            ),
            DocumentItem(
                id = "doc_cessazione",
                title = "Dismissal Letter or Employment Contract",
                mandatory = true,
                description = "Employer dismissal letter, fixed-term contract showing end date, or Labor Inspectorate (ITL) conciliation agreement.",
                inpsNote = "Proves involuntary termination of employment under Art. 3 of Leg. Decree 22/2015."
            ),
            DocumentItem(
                id = "doc_giusta_causa",
                title = "Evidence of Just Cause Resignation (if applicable)",
                mandatory = false,
                description = "Formal warning to pay, letters regarding unpaid wages, labor court filing, or formal affidavit.",
                inpsNote = "Required by INPS Circular 94/2015 sec. 2.2 to verify involuntary nature of resignation."
            ),
            DocumentItem(
                id = "doc_permesso",
                title = "Residence Permit (for non-EU citizens)",
                mandatory = false,
                description = "Work residence permit or EU long-term residence permit, valid or with renewal application receipt.",
                inpsNote = "Legally employed non-EU workers are fully entitled to NASpI with equal contribution conditions."
            )
        )
    }

    fun getFaqList(lang: AppLanguage): List<FaqItem> {
        if (lang != AppLanguage.EN) return faqList
        return listOf(
            FaqItem(
                id = "faq_recess",
                question = "I resigned voluntarily: am I entitled to NASpI?",
                answer = "No. Ordinary voluntary resignation does not qualify because unemployment must be involuntary. Allowed exceptions: resignation for just cause (e.g., 2+ months unpaid wages, harassment, mobbing) or resignation during the statutory maternity/paternity protection period.",
                legalCitation = "Art. 3 Leg. Decree 22/2015 • INPS Circular 94/2015 sec. 2.2",
                tag = "Requirements"
            ),
            FaqItem(
                id = "faq_decalage",
                question = "What is the 3% monthly reduction (décalage) and how does it work?",
                answer = "By law, starting from the 6th month (91st day), the monthly NASpI amount is progressively reduced by 3% each month. For workers aged 55 or older on the termination date, the reduction only starts from the 8th month.",
                legalCitation = "Law 234/2021 (Budget Law) • Art. 4 Leg. Decree 22/2015",
                tag = "Amounts"
            ),
            FaqItem(
                id = "faq_anticipata",
                question = "Can I receive all NASpI payments in a single lump sum?",
                answer = "Yes, by applying for 'Advance NASpI' to start a freelance business, sole proprietorship, or purchase capital shares in a cooperative. The application must be filed online with INPS within 30 days of business commencement.",
                legalCitation = "Art. 8 Leg. Decree 22/2015 • INPS Circular 174/2017",
                tag = "Settlement"
            ),
            FaqItem(
                id = "faq_nuovo_lavoro",
                question = "What happens if I find a new job while receiving NASpI?",
                answer = "If a new contract lasts up to 6 months and produces annual income under €8,500 (€5,500 for self-employment), the benefit is reduced by 80% of expected income upon notifying INPS via NASpI-Com within 30 days. If it exceeds 6 months or income limits, the benefit terminates.",
                legalCitation = "Arts. 9 & 10 Leg. Decree 22/2015 • INPS Circular 94/2015",
                tag = "Compatibility"
            ),
            FaqItem(
                id = "faq_termini",
                question = "What happens if I submit the application after 68 days?",
                answer = "The 68-day deadline is strictly mandatory: if submitted even one day late (day 69), INPS will reject the claim due to statutory forfeiture of rights with no exceptions.",
                legalCitation = "Art. 6 c. 1 Leg. Decree 22/2015",
                tag = "Deadlines"
            ),
            FaqItem(
                id = "faq_siisl",
                question = "What are the mandatory obligations on SIISL platform and PAD?",
                answer = "SIISL platform registration is mandatory to retain NASpI benefits. Upon claim approval, INPS registers you automatically and notifies you via SMS/email. Within 15 days of approval, log in to SIISL via SPID/CIE to: 1) Sign the PAD (Digital Activation Pact); 2) Update your CV. Next, your local Job Center (CPI) will contact you to sign the Service Agreement. Failure to comply can cause suspension or cancellation of NASpI.",
                legalCitation = "Labor Decree (D.L. 48/2023) • INPS Circulars",
                tag = "SIISL & Job Center"
            ),
            FaqItem(
                id = "faq_naspi_com",
                question = "NASpI Communications (NASpI-Com): when to file and income rules?",
                answer = "INFORMATIONAL NOTICE: The NASpI-Com form is used to notify INPS of any changes during unemployment. It must be filed within 30 days of the event.\n\nABSOLUTE RULE: NASpI-Com must always be filed upon any resumption of work (even for a single day, zero income, or if the company already reports it).\n\nMandatory filing cases (Within 30 days):\n• New employment or self-employment: To report start and estimated annual income.\n• Income changes: If previously declared expected income changes.\n• Personal/social events: Sickness, maternity, accident, moving abroad, or waiver of NASpI.",
                legalCitation = "Arts. 9 & 10 Leg. Decree 22/2015 • INPS Circular 94/2015",
                tag = "NASpI-Com"
            )
        )
    }
}

