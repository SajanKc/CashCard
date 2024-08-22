package com.iamsajan.cashcardservice;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/cashcards")
public class CashCardController {
    private final CashCardRepository cashCardRepository;

    private CashCardController(CashCardRepository cashCardRepository) {
        this.cashCardRepository = cashCardRepository;
    }

    @GetMapping("/{requestedId}")
    private ResponseEntity<CashCard> getCashCardById(@PathVariable Long requestedId) {
        Optional<CashCard> optionalCashCard = cashCardRepository.findById(requestedId);

//        if (optionalCashCard.isPresent()) {
//            return ResponseEntity.ok(optionalCashCard.get());
//        } else {
//            return ResponseEntity.notFound().build();
//        }

        // OR

        return optionalCashCard.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

}
