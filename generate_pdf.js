const fs = require('fs');
const path = require('path');
const { PDFDocument, rgb } = require('pdf-lib');
const fontkit = require('fontkit');

async function buildPdf() {
  const pdfDoc = await PDFDocument.create();
  pdfDoc.registerFontkit(fontkit);

  // Load TTF fonts
  const fontDir = '/root/.android/cli/bundles/24ee0e5988395db1221e2f4e4b5993ebe4749348/jre/lib/fonts';
  const regularBytes = fs.readFileSync(path.join(fontDir, 'DroidSans.ttf'));
  const boldBytes = fs.readFileSync(path.join(fontDir, 'DroidSans-Bold.ttf'));

  const fontRegular = await pdfDoc.embedFont(regularBytes);
  const fontBold = await pdfDoc.embedFont(boldBytes);

  // Palette from reference image (Screenshot 2026-09-20 alle 16.12.53.png)
  const cSageBg = rgb(0.78, 0.82, 0.77);        // #C7D1C4 TypeSafe Sage canvas
  const cSageCard = rgb(0.86, 0.89, 0.85);      // #DBE3D9 Muted light card
  const cSageSurface = rgb(0.93, 0.95, 0.92);   // #EDF2EC Clean surface
  const cCharcoal = rgb(0.12, 0.14, 0.13);      // #1E2421 TypeSafe dark charcoal
  const cCharcoalSec = rgb(0.24, 0.28, 0.25);   // #3D4740 Secondary dark text
  const cCharcoalMuted = rgb(0.42, 0.48, 0.43); // #6B7B6E Tertiary metadata
  const cBorder = rgb(0.58, 0.64, 0.58);        // #94A394 Line divider
  const cBorderDark = rgb(0.12, 0.14, 0.13);    // Sharp container borders
  const cWhite = rgb(1, 1, 1);
  const cAccentGreen = rgb(0.16, 0.48, 0.26);   // Verified / Success
  const cAccentYellow = rgb(0.76, 0.50, 0.10);  // Notice / Attention
  const cAccentRed = rgb(0.72, 0.18, 0.18);     // Mandatory / Alert

  const W = 960;
  const H = 540;
  const TOTAL_PAGES = 23;

  function addSlide() {
    const page = pdfDoc.addPage([W, H]);
    page.drawRectangle({
      x: 0,
      y: 0,
      width: W,
      height: H,
      color: cSageBg,
    });
    return page;
  }

  function drawFooter(page, pageNum) {
    const y = 22;
    // Divider line
    page.drawLine({
      start: { x: 40, y: y + 14 },
      end: { x: W - 40, y: y + 14 },
      thickness: 0.8,
      color: cBorder,
    });

    // Left: Normative context
    page.drawText('Banca Dati Normativa INPS • D.Lgs. 22/2015 • Circolari 94/2015 & 142/2015', {
      x: 40,
      y: y,
      size: 8.5,
      font: fontRegular,
      color: cCharcoalMuted,
    });

    // Center: Created by Croz Design® (Mandatory: regular/light, on every page)
    const centerText = 'Created by Croz Design®';
    const centerWidth = fontRegular.widthOfTextAtSize(centerText, 10);
    page.drawText(centerText, {
      x: (W - centerWidth) / 2,
      y: y,
      size: 10,
      font: fontRegular,
      color: cCharcoalSec,
    });

    // Right: Page counter
    const pageText = `Pagina ${pageNum} di ${TOTAL_PAGES}`;
    const pageWidth = fontRegular.widthOfTextAtSize(pageText, 8.5);
    page.drawText(pageText, {
      x: W - 40 - pageWidth,
      y: y,
      size: 8.5,
      font: fontRegular,
      color: cCharcoalMuted,
    });
  }

  function drawTopBar(page, title, phaseTag = null) {
    page.drawRectangle({
      x: 40,
      y: H - 52,
      width: W - 80,
      height: 34,
      color: cCharcoal,
      borderRadius: 4,
    });

    page.drawText(title, {
      x: 55,
      y: H - 41,
      size: 11,
      font: fontBold,
      color: cWhite,
    });

    if (phaseTag) {
      const tagW = fontBold.widthOfTextAtSize(phaseTag, 9) + 18;
      page.drawRectangle({
        x: W - 54 - tagW,
        y: H - 47,
        width: tagW,
        height: 24,
        color: cSageCard,
        borderRadius: 3,
      });
      page.drawText(phaseTag, {
        x: W - 54 - tagW + 9,
        y: H - 39,
        size: 9,
        font: fontBold,
        color: cCharcoal,
      });
    }
  }

  function wrapText(text, maxWidth, size, font) {
    const paragraphs = text.split('\n');
    const lines = [];
    for (const para of paragraphs) {
      if (para.trim() === '') {
        lines.push('');
        continue;
      }
      const words = para.split(' ');
      let currentLine = '';
      for (let i = 0; i < words.length; i++) {
        const word = words[i];
        if (!word) continue;
        const testLine = currentLine ? currentLine + ' ' + word : word;
        const testWidth = font.widthOfTextAtSize(testLine, size);
        if (testWidth > maxWidth && currentLine) {
          lines.push(currentLine);
          currentLine = word;
        } else {
          currentLine = testLine;
        }
      }
      if (currentLine) lines.push(currentLine);
    }
    return lines;
  }

  function drawTextBlock(page, text, x, y, maxWidth, size, font, color, lineSpacing = 1.35) {
    const lines = wrapText(text, maxWidth, size, font);
    const lineHeight = size * lineSpacing;
    let currentY = y;
    for (const line of lines) {
      if (line === '') {
        currentY -= lineHeight * 0.65;
        continue;
      }
      page.drawText(line, {
        x,
        y: currentY,
        size,
        font,
        color,
      });
      currentY -= lineHeight;
    }
    return currentY;
  }

  // =========================================================================
  // PAGE 1: Copertina Ufficiale Guida NASpI (TypeSafe Editorial Style)
  // =========================================================================
  {
    const p = addSlide();

    // Large Header (introducing style)
    p.drawText('INPS', {
      x: 50,
      y: H - 105,
      size: 40,
      font: fontBold,
      color: cCharcoal,
    });

    p.drawText('Guida all\'uso dei Servizi NASpI', {
      x: 50,
      y: H - 160,
      size: 27,
      font: fontBold,
      color: cCharcoal,
    });

    p.drawText('Compilazione e Invio Domanda Online 2026', {
      x: 50,
      y: H - 192,
      size: 17,
      font: fontRegular,
      color: cCharcoalSec,
    });

    // Vertical line beside metadata (Signature TypeSafe layout element)
    p.drawLine({
      start: { x: 50, y: H - 220 },
      end: { x: 50, y: H - 330 },
      thickness: 2.5,
      color: cCharcoal,
    });

    p.drawText('Guida Operativa Telematica per Postazioni Self Service', {
      x: 64,
      y: H - 238,
      size: 12,
      font: fontBold,
      color: cCharcoal,
    });

    drawTextBlock(
      p,
      'Istruzioni passo-passo per la presentazione della domanda di disoccupazione ai sensi del D.Lgs. 4 marzo 2015, n. 22 e successive circolari INPS.\n\nVerifica preliminare dei dati anagrafici, ultimi rapporti contrattuali, coordinate IBAN e invio telematico con protocollo.',
      64,
      H - 258,
      360,
      10,
      fontRegular,
      cCharcoalSec,
      1.4
    );

    // Pill badge for year update
    p.drawRectangle({
      x: 50,
      y: H - 370,
      width: 175,
      height: 26,
      color: cCharcoal,
      borderRadius: 4,
    });
    p.drawText('EDIZIONE AGGIORNATA 2026', {
      x: 62,
      y: H - 359,
      size: 9,
      font: fontBold,
      color: cWhite,
    });

    // Disclaimer Box on Page 1
    p.drawRectangle({
      x: 50,
      y: 45,
      width: 375,
      height: 68,
      color: cSageCard,
      borderColor: cBorder,
      borderWidth: 0.8,
      borderRadius: 4,
    });
    p.drawText('AVVERTENZA E DISCLAIMER:', {
      x: 60,
      y: 98,
      size: 8,
      font: fontBold,
      color: cCharcoal,
    });
    drawTextBlock(
      p,
      'La presente procedura non sostituisce la consulenza di un professionista (CAF/Patronato), ma fornisce una linea guida semplice e chiara per il cittadino volta a semplificare le fasi di compilazione e inoltro della domanda NASpI.',
      60,
      86,
      355,
      7.5,
      fontRegular,
      cCharcoalSec,
      1.35
    );

    // Right Column: Services Directory (Cards)
    const cardX = 460;
    const cardW = 450;
    const services = [
      { t: 'Nuova Assicurazione Sociale per l\'Impiego (NASpI)', s: 'Invio telematico domanda ordinaria' },
      { t: 'Nuova Assicurazione Sociale per l\'Impiego (NASpI)', s: 'Consultazione domande e avanzamento istruttoria' },
      { t: 'NASpI Anticipata', s: 'Invio domanda per incentivo all\'autoimprenditorialità' },
      { t: 'NASpI Anticipata', s: 'Consultazione esito domande anticipate' },
      { t: 'NASpI-Com', s: 'Comunicazione variazioni reddito o nuovo impiego' },
      { t: 'NASpI-Com', s: 'Consultazione comunicazioni integrative inviate' }
    ];

    p.drawText('CATALOGO DEI SERVIZI TELEMATICI NASpI', {
      x: cardX,
      y: H - 85,
      size: 11,
      font: fontBold,
      color: cCharcoal,
    });

    let sY = H - 105;
    services.forEach((s, idx) => {
      p.drawRectangle({
        x: cardX,
        y: sY - 44,
        width: cardW,
        height: 44,
        color: cSageCard,
        borderColor: cBorder,
        borderWidth: 0.8,
        borderRadius: 4,
      });

      p.drawRectangle({
        x: cardX + 12,
        y: sY - 34,
        width: 24,
        height: 24,
        color: cCharcoal,
        borderRadius: 12,
      });
      p.drawText(`${idx + 1}`, {
        x: cardX + (idx < 9 ? 21 : 17),
        y: sY - 26,
        size: 10,
        font: fontBold,
        color: cWhite,
      });

      p.drawText(s.t, {
        x: cardX + 46,
        y: sY - 21,
        size: 10,
        font: fontBold,
        color: cCharcoal,
      });

      p.drawText(s.s, {
        x: cardX + 46,
        y: sY - 34,
        size: 8.5,
        font: fontRegular,
        color: cCharcoalMuted,
      });

      sY -= 54;
    });

    drawFooter(p, 1);
  }

  // =========================================================================
  // PAGE 2: Panoramica e Modalità di Accesso
  // =========================================================================
  {
    const p = addSlide();
    drawTopBar(p, 'GUIDA AI SERVIZI NASpI • MODALITÀ DI ACCESSO E FUNZIONALITÀ', 'PANORAMICA');

    const cols = [
      {
        title: 'AUTENTICAZIONE FORTE',
        badge: 'SPID / CIE / CNS',
        text: 'L\'accesso ai servizi telematici NASpI sul portale istituzionale www.inps.it richiede l\'identità digitale certificata conforme AgID:\n\n• SPID di livello 2 (Poste, Aruba, InfoCert, ecc.)\n• Carta d\'Identità Elettronica (CIE 3.0 con lettore o app CieID)\n• Carta Nazionale dei Servizi (CNS)\n\nNelle postazioni fisiche Self Service assistite presso le Sedi INPS è possibile utilizzare anche il PIN dispositivo.'
      },
      {
        title: 'COMPILAZIONE GUIDATA',
        badge: 'FLUSSO IN 6 FASI',
        text: 'La procedura online verifica in tempo reale le banche dati anagrafiche e contributive:\n\n• Controllo indirizzo di residenza e domicilio\n• Verifica dati dell\'ultimo rapporto di lavoro cessato\n• Estratto contributivo degli ultimi 4 anni\n• Modalità di pagamento (Accredito IBAN / Bonifico domiciliato)\n• Dichiarazione Immediata Disponibilità (DID) al CPI.'
      },
      {
        title: 'RICEVUTA E PROTOCOLLO',
        badge: 'NOTIFICHE REAL-TIME',
        text: 'Al termine dell\'inoltro telematico:\n\n• Viene rilasciata la ricevuta ufficiale con numero di protocollo univoco\n• Il fascicolo integrale in PDF può essere scaricato e salvato sul proprio dispositivo\n• Lo stato di avanzamento della pratica è monitorabile nella sezione MyINPS\n• Aggiornamenti automatici tramite SMS e avvisi nella Cassetta Postale online.'
      }
    ];

    cols.forEach((col, idx) => {
      const x = 50 + idx * 295;
      p.drawRectangle({
        x,
        y: 85,
        width: 270,
        height: 380,
        color: cSageCard,
        borderColor: cCharcoal,
        borderWidth: 1,
        borderRadius: 6,
      });

      p.drawRectangle({
        x: x + 18,
        y: 425,
        width: 234,
        height: 24,
        color: cCharcoal,
        borderRadius: 3,
      });
      p.drawText(col.badge, {
        x: x + 26,
        y: 432,
        size: 9.5,
        font: fontBold,
        color: cWhite,
      });

      p.drawText(col.title, {
        x: x + 18,
        y: 398,
        size: 12.5,
        font: fontBold,
        color: cCharcoal,
      });

      p.drawLine({
        start: { x: x + 18, y: 385 },
        end: { x: x + 100, y: 385 },
        thickness: 2,
        color: cCharcoal,
      });

      drawTextBlock(p, col.text, x + 18, 360, 234, 10, fontRegular, cCharcoalSec, 1.45);
    });

    drawFooter(p, 2);
  }

  // =========================================================================
  // PAGE 3: Sezione 1 - Invio Domanda NASpI
  // =========================================================================
  {
    const p = addSlide();

    p.drawRectangle({
      x: 60,
      y: 80,
      width: W - 120,
      height: 385,
      color: cCharcoal,
      borderRadius: 8,
    });

    p.drawRectangle({
      x: 95,
      y: 400,
      width: 155,
      height: 26,
      color: cSageBg,
      borderRadius: 4,
    });
    p.drawText('SEZIONE OPERATIVA 01', {
      x: 107,
      y: 409,
      size: 9.5,
      font: fontBold,
      color: cCharcoal,
    });

    p.drawText('Nuova Assicurazione Sociale per l\'Impiego', {
      x: 95,
      y: 345,
      size: 28,
      font: fontBold,
      color: cWhite,
    });

    p.drawText('(NASpI): Invio Domanda Online', {
      x: 95,
      y: 305,
      size: 23,
      font: fontRegular,
      color: cSageBg,
    });

    p.drawLine({
      start: { x: 95, y: 270 },
      end: { x: 95, y: 140 },
      thickness: 2.5,
      color: cSageBg,
    });

    drawTextBlock(
      p,
      'Procedura telematica di compilazione e inoltro della domanda di indennità mensile di disoccupazione sul portale web dell\'Istituto Nazionale della Previdenza Sociale.\n\nTermine perentorio di legge: la domanda deve essere inviata entro e non oltre 68 giorni dalla data di cessazione del rapporto di lavoro a pena di decadenza (art. 6 D.Lgs. 22/2015).',
      110,
      255,
      670,
      12.5,
      fontRegular,
      cWhite,
      1.5
    );

    drawFooter(p, 3);
  }

  // =========================================================================
  // PAGE 4: Requisiti e Descrizione del Servizio
  // =========================================================================
  {
    const p = addSlide();
    drawTopBar(p, 'NUOVA ASSICURAZIONE SOCIALE PER L\'IMPIEGO (NASpI): INVIO DOMANDA', 'REQUISITI E DESCRIZIONE');

    // Left Column
    p.drawRectangle({
      x: 40,
      y: 65,
      width: 420,
      height: 395,
      color: cSageCard,
      borderColor: cCharcoal,
      borderWidth: 1,
      borderRadius: 6,
    });

    p.drawRectangle({
      x: 55,
      y: 422,
      width: 250,
      height: 24,
      color: cCharcoal,
      borderRadius: 3,
    });
    p.drawText('QUANDO UTILIZZARE IL SERVIZIO', {
      x: 65,
      y: 430,
      size: 9.5,
      font: fontBold,
      color: cWhite,
    });

    const textQuando = 
      'I lavoratori con rapporto di lavoro subordinato che hanno perduto involontariamente l\'occupazione possono utilizzare questo servizio per inviare la domanda di NASpI.\n\n' +
      'Categorie aventi diritto:\n' +
      '• Lavoratori dipendenti del settore privato (determinato e indeterminato)\n' +
      '• Apprendisti di tutti i livelli professionali\n' +
      '• Soci lavoratori di cooperative con contratto subordinato\n' +
      '• Personale artistico con contratto subordinato\n' +
      '• Dipendenti a tempo determinato delle pubbliche amministrazioni\n\n' +
      'Per ulteriori informazioni sulla Prestazione consulta nel sito la scheda "NASpI: indennità mensile di disoccupazione (per lavoratori subordinati con rapporto di lavoro cessato dal 1° maggio 2015)".';

    drawTextBlock(p, textQuando, 55, 395, 390, 10, fontRegular, cCharcoal, 1.45);

    // Right Column
    const rx = 480;
    const rw = 440;

    // Box Descrizione
    p.drawRectangle({
      x: rx,
      y: 245,
      width: rw,
      height: 215,
      color: cSageCard,
      borderColor: cCharcoal,
      borderWidth: 1,
      borderRadius: 6,
    });

    p.drawRectangle({
      x: rx + 15,
      y: 422,
      width: 140,
      height: 24,
      color: cCharcoal,
      borderRadius: 3,
    });
    p.drawText('DESCRIZIONE', {
      x: rx + 25,
      y: 430,
      size: 9.5,
      font: fontBold,
      color: cWhite,
    });

    const textDesc = 
      'Il servizio consente al beneficiario di NASpI di compilare e inviare la domanda con le informazioni e la documentazione relative all\'ultimo rapporto di lavoro e alle modalità di pagamento degli importi spettanti.\n\n' +
      'Al termine della procedura viene fornita una ricevuta con il numero di protocollo della domanda, che l\'utente potrà utilizzare per seguirne l\'iter tramite il servizio di Consultazione domande, per inviare la domanda di NASpI anticipata e per eventuali comunicazioni inerenti variazioni della propria condizione.';

    drawTextBlock(p, textDesc, rx + 15, 395, rw - 30, 9.8, fontRegular, cCharcoal, 1.4);

    // Box A Chi è Dedicato
    p.drawRectangle({
      x: rx,
      y: 155,
      width: rw,
      height: 75,
      color: cSageCard,
      borderColor: cBorder,
      borderWidth: 0.8,
      borderRadius: 6,
    });

    p.drawText('A CHI È DEDICATO', {
      x: rx + 15,
      y: 208,
      size: 9.5,
      font: fontBold,
      color: cCharcoal,
    });
    p.drawText('Disoccupati con perdita involontaria del lavoro e contribuzione minima (art. 3 D.Lgs. 22/2015).', {
      x: rx + 15,
      y: 185,
      size: 9.5,
      font: fontRegular,
      color: cCharcoalSec,
    });

    // Box Come Accedere
    p.drawRectangle({
      x: rx,
      y: 65,
      width: rw,
      height: 75,
      color: cSageCard,
      borderColor: cBorder,
      borderWidth: 0.8,
      borderRadius: 6,
    });

    p.drawText('COME ACCEDERE', {
      x: rx + 15,
      y: 118,
      size: 9.5,
      font: fontBold,
      color: cCharcoal,
    });
    p.drawText('Codice Fiscale e credenziali SPID (livello 2), CIE 3.0, CNS o PIN dispositivo.', {
      x: rx + 15,
      y: 95,
      size: 9.5,
      font: fontRegular,
      color: cCharcoalSec,
    });

    drawFooter(p, 4);
  }

  // =========================================================================
  // PAGE 5: Documentazione e Notifiche
  // =========================================================================
  {
    const p = addSlide();
    drawTopBar(p, 'NUOVA ASSICURAZIONE SOCIALE PER L\'IMPIEGO (NASpI): INVIO DOMANDA', 'PREPARAZIONE PRATICA');

    const cardW = 420;

    // Left Column
    p.drawRectangle({
      x: 40,
      y: 250,
      width: cardW,
      height: 210,
      color: cSageCard,
      borderColor: cCharcoal,
      borderWidth: 1,
      borderRadius: 6,
    });

    p.drawRectangle({
      x: 55,
      y: 422,
      width: 160,
      height: 24,
      color: cCharcoal,
      borderRadius: 3,
    });
    p.drawText('DOCUMENTAZIONE', {
      x: 65,
      y: 430,
      size: 9.5,
      font: fontBold,
      color: cWhite,
    });

    const textDoc = 
      'Prima di inserire la domanda online, ti raccomandiamo di procurarti la documentazione relativa al tuo ultimo rapporto di lavoro dalla quale ricavare i dati richiesti.\n\n' +
      'Accredito su conto corrente:\n' +
      'Inoltre, se intendi chiedere l\'accredito degli importi spettanti della NASpI sul tuo conto corrente bancario o postale, ti ricordiamo di verificare la correttezza dell\'IBAN indicato e, ove richiesto dalla specifica procedura, allegare il Modulo SR163 acquisendo il visto della tua banca come attestazione della titolarità del conto.';

    drawTextBlock(p, textDoc, 55, 395, cardW - 30, 9.6, fontRegular, cCharcoal, 1.4);

    // Box 2: Notifiche Esito
    p.drawRectangle({
      x: 40,
      y: 65,
      width: cardW,
      height: 170,
      color: cSageCard,
      borderColor: cBorder,
      borderWidth: 0.8,
      borderRadius: 6,
    });

    p.drawText('SERVIZI DI NOTIFICA SU ESITO E PAGAMENTI', {
      x: 55,
      y: 208,
      size: 9.5,
      font: fontBold,
      color: cCharcoal,
    });

    const textNotif = 
      'Una volta inviata la domanda, puoi monitorarne lo stato con il servizio "NASpI: consultazione domande". Al termine della lavorazione, la Sede INPS invia comunicazione di esito tramite POSTEL e ti avverte con un SMS al cellulare fornito. Il messaggio segnala la presenza di un Avviso nell\'area MyINPS con il link alla Cassetta postale online e l\'accredito della prima rata.';

    drawTextBlock(p, textNotif, 55, 185, cardW - 30, 9.3, fontRegular, cCharcoalSec, 1.4);

    // Right Column: Nota Bene su Residenza
    const rx = 480;
    const rw = 440;

    p.drawRectangle({
      x: rx,
      y: 65,
      width: rw,
      height: 395,
      color: cSageCard,
      borderColor: cCharcoal,
      borderWidth: 1,
      borderRadius: 6,
    });

    p.drawRectangle({
      x: rx + 15,
      y: 422,
      width: 250,
      height: 24,
      color: cCharcoal,
      borderRadius: 3,
    });
    p.drawText('N.B. INDIRIZZO DI RESIDENZA E SEDI INPS', {
      x: rx + 25,
      y: 430,
      size: 9.5,
      font: fontBold,
      color: cWhite,
    });

    const textNb = 
      '1) Le domande di indennità di disoccupazione NASpI sono assegnate per la lavorazione, in via generale, alle Sedi territoriali INPS sulla base della residenza ufficiale del richiedente e non più in base all\'eventuale diverso domicilio indicato.\n\n' +
      'Pertanto, la procedura di presentazione online delle domande ti propone automaticamente, come indirizzo di residenza, l\'indirizzo più affidabile prelevato dall\'archivio anagrafico INPS, censito nel sistema attraverso il collegamento telematico con le anagrafi comunali e l\'anagrafe tributaria.\n\n' +
      'Di conseguenza, se dopo aver effettuato l\'accesso con le tue credenziali riscontri nella sezione dei Dati Anagrafici che l\'indirizzo di residenza proposto non è corretto, segui le istruzioni riportate nella pagina successiva.';

    drawTextBlock(p, textNb, rx + 15, 395, rw - 30, 10, fontRegular, cCharcoal, 1.45);

    drawFooter(p, 5);
  }

  // =========================================================================
  // PAGE 6: Aggiornamento Residenza e Mancato Preavviso
  // =========================================================================
  {
    const p = addSlide();
    drawTopBar(p, 'NUOVA ASSICURAZIONE SOCIALE PER L\'IMPIEGO (NASpI): INVIO DOMANDA', 'INDICAZIONI CRITICHE');

    // Box 1: Modifica Residenza
    p.drawRectangle({
      x: 40,
      y: 65,
      width: 420,
      height: 395,
      color: cSageCard,
      borderColor: cCharcoal,
      borderWidth: 1,
      borderRadius: 6,
    });

    p.drawRectangle({
      x: 55,
      y: 420,
      width: 280,
      height: 24,
      color: cCharcoal,
      borderRadius: 3,
    });
    p.drawText('VARIAZIONE RESIDENZA E COMPETENZA TERRITORIALE', {
      x: 65,
      y: 428,
      size: 9,
      font: fontBold,
      color: cWhite,
    });

    const textP6Left = 
      'Se l\'indirizzo di residenza proposto non è corretto, sei invitato con uno specifico avviso ad aggiornare l\'indirizzo di residenza accedendo al servizio apposito presente nella sezione anagrafica del profilo utente in "MyINPS".\n\n' +
      'La nuova residenza da te inserita viene comunque sottoposta a verifica di congruenza con quanto presente nelle anagrafi comunali e anagrafe tributaria e accettata dalla procedura solo in caso di esito positivo.\n\n' +
      'Tale verifica è asincrona e potrebbe richiedere qualche minuto. Nel caso in cui la variazione non vada a buon fine, la domanda di NASpI è indirizzata alla Struttura territoriale competente sulla base del vecchio indirizzo registrato negli archivi INPS.\n\n' +
      'Per rettifiche manuali occorrerà rivolgersi agli uffici competenti del Comune nel quale risiedi.';

    drawTextBlock(p, textP6Left, 55, 395, 390, 9.8, fontRegular, cCharcoal, 1.42);

    // Box 2: Mancato Preavviso
    const rx = 480;
    const rw = 440;

    p.drawRectangle({
      x: rx,
      y: 65,
      width: rw,
      height: 395,
      color: cSageCard,
      borderColor: cCharcoal,
      borderWidth: 1,
      borderRadius: 6,
    });

    p.drawRectangle({
      x: rx + 15,
      y: 420,
      width: 290,
      height: 24,
      color: cAccentYellow,
      borderRadius: 3,
    });
    p.drawText('INDENNITÀ DI MANCATO PREAVVISO (AVVERTENZA)', {
      x: rx + 25,
      y: 428,
      size: 9,
      font: fontBold,
      color: cWhite,
    });

    const textP6Right = 
      '2) Tieni presente che, se sei stato licenziato senza preavviso e nella domanda ometti il riferimento all\'indennità di mancato preavviso da te percepita, ciò comporterà un ricalcolo dell\'indennità NASpI con un ritardo nei tempi di erogazione.\n\n' +
      'In questo caso potrebbe essere necessario allegare alla domanda anche la lettera di licenziamento dalla quale si possa desumere se hai percepito questa indennità per velocizzare la lavorazione della domanda.\n\n' +
      'Regola di decorrenza:\n' +
      'L\'indennità NASpI spetta solo dal giorno successivo alla fine del periodo coperto dall\'indennità sostitutiva del preavviso contrattuale.';

    drawTextBlock(p, textP6Right, rx + 15, 395, rw - 30, 10, fontRegular, cCharcoal, 1.45);

    drawFooter(p, 6);
  }

  // =========================================================================
  // HELPER FOR SCREEN STEPS (PAGES 7 TO 22)
  // =========================================================================
  function renderStepPage(pageNum, stepNumber, stepTitle, instructionsTitle, instructionsBody, renderMockupFn) {
    const p = addSlide();
    drawTopBar(p, `DOMANDA NASpI • FASE ${stepNumber}: ${stepTitle.toUpperCase()}`, `FASE ${stepNumber}`);

    // Left Mockup Box
    p.drawRectangle({
      x: 40,
      y: 65,
      width: 530,
      height: 395,
      color: cSageSurface,
      borderColor: cCharcoal,
      borderWidth: 1,
      borderRadius: 6,
    });

    // Mockup Title Bar
    p.drawRectangle({
      x: 40,
      y: 432,
      width: 530,
      height: 28,
      color: cCharcoal,
      borderTopLeftRadius: 6,
      borderTopRightRadius: 6,
    });
    p.drawText(`Portale INPS • Servizi Telematici • ${stepTitle}`, {
      x: 54,
      y: 442,
      size: 9.5,
      font: fontBold,
      color: cWhite,
    });

    renderMockupFn(p, 50, 75, 510, 345);

    // Right Area: Instructions Box
    const rx = 590;
    const rw = 330;
    p.drawRectangle({
      x: rx,
      y: 65,
      width: rw,
      height: 395,
      color: cSageCard,
      borderColor: cCharcoal,
      borderWidth: 1,
      borderRadius: 6,
    });

    p.drawRectangle({
      x: rx + 15,
      y: 420,
      width: 170,
      height: 24,
      color: cCharcoal,
      borderRadius: 3,
    });
    p.drawText('ISTRUZIONI OPERATIVE', {
      x: rx + 25,
      y: 428,
      size: 9.5,
      font: fontBold,
      color: cWhite,
    });

    p.drawLine({
      start: { x: rx + 15, y: 395 },
      end: { x: rx + 15, y: 360 },
      thickness: 2.5,
      color: cCharcoal,
    });

    p.drawText(instructionsTitle, {
      x: rx + 25,
      y: 382,
      size: 11.5,
      font: fontBold,
      color: cCharcoal,
    });

    drawTextBlock(p, instructionsBody, rx + 15, 345, rw - 30, 10.2, fontRegular, cCharcoal, 1.42);

    // Action indicator at bottom
    p.drawRectangle({
      x: rx + 15,
      y: 80,
      width: rw - 30,
      height: 52,
      color: cCharcoal,
      borderRadius: 4,
    });
    p.drawText('AZIONE RICHIESTA:', {
      x: rx + 25,
      y: 114,
      size: 8.5,
      font: fontBold,
      color: cSageBg,
    });
    p.drawText('Verifica i dati a video e clicca sul pulsante "AVANTI"', {
      x: rx + 25,
      y: 96,
      size: 9,
      font: fontRegular,
      color: cWhite,
    });

    drawFooter(p, pageNum);
  }

  // ==========================================
  // PAGE 7: Fase 1 - Dati Anagrafici
  // ==========================================
  renderStepPage(
    7,
    1,
    'Dati Anagrafici e Indirizzo di Residenza',
    'Verifica e Conferma Residenza',
    'Quando il datore di lavoro comunica la cessazione del rapporto, trovi precompilati oltre ai dati anagrafici, anche i dati del rapporto di lavoro cessato.\n\n' +
    'Verifica e conferma l\'indirizzo di residenza e, se occorre, spunta il NO qui e aggiungi un recapito postale diverso nella maschera successiva.',
    (p, mx, my, mw, mh) => {
      p.drawRectangle({ x: mx, y: my + 210, width: mw, height: 125, color: cWhite, borderColor: cBorder, borderWidth: 1 });
      p.drawText('Dati Anagrafici dell\'Assicurato', { x: mx + 10, y: my + 318, size: 10, font: fontBold, color: cCharcoal });
      p.drawLine({ start: { x: mx, y: my + 310 }, end: { x: mx + mw, y: my + 310 }, thickness: 0.8, color: cBorder });
      const fields = [
        ['Cognome:', 'ROS*****', 'Nome:', 'MAR*****'],
        ['Codice Fiscale:', 'RSS*************', 'Sesso:', 'M'],
        ['Data di Nascita:', '01/********', 'Comune Nascita:', 'ROM******'],
        ['Stato di Cittadinanza:', 'ITALIA', 'Stato Civile:', 'Celibe / Nubile']
      ];
      fields.forEach((row, i) => {
        const y = my + 292 - i * 22;
        p.drawText(row[0], { x: mx + 10, y, size: 8.5, font: fontBold, color: cCharcoalMuted });
        p.drawText(row[1], { x: mx + 110, y, size: 8.5, font: fontRegular, color: cCharcoal });
        p.drawText(row[2], { x: mx + 260, y, size: 8.5, font: fontBold, color: cCharcoalMuted });
        p.drawText(row[3], { x: mx + 360, y, size: 8.5, font: fontRegular, color: cCharcoal });
      });

      p.drawRectangle({ x: mx, y: my + 75, width: mw, height: 120, color: cWhite, borderColor: cBorder, borderWidth: 1 });
      p.drawText('Dettaglio Indirizzo di Residenza Proposto', { x: mx + 10, y: my + 178, size: 10, font: fontBold, color: cCharcoal });
      p.drawLine({ start: { x: mx, y: my + 170 }, end: { x: mx + mw, y: my + 170 }, thickness: 0.8, color: cBorder });
      p.drawText('Indirizzo: VIA ********************************', { x: mx + 10, y: my + 150, size: 9, font: fontRegular, color: cCharcoal });
      p.drawText('Conferma che questo è il tuo attuale indirizzo di residenza? *', { x: mx + 10, y: my + 124, size: 9, font: fontBold, color: cCharcoal });
      
      p.drawText('(•) SÌ     ( ) NO', { x: mx + 10, y: my + 105, size: 9.5, font: fontBold, color: cCharcoal });
      p.drawText('L\'indirizzo del domicilio coincide con quello sopra riportato? *', { x: mx + 10, y: my + 88, size: 9, font: fontBold, color: cCharcoal });
      p.drawText('(•) SÌ     ( ) NO', { x: mx + 10, y: my + 68, size: 9.5, font: fontBold, color: cCharcoal });

      p.drawRectangle({ x: mx + mw - 95, y: my + 15, width: 85, height: 26, color: cCharcoal, borderRadius: 3 });
      p.drawText('AVANTI →', { x: mx + mw - 80, y: my + 24, size: 9, font: fontBold, color: cWhite });
    }
  );

  // ==========================================
  // PAGE 8: Fase 2 - Inserimento Domicilio
  // ==========================================
  renderStepPage(
    8,
    2,
    'Inserimento Dati Domicilio',
    'Domicilio Diverso per CPI',
    'Inserisci qui, se diverso dall\'indirizzo di residenza, il domicilio (utile per l\'individuazione del Centro per l\'Impiego).\n\n' +
    'Compila tutti i campi contrassegnati con asterisco (*): indirizzo, civico, provincia, comune e CAP.\n\n' +
    'Clicca su CONTINUA.',
    (p, mx, my, mw, mh) => {
      p.drawRectangle({ x: mx, y: my + 60, width: mw, height: 275, color: cWhite, borderColor: cBorder, borderWidth: 1 });
      p.drawText('Inserimento Dati Domicilio (Se diverso da residenza)', { x: mx + 15, y: my + 315, size: 10, font: fontBold, color: cCharcoal });
      p.drawLine({ start: { x: mx, y: my + 305 }, end: { x: mx + mw, y: my + 305 }, thickness: 0.8, color: cBorder });

      const dFields = [
        ['Indirizzo *', 'Via ********************'],
        ['Numero Civico *', '45/*****'],
        ['Provincia *', 'MIL*** (MI)'],
        ['Comune *', 'MIL***'],
        ['CAP *', '201**']
      ];
      dFields.forEach((df, idx) => {
        const fy = my + 270 - idx * 42;
        p.drawText(df[0], { x: mx + 20, y: fy + 8, size: 9, font: fontBold, color: cCharcoalSec });
        p.drawRectangle({ x: mx + 150, y: fy - 4, width: 320, height: 24, color: cSageSurface, borderColor: cBorder, borderWidth: 0.8 });
        p.drawText(df[1], { x: mx + 160, y: fy + 4, size: 9, font: fontRegular, color: cCharcoal });
      });

      p.drawRectangle({ x: mx + mw - 110, y: my + 15, width: 100, height: 26, color: cCharcoal, borderRadius: 3 });
      p.drawText('CONTINUA →', { x: mx + mw - 98, y: my + 24, size: 9, font: fontBold, color: cWhite });

      p.drawRectangle({ x: mx + 15, y: my + 15, width: 85, height: 26, color: cSageCard, borderColor: cCharcoal, borderWidth: 1, borderRadius: 3 });
      p.drawText('← INDIETRO', { x: mx + 25, y: my + 24, size: 9, font: fontBold, color: cCharcoal });
    }
  );

  // ==========================================
  // PAGE 9: Fase 3 - Ultima Posizione Lavorativa
  // ==========================================
  renderStepPage(
    9,
    3,
    'Ultima Posizione Lavorativa',
    'Controllo Dati Ultimo Datore',
    'Verifica quindi i dati dell\'ultimo datore di lavoro presso il quale hai lavorato e l\'inizio e fine del rapporto di lavoro.\n\n' +
    'Conferma cliccando su AVANTI per procedere.',
    (p, mx, my, mw, mh) => {
      p.drawRectangle({ x: mx, y: my + 80, width: mw, height: 255, color: cWhite, borderColor: cBorder, borderWidth: 1 });
      p.drawText('Ultima Posizione Lavorativa', { x: mx + 15, y: my + 315, size: 10.5, font: fontBold, color: cCharcoal });
      p.drawLine({ start: { x: mx, y: my + 305 }, end: { x: mx + mw, y: my + 305 }, thickness: 0.8, color: cBorder });

      const wRows = [
        ['Data inizio rapporto:', '01/********', 'Data fine rapporto:', '31/********'],
        ['Matricola azienda / CF:', '012********', 'Codice Fiscale:', '987********'],
        ['Ragione sociale:', 'AZI*****************************', '', ''],
        ['Motivo cessazione:', 'SCADENZA CONTRATTO A TERMINE', '', ''],
        ['Tipo di rapporto:', 'TEMPO DETERMINATO SUBORDINATO', '', '']
      ];
      wRows.forEach((r, idx) => {
        const fy = my + 280 - idx * 30;
        p.drawText(r[0], { x: mx + 15, y: fy, size: 9, font: fontBold, color: cCharcoalMuted });
        p.drawText(r[1], { x: mx + 140, y: fy, size: 9, font: fontBold, color: cCharcoal });
        if (r[2]) {
          p.drawText(r[2], { x: mx + 270, y: fy, size: 9, font: fontBold, color: cCharcoalMuted });
          p.drawText(r[3], { x: mx + 380, y: fy, size: 9, font: fontBold, color: cCharcoal });
        }
      });

      p.drawRectangle({ x: mx + 15, y: my + 100, width: mw - 30, height: 35, color: cSageSurface, borderColor: cBorder, borderWidth: 0.8 });
      p.drawText('I dati visualizzati sono corretti e completi? *', { x: mx + 25, y: my + 115, size: 9.5, font: fontBold, color: cCharcoal });
      p.drawText('(•) SÌ       ( ) NO', { x: mx + 330, y: my + 115, size: 9.5, font: fontBold, color: cCharcoal });

      p.drawRectangle({ x: mx + mw - 95, y: my + 20, width: 85, height: 26, color: cCharcoal, borderRadius: 3 });
      p.drawText('AVANTI →', { x: mx + mw - 80, y: my + 29, size: 9, font: fontBold, color: cWhite });
    }
  );

  // ==========================================
  // PAGE 10: Fase 4 - Modifica Dati Datore Lavoro
  // ==========================================
  renderStepPage(
    10,
    4,
    'Modifica Dati Ultimo Rapporto',
    'Inserimento Manuale Dati',
    'Se i dati non sono corretti, spunta NO sotto alla tabella e inserisci manualmente i dati dell\'ultimo datore di lavoro e rapporto di lavoro.\n\n' +
    'Procedi cliccando sul bottone AVANTI.',
    (p, mx, my, mw, mh) => {
      p.drawRectangle({ x: mx, y: my + 60, width: mw, height: 275, color: cWhite, borderColor: cBorder, borderWidth: 1 });
      p.drawText('Modifica dei Dati dell\'Ultimo Rapporto Lavorativo', { x: mx + 15, y: my + 315, size: 10, font: fontBold, color: cCharcoal });
      p.drawLine({ start: { x: mx, y: my + 305 }, end: { x: mx + mw, y: my + 305 }, thickness: 0.8, color: cBorder });

      p.drawRectangle({ x: mx + 15, y: my + 250, width: mw - 30, height: 45, color: cSageSurface, borderColor: cBorder, borderWidth: 0.8 });
      p.drawText('Ricerca per CF o Matricola:', { x: mx + 25, y: my + 270, size: 8.5, font: fontBold, color: cCharcoalSec });
      p.drawRectangle({ x: mx + 160, y: my + 260, width: 180, height: 22, color: cWhite, borderColor: cBorder, borderWidth: 0.8 });
      p.drawText('123********', { x: mx + 170, y: my + 268, size: 8.5, font: fontRegular, color: cCharcoal });
      p.drawRectangle({ x: mx + 355, y: my + 260, width: 75, height: 22, color: cCharcoal, borderRadius: 2 });
      p.drawText('CERCA', { x: mx + 375, y: my + 268, size: 8, font: fontBold, color: cWhite });

      const modFields = [
        ['Ragione Sociale Datore *', 'TEC****************************'],
        ['Periodo Lavorativo *', 'Inizio: 01/********      Fine: 31/********'],
        ['Motivo Cessazione *', 'LICENZIAMENTO PER GIUSTIFICATO MOTIVO OGGETTIVO'],
        ['Tipologia Contratto *', 'TEMPO INDETERMINATO FULL-TIME']
      ];
      modFields.forEach((mf, i) => {
        const fy = my + 215 - i * 35;
        p.drawText(mf[0], { x: mx + 20, y: fy + 8, size: 8.5, font: fontBold, color: cCharcoalMuted });
        p.drawRectangle({ x: mx + 160, y: fy - 4, width: 330, height: 22, color: cWhite, borderColor: cBorder, borderWidth: 0.8 });
        p.drawText(mf[1], { x: mx + 170, y: fy + 4, size: 8.5, font: fontRegular, color: cCharcoal });
      });

      p.drawRectangle({ x: mx + mw - 95, y: my + 15, width: 85, height: 26, color: cCharcoal, borderRadius: 3 });
      p.drawText('AVANTI →', { x: mx + mw - 80, y: my + 24, size: 9, font: fontBold, color: cWhite });
    }
  );

  // ==========================================
  // PAGE 11: Fase 5 - Contributi Utili (Ultimi 4 Anni)
  // ==========================================
  renderStepPage(
    11,
    5,
    'Verifica Estratto Contributivo',
    'Mesi Utili per la NASpI',
    'In questa maschera trovi il riepilogo dei mesi di contribuzione relativa al tuo lavoro degli ultimi quattro anni, utile per la NASpI.\n\n' +
    'Per procedere utilizza il bottone COMPILA DOMANDA.',
    (p, mx, my, mw, mh) => {
      p.drawRectangle({ x: mx, y: my + 90, width: mw, height: 245, color: cWhite, borderColor: cBorder, borderWidth: 1 });
      p.drawText('Contributi Utili (Ultimi 4 Anni)', { x: mx + 15, y: my + 315, size: 10.5, font: fontBold, color: cCharcoal });
      p.drawLine({ start: { x: mx, y: my + 305 }, end: { x: mx + mw, y: my + 305 }, thickness: 0.8, color: cBorder });

      p.drawRectangle({ x: mx + 10, y: my + 278, width: mw - 20, height: 22, color: cCharcoal });
      const ths = [
        ['PERIODO', 20],
        ['TIPOLOGIA', 140],
        ['SETT. UTILI', 240],
        ['RETRIBUZIONE (€)', 340],
        ['AZIENDA', 440]
      ];
      ths.forEach(th => {
        p.drawText(th[0], { x: mx + th[1], y: my + 286, size: 7.5, font: fontBold, color: cWhite });
      });

      const rows = [
        ['01/01/2022 - 31/12/2022', 'Lavoro dipendente', '52 sett.', '€ 28.500,00', 'Accreditati'],
        ['01/01/2023 - 31/12/2023', 'Lavoro dipendente', '52 sett.', '€ 29.200,00', 'Accreditati'],
        ['01/01/2024 - 31/12/2024', 'Lavoro dipendente', '52 sett.', '€ 30.100,00', 'Accreditati'],
        ['01/01/2025 - 31/10/2025', 'Lavoro dipendente', '43 sett.', '€ 25.800,00', 'Accreditati']
      ];
      rows.forEach((r, idx) => {
        const ry = my + 252 - idx * 24;
        p.drawRectangle({ x: mx + 10, y: ry - 4, width: mw - 20, height: 22, color: idx % 2 === 0 ? cSageSurface : cWhite });
        p.drawText(r[0], { x: mx + 20, y: ry + 3, size: 8, font: fontRegular, color: cCharcoal });
        p.drawText(r[1], { x: mx + 140, y: ry + 3, size: 8, font: fontRegular, color: cCharcoal });
        p.drawText(r[2], { x: mx + 240, y: ry + 3, size: 8, font: fontBold, color: cCharcoal });
        p.drawText(r[3], { x: mx + 340, y: ry + 3, size: 8, font: fontRegular, color: cCharcoal });
        p.drawText(r[4], { x: mx + 440, y: ry + 3, size: 8, font: fontBold, color: cAccentGreen });
      });

      p.drawRectangle({ x: mx + 10, y: my + 115, width: mw - 20, height: 35, color: cSageCard, borderColor: cBorder, borderWidth: 0.8 });
      p.drawText('Totale Settimane Utili Rilevate: 199 settimane (Requisito soddisfatto: oltre 13 settimane)', {
        x: mx + 20,
        y: my + 128,
        size: 8.5,
        font: fontBold,
        color: cCharcoal
      });

      p.drawRectangle({ x: mx + mw - 145, y: my + 20, width: 135, height: 28, color: cCharcoal, borderRadius: 3 });
      p.drawText('COMPILA DOMANDA →', { x: mx + mw - 132, y: my + 30, size: 8.5, font: fontBold, color: cWhite });
    }
  );

  // ==========================================
  // PAGE 12: Fase 6 - Domanda 1/6 (Dati Personali)
  // ==========================================
  renderStepPage(
    12,
    6,
    'Domanda di Indennità 1/6 • Recapiti e Qualifica',
    'Informazioni Obbligatorie e Contatti',
    'Inserisci le informazioni obbligatorie richieste nei campi contrassegnati con * e almeno uno dei campi indicati con il doppio * col numero di telefono o cellulare.\n\n' +
    'Per procedere utilizza il pulsante AVANTI.',
    (p, mx, my, mw, mh) => {
      p.drawRectangle({ x: mx, y: my + 60, width: mw, height: 275, color: cWhite, borderColor: cBorder, borderWidth: 1 });
      p.drawText('Domanda di indennità di NASpI - Scheda 1 di 6', { x: mx + 15, y: my + 315, size: 10.5, font: fontBold, color: cCharcoal });
      p.drawLine({ start: { x: mx, y: my + 305 }, end: { x: mx + mw, y: my + 305 }, thickness: 0.8, color: cBorder });

      const d1Fields = [
        ['Sede INPS di competenza *', 'SED****************************'],
        ['Telefono / Prefisso **', '061********'],
        ['Cellulare per notifiche SMS **', '333********'],
        ['Indirizzo E-mail ordinario *', 'mar*****************'],
        ['Titolo di Studio *', 'Diploma di Istruzione Secondaria Superiore'],
        ['Qualifica / Categoria *', 'Impiegato Tecnico - Livello 3 CCNL Commercio']
      ];
      d1Fields.forEach((f, i) => {
        const fy = my + 275 - i * 36;
        p.drawText(f[0], { x: mx + 15, y: fy + 8, size: 8.5, font: fontBold, color: cCharcoalMuted });
        p.drawRectangle({ x: mx + 180, y: fy - 4, width: 310, height: 22, color: cWhite, borderColor: cBorder, borderWidth: 0.8 });
        p.drawText(f[1], { x: mx + 190, y: fy + 4, size: 8.5, font: fontRegular, color: cCharcoal });
      });

      p.drawRectangle({ x: mx + mw - 95, y: my + 15, width: 85, height: 26, color: cCharcoal, borderRadius: 3 });
      p.drawText('AVANTI →', { x: mx + mw - 80, y: my + 24, size: 9, font: fontBold, color: cWhite });
    }
  );

  // ==========================================
  // PAGE 13: Fase 7 - Domanda 2/6 (Dichiarazioni)
  // ==========================================
  renderStepPage(
    13,
    7,
    'Domanda 2/6 • Dichiarazioni Obbligatorie',
    'Risposte Si / No alle Domande',
    'Per compilare la domanda spunta No oppure Si in risposta alle domande elencate nella maschera.\n\n' +
    'Procedi utilizzando il pulsante AVANTI.',
    (p, mx, my, mw, mh) => {
      p.drawRectangle({ x: mx, y: my + 60, width: mw, height: 275, color: cWhite, borderColor: cBorder, borderWidth: 1 });
      p.drawText('Domanda di indennità di NASpI - Scheda 2 di 6 (Dichiarazioni)', { x: mx + 15, y: my + 315, size: 10, font: fontBold, color: cCharcoal });
      p.drawLine({ start: { x: mx, y: my + 305 }, end: { x: mx + mw, y: my + 305 }, thickness: 0.8, color: cBorder });

      const qList = [
        'Ho percepito o percepirò l\'indennità di mancato preavviso dal datore',
        'Dichiaro di aver svolto periodi di lavoro all\'estero',
        'Dichiaro di essere titolare di assegno ordinario di invalidità INPS',
        'Sono stato licenziato per superamento del periodo di comporto per malattia',
        'Sono attualmente in malattia o ricoverato alla data di trasmissione'
      ];

      qList.forEach((q, i) => {
        const qy = my + 265 - i * 44;
        p.drawText(`${i + 1}. ${q}`, { x: mx + 15, y: qy + 12, size: 8.5, font: fontBold, color: cCharcoal });
        p.drawRectangle({ x: mx + 15, y: qy - 12, width: 140, height: 20, color: cSageSurface, borderColor: cBorder, borderWidth: 0.8 });
        p.drawText('( ) SÌ       (•) NO', { x: mx + 25, y: qy - 6, size: 8.5, font: fontBold, color: cCharcoal });
      });

      p.drawRectangle({ x: mx + mw - 95, y: my + 15, width: 85, height: 26, color: cCharcoal, borderRadius: 3 });
      p.drawText('AVANTI →', { x: mx + mw - 80, y: my + 24, size: 9, font: fontBold, color: cWhite });
    }
  );

  // ==========================================
  // PAGE 14: Fase 8 - Domanda 3/6 (Attività Lavorativa e Redditi)
  // ==========================================
  renderStepPage(
    14,
    8,
    'Domanda 3/6 • 30 Giornate e Altre Attività',
    'Specificazione Tipo Attività e Redditi',
    'Se hai lavorato per almeno 30 giornate negli ultimi 12 mesi, spunta la casella in testa. Rispondi per specificare il tipo di attività svolta.\n\n' +
    'Se non hai intrapreso attività imprenditoriale o professionale compila le caselle NO e procedi col pulsante AVANTI.\n\n' +
    'In caso affermativo, dovrai inserire anche le ulteriori informazioni richieste (data inizio attività e reddito).',
    (p, mx, my, mw, mh) => {
      p.drawRectangle({ x: mx, y: my + 60, width: mw, height: 275, color: cWhite, borderColor: cBorder, borderWidth: 1 });
      p.drawText('Domanda di indennità di NASpI - Scheda 3 di 6 (Attività e Redditi)', { x: mx + 15, y: my + 315, size: 10, font: fontBold, color: cCharcoal });
      p.drawLine({ start: { x: mx, y: my + 305 }, end: { x: mx + mw, y: my + 305 }, thickness: 0.8, color: cBorder });

      const actList = [
        ['Attività autonoma / professionale / P.IVA', '( ) SÌ   (•) NO', 'Reddito presunto: € 0,00'],
        ['Attività parasubordinata (co.co.co.)', '( ) SÌ   (•) NO', 'Reddito presunto: € 0,00'],
        ['Lavoro occasionale accessorio', '( ) SÌ   (•) NO', 'Reddito presunto: € 0,00'],
        ['Cariche sociali in società di capitali o persone', '( ) SÌ   (•) NO', 'Compenso presunto: € 0,00'],
        ['Iscrizione ad Albi o Casse Professionali', '( ) SÌ   (•) NO', 'Nessuna iscrizione attiva']
      ];

      actList.forEach((a, i) => {
        const ay = my + 265 - i * 44;
        p.drawText(a[0], { x: mx + 15, y: ay + 10, size: 8.5, font: fontBold, color: cCharcoal });
        p.drawText(a[1], { x: mx + 260, y: ay + 10, size: 8.5, font: fontBold, color: cCharcoal });
        p.drawText(a[2], { x: mx + 360, y: ay + 10, size: 8, font: fontRegular, color: cCharcoalMuted });
      });

      p.drawRectangle({ x: mx + mw - 95, y: my + 15, width: 85, height: 26, color: cCharcoal, borderRadius: 3 });
      p.drawText('AVANTI →', { x: mx + mw - 80, y: my + 24, size: 9, font: fontBold, color: cWhite });
    }
  );

  // ==========================================
  // PAGE 15: Fase 9 - Domanda 4/6 (Modalità di Pagamento)
  // ==========================================
  renderStepPage(
    15,
    9,
    'Domanda 4/6 • Modalità di Pagamento',
    'Accredito e Coordinate Bancarie',
    'Scegli la modalità di accredito degli importi di NASpI.\n\n' +
    'Se opti per l\'accredito sul tuo conto corrente, a tua tutela, indica le coordinate IBAN e scansiona il Modulo SR163, che avrai già scaricato e compilato, acquisendo il timbro/visto della tua banca.',
    (p, mx, my, mw, mh) => {
      p.drawRectangle({ x: mx, y: my + 60, width: mw, height: 275, color: cWhite, borderColor: cBorder, borderWidth: 1 });
      p.drawText('Domanda di indennità di NASpI - Scheda 4 di 6 (Pagamento)', { x: mx + 15, y: my + 315, size: 10, font: fontBold, color: cCharcoal });
      p.drawLine({ start: { x: mx, y: my + 305 }, end: { x: mx + mw, y: my + 305 }, thickness: 0.8, color: cBorder });

      p.drawText('Seleziona la modalità di pagamento desiderata: *', { x: mx + 15, y: my + 278, size: 9, font: fontBold, color: cCharcoalSec });
      p.drawRectangle({ x: mx + 15, y: my + 240, width: mw - 30, height: 28, color: cSageSurface, borderColor: cBorder, borderWidth: 0.8 });
      p.drawText('(•) ACCREDITO SU CONTO CORRENTE BANCARIO O POSTALE     ( ) BONIFICO DOMICILIATO', { x: mx + 25, y: my + 250, size: 8.5, font: fontBold, color: cCharcoal });

      p.drawText('Codice IBAN (27 caratteri alfanumerici) *', { x: mx + 15, y: my + 215, size: 9, font: fontBold, color: cCharcoalMuted });
      p.drawRectangle({ x: mx + 15, y: my + 185, width: mw - 30, height: 24, color: cWhite, borderColor: cCharcoal, borderWidth: 1 });
      p.drawText('IT0************************', { x: mx + 25, y: my + 192, size: 10.5, font: fontBold, color: cCharcoal });

      p.drawRectangle({ x: mx + 15, y: my + 95, width: mw - 30, height: 65, color: cSageCard, borderColor: cBorder, borderWidth: 0.8 });
      p.drawText('Richiesta Detrazioni Fiscali e Assegno Nucleo Familiare (ANF):', { x: mx + 25, y: my + 140, size: 8.5, font: fontBold, color: cCharcoal });
      drawTextBlock(
        p,
        'La dichiarazione telematica per il diritto alle detrazioni d\'imposta può essere fatta dopo l\'invio della domanda tramite il tasto "Detrazioni fiscali" o dal servizio dedicato del portale.',
        mx + 25,
        my + 125,
        mw - 50,
        8,
        fontRegular,
        cCharcoalSec,
        1.35
      );

      p.drawRectangle({ x: mx + mw - 95, y: my + 15, width: 85, height: 26, color: cCharcoal, borderRadius: 3 });
      p.drawText('AVANTI →', { x: mx + mw - 80, y: my + 24, size: 9, font: fontBold, color: cWhite });
    }
  );

  // ==========================================
  // PAGE 16: Fase 10 - Domanda 5/6 (DID e Centro Impiego)
  // ==========================================
  renderStepPage(
    16,
    10,
    'Domanda 5/6 • DID e Centro per l\'Impiego',
    'Ultima Qualifica Professionale',
    'Ora inserisci la tua ultima qualifica, prima di cessare il rapporto di lavoro.\n\n' +
    'Procedi utilizzando il pulsante AVANTI.',
    (p, mx, my, mw, mh) => {
      p.drawRectangle({ x: mx, y: my + 60, width: mw, height: 275, color: cWhite, borderColor: cBorder, borderWidth: 1 });
      p.drawText('Domanda di indennità di NASpI - Scheda 5 di 6 (DID e CPI)', { x: mx + 15, y: my + 315, size: 10, font: fontBold, color: cCharcoal });
      p.drawLine({ start: { x: mx, y: my + 305 }, end: { x: mx + mw, y: my + 305 }, thickness: 0.8, color: cBorder });

      const textDidBox = 
        'ATTENZIONE, ai sensi dell\'art. 21 del decreto legislativo n. 150 del 2015, la presente domanda di indennità NASpI equivale a dichiarazione di immediata disponibilità. Le ricordiamo comunque che lei è tenuto a contattare il centro per l\'impiego competente in relazione al luogo del suo domicilio entro i prossimi quindici giorni ai fini della stipula del patto di servizio personalizzato.';

      p.drawRectangle({ x: mx + 15, y: my + 195, width: mw - 30, height: 95, color: cSageSurface, borderColor: cBorder, borderWidth: 0.8 });
      drawTextBlock(p, textDidBox, mx + 25, my + 270, mw - 50, 8.5, fontRegular, cCharcoal, 1.4);

      p.drawText('Ove rivestivo la seguente qualifica: *', { x: mx + 15, y: my + 165, size: 9, font: fontBold, color: cCharcoalMuted });
      p.drawRectangle({ x: mx + 15, y: my + 135, width: mw - 30, height: 24, color: cWhite, borderColor: cCharcoal, borderWidth: 1 });
      p.drawText('IMPIEGATO AMMINISTRATIVO - GESTIONE PRATICHE E CONTABILITÀ', { x: mx + 25, y: my + 142, size: 9, font: fontBold, color: cCharcoal });

      p.drawText('• Dichiaro di non svolgere attività lavorativa subordinata', { x: mx + 15, y: my + 112, size: 8.5, font: fontRegular, color: cCharcoal });
      p.drawText('• Dichiaro di non svolgere attività lavorativa in forma autonoma o parasubordinata', { x: mx + 15, y: my + 94, size: 8.5, font: fontRegular, color: cCharcoal });

      p.drawRectangle({ x: mx + mw - 95, y: my + 15, width: 85, height: 26, color: cCharcoal, borderRadius: 3 });
      p.drawText('AVANTI →', { x: mx + mw - 80, y: my + 24, size: 9, font: fontBold, color: cWhite });
    }
  );

  // ==========================================
  // PAGE 17: Fase 11 - Domanda 6/6 (Impegni e Veridicità)
  // ==========================================
  renderStepPage(
    17,
    11,
    'Domanda 6/6 • Comunicazioni e Veridicità',
    'Conferma Impegni e Veridicità',
    'Per completare la domanda, inserisci le spunte a conferma dell\'impegno a comunicare qualsiasi eventuale variazione successiva all\'invio della domanda e della veridicità delle tue dichiarazioni.\n\n' +
    'Clicca AVANTI e visualizza il riepilogo.',
    (p, mx, my, mw, mh) => {
      p.drawRectangle({ x: mx, y: my + 60, width: mw, height: 275, color: cWhite, borderColor: cBorder, borderWidth: 1 });
      p.drawText('Domanda di indennità di NASpI - Scheda 6 di 6 (Veridicità)', { x: mx + 15, y: my + 315, size: 10, font: fontBold, color: cCharcoal });
      p.drawLine({ start: { x: mx, y: my + 305 }, end: { x: mx + mw, y: my + 305 }, thickness: 0.8, color: cBorder });

      p.drawText('[X] Mi impegno a comunicare all\'INPS:', { x: mx + 15, y: my + 280, size: 9, font: fontBold, color: cCharcoal });
      const impList = [
        '• entro cinque giorni, la variazione della mia condizione di disoccupato;',
        '• entro un mese dall\'inizio, l\'avvio di un\'attività lavorativa autonoma con stima reddito;',
        '• entro trenta giorni, la variazione dei dati relativi ai componenti e ai redditi;',
        '• l\'espatrio verso altro stato membro UE o non comunitario alla ricerca di lavoro.'
      ];
      impList.forEach((im, idx) => {
        p.drawText(im, { x: mx + 25, y: my + 255 - idx * 20, size: 8.5, font: fontRegular, color: cCharcoalSec });
      });

      p.drawRectangle({ x: mx + 15, y: my + 100, width: mw - 30, height: 50, color: cSageSurface, borderColor: cBorder, borderWidth: 0.8 });
      p.drawText('[X] Dichiarazione di veridicità *', { x: mx + 25, y: my + 130, size: 9, font: fontBold, color: cCharcoal });
      p.drawText('Dichiaro che le notizie fornite rispondono a verità e sono consapevole delle conseguenze previste (D.P.R. 445/2000).', {
        x: mx + 25,
        y: my + 112,
        size: 8,
        font: fontRegular,
        color: cCharcoalSec
      });

      p.drawRectangle({ x: mx + mw - 95, y: my + 15, width: 85, height: 26, color: cCharcoal, borderRadius: 3 });
      p.drawText('AVANTI →', { x: mx + mw - 80, y: my + 24, size: 9, font: fontBold, color: cWhite });
    }
  );

  // ==========================================
  // PAGE 18: Fase 12 - Riepilogo Dati e Invio (Accredito C/C)
  // ==========================================
  renderStepPage(
    18,
    12,
    'Riepilogo Dati e Invio Definitivo',
    'Verifica Correttezza Dati',
    'Visualizza la pagina di riepilogo dati per verificarne la correttezza.\n\n' +
    'Se hai scelto l\'accredito degli importi sul proprio Conto Corrente, per allegare il modulo SR163 (richiesto per la verifica dell\'IBAN), clicca sull\'apposito bottone. Se utilizzi il pulsante Salva e Esci, salvi la domanda in bozza e puoi integrarla in seguito.\n\n' +
    'Con il pulsante CONFERMA, la invii direttamente.',
    (p, mx, my, mw, mh) => {
      p.drawRectangle({ x: mx, y: my + 75, width: mw, height: 260, color: cWhite, borderColor: cBorder, borderWidth: 1 });
      p.drawText('Riepilogo dei Dati Inseriti', { x: mx + 15, y: my + 315, size: 10, font: fontBold, color: cCharcoal });
      p.drawLine({ start: { x: mx, y: my + 305 }, end: { x: mx + mw, y: my + 305 }, thickness: 0.8, color: cBorder });

      const sumLines = [
        'Il/la sig. MAR*** ROS*** (CF: RSS*************) chiede all\'INPS l\'indennità di NASpI per cessazione del rapporto',
        'avvenuta in data 19/******** presso l\'azienda BAN***************** (CF: 001*********)',
        'DICHIARA che l\'indirizzo proposto corrisponde a quello di residenza',
        'di non percepire l\'indennità di mancato preavviso e di poter far valere 30 giornate',
        'di voler ricevere l\'indennità richiesta tramite ACCREDITO SU CONTO CORRENTE (IBAN: IT0************************)'
      ];
      sumLines.forEach((sl, i) => {
        p.drawText(sl, { x: mx + 15, y: my + 280 - i * 22, size: 8.5, font: fontRegular, color: cCharcoal });
      });

      p.drawRectangle({ x: mx + 15, y: my + 140, width: 120, height: 26, color: cSageCard, borderColor: cCharcoal, borderWidth: 1, borderRadius: 3 });
      p.drawText('+ ALLEGATI', { x: mx + 38, y: my + 149, size: 9, font: fontBold, color: cCharcoal });

      p.drawRectangle({ x: mx + 15, y: my + 20, width: 90, height: 26, color: cSageCard, borderColor: cBorder, borderWidth: 0.8, borderRadius: 3 });
      p.drawText('← INDIETRO', { x: mx + 25, y: my + 29, size: 8.5, font: fontBold, color: cCharcoal });

      p.drawRectangle({ x: mx + 115, y: my + 20, width: 90, height: 26, color: cSageCard, borderColor: cAccentRed, borderWidth: 0.8, borderRadius: 3 });
      p.drawText('ABBANDONA', { x: mx + 125, y: my + 29, size: 8.5, font: fontBold, color: cAccentRed });

      p.drawRectangle({ x: mx + 280, y: my + 20, width: 100, height: 26, color: cSageCard, borderColor: cCharcoal, borderWidth: 1, borderRadius: 3 });
      p.drawText('SALVA E ESCI', { x: mx + 295, y: my + 29, size: 8.5, font: fontBold, color: cCharcoal });

      p.drawRectangle({ x: mx + 395, y: my + 20, width: 100, height: 26, color: cCharcoal, borderRadius: 3 });
      p.drawText('CONFERMA ✓', { x: mx + 410, y: my + 29, size: 8.5, font: fontBold, color: cWhite });
    }
  );

  // ==========================================
  // PAGE 19: Fase 13 - Riepilogo Bonifico Domiciliato
  // ==========================================
  renderStepPage(
    19,
    13,
    'Riepilogo Bonifico Domiciliato',
    'Scelta Sede INPS e Bonifico',
    'La pagina di riepilogo sarà leggermente diversa se hai scelto di ricevere gli importi con bonifico domiciliato.\n\n' +
    'Se hai Allegati da inoltrare, clicca sull\'apposito bottone.\n\n' +
    'Poi Salva e esci e CONFERMA con i bottoni in fondo.\n\n' +
    '(Nella maggior parte dei casi la Sede Inps viene determinata dalla procedura e non è modificabile. Potrai scegliere la Sede Inps solo se al CAP del tuo Comune sono associate due Sedi).',
    (p, mx, my, mw, mh) => {
      p.drawRectangle({ x: mx, y: my + 75, width: mw, height: 260, color: cWhite, borderColor: cBorder, borderWidth: 1 });
      p.drawText('Riepilogo Dati • Modalità Bonifico Domiciliato', { x: mx + 15, y: my + 315, size: 10, font: fontBold, color: cCharcoal });
      p.drawLine({ start: { x: mx, y: my + 305 }, end: { x: mx + mw, y: my + 305 }, thickness: 0.8, color: cBorder });

      p.drawText('Modalità di riscossione: BONIFICO DOMICILIATO PRESSO POSTE ITALIANE', { x: mx + 15, y: my + 280, size: 9, font: fontBold, color: cCharcoal });
      p.drawText('Sede INPS assegnata dalla procedura: ROM******************** (Codice 7012)', { x: mx + 15, y: my + 258, size: 8.5, font: fontRegular, color: cCharcoal });

      p.drawRectangle({ x: mx + 15, y: my + 155, width: mw - 30, height: 75, color: cSageCard, borderColor: cBorder, borderWidth: 0.8 });
      p.drawText('Avviso sulla Sede Territoriale INPS:', { x: mx + 25, y: my + 210, size: 9, font: fontBold, color: cCharcoal });
      drawTextBlock(
        p,
        'Nella maggior parte dei casi la Sede Inps viene determinata dalla procedura e non è modificabile. Potrai scegliere la Sede Inps solo se al CAP del tuo Comune sono associate due Sedi.',
        mx + 25,
        my + 195,
        mw - 50,
        8.5,
        fontRegular,
        cCharcoalSec,
        1.4
      );

      p.drawRectangle({ x: mx + mw - 110, y: my + 20, width: 100, height: 26, color: cCharcoal, borderRadius: 3 });
      p.drawText('CONFERMA ✓', { x: mx + mw - 95, y: my + 29, size: 8.5, font: fontBold, color: cWhite });
    }
  );

  // ==========================================
  // PAGE 20: Fase 14 - Gestione Allegati Telematici
  // ==========================================
  renderStepPage(
    20,
    14,
    'Gestione Allegati Telematici',
    'Selezione e Caricamento Documenti',
    'Se hai necessità di allegare documenti, Sfoglia le cartelle del tuo computer per selezionare i documenti da allegare e clicca Allega.\n\n' +
    'Usa il pulsante Torna allo sportello per tornare alla maschera di Riepilogo per cliccare su Conferma (vedi pagina 18 o 19).',
    (p, mx, my, mw, mh) => {
      p.drawRectangle({ x: mx, y: my + 60, width: mw, height: 275, color: cWhite, borderColor: cBorder, borderWidth: 1 });
      p.drawText('Nuova assicurazione sociale per l\'impiego > Allegati', { x: mx + 15, y: my + 315, size: 10, font: fontBold, color: cCharcoal });
      p.drawLine({ start: { x: mx, y: my + 305 }, end: { x: mx + mw, y: my + 305 }, thickness: 0.8, color: cBorder });

      p.drawRectangle({ x: mx + 15, y: my + 200, width: mw - 30, height: 95, color: cSageSurface, borderColor: cBorder, borderWidth: 0.8 });
      p.drawText('Documento da allegare:', { x: mx + 25, y: my + 275, size: 9, font: fontBold, color: cCharcoal });
      p.drawText('Nome file: mod************************.pdf', { x: mx + 25, y: my + 255, size: 8.5, font: fontRegular, color: cCharcoal });
      p.drawRectangle({ x: mx + 220, y: my + 245, width: 75, height: 22, color: cSageCard, borderColor: cCharcoal, borderWidth: 1, borderRadius: 2 });
      p.drawText('Sfoglia...', { x: mx + 235, y: my + 253, size: 8, font: fontBold, color: cCharcoal });

      p.drawText('Descrizione: Modulo SR163 con timbro banca', { x: mx + 25, y: my + 228, size: 8.5, font: fontRegular, color: cCharcoal });
      p.drawRectangle({ x: mx + 380, y: my + 218, width: 85, height: 24, color: cCharcoal, borderRadius: 3 });
      p.drawText('+ ALLEGA', { x: mx + 400, y: my + 226, size: 8.5, font: fontBold, color: cWhite });

      p.drawRectangle({ x: mx + 15, y: my + 85, width: mw - 30, height: 100, color: cWhite, borderColor: cBorder, borderWidth: 0.8 });
      p.drawText('Lista Allegati alla Domanda:', { x: mx + 25, y: my + 165, size: 9, font: fontBold, color: cCharcoal });
      p.drawText('1. mod**********************.pdf (340 KB) • Modulo IBAN validato [OK]', { x: mx + 25, y: my + 140, size: 8.5, font: fontBold, color: cAccentGreen });
      p.drawText('2. let***********************.pdf (520 KB) • Atto di recesso [OK]', { x: mx + 25, y: my + 115, size: 8.5, font: fontBold, color: cAccentGreen });

      p.drawRectangle({ x: mx + 15, y: my + 15, width: 145, height: 26, color: cCharcoal, borderRadius: 3 });
      p.drawText('← TORNA ALLO SPORTELLO', { x: mx + 25, y: my + 24, size: 8.5, font: fontBold, color: cWhite });
    }
  );

  // ==========================================
  // PAGE 21: Fase 15 - Esito Invio e Ricevuta
  // ==========================================
  renderStepPage(
    21,
    15,
    'Domanda Presentata con Successo',
    'Conferma Invio e Ricevuta con Protocollo',
    'Cliccando sul pulsante Conferma hai protocollato e inviato la tua domanda.\n\n' +
    'A seguito dell\'invio con successo, visualizzi questa maschera con i pulsanti per la Stampa Ricevuta e/o Domanda.\n\n' +
    'Puoi scaricare e salvare entrambe i pdf sul tuo computer senza consumare carta, cliccando sulla freccina rossa.',
    (p, mx, my, mw, mh) => {
      p.drawRectangle({ x: mx, y: my + 60, width: mw, height: 275, color: cWhite, borderColor: cBorder, borderWidth: 1 });
      p.drawText('Invio domanda di NASpI • Ricevuta di Presentazione', { x: mx + 15, y: my + 315, size: 10, font: fontBold, color: cCharcoal });
      p.drawLine({ start: { x: mx, y: my + 305 }, end: { x: mx + mw, y: my + 305 }, thickness: 0.8, color: cBorder });

      p.drawRectangle({ x: mx + 15, y: my + 195, width: mw - 30, height: 95, color: cSageSurface, borderColor: cAccentGreen, borderWidth: 1.5, borderRadius: 4 });
      p.drawText('✓ DOMANDA PRESENTATA CON SUCCESSO', { x: mx + 30, y: my + 265, size: 12, font: fontBold, color: cAccentGreen });
      p.drawText('Numero Protocollo Ufficiale: INP********************************', { x: mx + 30, y: my + 240, size: 10, font: fontBold, color: cCharcoal });
      p.drawText('Gentile signore/a MAR*** ROS*** la sua domanda è stata inviata alla sede di competenza.', { x: mx + 30, y: my + 215, size: 8.5, font: fontRegular, color: cCharcoalSec });

      p.drawRectangle({ x: mx + 15, y: my + 110, width: 180, height: 35, color: cCharcoal, borderRadius: 4 });
      p.drawText('STAMPA RICEVUTA (PDF) ↓', { x: mx + 30, y: my + 124, size: 9.5, font: fontBold, color: cWhite });

      p.drawRectangle({ x: mx + 215, y: my + 110, width: 180, height: 35, color: cSageCard, borderColor: cCharcoal, borderWidth: 1.5, borderRadius: 4 });
      p.drawText('STAMPA DOMANDA (PDF) ↓', { x: mx + 230, y: my + 124, size: 9.5, font: fontBold, color: cCharcoal });

      p.drawText('Entro breve riceverai una comunicazione di merito e avviso via SMS.', { x: mx + 15, y: my + 75, size: 9, font: fontBold, color: cCharcoalSec });
    }
  );

  // ==========================================
  // PAGE 22: Fase 16 - Salvataggio Bozza
  // ==========================================
  renderStepPage(
    22,
    16,
    'Salvataggio in Bozza e Recupero Pratica',
    'Salvataggio Pratica in Bozza',
    'Se nella pagina di riepilogo, dopo aver eventualmente allegato documenti, clicchi sul bottone Salva ed esci compare questa maschera con il pulsante di Stampa ricevuta.\n\n' +
    'Cliccando sulla freccina rossa puoi aprire e salvare sul tuo PC la ricevuta in pdf.',
    (p, mx, my, mw, mh) => {
      p.drawRectangle({ x: mx, y: my + 60, width: mw, height: 275, color: cWhite, borderColor: cBorder, borderWidth: 1 });
      p.drawText('Invio domanda di NASpI • Stato Salvataggio', { x: mx + 15, y: my + 315, size: 10, font: fontBold, color: cCharcoal });
      p.drawLine({ start: { x: mx, y: my + 305 }, end: { x: mx + mw, y: my + 305 }, thickness: 0.8, color: cBorder });

      p.drawRectangle({ x: mx + 15, y: my + 195, width: mw - 30, height: 95, color: cSageSurface, borderColor: cAccentYellow, borderWidth: 1.5, borderRadius: 4 });
      p.drawText('⚠ DOMANDA SALVATA - IN ATTESA DI PROTOCOLLAZIONE', { x: mx + 30, y: my + 265, size: 11, font: fontBold, color: cAccentYellow });
      p.drawText('La domanda è stata presa in carico come bozza non ancora inviata definitivamente.', { x: mx + 30, y: my + 240, size: 9, font: fontBold, color: cCharcoal });
      p.drawText('Puoi riaprire e completare l\'invio in qualsiasi momento entro i termini di legge.', { x: mx + 30, y: my + 215, size: 8.5, font: fontRegular, color: cCharcoalSec });

      p.drawRectangle({ x: mx + 15, y: my + 110, width: 220, height: 35, color: cCharcoal, borderRadius: 4 });
      p.drawText('SCARICA RICEVUTA BOZZA (PDF) ↓', { x: mx + 25, y: my + 124, size: 9, font: fontBold, color: cWhite });

      p.drawText('Qualora nei prossimi giorni volessi procedere, riaccedi al servizio Consultazione domande.', { x: mx + 15, y: my + 75, size: 9, font: fontBold, color: cCharcoalSec });
    }
  );

  // ==========================================
  // PAGE 23: Chiusura e Note Finali
  // ==========================================
  {
    const p = addSlide();

    p.drawRectangle({
      x: 60,
      y: 75,
      width: W - 120,
      height: 390,
      color: cSageCard,
      borderColor: cCharcoal,
      borderWidth: 1.2,
      borderRadius: 8,
    });

    p.drawRectangle({
      x: 90,
      y: 410,
      width: 170,
      height: 24,
      color: cCharcoal,
      borderRadius: 3,
    });
    p.drawText('RIASSUNTO E CONTATTI', {
      x: 100,
      y: 418,
      size: 9.5,
      font: fontBold,
      color: cWhite,
    });

    p.drawText('Nuova Assicurazione Sociale per l\'Impiego (NASpI)', {
      x: 90,
      y: 375,
      size: 24,
      font: fontBold,
      color: cCharcoal,
    });

    p.drawLine({
      start: { x: 90, y: 355 },
      end: { x: 90, y: 155 },
      thickness: 2.5,
      color: cCharcoal,
    });

    const closePoints = [
      '• Termine di legge: La domanda deve pervenire all\'INPS entro 68 giorni a pena di decadenza (art. 6 D.Lgs. 22/2015).',
      '• Decorrenza erogazione: Dall\'ottavo giorno successivo alla cessazione o dal giorno successivo alla domanda se inoltrata dopo.',
      '• Adempimenti Centro per l\'Impiego: Contattare il CPI entro 15 giorni per la firma del Patto di Servizio Personalizzato.',
      '• Canali ufficiali INPS: Contact Center 803 164 (gratuito da rete fissa) oppure 06 164 164 (da rete mobile a tariffazione del gestore).',
      '• Consultazione stato pratica: Sempre disponibile nell\'area riservata MyINPS > NASpI: Consultazione domande.'
    ];

    closePoints.forEach((pt, idx) => {
      p.drawText(pt, {
        x: 105,
        y: 340 - idx * 30,
        size: 9.5,
        font: fontRegular,
        color: cCharcoal,
      });
    });

    // Disclaimer Box on Page 23
    p.drawRectangle({
      x: 90,
      y: 145,
      width: W - 180,
      height: 48,
      color: cSageSurface,
      borderColor: cBorder,
      borderWidth: 0.8,
      borderRadius: 4,
    });
    p.drawText('NOTA BENE • DISCLAIMER INFORMATIVO:', {
      x: 105,
      y: 178,
      size: 8.5,
      font: fontBold,
      color: cCharcoal,
    });
    p.drawText('La presente procedura non sostituisce la consulenza di un professionista abilitato (Patronato, CAF o Consulente del Lavoro),', {
      x: 105,
      y: 164,
      size: 8,
      font: fontRegular,
      color: cCharcoalSec,
    });
    p.drawText('ma fornisce una linea guida semplice e chiara per il cittadino volta a semplificare la compilazione e l\'inoltro della domanda NASpI.', {
      x: 105,
      y: 152,
      size: 8,
      font: fontRegular,
      color: cCharcoalSec,
    });

    p.drawRectangle({
      x: 90,
      y: 92,
      width: W - 180,
      height: 38,
      color: cCharcoal,
      borderRadius: 4,
    });
    p.drawText('PROCEDURA TELEMATICA CONFORME ALLE CIRCOLARI INPS VIGENTI', {
      x: 230,
      y: 107,
      size: 10.5,
      font: fontBold,
      color: cWhite,
    });

    drawFooter(p, 23);
  }

  const pdfBytes = await pdfDoc.save();
  const outPath = 'guidanaspicompilazione2026.pdf';
  fs.writeFileSync(outPath, pdfBytes);
  console.log('PDF successfully generated! Size:', pdfBytes.length, 'bytes');

  // Also save to app assets so it can be accessed within the Android app
  try {
    const assetsDir = './app/src/main/assets';
    if (!fs.existsSync(assetsDir)) {
      fs.mkdirSync(assetsDir, { recursive: true });
    }
    fs.writeFileSync(path.join(assetsDir, outPath), pdfBytes);
    console.log('PDF also saved to:', path.join(assetsDir, outPath));
  } catch (e) {
    console.log('Notice on assets:', e.message);
  }
}

buildPdf().catch(err => {
  console.error('Error generating PDF:', err);
  process.exit(1);
});
