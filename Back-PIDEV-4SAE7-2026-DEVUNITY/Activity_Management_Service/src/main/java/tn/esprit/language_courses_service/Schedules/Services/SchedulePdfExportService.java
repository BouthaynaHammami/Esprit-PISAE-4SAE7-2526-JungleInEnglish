package tn.esprit.language_courses_service.Schedules.Services;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import org.springframework.stereotype.Service;
import tn.esprit.language_courses_service.Schedules.Entities.Schedule;

import java.io.ByteArrayOutputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class SchedulePdfExportService {

    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final DateTimeFormatter DATE_TIME_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    public byte[] generateWeeklySchedulePdf(String ownerLabel, LocalDate weekStart, List<Schedule> schedules) {
        Document document = new Document();
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        try {
            PdfWriter.getInstance(document, out);
            document.open();

            Font titleFont = new Font(Font.FontFamily.HELVETICA, 16, Font.BOLD, new BaseColor(0, 109, 119));
            Font subtitleFont = new Font(Font.FontFamily.HELVETICA, 11, Font.NORMAL, BaseColor.DARK_GRAY);
            Font headerFont = new Font(Font.FontFamily.HELVETICA, 10, Font.BOLD, BaseColor.WHITE);
            Font cellFont = new Font(Font.FontFamily.HELVETICA, 9, Font.NORMAL);

            LocalDate weekEnd = weekStart.plusDays(6);
            document.add(new Paragraph("Weekly Schedule", titleFont));
            document.add(new Paragraph(ownerLabel, subtitleFont));
            document.add(new Paragraph("Week: " + weekStart.format(DATE_FORMAT) + " -> " + weekEnd.format(DATE_FORMAT), subtitleFont));
            document.add(new Paragraph("Total Sessions: " + schedules.size(), subtitleFont));
            document.add(new Paragraph(" "));

            PdfPTable table = new PdfPTable(6);
            table.setWidthPercentage(100);
            table.setWidths(new float[]{2.1f, 2.2f, 2.2f, 3.0f, 1.6f, 1.8f});

            addHeaderCell(table, "Date", headerFont);
            addHeaderCell(table, "Start", headerFont);
            addHeaderCell(table, "End", headerFont);
            addHeaderCell(table, "Title", headerFont);
            addHeaderCell(table, "Room", headerFont);
            addHeaderCell(table, "Class", headerFont);

            for (Schedule schedule : schedules) {
                addBodyCell(table, schedule.getStartTime() != null ? schedule.getStartTime().toLocalDate().format(DATE_FORMAT) : "-", cellFont);
                addBodyCell(table, schedule.getStartTime() != null ? schedule.getStartTime().format(DATE_TIME_FORMAT) : "-", cellFont);
                addBodyCell(table, schedule.getEndTime() != null ? schedule.getEndTime().format(DATE_TIME_FORMAT) : "-", cellFont);
                addBodyCell(table, schedule.getTitle() != null ? schedule.getTitle() : "-", cellFont);
                addBodyCell(table, schedule.getRoom() != null && schedule.getRoom().getRoomId() != null ? "#" + schedule.getRoom().getRoomId() : "-", cellFont);
                addBodyCell(table, schedule.getClassEntity() != null && schedule.getClassEntity().getClassId() != null ? "#" + schedule.getClassEntity().getClassId() : "-", cellFont);
            }

            document.add(table);
        } catch (DocumentException e) {
            throw new RuntimeException("Failed to generate schedule PDF.", e);
        } finally {
            document.close();
        }

        return out.toByteArray();
    }

    private void addHeaderCell(PdfPTable table, String text, Font font) {
        PdfPCell cell = new PdfPCell(new Phrase(text, font));
        cell.setBackgroundColor(new BaseColor(0, 109, 119));
        cell.setPadding(6f);
        table.addCell(cell);
    }

    private void addBodyCell(PdfPTable table, String text, Font font) {
        PdfPCell cell = new PdfPCell(new Phrase(text, font));
        cell.setPadding(5f);
        table.addCell(cell);
    }
}
