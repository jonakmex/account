package com.jnksoft.account.gateway;

import com.jnksoft.account.model.Transaction;

public interface TransactionSaveGateway {
    Transaction save(Transaction transaction);
}
