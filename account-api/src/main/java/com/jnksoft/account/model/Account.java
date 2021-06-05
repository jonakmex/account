package com.jnksoft.account.model;

import lombok.Data;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Data
public class Account {
    private String id;
    private String description;
    private Double initialBalance;
    private Double balance;
    private List<Transaction> transactions;

    public Account(){
        initialBalance = 0.0;
        balance = 0.0;
    }

    public Account(Double initialBalance) {
        this.initialBalance = initialBalance;
        this.balance = initialBalance;
    }

    public void postTransaction(Transaction transaction) {
        if(transactions == null)
            transactions = new ArrayList<>();

        Optional<Transaction> bottomTransaction = getBottomTransaction(transaction.getDate());
        if(bottomTransaction.isPresent()){
            balance = bottomTransaction.get().getBalance();
        }
        else {
            balance = initialBalance;
        }

        List<Transaction> topTransactions = getTopTransactions(transaction.getDate());
        if(topTransactions.isEmpty()){
            balance += transaction.getAddAmount();
            transaction.setBalance(balance);
            transactions.add(transaction);
        }
        else {
            balance += transaction.getAddAmount();
            transaction.setBalance(balance);
            transactions.add(transaction);
            topTransactions
                    .stream()
                    .sorted(Comparator.comparing(Transaction::getDate))
                    .forEach(t -> {
                        balance += t.getAddAmount();
                        t.setBalance(balance);
                    });
        }
    }

    private List<Transaction> getTopTransactions(LocalDateTime cutDate) {
        if(transactions == null)
            transactions = new ArrayList<>();

        return transactions
                .stream()
                .filter(t -> t.getDate().compareTo(cutDate) >= 0)
                .collect(Collectors.toList());
    }

    private Optional<Transaction> getBottomTransaction(LocalDateTime cutDate) {
        if(transactions == null)
            return Optional.empty();

        return  transactions
                .stream()
                .filter(t -> t.getDate().compareTo(cutDate) <= 0)
                .sorted(Comparator.comparing(Transaction::getDate).reversed())
                .findFirst();
    }

    public Optional<Transaction> getHeadTransaction() {
        if(transactions == null)
            return Optional.empty();

        return transactions
                .stream()
                .sorted(Comparator.comparing(Transaction::getDate).reversed())
                .findFirst();
    }
}
