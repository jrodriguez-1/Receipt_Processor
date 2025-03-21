package com.example.ReceiptProcessor.service;

import com.example.ReceiptProcessor.model.Receipt;
import com.example.ReceiptProcessor.repository.ReceiptRepository;
import com.example.ReceiptProcessor.util.PointsCalculator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ReceiptService {

    private final ReceiptRepository receiptRepository;

    @Transactional
    public String processReceiptId(Receipt receipt) {
        String id = UUID.randomUUID().toString();
        receipt.setId(id);
        receiptRepository.save(receipt);
        return id;
    }

    @Transactional(readOnly = true)
    public int getPointsForReceipt(String id) {
        Receipt receipt = receiptRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Receipt not found for ID: " + id));

        return PointsCalculator.calculatePoints(receipt);
    }
}
