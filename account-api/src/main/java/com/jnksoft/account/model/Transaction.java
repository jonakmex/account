package com.jnksoft.account.model;

import com.jnksoft.account.model.enums.TransactionType;
import lombok.Data;
import org.joda.time.LocalDateTime;

import static com.jnksoft.account.model.enums.TransactionType.INCOME;

@Data
public class Transaction {
    private String id;
    private LocalDateTime date;
    private String description;
    private TransactionType type;
    private Double amount;
    private Double balance;

    public Transaction(LocalDateTime date, String description, TransactionType type, Double amount) {
        this.date = date;
        this.description = description;
        this.type = type;
        this.amount = amount;
    }

    public Double getAddAmount(){
        return INCOME.equals(type)?amount:-amount;
    }
}
