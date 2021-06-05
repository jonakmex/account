package com.jnksoft.account.model;

import com.jnksoft.account.model.enums.TransactionType;
import lombok.Data;
import org.joda.time.LocalDateTime;
import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        return Objects.equals(id, ((Transaction) o).id);
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}
