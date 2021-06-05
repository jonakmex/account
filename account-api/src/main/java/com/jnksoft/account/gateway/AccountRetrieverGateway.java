package com.jnksoft.account.gateway;

import com.jnksoft.account.model.Account;
import com.jnksoft.account.model.Transaction;
import org.joda.time.LocalDateTime;


import java.util.List;

public interface AccountRetrieverGateway {
    Account findById(String id);
    List<Transaction> findTopTransactions(String accountId, LocalDateTime localDateTime);
    Transaction findBottomTransaction(String accountId, LocalDateTime localDateTime);
}
