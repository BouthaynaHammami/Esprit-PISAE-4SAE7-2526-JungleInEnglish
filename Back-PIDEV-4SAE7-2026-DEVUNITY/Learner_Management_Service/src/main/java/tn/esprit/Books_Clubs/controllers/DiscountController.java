package tn.esprit.Books_Clubs.Controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import tn.esprit.jungleinenglishuser.Services.ImplServices.DiscountService;

@RestController
@RequestMapping("/discount")
@CrossOrigin(origins = "http://localhost:4200")
@RequiredArgsConstructor
public class DiscountController {

    private final DiscountService discountService;

    @GetMapping("/eligible")
    public boolean isEligible(@RequestParam Integer userId) {
        return discountService.isEligibleForDiscount(userId);
    }
}