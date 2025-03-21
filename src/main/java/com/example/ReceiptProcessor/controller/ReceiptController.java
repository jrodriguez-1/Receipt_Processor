package com.example.ReceiptProcessor.controller;

import com.example.ReceiptProcessor.dto.PointsResponseDto;
import com.example.ReceiptProcessor.dto.ProcessReceiptDto;
import com.example.ReceiptProcessor.model.Receipt;
import com.example.ReceiptProcessor.service.ReceiptService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@Slf4j
@RestController
@RequestMapping("/receipts")
@RequiredArgsConstructor
public class ReceiptController {

    private final ReceiptService receiptService;

//    @Autowired
//    public ReceiptController(ReceiptService receiptService) {
//        this.receiptService = receiptService;
//    }

    @GetMapping("/test")
    public String test() {
        return "Controller is working";
    }

    /**
     * Get the points for a receipt by ID
     *
     * @param id The receipt ID
     * @return A response containing the points
     */
    @GetMapping("/{id}/points")
    public ResponseEntity<PointsResponseDto> getPoints(@PathVariable String id) {
        try {
            log.info("Getting points for receipt with ID: {}", id);
            int points = receiptService.getPointsForReceipt(id);
            return ResponseEntity.ok(new PointsResponseDto(points));
        } catch (IllegalArgumentException e) {
            log.error("Receipt not found with ID: {}", id);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Receipt not found", e);
        }
    }

    /**
     * Process a receipt and return an ID
     *
     * @param receipt The receipt to process
     * @return A response containing the ID
     */
    @PostMapping("/process")
    public ResponseEntity<ProcessReceiptDto> processReceipt(@RequestBody Receipt receipt) {
        try {
            log.info("Processing receipt for retailer: {}", receipt.getRetailer());
            String id = receiptService.processReceiptId(receipt);
            return ResponseEntity.ok(new ProcessReceiptDto(id));
        } catch (Exception e) {
            log.error("Error processing receipt: {}", e.getMessage());
            return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ProcessReceiptDto(null));
        }
    }
}
