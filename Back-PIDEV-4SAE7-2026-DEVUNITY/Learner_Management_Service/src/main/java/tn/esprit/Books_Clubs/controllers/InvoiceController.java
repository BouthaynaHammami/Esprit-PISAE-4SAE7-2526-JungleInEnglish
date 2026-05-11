package tn.esprit.Books_Clubs.Controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.Services.ImplServices.InvoiceService;

@RestController
@RequestMapping("/invoice")
@CrossOrigin(origins = "http://localhost:4200")
@RequiredArgsConstructor
public class InvoiceController {

    private final InvoiceService invoiceService;

    @GetMapping
    public ResponseEntity<byte[]> generate(
            @RequestParam(required = false) Long orderId,
            @RequestParam(required = false) Long rentalId) throws Exception {

        byte[] pdf = invoiceService.generateInvoice(orderId, rentalId);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"facture.pdf\"")
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdf);
    }
}