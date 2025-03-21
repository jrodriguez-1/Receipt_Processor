package com.example.ReceiptProcessor.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Receipt {
    @Id
    private String id;

    private String retailer;

    private LocalDate purchaseDate;

    private LocalTime purchaseTime;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
//    @JoinColumn(name = "receipt_id")
    private List<Item> items;

    private String total;
}
