package tn.esprit.Books_Clubs.Controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import tn.esprit.jungleinenglishuser.Services.IServices.IStockService;
import tn.esprit.jungleinenglishuser.entities.Stock;

@RestController
@RequestMapping("/api/stocks")
@CrossOrigin(origins = "http://localhost:4200")
@RequiredArgsConstructor
public class StockController {

    private final IStockService service;

    @GetMapping("/{bookId}")
    public Stock get(@PathVariable Long bookId) {
        return service.getStockByBookId(bookId);
    }

    @PutMapping("/{bookId}")
    public Stock update(@PathVariable Long bookId, @RequestParam int quantity) {
        return service.updateStock(bookId, quantity);
    }

    @PostMapping("/{bookId}/add")
    public Stock add(@PathVariable Long bookId, @RequestParam int qty) {
        return service.addQuantity(bookId, qty);
    }

    @PostMapping("/{bookId}/remove")
    public Stock remove(@PathVariable Long bookId, @RequestParam int qty) {
        return service.removeQuantity(bookId, qty);
    }
}