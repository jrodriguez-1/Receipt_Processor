package com.example.ReceiptProcessor.controller;

import com.example.ReceiptProcessor.dto.PointsResponseDto;
import com.example.ReceiptProcessor.dto.ProcessReceiptDto;
import com.example.ReceiptProcessor.model.Receipt;
import com.example.ReceiptProcessor.service.ReceiptService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/receipts")
@RequiredArgsConstructor
public class ReceiptController {

    private final ReceiptService receiptService;

    @PostMapping("/process")
    public ResponseEntity<ProcessReceiptDto> processReceipts(@RequestBody Receipt receipt) {
        try {
            String id = receiptService.generateReceiptId(receipt);
            return ResponseEntity.ok(new ProcessReceiptDto(id));
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The receipt is invalid.", e);
        }
    }

    @GetMapping("/{id}/points")
    public ResponseEntity<PointsResponseDto> getPoints(@PathVariable String id) {
        try {
            int points = receiptService.processReceiptPoints(id);
            return ResponseEntity.ok(new PointsResponseDto(points));
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Receipt not found for that ID.", e);
        }
    }
}