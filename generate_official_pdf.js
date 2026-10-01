const fs = require('fs');
const path = require('path');
const { PDFDocument, rgb, StandardFonts } = require('pdf-lib');

function sanitizeText(str) {
  if (!str) return '';
  return str
    .replace(/[’‘`]/g, "'")
    .replace(/[“”]/g, '"')
    .replace(/→/g, '>')
    .replace(/←/g, '<')
    .replace(/•/g, '-')
    .replace(/✓/g, 'OK')
    .replace(/⏳/g, '')
    .replace(/↓/g, '')
    .replace(/[^\x00-\x7F\xA0-\xFF]/g, '');
}

async function createOfficialNaspiPdf() {
  const pdfDoc = await PDFDocument.create();

  const fontRegular = await pdfDoc.embedFont(StandardFonts.Helvetica);
  const fontBold = await pdfDoc.embedFont(StandardFonts.HelveticaBold);
  const fontOblique = await pdfDoc.embedFont(StandardFonts.HelveticaOblique);

  // Palette matching official INPS Self-Service NASpI slides
  const cBurgundy = rgb(0.502, 0.098, 0.286); // #801949 / Deep Burgundy
  const cBurgundyDark = rgb(0.42, 0.07, 0.23);
  const cBurgundyLight = rgb(0.58, 0.15, 0.35);
  const cBgGray = rgb(0.87, 0.90, 0.90);     // #DEE6E6
  const cWhite = rgb(1, 1, 1);
  const cCharcoal = rgb(0.18, 0.20, 0.22);
  const cCharcoalSec = rgb(0.35, 0.38, 0.40);
  const cTextMuted = rgb(0.48, 0.52, 0.55);
  const cBlueHeader = rgb(0.18, 0.36, 0.65); // #2E5C9A
  const cBlueLight = rgb(0.90, 0.94, 0.98);
  const cYellowAlert = rgb(0.99, 0.96, 0.85);
  const cYellowBorder = rgb(0.90, 0.80, 0.30);
  const cBorder = rgb(0.75, 0.80, 0.82);
  const cGreen = rgb(0.15, 0.58, 0.32);
  const cRed = rgb(0.80, 0.20, 0.20);
  const cButtonBlue = rgb(0.20, 0.45, 0.80);

  const W = 960;
  const H = 540;

  function safeDrawText(page, text, options) {
    const clean = sanitizeText(text);
    return page.drawText(clean, options);
  }

  function drawWrappedText(page, text, x, y, maxWidth, fontSize, font, color, lineHeight = 1.35) {
    const cleanText = sanitizeText(text);
    const paragraphs = cleanText.split('\n');
    let currentY = y;
    for (const para of paragraphs) {
      if (para.trim() === '') {
        currentY -= fontSize * 0.8;
        continue;
      }
      const words = para.split(' ');
      let currentLine = '';
      for (const word of words) {
        const testLine = currentLine ? `${currentLine} ${word}` : word;
        const testWidth = font.widthOfTextAtSize(testLine, fontSize);
        if (testWidth > maxWidth && currentLine !== '') {
          page.drawText(currentLine, { x, y: currentY, size: fontSize, font, color });
          currentY -= fontSize * lineHeight;
          currentLine = word;
        } else {
          currentLine = testLine;
        }
      }
      if (currentLine) {
        page.drawText(currentLine, { x, y: currentY, size: fontSize, font, color });
        currentY -= fontSize * lineHeight;
      }
    }
    return currentY;
  }

  function drawHeaderBar(page, title = "Nuova Assicurazione Sociale per l'Impiego (NASpI): invio domanda") {
    page.drawRectangle({ x: 0, y: H - 45, width: W, height: 45, color: cBurgundy });
    // Screen icon
    page.drawRectangle({ x: 20, y: H - 34, width: 24, height: 16, color: cWhite });
    page.drawRectangle({ x: 23, y: H - 31, width: 18, height: 10, color: cBurgundy });
    page.drawRectangle({ x: 18, y: H - 37, width: 28, height: 3, color: cWhite });

    safeDrawText(page, title, {
      x: 58,
      y: H - 28,
      size: 13,
      font: fontBold,
      color: cWhite
    });
  }

  function drawCalloutBanner(page, text, pageNum) {
    const bannerX = 730;
    const bannerW = W - bannerX;

    // Background banner
    page.drawRectangle({
      x: bannerX,
      y: 0,
      width: bannerW,
      height: H,
      color: cBurgundy
    });

    // Callout triangle pointer pointing left
    const triY = 380;
    for (let i = 0; i < 35; i++) {
      const stepW = (35 - i);
      page.drawRectangle({
        x: bannerX - stepW,
        y: triY - i,
        width: stepW,
        height: 1,
        color: cBurgundy
      });
      page.drawRectangle({
        x: bannerX - stepW,
        y: triY + i,
        width: stepW,
        height: 1,
        color: cBurgundy
      });
    }

    // Top Right Icon badge
    page.drawCircle({
      x: W - 45,
      y: H - 45,
      size: 18,
      color: cBurgundyLight,
      borderColor: cWhite,
      borderWidth: 1.5
    });

    // Page instructional text in white
    drawWrappedText(page, text, bannerX + 24, H - 95, bannerW - 48, 11, fontRegular, cWhite, 1.45);

    // Page number at bottom right
    safeDrawText(page, String(pageNum), {
      x: W - 35,
      y: 25,
      size: 11,
      font: fontRegular,
      color: rgb(0.85, 0.70, 0.78)
    });
  }

  // ==========================================
  // SLIDE 1 & 2: Cover & Table of Contents
  // ==========================================
  for (let slideIdx = 1; slideIdx <= 2; slideIdx++) {
    const p = pdfDoc.addPage([W, H]);

    // Left Burgundy Column
    p.drawRectangle({ x: 0, y: 0, width: 380, height: H, color: cBurgundy });

    // Logo INPS
    safeDrawText(p, 'INPS', { x: 40, y: H - 110, size: 56, font: fontBold, color: cWhite });

    // Subtitle
    safeDrawText(p, "Guida all'uso dei Servizi NASpI", { x: 40, y: H - 165, size: 18, font: fontBold, color: cWhite });
    safeDrawText(p, 'per Postazioni Self Service', { x: 40, y: H - 195, size: 18, font: fontBold, color: cWhite });

    // Bottom INPS Emblem
    p.drawCircle({ x: 70, y: 65, size: 28, color: cWhite });
    safeDrawText(p, 'INPS', { x: 52, y: 58, size: 14, font: fontBold, color: cBlueHeader });

    // Decorative illustration graphics on left
    p.drawCircle({ x: 300, y: 280, size: 35, color: rgb(0.95, 0.60, 0.30) });
    p.drawCircle({ x: 320, y: 150, size: 30, color: rgb(0.95, 0.85, 0.75) });

    // Right Column: Services List
    p.drawRectangle({ x: 380, y: 0, width: W - 380, height: H, color: cWhite });

    const services = [
      "Nuova Assicurazione Sociale per l'Impiego\n(NASpI): invio domanda",
      "Nuova Assicurazione Sociale per l'Impiego\n(NASpI): consultazione domande",
      "NASpI anticipata: invio domanda",
      "NASpI anticipata: consultazione domande",
      "NASpI-Com: invio comunicazione",
      "NASpI-Com: consultazione comunicazione"
    ];

    services.forEach((srv, i) => {
      const rowY = H - 85 - i * 72;

      // Burgundy Circle with screen icon
      p.drawCircle({ x: 435, y: rowY + 12, size: 22, color: cBurgundy });
      p.drawRectangle({ x: 423, y: rowY + 5, width: 24, height: 15, color: cWhite });
      p.drawRectangle({ x: 426, y: rowY + 8, width: 18, height: 9, color: cBurgundy });
      p.drawRectangle({ x: 421, y: rowY + 2, width: 28, height: 3, color: cWhite });

      // Service text
      const lines = srv.split('\n');
      if (lines.length === 2) {
        safeDrawText(p, lines[0], { x: 475, y: rowY + 14, size: 12.5, font: fontRegular, color: cCharcoal });
        safeDrawText(p, lines[1], { x: 475, y: rowY - 3, size: 12.5, font: fontRegular, color: cCharcoal });
      } else {
        safeDrawText(p, srv, { x: 475, y: rowY + 6, size: 13, font: fontRegular, color: cCharcoal });
      }
    });
  }

  // ==========================================
  // SLIDE 3: Module Divider
  // ==========================================
  {
    const p = pdfDoc.addPage([W, H]);
    p.drawRectangle({ x: 0, y: 0, width: 310, height: H, color: cWhite });
    p.drawRectangle({ x: 310, y: 0, width: W - 310, height: H, color: cBurgundy });

    // Big central badge
    p.drawCircle({ x: 310, y: H / 2, size: 120, color: rgb(0.92, 0.92, 0.92) });
    p.drawCircle({ x: 310, y: H / 2, size: 110, color: cWhite });

    // Burgundy Laptop Icon
    p.drawRectangle({ x: 245, y: H / 2 - 25, width: 130, height: 80, color: cBurgundy });
    p.drawRectangle({ x: 260, y: H / 2 - 10, width: 100, height: 50, color: cWhite });
    p.drawRectangle({ x: 235, y: H / 2 - 40, width: 150, height: 15, color: cBurgundy });

    // Text on right
    safeDrawText(p, 'Nuova Assicurazione Sociale per', { x: 470, y: H / 2 + 35, size: 21, font: fontRegular, color: cWhite });
    safeDrawText(p, "l'Impiego (NASpI):", { x: 470, y: H / 2 + 5, size: 21, font: fontRegular, color: cWhite });
    safeDrawText(p, 'invio domanda', { x: 470, y: H / 2 - 25, size: 21, font: fontBold, color: cWhite });
  }

  // ==========================================
  // SLIDE 4: General Info 1
  // ==========================================
  {
    const p = pdfDoc.addPage([W, H]);
    drawHeaderBar(p);

    // Left Column: QUANDO UTILIZZARE IL SERVIZIO
    p.drawRectangle({ x: 40, y: H - 95, width: 330, height: 26, color: cBurgundy });
    safeDrawText(p, 'QUANDO UTILIZZARE IL SERVIZIO', { x: 50, y: H - 87, size: 10.5, font: fontBold, color: cWhite });

    const qText = "I lavoratori con rapporto di lavoro subordinato che hanno perduto involontariamente l'occupazione, (compresi gli apprendisti, i soci lavoratori di cooperative con rapporto di lavoro subordinato con le medesime cooperative, il personale artistico con rapporto di lavoro subordinato e i dipendenti a tempo determinato delle pubbliche amministrazioni) possono utilizzare questo servizio per inviare la domanda di NASpI.\n\nPer ulteriori informazioni sulla Prestazione consulta nel sito la scheda NASpI: indennità mensile di disoccupazione (per lavoratori subordinati con rapporto di lavoro cessato involontariamente dal 1 maggio 2015).";
    drawWrappedText(p, qText, 40, H - 120, 390, 10.5, fontRegular, cCharcoal, 1.45);

    // Right Column: DESCRIZIONE
    p.drawRectangle({ x: 500, y: H - 95, width: 330, height: 26, color: cBurgundy });
    safeDrawText(p, 'DESCRIZIONE', { x: 510, y: H - 87, size: 10.5, font: fontBold, color: cWhite });

    const dText = "Il servizio consente al beneficiario di NASpI di compilare e inviare la domanda con le informazioni e la documentazione relative all'ultimo rapporto di lavoro e alle modalità di pagamento degli importi spettanti.\n\nAl termine della procedura viene fornita una ricevuta con il numero di protocollo della domanda, che l'utente potrà utilizzare per seguirne l'iter tramite il servizio di Consultazione domande, per inviare la domanda di NASpI anticipata e per eventuali comunicazioni inerenti le variazioni della propria condizione che possono incidere sulla fruizione dell'indennità di NASpI.";
    drawWrappedText(p, dText, 500, H - 120, 400, 10, fontRegular, cCharcoal, 1.4);

    // A CHI È DEDICATO
    p.drawRectangle({ x: 500, y: H - 340, width: 330, height: 24, color: cBurgundy });
    safeDrawText(p, 'A CHI E DEDICATO', { x: 510, y: H - 333, size: 10, font: fontBold, color: cWhite });
    safeDrawText(p, 'Disoccupati', { x: 500, y: H - 362, size: 10.5, font: fontRegular, color: cCharcoal });

    // COME ACCEDERE
    p.drawRectangle({ x: 500, y: H - 405, width: 330, height: 24, color: cBurgundy });
    safeDrawText(p, 'COME ACCEDERE', { x: 510, y: H - 398, size: 10, font: fontBold, color: cWhite });
    safeDrawText(p, 'Codice Fiscale - SPID o PIN dispositivo o CNS', { x: 500, y: H - 425, size: 10.5, font: fontRegular, color: cCharcoal });

    safeDrawText(p, '4', { x: W - 40, y: 25, size: 10, font: fontRegular, color: cTextMuted });
  }

  // ==========================================
  // SLIDE 5: General Info 2
  // ==========================================
  {
    const p = pdfDoc.addPage([W, H]);
    drawHeaderBar(p);

    // Left Column: DOCUMENTAZIONE
    p.drawRectangle({ x: 40, y: H - 95, width: 330, height: 26, color: cBurgundy });
    safeDrawText(p, 'DOCUMENTAZIONE', { x: 50, y: H - 87, size: 10.5, font: fontBold, color: cWhite });

    const docText = "Prima di inserire la domanda online, ti raccomandiamo di procurarti la documentazione relativa al tuo ultimo rapporto di lavoro dalla quale ricavare i dati richiesti. Inoltre, se intendi chiedere l'accredito degli importi spettanti della NASpI sul tuo conto corrente bancario, ti ricordiamo di scaricare dal sito e compilare il Modulo SR163, acquisendo il visto della tua banca, da digitalizzare e caricare nella procedura, come attestazione della correttezza dell'IBAN indicato nella domanda.";
    drawWrappedText(p, docText, 40, H - 120, 390, 10, fontRegular, cCharcoal, 1.45);

    // SERVIZI DI NOTIFICA
    p.drawRectangle({ x: 40, y: H - 280, width: 330, height: 26, color: cBurgundy });
    safeDrawText(p, 'SERVIZI DI NOTIFICA SU ESITO E PAGAMENTI', { x: 50, y: H - 272, size: 9.5, font: fontBold, color: cWhite });

    const notifText = "Una volta inviata la domanda, puoi monitorarne lo stato con il servizio NASpI Anticipata: consultazione domande.\nAl termine della lavorazione della domanda la Sede INPS ti invia la comunicazione di esito tramite POSTEL e ti avverte con un SMS al cellulare che hai fornito nella domanda. Il messaggio ti segnala la presenza di un Avviso nella tua area MY INPS, accessibile con il tuo codice fiscale e credenziali SPID, PIN o CNS (per ulteriori informazioni su come utilizzare l'Area My INPS e come acquisire e gestire il PIN, vai al Menu Assistenza del portale INPS).\nTale Avviso contiene il link al servizio online Cassetta postale online all'interno del quale resta...";
    drawWrappedText(p, notifText, 40, H - 305, 390, 9.5, fontRegular, cCharcoal, 1.4);

    // Right Column: Continuation & N.B.
    const contText = "...disponibile il pdf della comunicazione di esito inviata via POSTEL. Con analogo sistema di notifica (SMS+Avviso in area MY INPS) sarai informato dell'accredito del primo pagamento.";
    drawWrappedText(p, contText, 500, H - 95, 400, 10, fontRegular, cCharcoal, 1.45);

    safeDrawText(p, 'N.B.', { x: 500, y: H - 170, size: 10.5, font: fontBold, color: cCharcoal });
    const nbText = "1) Le domande di indennità di disoccupazione NASpI sono assegnate per la lavorazione, in via generale, alle Sedi territoriali INPS sulla base della residenza ufficiale del richiedente e non più in base all'eventuale diverso domicilio indicato dal medesimo. Pertanto, la procedura di presentazione online delle domande di NASpI ti propone automaticamente, come indirizzo di residenza, l'indirizzo più affidabile prelevato dall'archivio anagrafico INPS, ovvero l'indirizzo censito nel sistema attraverso il collegamento telematico con le anagrafi comunali e l'anagrafe tributaria. Di conseguenza, se dopo aver effettuato l'accesso al servizio di invio domanda con le tue credenziali, riscontri nella sezione dei Dati Anagrafici della domanda di NASpI, che l'indirizzo di residenza proposto...";
    drawWrappedText(p, nbText, 500, H - 195, 400, 9.8, fontRegular, cCharcoal, 1.4);

    safeDrawText(p, '5', { x: W - 40, y: 25, size: 10, font: fontRegular, color: cTextMuted });
  }

  // ==========================================
  // SLIDE 6: General Info 3
  // ==========================================
  {
    const p = pdfDoc.addPage([W, H]);
    drawHeaderBar(p);

    const s6Left = "...non è corretto, sei invitato con uno specifico avviso ad aggiornare l'indirizzo di residenza accedendo al servizio apposito, presente nella sezione anagrafica del profilo utente in \"MyINPS\". La nuova residenza da te inserita viene comunque sottoposta a verifica di congruenza con quanto presente nelle anagrafi comunali e anagrafe tributaria e accettata dalla procedura solo in caso di esito positivo.\nTale verifica è asincrona e potrebbe richiedere qualche minuto.\nNel caso in cui la variazione non vada a buon fine, la domanda di NASpI è indirizzata alla Struttura territoriale competente sulla base del vecchio indirizzo di residenza, registrato negli archivi INPS. Quindi, se a seguito di un tentativo di modifica della residenza non andato a buon fine, volessi recarti presso la tua Struttura territoriale di riferimento per chiedere la medesima modifica, tieni presente che sarai invitato a rivolgerti agli uffici competenti del Comune nel quale risiedi.";
    drawWrappedText(p, s6Left, 40, H - 85, 420, 10, fontRegular, cCharcoal, 1.45);

    const s6Right = "2) Tieni presente che, se sei stato licenziato senza preavviso e nella domanda ometti il riferimento all'indennità di mancato preavviso da te percepita, ciò comporterà un ricalcolo dell'indennità NASpI con un ritardo nei tempi di erogazione.\nIn questo caso potrebbe essere necessario allegare alla domanda anche la lettera di licenziamento dalla quale si possa desumere se hai percepito questa indennità per velocizzare la lavorazione della domanda.";
    drawWrappedText(p, s6Right, 500, H - 85, 400, 10, fontRegular, cCharcoal, 1.45);

    safeDrawText(p, '6', { x: W - 40, y: 25, size: 10, font: fontRegular, color: cTextMuted });
  }

  // ==========================================
  // HELPER: Base Portal Window for slides 7 to 22
  // ==========================================
  function createPortalSlide(pageNum, calloutText, renderContent) {
    const p = pdfDoc.addPage([W, H]);
    p.drawRectangle({ x: 0, y: 0, width: W, height: H, color: cBgGray });

    // Portal Window Container (x: 20, y: 20, width: 680, height: 500)
    const wx = 20;
    const wy = 20;
    const ww = 680;
    const wh = 500;

    p.drawRectangle({ x: wx, y: wy, width: ww, height: wh, color: cWhite, borderColor: cBorder, borderWidth: 1 });

    // Portal Top Bar (Blue)
    p.drawRectangle({ x: wx, y: wy + wh - 30, width: ww, height: 30, color: cBlueLight });
    p.drawLine({ start: { x: wx, y: wy + wh - 30 }, end: { x: wx + ww, y: wy + wh - 30 }, thickness: 1, color: cBorder });
    safeDrawText(p, 'Invio domanda di NASpI', { x: wx + 15, y: wy + wh - 20, size: 11, font: fontBold, color: cBlueHeader });
    safeDrawText(p, 'Scarica il Manuale Utente', { x: wx + ww - 145, y: wy + wh - 20, size: 9, font: fontRegular, color: cRed });

    renderContent(p, wx, wy, ww, wh);

    drawCalloutBanner(p, calloutText, pageNum);
    return p;
  }

  // ==========================================
  // SLIDE 7: Dati Anagrafici e Residenza
  // ==========================================
  createPortalSlide(
    7,
    "Quando il datore di lavoro comunica la cessazione del rapporto, trovi precompilati oltre ai dati anagrafici, anche i dati del rapporto di lavoro cessato.\n\nVerifica e conferma l'indirizzo di residenza e, se occorre, spunta il NO qui e aggiungi un recapito postale diverso nella maschera successiva",
    (p, wx, wy, ww, wh) => {
      // Dati Anagrafici box
      p.drawRectangle({ x: wx + 15, y: wy + 320, width: ww - 30, height: 140, color: cWhite, borderColor: cBorder, borderWidth: 1 });
      p.drawRectangle({ x: wx + 15, y: wy + 430, width: ww - 30, height: 30, color: cBlueHeader });
      safeDrawText(p, 'Dati Anagrafici', { x: wx + 25, y: wy + 440, size: 10.5, font: fontBold, color: cWhite });

      // Fields
      safeDrawText(p, 'Cognome: ROS*****', { x: wx + 25, y: wy + 405, size: 9, font: fontRegular, color: cCharcoal });
      safeDrawText(p, 'Nome: MAR*****', { x: wx + 280, y: wy + 405, size: 9, font: fontRegular, color: cCharcoal });
      safeDrawText(p, 'Codice Fiscale: RSS*************', { x: wx + 25, y: wy + 380, size: 9, font: fontRegular, color: cCharcoal });
      safeDrawText(p, 'Data di Nascita: 01/********', { x: wx + 280, y: wy + 380, size: 9, font: fontRegular, color: cCharcoal });
      safeDrawText(p, 'Comune di Nascita: ROM******', { x: wx + 25, y: wy + 355, size: 9, font: fontRegular, color: cCharcoal });
      safeDrawText(p, 'Cittadinanza: ITALIANA', { x: wx + 280, y: wy + 355, size: 9, font: fontRegular, color: cCharcoal });
      safeDrawText(p, 'Stato Civile: Celibe / Nubile', { x: wx + 25, y: wy + 330, size: 9, font: fontRegular, color: cCharcoal });

      // Dettaglio Indirizzo
      p.drawRectangle({ x: wx + 15, y: wy + 200, width: ww - 30, height: 105, color: cWhite, borderColor: cBorder, borderWidth: 1 });
      p.drawRectangle({ x: wx + 15, y: wy + 280, width: ww - 30, height: 25, color: cBlueHeader });
      safeDrawText(p, 'Dettaglio Indirizzo di Residenza', { x: wx + 25, y: wy + 288, size: 10, font: fontBold, color: cWhite });

      safeDrawText(p, 'Indirizzo: VIA ********************************', { x: wx + 25, y: wy + 255, size: 9, font: fontRegular, color: cCharcoal });
      safeDrawText(p, 'Comune: ROMA (RM)   CAP: 001**   Stato: ITALIA', { x: wx + 25, y: wy + 230, size: 9, font: fontRegular, color: cCharcoal });
      safeDrawText(p, 'Conferma che questo e il suo attuale indirizzo di residenza?   (X) SI    ( ) NO', { x: wx + 25, y: wy + 208, size: 9, font: fontBold, color: cCharcoal });

      // Avviso giallo
      p.drawRectangle({ x: wx + 15, y: wy + 80, width: ww - 30, height: 105, color: cYellowAlert, borderColor: cYellowBorder, borderWidth: 1 });
      const warn = "Se non si conferma l'indirizzo proposto, lo si deve aggiornare dalla voce Anagrafica del proprio profilo utente MyInps. La compilazione della domanda di Naspi on line puo comunque essere portata a termine. Appena conclusa la procedura di aggiornamento della residenza, i dati corretti saranno riportati automaticamente sulla domanda.";
      drawWrappedText(p, warn, wx + 25, wy + 165, ww - 50, 8.5, fontRegular, cCharcoal, 1.4);

      // Button AVANTI
      p.drawRectangle({ x: wx + ww - 110, y: wy + 25, width: 95, height: 30, color: cButtonBlue });
      safeDrawText(p, 'AVANTI >', { x: wx + ww - 95, y: wy + 35, size: 9.5, font: fontBold, color: cWhite });
    }
  );

  // ==========================================
  // SLIDE 8: Dati Domicilio
  // ==========================================
  createPortalSlide(
    8,
    "Inserisci qui, se diverso dall'indirizzo di residenza, il domicilio (utile per l'individuazione del Centro per l'Impiego)\n\nClicca su CONTINUA",
    (p, wx, wy, ww, wh) => {
      p.drawRectangle({ x: wx + 15, y: wy + 180, width: ww - 30, height: 260, color: cWhite, borderColor: cBorder, borderWidth: 1 });
      p.drawRectangle({ x: wx + 15, y: wy + 410, width: ww - 30, height: 30, color: cBlueHeader });
      safeDrawText(p, 'Inserimento Dati Domicilio', { x: wx + 25, y: wy + 420, size: 10.5, font: fontBold, color: cWhite });

      const dFields = [
        ['Indirizzo *', 'Via ********************'],
        ['Numero Civico *', '45/*****'],
        ['Provincia *', 'MIL*** (MI)'],
        ['Comune *', 'MIL***'],
        ['Cap *', '201**']
      ];

      dFields.forEach((f, i) => {
        const fy = wy + 370 - i * 40;
        safeDrawText(p, f[0], { x: wx + 30, y: fy + 5, size: 9.5, font: fontBold, color: cCharcoal });
        p.drawRectangle({ x: wx + 180, y: fy - 4, width: 380, height: 24, color: cWhite, borderColor: cBorder, borderWidth: 1 });
        safeDrawText(p, f[1], { x: wx + 190, y: fy + 3, size: 9, font: fontRegular, color: cCharcoal });
      });

      safeDrawText(p, 'I campi contrassegnati con * sono obbligatori', { x: wx + 30, y: wy + 140, size: 9, font: fontOblique, color: cTextMuted });

      // Buttons
      p.drawRectangle({ x: wx + 25, y: wy + 30, width: 90, height: 30, color: cButtonBlue });
      safeDrawText(p, '< INDIETRO', { x: wx + 35, y: wy + 40, size: 9, font: fontBold, color: cWhite });

      p.drawRectangle({ x: wx + 125, y: wy + 30, width: 90, height: 30, color: cRed });
      safeDrawText(p, 'ANNULLA', { x: wx + 142, y: wy + 40, size: 9, font: fontBold, color: cWhite });

      p.drawRectangle({ x: wx + ww - 125, y: wy + 30, width: 110, height: 30, color: cButtonBlue });
      safeDrawText(p, 'CONTINUA >', { x: wx + ww - 110, y: wy + 40, size: 9.5, font: fontBold, color: cWhite });
    }
  );

  // ==========================================
  // SLIDE 9: Ultima Posizione Lavorativa
  // ==========================================
  createPortalSlide(
    9,
    "Verifica quindi i dati dell'ultimo datore di lavoro presso il quale hai lavorato e l'inizio e fine del rapporto di lavoro\n\nConferma cliccando su AVANTI per procedere",
    (p, wx, wy, ww, wh) => {
      p.drawRectangle({ x: wx + 15, y: wy + 200, width: ww - 30, height: 240, color: cWhite, borderColor: cBorder, borderWidth: 1 });
      p.drawRectangle({ x: wx + 15, y: wy + 410, width: ww - 30, height: 30, color: cBlueHeader });
      safeDrawText(p, 'Ultima Posizione Lavorativa', { x: wx + 25, y: wy + 420, size: 10.5, font: fontBold, color: cWhite });

      const wRows = [
        ['Data inizio rapporto lavorativo', '01/********'],
        ['Data fine rapporto lavorativo', '31/********'],
        ['Matricola azienda / C.F. datore', '012******** / 987********'],
        ['Ragione sociale azienda / datore', 'AZI*****************************'],
        ['Motivo cessazione', 'LICENZIAMENTO PER GIUSTIFICATO MOTIVO OGGETTIVO'],
        ['Tipo di rapporto', 'A TEMPO INDETERMINATO']
      ];

      wRows.forEach((r, i) => {
        const ry = wy + 375 - i * 28;
        safeDrawText(p, r[0], { x: wx + 30, y: ry, size: 9, font: fontBold, color: cCharcoal });
        safeDrawText(p, r[1], { x: wx + 250, y: ry, size: 9, font: fontRegular, color: cCharcoal });
      });

      safeDrawText(p, 'I dati visualizzati sono corretti e completi?   (X) SI    ( ) NO', { x: wx + 30, y: wy + 150, size: 9.5, font: fontBold, color: cCharcoal });

      p.drawRectangle({ x: wx + 25, y: wy + 30, width: 90, height: 30, color: cButtonBlue });
      safeDrawText(p, '< INDIETRO', { x: wx + 35, y: wy + 40, size: 9, font: fontBold, color: cWhite });

      p.drawRectangle({ x: wx + ww - 110, y: wy + 30, width: 95, height: 30, color: cButtonBlue });
      safeDrawText(p, 'AVANTI >', { x: wx + ww - 95, y: wy + 40, size: 9.5, font: fontBold, color: cWhite });
    }
  );

  // ==========================================
  // SLIDE 10: Modifica Dati Datore di Lavoro
  // ==========================================
  createPortalSlide(
    10,
    "Se i dati non sono corretti, spunta NO sotto alla tabella e inserisci manualmente i dati dell'ultimo datore di lavoro e rapporto di lavoro\n\nProcedi cliccando sul bottone AVANTI",
    (p, wx, wy, ww, wh) => {
      p.drawRectangle({ x: wx + 15, y: wy + 100, width: ww - 30, height: 340, color: cWhite, borderColor: cBorder, borderWidth: 1 });
      p.drawRectangle({ x: wx + 15, y: wy + 410, width: ww - 30, height: 30, color: cBlueHeader });
      safeDrawText(p, "Modifica dei Dati dell'Ultimo Rapporto Lavorativo", { x: wx + 25, y: wy + 420, size: 10.5, font: fontBold, color: cWhite });

      safeDrawText(p, 'I dati visualizzati sono corretti e completi?   ( ) SI    (X) NO', { x: wx + 30, y: wy + 380, size: 9, font: fontBold, color: cCharcoal });
      safeDrawText(p, 'Datore di lavoro estero:   ( ) SI    (X) NO', { x: wx + 30, y: wy + 355, size: 9, font: fontBold, color: cCharcoal });

      const eRows = [
        ['Matricola Azienda *', '012********'],
        ['CF Azienda *', '123********'],
        ['Ragione sociale azienda *', 'TEC****************************'],
        ['Data inizio rapporto *', '01/********'],
        ['Data fine rapporto *', '31/********'],
        ['Motivo Cessazione *', 'Licenziamento per giustificato motivo oggettivo'],
        ['Tipo di rapporto *', 'Tempo Indeterminato']
      ];

      eRows.forEach((r, i) => {
        const ry = wy + 320 - i * 30;
        safeDrawText(p, r[0], { x: wx + 30, y: ry + 3, size: 8.5, font: fontBold, color: cCharcoal });
        p.drawRectangle({ x: wx + 200, y: ry - 4, width: 360, height: 20, color: cWhite, borderColor: cBorder, borderWidth: 1 });
        safeDrawText(p, r[1], { x: wx + 210, y: ry + 2, size: 8.5, font: fontRegular, color: cCharcoal });
      });

      p.drawRectangle({ x: wx + 25, y: wy + 25, width: 90, height: 30, color: cButtonBlue });
      safeDrawText(p, '< INDIETRO', { x: wx + 35, y: wy + 35, size: 9, font: fontBold, color: cWhite });

      p.drawRectangle({ x: wx + ww - 110, y: wy + 25, width: 95, height: 30, color: cButtonBlue });
      safeDrawText(p, 'AVANTI >', { x: wx + ww - 95, y: wy + 35, size: 9.5, font: fontBold, color: cWhite });
    }
  );

  // ==========================================
  // SLIDE 11: Contributi Utili
  // ==========================================
  createPortalSlide(
    11,
    'In questa maschera trovi il riepilogo dei mesi di contribuzione relativa al tuo lavoro degli ultimi quattro anni, utile per la NASpI\n\nPer procedere utilizza il bottone COMPILA DOMANDA',
    (p, wx, wy, ww, wh) => {
      p.drawRectangle({ x: wx + 15, y: wy + 160, width: ww - 30, height: 280, color: cWhite, borderColor: cBorder, borderWidth: 1 });
      p.drawRectangle({ x: wx + 15, y: wy + 410, width: ww - 30, height: 30, color: cBlueHeader });
      safeDrawText(p, 'Contributi utili (ultimi 4 anni)', { x: wx + 25, y: wy + 420, size: 10.5, font: fontBold, color: cWhite });

      // Table header
      p.drawRectangle({ x: wx + 25, y: wy + 360, width: ww - 50, height: 25, color: cBlueLight });
      safeDrawText(p, 'Periodo Dal', { x: wx + 35, y: wy + 368, size: 8.5, font: fontBold, color: cCharcoal });
      safeDrawText(p, 'Periodo Al', { x: wx + 130, y: wy + 368, size: 8.5, font: fontBold, color: cCharcoal });
      safeDrawText(p, 'Mesi Assicurativi', { x: wx + 230, y: wy + 368, size: 8.5, font: fontBold, color: cCharcoal });
      safeDrawText(p, 'Utili Disoccupazione', { x: wx + 350, y: wy + 368, size: 8.5, font: fontBold, color: cCharcoal });
      safeDrawText(p, 'Retribuzione Periodo', { x: wx + 480, y: wy + 368, size: 8.5, font: fontBold, color: cCharcoal });

      const cRows = [
        ['01/02/2022', '31/12/2022', '11 Mesi', '11 Mesi', 'EUR 18.250,00'],
        ['01/01/2023', '31/12/2023', '12 Mesi', '12 Mesi', 'EUR 22.400,00'],
        ['01/01/2024', '31/12/2024', '12 Mesi', '12 Mesi', 'EUR 23.100,00'],
        ['01/01/2025', '31/10/2025', '10 Mesi', '10 Mesi', 'EUR 19.800,00']
      ];

      cRows.forEach((r, i) => {
        const ry = wy + 330 - i * 28;
        p.drawLine({ start: { x: wx + 25, y: ry - 4 }, end: { x: wx + ww - 25, y: ry - 4 }, thickness: 0.5, color: cBorder });
        safeDrawText(p, r[0], { x: wx + 35, y: ry, size: 8.5, font: fontRegular, color: cCharcoal });
        safeDrawText(p, r[1], { x: wx + 130, y: ry, size: 8.5, font: fontRegular, color: cCharcoal });
        safeDrawText(p, r[2], { x: wx + 240, y: ry, size: 8.5, font: fontRegular, color: cCharcoal });
        safeDrawText(p, r[3], { x: wx + 360, y: ry, size: 8.5, font: fontRegular, color: cCharcoal });
        safeDrawText(p, r[4], { x: wx + 490, y: ry, size: 8.5, font: fontRegular, color: cCharcoal });
      });

      safeDrawText(p, 'Questo estratto ha valore puramente informativo e non certificativo.', { x: wx + 25, y: wy + 190, size: 8, font: fontOblique, color: cTextMuted });

      p.drawRectangle({ x: wx + 25, y: wy + 30, width: 90, height: 30, color: cButtonBlue });
      safeDrawText(p, '< INDIETRO', { x: wx + 35, y: wy + 40, size: 9, font: fontBold, color: cWhite });

      p.drawRectangle({ x: wx + ww - 165, y: wy + 30, width: 150, height: 30, color: cButtonBlue });
      safeDrawText(p, 'COMPILA DOMANDA >', { x: wx + ww - 155, y: wy + 40, size: 9, font: fontBold, color: cWhite });
    }
  );

  // ==========================================
  // SLIDE 12: Domanda 1/6
  // ==========================================
  createPortalSlide(
    12,
    'Inserisci le informazioni obbligatorie richieste nei campi contrassegnati con * e almeno uno dei campi indicati con il doppio * col numero di telefono o cellulare\n\nPer procedere utilizza il pulsante AVANTI',
    (p, wx, wy, ww, wh) => {
      p.drawRectangle({ x: wx + 15, y: wy + 120, width: ww - 30, height: 320, color: cWhite, borderColor: cBorder, borderWidth: 1 });
      p.drawRectangle({ x: wx + 15, y: wy + 410, width: ww - 30, height: 30, color: cBlueHeader });
      safeDrawText(p, 'Domanda di indennita di NASpI - 1/6', { x: wx + 25, y: wy + 420, size: 10.5, font: fontBold, color: cWhite });

      const d1 = [
        ['Sede INPS di competenza *', 'SED****************************'],
        ['Telefono / Prefisso **', '061********'],
        ['Cellulare per notifiche SMS **', '333********'],
        ['Indirizzo E-mail ordinario *', 'mar*****************'],
        ['Titolo di Studio *', 'Diploma di Istruzione Secondaria Superiore'],
        ['Qualifica / Categoria *', 'Impiegato Tecnico - Livello 3 CCNL Commercio']
      ];

      d1.forEach((f, i) => {
        const fy = wy + 370 - i * 38;
        safeDrawText(p, f[0], { x: wx + 30, y: fy + 3, size: 8.5, font: fontBold, color: cCharcoal });
        p.drawRectangle({ x: wx + 220, y: fy - 4, width: 340, height: 22, color: cWhite, borderColor: cBorder, borderWidth: 1 });
        safeDrawText(p, f[1], { x: wx + 230, y: fy + 3, size: 8.5, font: fontRegular, color: cCharcoal });
      });

      p.drawRectangle({ x: wx + 15, y: wy + 80, width: ww - 30, height: 32, color: cYellowAlert, borderColor: cYellowBorder, borderWidth: 1 });
      safeDrawText(p, "L'indirizzo e-mail e/o cellulare saranno utilizzati per comunicazioni e SMS sullo stato di avanzamento.", { x: wx + 25, y: wy + 90, size: 8, font: fontRegular, color: cCharcoal });

      p.drawRectangle({ x: wx + 25, y: wy + 25, width: 90, height: 30, color: cButtonBlue });
      safeDrawText(p, '< INDIETRO', { x: wx + 35, y: wy + 35, size: 9, font: fontBold, color: cWhite });

      p.drawRectangle({ x: wx + ww - 110, y: wy + 25, width: 95, height: 30, color: cButtonBlue });
      safeDrawText(p, 'AVANTI >', { x: wx + ww - 95, y: wy + 35, size: 9.5, font: fontBold, color: cWhite });
    }
  );

  // ==========================================
  // SLIDE 13: Domanda 2/6
  // ==========================================
  createPortalSlide(
    13,
    'Per compilare la domanda spunta No oppure Si in risposta alle domande elencate nella maschera\n\nProcedi utilizzando il pulsante AVANTI',
    (p, wx, wy, ww, wh) => {
      p.drawRectangle({ x: wx + 15, y: wy + 100, width: ww - 30, height: 340, color: cWhite, borderColor: cBorder, borderWidth: 1 });
      p.drawRectangle({ x: wx + 15, y: wy + 410, width: ww - 30, height: 30, color: cBlueHeader });
      safeDrawText(p, 'Domanda di indennita di NASpI - 2/6', { x: wx + 25, y: wy + 420, size: 10.5, font: fontBold, color: cWhite });

      const d2Questions = [
        "Ho percepito/percepiro l'indennita di mancato preavviso?",
        'Dichiaro di aver svolto periodi di lavoro all estero?',
        'Dichiaro di essere titolare di assegno ordinario di invalidita?',
        'Sono stato licenziato per superamento del periodo di comporto di malattia?',
        'Sono in malattia o ricoverato alla data della presente domanda?',
        'Sono stato in malattia o ricoverato negli ultimi 12 mesi?'
      ];

      d2Questions.forEach((q, i) => {
        const qy = wy + 370 - i * 44;
        safeDrawText(p, q, { x: wx + 30, y: qy + 10, size: 8.5, font: fontBold, color: cCharcoal });
        safeDrawText(p, '(X) NO    ( ) SI', { x: wx + 30, y: qy - 6, size: 8.5, font: fontRegular, color: cCharcoal });
      });

      p.drawRectangle({ x: wx + 25, y: wy + 25, width: 90, height: 30, color: cButtonBlue });
      safeDrawText(p, '< INDIETRO', { x: wx + 35, y: wy + 35, size: 9, font: fontBold, color: cWhite });

      p.drawRectangle({ x: wx + ww - 110, y: wy + 25, width: 95, height: 30, color: cButtonBlue });
      safeDrawText(p, 'AVANTI >', { x: wx + ww - 95, y: wy + 35, size: 9.5, font: fontBold, color: cWhite });
    }
  );

  // ==========================================
  // SLIDE 14: Domanda 3/6
  // ==========================================
  createPortalSlide(
    14,
    'Se hai lavorato per almeno 30 giornate negli ultimi 12 mesi, spunta la casella in testa. Rispondi per specificare il tipo di attività svolta\n\nSe non hai intrapreso attività imprenditoriale o professionale compila le caselle NO e procedi col pulsante AVANTI.\nIn caso affermativo, dovrai inserire anche le ulteriori informazioni richieste (data inizio attività e reddito)',
    (p, wx, wy, ww, wh) => {
      p.drawRectangle({ x: wx + 15, y: wy + 80, width: ww - 30, height: 360, color: cWhite, borderColor: cBorder, borderWidth: 1 });
      p.drawRectangle({ x: wx + 15, y: wy + 410, width: ww - 30, height: 30, color: cBlueHeader });
      safeDrawText(p, 'Domanda di indennita di NASpI - 3/6', { x: wx + 25, y: wy + 420, size: 10.5, font: fontBold, color: cWhite });

      safeDrawText(p, '[X] Dichiaro di poter far valere 30 giornate di lavoro effettivo nei 12 mesi precedenti', { x: wx + 30, y: wy + 380, size: 8.5, font: fontBold, color: cCharcoal });

      const d3Acts = [
        ['Attivita autonoma', 'NO', '-'],
        ['Attivita parasubordinata (Co.co.co)', 'NO', '-'],
        ['Lavoro occasionale / accessorio', 'NO', '-'],
        ['Attivita subordinata (altra)', 'NO', '-'],
        ['Rivesto carica di socio in societa', 'NO', '-'],
        ['Iscritto ad Albo Professionale / Cassa', 'NO', '-'],
        ['Titolare Partita IVA o iscritto REA', 'NO', '-']
      ];

      d3Acts.forEach((a, i) => {
        const ay = wy + 345 - i * 32;
        safeDrawText(p, a[0], { x: wx + 30, y: ay, size: 8, font: fontRegular, color: cCharcoal });
        safeDrawText(p, '(X) NO   ( ) SI', { x: wx + 280, y: ay, size: 8, font: fontRegular, color: cCharcoal });
      });

      p.drawRectangle({ x: wx + 25, y: wy + 20, width: 90, height: 30, color: cButtonBlue });
      safeDrawText(p, '< INDIETRO', { x: wx + 35, y: wy + 30, size: 9, font: fontBold, color: cWhite });

      p.drawRectangle({ x: wx + ww - 110, y: wy + 20, width: 95, height: 30, color: cButtonBlue });
      safeDrawText(p, 'AVANTI >', { x: wx + ww - 95, y: wy + 30, size: 9.5, font: fontBold, color: cWhite });
    }
  );

  // ==========================================
  // SLIDE 15: Domanda 4/6
  // ==========================================
  createPortalSlide(
    15,
    "Scegli la modalità di accredito degli importi di NASpI.\nSe opti per l'accredito sul tuo conto corrente, a tua tutela, indica le coordinate IBAN e scansiona il Modulo SR163, che avrai già scaricato e compilato, acquisendo il timbro/visto della tua banca",
    (p, wx, wy, ww, wh) => {
      p.drawRectangle({ x: wx + 15, y: wy + 100, width: ww - 30, height: 340, color: cWhite, borderColor: cBorder, borderWidth: 1 });
      p.drawRectangle({ x: wx + 15, y: wy + 410, width: ww - 30, height: 30, color: cBlueHeader });
      safeDrawText(p, 'Domanda di indennita di NASpI - 4/6', { x: wx + 25, y: wy + 420, size: 10.5, font: fontBold, color: cWhite });

      safeDrawText(p, 'Modalita di pagamento:', { x: wx + 30, y: wy + 375, size: 9.5, font: fontBold, color: cCharcoal });
      safeDrawText(p, 'Selezionato: ACCREDITO SU CONTO CORRENTE BANCARIO / POSTALE', { x: wx + 160, y: wy + 375, size: 9, font: fontRegular, color: cCharcoal });

      safeDrawText(p, 'Codice IBAN (27 caratteri):', { x: wx + 30, y: wy + 340, size: 9, font: fontBold, color: cCharcoal });
      p.drawRectangle({ x: wx + 30, y: wy + 305, width: 500, height: 26, color: cWhite, borderColor: cCharcoal, borderWidth: 1 });
      safeDrawText(p, 'IT0************************', { x: wx + 40, y: wy + 313, size: 10, font: fontBold, color: cCharcoal });

      p.drawRectangle({ x: wx + 30, y: wy + 215, width: 500, height: 75, color: cBlueLight, borderColor: cBorder, borderWidth: 0.8 });
      safeDrawText(p, 'Richiesta Detrazioni Fiscali:', { x: wx + 40, y: wy + 265, size: 8.5, font: fontBold, color: cCharcoal });
      safeDrawText(p, '- Tramite tasto Detrazioni fiscali disponibile subito dopo l invio della domanda', { x: wx + 40, y: wy + 245, size: 8, font: fontRegular, color: cCharcoal });
      safeDrawText(p, '- Tramite consultazione delle domande inviate sul portale MyINPS', { x: wx + 40, y: wy + 228, size: 8, font: fontRegular, color: cCharcoal });

      p.drawRectangle({ x: wx + 30, y: wy + 120, width: 500, height: 80, color: cBlueLight, borderColor: cBorder, borderWidth: 0.8 });
      safeDrawText(p, 'Richiesta Assegno per il Nucleo Familiare (ANF):', { x: wx + 40, y: wy + 175, size: 8.5, font: fontBold, color: cCharcoal });
      safeDrawText(p, '- Tramite tasto Assegno al Nucleo Familiare disponibile subito dopo l invio', { x: wx + 40, y: wy + 155, size: 8, font: fontRegular, color: cCharcoal });
      safeDrawText(p, '- Tramite elenco dei servizi online dedicati', { x: wx + 40, y: wy + 138, size: 8, font: fontRegular, color: cCharcoal });

      p.drawRectangle({ x: wx + 25, y: wy + 25, width: 90, height: 30, color: cButtonBlue });
      safeDrawText(p, '< INDIETRO', { x: wx + 35, y: wy + 35, size: 9, font: fontBold, color: cWhite });

      p.drawRectangle({ x: wx + ww - 110, y: wy + 25, width: 95, height: 30, color: cButtonBlue });
      safeDrawText(p, 'AVANTI >', { x: wx + ww - 95, y: wy + 35, size: 9.5, font: fontBold, color: cWhite });
    }
  );

  // ==========================================
  // SLIDE 16: Domanda 5/6
  // ==========================================
  createPortalSlide(
    16,
    'Ora inserisci la tua ultima qualifica, prima di cessare il rapporto di lavoro\n\nProcedi utilizzando il pulsante AVANTI',
    (p, wx, wy, ww, wh) => {
      p.drawRectangle({ x: wx + 15, y: wy + 100, width: ww - 30, height: 340, color: cWhite, borderColor: cBorder, borderWidth: 1 });
      p.drawRectangle({ x: wx + 15, y: wy + 410, width: ww - 30, height: 30, color: cBlueHeader });
      safeDrawText(p, 'Domanda di indennita di NASpI - 5/6', { x: wx + 25, y: wy + 420, size: 10.5, font: fontBold, color: cWhite });

      safeDrawText(p, 'ATTENZIONE: La presente domanda equivale a Dichiarazione di Immediata Disponibilita (DID).', { x: wx + 30, y: wy + 380, size: 8.5, font: fontBold, color: cCharcoal });
      safeDrawText(p, "Ai sensi dell'art. 21 del D.Lgs. n. 150 del 2015 al Centro per l'Impiego vengono trasmessi i dati anagrafici.", { x: wx + 30, y: wy + 360, size: 8, font: fontRegular, color: cCharcoal });

      safeDrawText(p, 'Ove rivestivo la seguente qualifica: *', { x: wx + 30, y: wy + 320, size: 9, font: fontBold, color: cCharcoal });
      p.drawRectangle({ x: wx + 30, y: wy + 285, width: 500, height: 26, color: cWhite, borderColor: rgb(0.95, 0.75, 0.20), borderWidth: 2 });
      safeDrawText(p, 'Impiegato Tecnico - Livello 3 CCNL Commercio', { x: wx + 40, y: wy + 293, size: 9, font: fontRegular, color: cCharcoal });

      safeDrawText(p, '- Dichiaro di essere disoccupato e disponibile alle attivita di politica attiva del lavoro', { x: wx + 30, y: wy + 250, size: 8, font: fontRegular, color: cCharcoal });
      safeDrawText(p, '- Dichiaro di non svolgere attivita lavorativa subordinata o autonoma', { x: wx + 30, y: wy + 230, size: 8, font: fontRegular, color: cCharcoal });

      p.drawRectangle({ x: wx + 25, y: wy + 25, width: 90, height: 30, color: cButtonBlue });
      safeDrawText(p, '< INDIETRO', { x: wx + 35, y: wy + 35, size: 9, font: fontBold, color: cWhite });

      p.drawRectangle({ x: wx + ww - 110, y: wy + 25, width: 95, height: 30, color: cButtonBlue });
      safeDrawText(p, 'AVANTI >', { x: wx + ww - 95, y: wy + 35, size: 9.5, font: fontBold, color: cWhite });
    }
  );

  // ==========================================
  // SLIDE 17: Domanda 6/6
  // ==========================================
  createPortalSlide(
    17,
    "Per completare la domanda, inserisci le spunte a conferma dell' impegno a comunicare qualsiasi eventuale variazione successiva all' invio della domanda e della veridicità delle tue dichiarazioni\n\nClicca AVANTI e visualizza il riepilogo",
    (p, wx, wy, ww, wh) => {
      p.drawRectangle({ x: wx + 15, y: wy + 100, width: ww - 30, height: 340, color: cWhite, borderColor: cBorder, borderWidth: 1 });
      p.drawRectangle({ x: wx + 15, y: wy + 410, width: ww - 30, height: 30, color: cBlueHeader });
      safeDrawText(p, 'Domanda di indennita di NASpI - 6/6', { x: wx + 25, y: wy + 420, size: 10.5, font: fontBold, color: cWhite });

      safeDrawText(p, "Comunicazione all'INPS *", { x: wx + 30, y: wy + 380, size: 9.5, font: fontBold, color: cCharcoal });
      safeDrawText(p, "[X] Mi impegno a comunicare all'INPS:", { x: wx + 30, y: wy + 360, size: 8.5, font: fontBold, color: cCharcoal });
      safeDrawText(p, '- entro 5 giorni la variazione della mia condizione di disoccupato;', { x: wx + 45, y: wy + 342, size: 8, font: fontRegular, color: cCharcoal });
      safeDrawText(p, "- entro 30 giorni l'avvio di un'attivita autonoma o parasubordinata o lavoro occasionale;", { x: wx + 45, y: wy + 326, size: 8, font: fontRegular, color: cCharcoal });
      safeDrawText(p, "- entro 30 giorni l'apertura di un contenzioso relativo al licenziamento;", { x: wx + 45, y: wy + 310, size: 8, font: fontRegular, color: cCharcoal });
      safeDrawText(p, "- l'espatrio verso altro Stato UE, Svizzera o Paese extracomunitario.", { x: wx + 45, y: wy + 294, size: 8, font: fontRegular, color: cCharcoal });

      safeDrawText(p, 'Dichiarazione di veridicita *', { x: wx + 30, y: wy + 260, size: 9.5, font: fontBold, color: cCharcoal });
      safeDrawText(p, '[X] Dichiaro che le notizie da me fornite rispondono a verita e sono consapevole delle conseguenze', { x: wx + 30, y: wy + 240, size: 8.5, font: fontBold, color: cCharcoal });
      safeDrawText(p, 'previste per chi rende dichiarazioni false (art. 48, 73, 75 e 76 D.P.R. 445/2000).', { x: wx + 45, y: wy + 224, size: 8, font: fontRegular, color: cCharcoal });

      p.drawRectangle({ x: wx + 25, y: wy + 25, width: 90, height: 30, color: cButtonBlue });
      safeDrawText(p, '< INDIETRO', { x: wx + 35, y: wy + 35, size: 9, font: fontBold, color: cWhite });

      p.drawRectangle({ x: wx + ww - 110, y: wy + 25, width: 95, height: 30, color: cButtonBlue });
      safeDrawText(p, 'AVANTI >', { x: wx + ww - 95, y: wy + 35, size: 9.5, font: fontBold, color: cWhite });
    }
  );

  // ==========================================
  // SLIDE 18: Riepilogo Dati Inseriti (Accredito C/C)
  // ==========================================
  createPortalSlide(
    18,
    "Visualizza la pagina di riepilogo dati per verificarne la correttezza.\nSe hai scelto l'accredito degli importi sul proprio Conto Corrente, per allegare il modulo SR163 (richiesto per la verifica dell'IBAN), clicca sull'apposito bottone. Se utilizzi il pulsante Salva e Esci, salvi la domanda in bozza e puoi integrarla in seguito\n\nCon il pulsante CONFERMA, la invii direttamente",
    (p, wx, wy, ww, wh) => {
      p.drawRectangle({ x: wx + 15, y: wy + 110, width: ww - 30, height: 330, color: cWhite, borderColor: cBorder, borderWidth: 1 });
      p.drawRectangle({ x: wx + 15, y: wy + 410, width: ww - 30, height: 30, color: cBlueHeader });
      safeDrawText(p, 'Riepilogo dei Dati Inseriti', { x: wx + 25, y: wy + 420, size: 10.5, font: fontBold, color: cWhite });

      const sumLines = [
        "Il/la sig. MAR*** ROS*** (CF: RSS*************) chiede all'INPS l'indennita di NASpI",
        "per cessazione del rapporto avvenuta in data 19/******** presso l'azienda BAN***************** (CF: 001********* )",
        "DICHIARA che l'indirizzo proposto corrisponde a quello di residenza",
        "di non percepire l'indennita di mancato preavviso e di poter far valere 30 giornate",
        "di voler ricevere l'indennita richiesta tramite ACCREDITO SU CONTO CORRENTE (IBAN: IT0************************)"
      ];

      sumLines.forEach((sl, i) => {
        safeDrawText(p, sl, { x: wx + 25, y: wy + 375 - i * 26, size: 8.5, font: fontRegular, color: cCharcoal });
      });

      p.drawRectangle({ x: wx + 25, y: wy + 200, width: 130, height: 30, color: cBlueLight, borderColor: cBlueHeader, borderWidth: 1 });
      safeDrawText(p, '+ ALLEGATI', { x: wx + 50, y: wy + 210, size: 9, font: fontBold, color: cBlueHeader });

      // Action Buttons
      p.drawRectangle({ x: wx + 20, y: wy + 25, width: 85, height: 30, color: cButtonBlue });
      safeDrawText(p, '< INDIETRO', { x: wx + 28, y: wy + 35, size: 8.5, font: fontBold, color: cWhite });

      p.drawRectangle({ x: wx + 115, y: wy + 25, width: 95, height: 30, color: cRed });
      safeDrawText(p, 'ABBANDONA', { x: wx + 128, y: wy + 35, size: 8.5, font: fontBold, color: cWhite });

      p.drawRectangle({ x: wx + 285, y: wy + 25, width: 110, height: 30, color: cButtonBlue });
      safeDrawText(p, 'SALVA E ESCI', { x: wx + 300, y: wy + 35, size: 8.5, font: fontBold, color: cWhite });

      p.drawRectangle({ x: wx + 410, y: wy + 25, width: 110, height: 30, color: cGreen });
      safeDrawText(p, 'CONFERMA', { x: wx + 430, y: wy + 35, size: 9, font: fontBold, color: cWhite });
    }
  );

  // ==========================================
  // SLIDE 19: Riepilogo Dati (Bonifico Domiciliato)
  // ==========================================
  createPortalSlide(
    19,
    "La pagina di riepilogo sarà leggermente diversa se hai scelto di ricevere gli importi con bonifico domiciliato.\nSe hai Allegati da inoltrare, clicca sull'apposito bottone\nPoi Salva e esci e CONFERMA con i bottoni in fondo\n\n(Nella maggior parte dei casi la Sede Inps viene determinata dalla procedura e non è modificabile. Potrai scegliere la Sede Inps solo se al CAP del tuo Comune sono associate due Sedi).",
    (p, wx, wy, ww, wh) => {
      p.drawRectangle({ x: wx + 15, y: wy + 110, width: ww - 30, height: 330, color: cWhite, borderColor: cBorder, borderWidth: 1 });
      p.drawRectangle({ x: wx + 15, y: wy + 410, width: ww - 30, height: 30, color: cBlueHeader });
      safeDrawText(p, 'Riepilogo Dati - Modalita Bonifico Domiciliato', { x: wx + 25, y: wy + 420, size: 10.5, font: fontBold, color: cWhite });

      safeDrawText(p, 'Modalita di riscossione: BONIFICO DOMICILIATO PRESSO POSTE ITALIANE', { x: wx + 25, y: wy + 375, size: 9, font: fontBold, color: cCharcoal });
      safeDrawText(p, 'Sede INPS assegnata dalla procedura: ROM******************** (Codice 7012)', { x: wx + 25, y: wy + 348, size: 8.5, font: fontRegular, color: cCharcoal });

      p.drawRectangle({ x: wx + 25, y: wy + 230, width: ww - 50, height: 95, color: cYellowAlert, borderColor: rgb(0.95, 0.75, 0.20), borderWidth: 1 });
      safeDrawText(p, 'Avviso sulla Sede Territoriale INPS:', { x: wx + 35, y: wy + 300, size: 9, font: fontBold, color: cCharcoal });
      const bInfo = 'Nella maggior parte dei casi la Sede Inps viene determinata dalla procedura e non e modificabile. Potrai scegliere la Sede Inps solo se al CAP del tuo Comune sono associate due Sedi.';
      drawWrappedText(p, bInfo, wx + 35, wy + 280, ww - 70, 8.5, fontRegular, cCharcoal, 1.4);

      p.drawRectangle({ x: wx + 25, y: wy + 160, width: 130, height: 30, color: cBlueLight, borderColor: cBlueHeader, borderWidth: 1 });
      safeDrawText(p, '+ ALLEGATI', { x: wx + 50, y: wy + 170, size: 9, font: fontBold, color: cBlueHeader });

      p.drawRectangle({ x: wx + ww - 130, y: wy + 25, width: 110, height: 30, color: cGreen });
      safeDrawText(p, 'CONFERMA', { x: wx + ww - 105, y: wy + 35, size: 9, font: fontBold, color: cWhite });
    }
  );

  // ==========================================
  // SLIDE 20: Gestione Allegati
  // ==========================================
  createPortalSlide(
    20,
    "Se hai necessità di allegare documenti, Sfoglia le cartelle del tuo computer per selezionare i documenti da allegare e clicca Allega\n\nUsa il pulsante Torna allo sportello per tornare alla maschera di Riepilogo per cliccare su Conferma (vedi pagina 18 o 19)",
    (p, wx, wy, ww, wh) => {
      p.drawRectangle({ x: wx + 15, y: wy + 80, width: ww - 30, height: 360, color: cWhite, borderColor: cBorder, borderWidth: 1 });
      p.drawRectangle({ x: wx + 15, y: wy + 410, width: ww - 30, height: 30, color: cBlueHeader });
      safeDrawText(p, "Nuova assicurazione sociale per l'impiego > Allegati", { x: wx + 25, y: wy + 420, size: 10.5, font: fontBold, color: cWhite });

      p.drawRectangle({ x: wx + 25, y: wy + 260, width: ww - 50, height: 130, color: cBlueLight, borderColor: cBorder, borderWidth: 1 });
      safeDrawText(p, 'Documento da allegare:', { x: wx + 35, y: wy + 365, size: 9, font: fontBold, color: cCharcoal });
      safeDrawText(p, 'Nome file: mod************************.pdf', { x: wx + 35, y: wy + 340, size: 8.5, font: fontRegular, color: cCharcoal });
      p.drawRectangle({ x: wx + 260, y: wy + 330, width: 80, height: 22, color: cWhite, borderColor: cCharcoal, borderWidth: 1 });
      safeDrawText(p, 'Sfoglia...', { x: wx + 280, y: wy + 337, size: 8, font: fontBold, color: cCharcoal });

      safeDrawText(p, 'Descrizione: Modulo SR163 timbrato dalla banca', { x: wx + 35, y: wy + 305, size: 8.5, font: fontRegular, color: cCharcoal });
      p.drawRectangle({ x: wx + 420, y: wy + 295, width: 90, height: 25, color: cButtonBlue });
      safeDrawText(p, '+ ALLEGA', { x: wx + 440, y: wy + 303, size: 8.5, font: fontBold, color: cWhite });

      // Allegati list
      p.drawRectangle({ x: wx + 25, y: wy + 130, width: ww - 50, height: 110, color: cWhite, borderColor: cBorder, borderWidth: 1 });
      safeDrawText(p, 'Lista Allegati alla Domanda:', { x: wx + 35, y: wy + 215, size: 9, font: fontBold, color: cCharcoal });
      safeDrawText(p, '1. mod**********************.pdf (340 KB) - Modulo IBAN validato [OK]', { x: wx + 35, y: wy + 185, size: 8.5, font: fontBold, color: cGreen });
      safeDrawText(p, '2. let***********************.pdf (520 KB) - Atto di recesso [OK]', { x: wx + 35, y: wy + 155, size: 8.5, font: fontBold, color: cGreen });

      p.drawRectangle({ x: wx + 25, y: wy + 25, width: 170, height: 30, color: cButtonBlue });
      safeDrawText(p, '< TORNA ALLO SPORTELLO', { x: wx + 35, y: wy + 35, size: 8.5, font: fontBold, color: cWhite });
    }
  );

  // ==========================================
  // SLIDE 21: Esito Invio e Ricevuta
  // ==========================================
  createPortalSlide(
    21,
    'Cliccando sul pulsante Conferma hai protocollato e inviato la tua domanda.\nA seguito dell’invio con successo, visualizzi questa maschera con i pulsanti per la Stampa Ricevuta e/o Domanda.\nPuoi scaricare e salvare entrambe i pdf sul tuo computer senza consumare carta, cliccando sulla freccina rossa',
    (p, wx, wy, ww, wh) => {
      p.drawRectangle({ x: wx + 15, y: wy + 80, width: ww - 30, height: 360, color: cWhite, borderColor: cBorder, borderWidth: 1 });
      p.drawRectangle({ x: wx + 15, y: wy + 410, width: ww - 30, height: 30, color: cBlueHeader });
      safeDrawText(p, 'Invio domanda di NASpI - Ricevuta di Presentazione', { x: wx + 25, y: wy + 420, size: 10.5, font: fontBold, color: cWhite });

      p.drawRectangle({ x: wx + 25, y: wy + 240, width: ww - 50, height: 140, color: rgb(0.92, 0.98, 0.94), borderColor: cGreen, borderWidth: 1.5 });
      safeDrawText(p, '[OK] Domanda presentata con successo', { x: wx + 40, y: wy + 345, size: 14, font: fontBold, color: cGreen });
      safeDrawText(p, 'Numero Protocollo Ufficiale: INP********************************', { x: wx + 40, y: wy + 315, size: 10.5, font: fontBold, color: cCharcoal });
      safeDrawText(p, 'Gentile signore/a MAR*** ROS*** la sua domanda e stata inviata alla sede di competenza.', { x: wx + 40, y: wy + 285, size: 9, font: fontRegular, color: cCharcoalSec });
      safeDrawText(p, 'Entro breve ricevera una comunicazione di merito.', { x: wx + 40, y: wy + 260, size: 9, font: fontRegular, color: cCharcoalSec });

      p.drawRectangle({ x: wx + 40, y: wy + 150, width: 220, height: 42, color: cButtonBlue });
      safeDrawText(p, 'STAMPA RICEVUTA (PDF)', { x: wx + 58, y: wy + 167, size: 10.5, font: fontBold, color: cWhite });

      p.drawRectangle({ x: wx + 280, y: wy + 150, width: 220, height: 42, color: cWhite, borderColor: cButtonBlue, borderWidth: 1.5 });
      safeDrawText(p, 'STAMPA DOMANDA (PDF)', { x: wx + 298, y: wy + 167, size: 10.5, font: fontBold, color: cButtonBlue });
    }
  );

  // ==========================================
  // SLIDE 22: Salvataggio Bozza
  // ==========================================
  createPortalSlide(
    22,
    'Se nella pagina di riepilogo, dopo aver eventualmente allegato documenti, clicchi sul bottone Salva ed esci (vedi pag. 18 e 19) compare questa maschera con il pulsante di Stampa ricevuta.\nCliccando sulla freccina rossa puoi aprire e salvare sul tuo PC la ricevuta in pdf',
    (p, wx, wy, ww, wh) => {
      p.drawRectangle({ x: wx + 15, y: wy + 80, width: ww - 30, height: 360, color: cWhite, borderColor: cBorder, borderWidth: 1 });
      p.drawRectangle({ x: wx + 15, y: wy + 410, width: ww - 30, height: 30, color: cBlueHeader });
      safeDrawText(p, 'Invio domanda di NASpI - Salvataggio in Bozza', { x: wx + 25, y: wy + 420, size: 10.5, font: fontBold, color: cWhite });

      p.drawRectangle({ x: wx + 25, y: wy + 220, width: ww - 50, height: 160, color: cYellowAlert, borderColor: rgb(0.95, 0.75, 0.20), borderWidth: 1.5 });
      safeDrawText(p, '[!] Domanda presentata - In attesa di protocollazione definitiva', { x: wx + 40, y: wy + 345, size: 12, font: fontBold, color: cCharcoal });
      safeDrawText(p, 'Gentile signore/a MAR*** ROS*** la sua domanda e stata salvata e presa in carico.', { x: wx + 40, y: wy + 315, size: 9, font: fontRegular, color: cCharcoalSec });
      safeDrawText(p, 'E possibile completare o confermare la domanda in un secondo momento.', { x: wx + 40, y: wy + 290, size: 9, font: fontRegular, color: cCharcoalSec });

      p.drawRectangle({ x: wx + 40, y: wy + 130, width: 220, height: 42, color: cButtonBlue });
      safeDrawText(p, 'STAMPA RICEVUTA (PDF)', { x: wx + 58, y: wy + 147, size: 10.5, font: fontBold, color: cWhite });
    }
  );

  // ==========================================
  // SLIDE 23: Closing Slide
  // ==========================================
  {
    const p = pdfDoc.addPage([W, H]);
    p.drawRectangle({ x: 0, y: 0, width: 310, height: H, color: cWhite });
    p.drawRectangle({ x: 310, y: 0, width: W - 310, height: H, color: cBurgundy });

    p.drawCircle({ x: 310, y: H / 2, size: 120, color: rgb(0.92, 0.92, 0.92) });
    p.drawCircle({ x: 310, y: H / 2, size: 110, color: cWhite });

    p.drawRectangle({ x: 245, y: H / 2 - 25, width: 130, height: 80, color: cBurgundy });
    p.drawRectangle({ x: 260, y: H / 2 - 10, width: 100, height: 50, color: cWhite });
    p.drawRectangle({ x: 235, y: H / 2 - 40, width: 150, height: 15, color: cBurgundy });

    safeDrawText(p, 'Nuova Assicurazione Sociale per', { x: 470, y: H / 2 + 35, size: 21, font: fontRegular, color: cWhite });
    safeDrawText(p, "l'Impiego (NASpI):", { x: 470, y: H / 2 + 5, size: 21, font: fontRegular, color: cWhite });
    safeDrawText(p, 'invio domanda', { x: 470, y: H / 2 - 25, size: 21, font: fontBold, color: cWhite });
  }

  const pdfBytes = await pdfDoc.save();

  fs.writeFileSync('guidanaspicompilazione2026.pdf', pdfBytes);
  fs.writeFileSync('public/guidanaspicompilazione2026.pdf', pdfBytes);
  fs.writeFileSync('app/src/main/assets/guidanaspicompilazione2026.pdf', pdfBytes);

  console.log(`Official 23-page INPS PDF generated successfully! Size: ${pdfBytes.length} bytes`);
}

createOfficialNaspiPdf().catch(err => {
  console.error('Error generating PDF:', err);
  process.exit(1);
});
