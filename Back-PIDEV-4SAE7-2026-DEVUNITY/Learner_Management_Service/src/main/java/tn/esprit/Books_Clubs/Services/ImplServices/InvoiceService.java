package tn.esprit.Books_Clubs.Services.ImplServices;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import com.itextpdf.text.pdf.draw.LineSeparator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tn.esprit.Books_Clubs.entities.*;
import tn.esprit.Books_Clubs.repositories.*;

import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Service
@RequiredArgsConstructor
public class InvoiceService {

    private final OrderRepository  orderRepository;
    private final RentalRepository rentalRepository;

    // â”€â”€ Palette â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€
    private static final BaseColor TEAL       = new BaseColor(0x00, 0x6D, 0x77); // #006D77
    private static final BaseColor TEAL_LIGHT = new BaseColor(0x83, 0xC5, 0xBE); // #83C5BE
    private static final BaseColor BG_LIGHT   = new BaseColor(0xED, 0xF6, 0xF9); // #EDF6F9
    private static final BaseColor PEACH      = new BaseColor(0xFF, 0xDD, 0xD2); // #FFDDD2
    private static final BaseColor SALMON     = new BaseColor(0xE2, 0x95, 0x78); // #E29578
    private static final BaseColor WHITE      = BaseColor.WHITE;
    private static final BaseColor GREY_TEXT  = new BaseColor(0x6B, 0x72, 0x80);
    private static final BaseColor DARK_TEXT  = new BaseColor(0x11, 0x18, 0x27);
    private static final BaseColor ROW_ALT    = new BaseColor(0xF0, 0xFB, 0xFC);

    private static final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public byte[] generateInvoice(Long orderId, Long rentalId) throws Exception {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Document doc = new Document(PageSize.A4, 50, 50, 50, 50);
        PdfWriter writer = PdfWriter.getInstance(doc, baos);
        doc.open();

        // â”€â”€ Fonts â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€
        Font fHero      = new Font(Font.FontFamily.HELVETICA, 26, Font.BOLD,   WHITE);
        Font fSubHero   = new Font(Font.FontFamily.HELVETICA, 10, Font.NORMAL, PEACH);
        Font fTagline   = new Font(Font.FontFamily.HELVETICA,  8, Font.NORMAL, TEAL_LIGHT);
        Font fSectionH  = new Font(Font.FontFamily.HELVETICA, 11, Font.BOLD,   TEAL);
        Font fColHead   = new Font(Font.FontFamily.HELVETICA,  8, Font.BOLD,   WHITE);
        Font fCell      = new Font(Font.FontFamily.HELVETICA,  9, Font.NORMAL, DARK_TEXT);
        Font fCellBold  = new Font(Font.FontFamily.HELVETICA,  9, Font.BOLD,   DARK_TEXT);
        Font fTotal     = new Font(Font.FontFamily.HELVETICA, 10, Font.BOLD,   WHITE);
        Font fMeta      = new Font(Font.FontFamily.HELVETICA,  9, Font.NORMAL, GREY_TEXT);
        Font fMetaBold  = new Font(Font.FontFamily.HELVETICA,  9, Font.BOLD,   DARK_TEXT);
        Font fFooter    = new Font(Font.FontFamily.HELVETICA,  8, Font.ITALIC, GREY_TEXT);
        Font fDiscount  = new Font(Font.FontFamily.HELVETICA,  9, Font.BOLD,   SALMON);
        Font fInvoiceNo = new Font(Font.FontFamily.HELVETICA, 10, Font.BOLD,   SALMON);

        // â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•
        //  HERO BANNER (fond dÃ©gradÃ© simulÃ© par tableau)
        // â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•
        PdfPTable banner = new PdfPTable(2);
        banner.setWidthPercentage(100);
        banner.setWidths(new float[]{60, 40});
        banner.setSpacingAfter(0);

        // Cellule gauche â€” Nom & coordonnÃ©es
        PdfPCell cBrandCell = new PdfPCell();
        cBrandCell.setBackgroundColor(TEAL);
        cBrandCell.setBorder(Rectangle.NO_BORDER);
        cBrandCell.setPadding(20);

        Paragraph brand = new Paragraph("Jungle in English", fHero);
        brand.setSpacingAfter(4);
        cBrandCell.addElement(brand);
        cBrandCell.addElement(new Paragraph("Librairie & Location de Livres", fSubHero));
        cBrandCell.addElement(new Paragraph(" ", fTagline));
        cBrandCell.addElement(new Paragraph("ðŸ“ Tunis, Tunisie", fTagline));
        cBrandCell.addElement(new Paragraph("âœ‰  contact@jungleinenglish.tn", fTagline));
        cBrandCell.addElement(new Paragraph("ðŸŒ www.jungleinenglish.tn", fTagline));
        banner.addCell(cBrandCell);

        // Cellule droite â€” NumÃ©ro de facture
        PdfPCell cInvoCell = new PdfPCell();
        cInvoCell.setBackgroundColor(TEAL_LIGHT);
        cInvoCell.setBorder(Rectangle.NO_BORDER);
        cInvoCell.setPadding(20);
        cInvoCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cInvoCell.setVerticalAlignment(Element.ALIGN_MIDDLE);

        String invoiceNum = orderId  != null ? "CMD-" + String.format("%04d", orderId)
                : rentalId != null ? "LOC-" + String.format("%04d", rentalId)
                : "INV-0000";

        Paragraph invoTitle = new Paragraph("FACTURE", fColHead);
        invoTitle.setAlignment(Element.ALIGN_RIGHT);
        cInvoCell.addElement(invoTitle);

        Paragraph invoNum = new Paragraph(invoiceNum, fInvoiceNo);
        invoNum.setAlignment(Element.ALIGN_RIGHT);
        cInvoCell.addElement(invoNum);

        Paragraph invoDate = new Paragraph("Ã‰mise le : " + LocalDate.now().format(FMT), fMeta);
        invoDate.setAlignment(Element.ALIGN_RIGHT);
        cInvoCell.addElement(invoDate);
        banner.addCell(cInvoCell);

        doc.add(banner);

        // Bande dÃ©corative fine sous le banner
        PdfPTable stripe = new PdfPTable(1);
        stripe.setWidthPercentage(100);
        stripe.setSpacingAfter(20);
        PdfPCell stripeCell = new PdfPCell(new Phrase(""));
        stripeCell.setBackgroundColor(SALMON);
        stripeCell.setFixedHeight(4);
        stripeCell.setBorder(Rectangle.NO_BORDER);
        stripe.addCell(stripeCell);
        doc.add(stripe);

        // â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•
        //  COMMANDE
        // â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•
        if (orderId != null) {
            Order order = orderRepository.findById(orderId).orElseThrow();

            // Bloc info commande
            addInfoBlock(doc, "ðŸ›’  DÃ©tail de la Commande",
                    "Commande NÂ°", "CMD-" + String.format("%04d", order.getOrderId()),
                    "Date", order.getOrderDate().format(FMT),
                    "Statut", order.getStatus().name(),
                    fSectionH, fMetaBold, fMeta, BG_LIGHT);

            // Tableau articles
            PdfPTable t = new PdfPTable(5);
            t.setWidthPercentage(100);
            t.setWidths(new float[]{36, 20, 8, 18, 18});
            t.setSpacingBefore(8);
            t.setSpacingAfter(4);

            String[] headers = {"Titre", "Auteur", "QtÃ©", "Prix unit.", "Total"};
            for (String h : headers) addHeaderCell(t, h, fColHead, TEAL);

            boolean alt = false;
            for (OrderItem item : order.getItems()) {
                BaseColor bg = alt ? ROW_ALT : WHITE;
                addDataCell(t, item.getBook().getTitle(),                              bg, fCellBold);
                addDataCell(t, item.getBook().getAuthor().getName(),                   bg, fCell);
                addDataCell(t, String.valueOf(item.getQuantity()),                     bg, fCell);
                addDataCell(t, fmt(item.getUnitSalePrice(), order.getCurrency()),      bg, fCell);
                addDataCell(t, fmt(item.getLineTotal(),     order.getCurrency()),      bg, fCell);
                alt = !alt;
            }

            // Ligne sous-total
            addSubtotalRow(t, "Sous-total",
                    fmt(order.getTotalAmount(), order.getCurrency()), fCell, BG_LIGHT);

            // RÃ©duction ?
            if (order.isDiscountApplied()) {
                BigDecimal original = order.getTotalAmount().divide(
                        BigDecimal.valueOf(0.80), 2, java.math.RoundingMode.HALF_UP);
                BigDecimal saved = original.subtract(order.getTotalAmount());
                addSubtotalRow(t, "ðŸŽ‰ RÃ©duction fidÃ©litÃ© (-20%)",
                        "- " + fmt(saved, order.getCurrency()), fDiscount, PEACH);
            }

            // Ligne TOTAL
            addTotalRow(t, fmt(order.getTotalAmount(), order.getCurrency()), fTotal, TEAL);

            doc.add(t);

            if (order.isDiscountApplied()) {
                Paragraph disc = new Paragraph("â˜…  RÃ©duction de fidÃ©litÃ© appliquÃ©e : -20% pour ce client !", fDiscount);
                disc.setSpacingBefore(4);
                doc.add(disc);
            }

            doc.add(new Paragraph(" "));
        }

        // â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•
        //  LOCATION
        // â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•
        if (rentalId != null) {
            Rental rental = rentalRepository.findById(rentalId).orElseThrow();

            addInfoBlock(doc, "ðŸ”–  DÃ©tail de la Location",
                    "Location NÂ°", "LOC-" + String.format("%04d", rental.getRentalId()),
                    "Statut", rental.getStatus().name(),
                    "PayÃ©e", rental.isPaid() ? "Oui" : "Non",
                    fSectionH, fMetaBold, fMeta, BG_LIGHT);

            PdfPTable t = new PdfPTable(5);
            t.setWidthPercentage(100);
            t.setWidths(new float[]{30, 15, 15, 20, 20});
            t.setSpacingBefore(8);
            t.setSpacingAfter(4);

            String[] headers = {"Titre", "DÃ©but", "Fin", "Prix / jour", "Total"};
            for (String h : headers) addHeaderCell(t, h, fColHead, TEAL);

            addDataCell(t, rental.getBook().getTitle(),                         WHITE, fCellBold);
            addDataCell(t, rental.getStartDate().format(FMT),                  WHITE, fCell);
            addDataCell(t, rental.getDueDate().format(FMT),                    WHITE, fCell);
            addDataCell(t, fmt(rental.getDailyRentalPrice(), rental.getCurrency()), WHITE, fCell);
            addDataCell(t, fmt(rental.getTotalRentalPrice(), rental.getCurrency()), WHITE, fCell);

            if (rental.isDiscountApplied()) {
                BigDecimal original = rental.getTotalRentalPrice().divide(
                        BigDecimal.valueOf(0.80), 2, java.math.RoundingMode.HALF_UP);
                BigDecimal saved = original.subtract(rental.getTotalRentalPrice());
                addSubtotalRow(t, "ðŸŽ‰ RÃ©duction fidÃ©litÃ© (-20%)",
                        "- " + fmt(saved, rental.getCurrency()), fDiscount, PEACH);
            }

            addTotalRow(t, fmt(rental.getTotalRentalPrice(), rental.getCurrency()), fTotal, TEAL);

            doc.add(t);

            if (rental.isDiscountApplied()) {
                Paragraph disc = new Paragraph("â˜…  RÃ©duction de fidÃ©litÃ© appliquÃ©e : -20% pour ce client !", fDiscount);
                disc.setSpacingBefore(4);
                doc.add(disc);
            }

            doc.add(new Paragraph(" "));
        }

        // â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•
        //  FOOTER
        // â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•
        doc.add(new Paragraph(" "));
        doc.add(new LineSeparator(1, 100, TEAL_LIGHT, Element.ALIGN_CENTER, -4));
        doc.add(new Paragraph(" "));

        PdfPTable footer = new PdfPTable(2);
        footer.setWidthPercentage(100);
        footer.setWidths(new float[]{50, 50});

        PdfPCell fLeft = new PdfPCell();
        fLeft.setBorder(Rectangle.NO_BORDER);
        fLeft.addElement(new Paragraph("Merci pour votre confiance ! ðŸŒ¿", fFooter));
        fLeft.addElement(new Paragraph("Jungle in English â€” Tunis, Tunisie", fFooter));
        footer.addCell(fLeft);

        PdfPCell fRight = new PdfPCell();
        fRight.setBorder(Rectangle.NO_BORDER);
        fRight.setHorizontalAlignment(Element.ALIGN_RIGHT);
        Paragraph generated = new Paragraph("Document gÃ©nÃ©rÃ© le " + LocalDate.now().format(FMT), fFooter);
        generated.setAlignment(Element.ALIGN_RIGHT);
        fRight.addElement(generated);
        footer.addCell(fRight);

        doc.add(footer);

        doc.close();
        return baos.toByteArray();
    }

    // â”€â”€ Helpers â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€

    private void addHeaderCell(PdfPTable t, String text, Font f, BaseColor bg) {
        PdfPCell c = new PdfPCell(new Phrase(text, f));
        c.setBackgroundColor(bg);
        c.setPadding(8);
        c.setBorderColor(WHITE);
        c.setBorderWidth(1);
        t.addCell(c);
    }

    private void addDataCell(PdfPTable t, String text, BaseColor bg, Font f) {
        PdfPCell c = new PdfPCell(new Phrase(text != null ? text : "â€”", f));
        c.setBackgroundColor(bg);
        c.setPadding(7);
        c.setBorderColor(new BaseColor(0xE5, 0xE7, 0xEB));
        c.setBorderWidth(0.5f);
        t.addCell(c);
    }

    private void addSubtotalRow(PdfPTable t, String label, String value,
                                Font f, BaseColor bg) {
        PdfPCell empty = new PdfPCell(new Phrase(""));
        empty.setColspan(3);
        empty.setBorder(Rectangle.NO_BORDER);
        empty.setBackgroundColor(bg);
        t.addCell(empty);

        PdfPCell lbl = new PdfPCell(new Phrase(label, f));
        lbl.setBackgroundColor(bg);
        lbl.setPadding(7);
        lbl.setBorderColor(new BaseColor(0xE5, 0xE7, 0xEB));
        lbl.setBorderWidth(0.5f);
        t.addCell(lbl);

        PdfPCell val = new PdfPCell(new Phrase(value, f));
        val.setBackgroundColor(bg);
        val.setPadding(7);
        val.setBorderColor(new BaseColor(0xE5, 0xE7, 0xEB));
        val.setBorderWidth(0.5f);
        t.addCell(val);
    }

    private void addTotalRow(PdfPTable t, String value, Font f, BaseColor bg) {
        PdfPCell empty = new PdfPCell(new Phrase(""));
        empty.setColspan(3);
        empty.setBorder(Rectangle.NO_BORDER);
        t.addCell(empty);

        PdfPCell lbl = new PdfPCell(new Phrase("TOTAL TTC", f));
        lbl.setBackgroundColor(bg);
        lbl.setPadding(10);
        lbl.setBorder(Rectangle.NO_BORDER);
        t.addCell(lbl);

        PdfPCell val = new PdfPCell(new Phrase(value, f));
        val.setBackgroundColor(bg);
        val.setPadding(10);
        val.setBorder(Rectangle.NO_BORDER);
        t.addCell(val);
    }

    private void addInfoBlock(Document doc,
                              String title,
                              String k1, String v1,
                              String k2, String v2,
                              String k3, String v3,
                              Font fSection, Font fKey, Font fVal,
                              BaseColor bg) throws DocumentException {

        Paragraph sectionTitle = new Paragraph(title, fSection);
        sectionTitle.setSpacingBefore(6);
        sectionTitle.setSpacingAfter(6);
        doc.add(sectionTitle);

        PdfPTable info = new PdfPTable(6);
        info.setWidthPercentage(100);
        info.setWidths(new float[]{18, 25, 18, 20, 15, 15});
        info.setSpacingAfter(6);

        for (String[] pair : new String[][]{{k1, v1}, {k2, v2}, {k3, v3}}) {
            PdfPCell key = new PdfPCell(new Phrase(pair[0], fKey));
            key.setBackgroundColor(bg);
            key.setPadding(6);
            key.setBorderColor(new BaseColor(0xE5, 0xE7, 0xEB));
            key.setBorderWidth(0.5f);
            info.addCell(key);

            PdfPCell val = new PdfPCell(new Phrase(pair[1], fVal));
            val.setBackgroundColor(WHITE);
            val.setPadding(6);
            val.setBorderColor(new BaseColor(0xE5, 0xE7, 0xEB));
            val.setBorderWidth(0.5f);
            info.addCell(val);
        }

        doc.add(info);
    }

    private String fmt(BigDecimal val, Currency currency) {
        return String.format("%.3f %s", val, currency.name());
    }
}
