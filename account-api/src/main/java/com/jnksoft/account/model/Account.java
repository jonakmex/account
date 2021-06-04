package com.jnksoft.account.model;

import com.jnksoft.account.model.enums.TransactionType;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class Account {
    private String id;
    private String description;
    private Double initialBalance;
    private List<Transaction> transactions;

    public Account(){
        initialBalance = 0.0;
    }

    public Account(Double initialBalance) {
        this.initialBalance = initialBalance;
    }

    public void addTransaction(Transaction transaction) {
        if(transactions == null)
            transactions = new ArrayList<>();
        transactions.add(transaction);
    }

    public Double getBalance() {
        Double income = transactions
                .stream()
                .filter(transaction -> TransactionType.INCOME.equals(transaction.getType()))
                .mapToDouble(t -> t.getValue())
                .sum();
        Double outcome = transactions
                .stream()
                .filter(transaction -> TransactionType.OUTCOME.equals(transaction.getType()))
                .mapToDouble(t -> t.getValue())
                .sum();
        return (initialBalance + income) - outcome;
    }
}
