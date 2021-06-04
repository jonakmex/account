package com.jnksoft.account.model;

import com.jnksoft.account.model.enums.TransactionType;
import lombok.Data;
import org.joda.time.LocalDate;

@Data
public class Transaction {
    private String id;
    private LocalDate date;
    private String description;
    private TransactionType type;
    private Double value;

    public Transaction(LocalDate date, String description, TransactionType type, Double value) {
        this.date = date;
        this.description = description;
        this.type = type;
        this.value = value;
    }
}
