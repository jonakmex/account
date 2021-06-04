package com.jnksoft.account.accountapi;

import com.jnksoft.account.model.Account;
import com.jnksoft.account.model.Transaction;
import org.joda.time.LocalDate;
import org.junit.jupiter.api.Test;

import static com.jnksoft.account.model.enums.TransactionType.INCOME;
import static com.jnksoft.account.model.enums.TransactionType.OUTCOME;
import static org.junit.jupiter.api.Assertions.assertEquals;


public class AccountTest {

    @Test
    public void should_create_an_account(){
        Account account = new Account();
    }

    @Test
    public void should_add_funds(){
        Account account = new Account();
        Transaction transaction = new Transaction(LocalDate.now(),"Description", INCOME,100.0);
        account.addTransaction(transaction);
        assertEquals(100.0,account.getBalance());
    }
    @Test
    public void should_add_one_income_five_outcomes(){
        Account account = new Account();
        account.addTransaction(new Transaction(LocalDate.now(),"Transaction 1", INCOME,1000.0));
        account.addTransaction(new Transaction(LocalDate.now(),"Transaction 2", OUTCOME,100.0));
        account.addTransaction(new Transaction(LocalDate.now(),"Transaction 3", OUTCOME,100.0));
        account.addTransaction(new Transaction(LocalDate.now(),"Transaction 4", OUTCOME,100.0));
        account.addTransaction(new Transaction(LocalDate.now(),"Transaction 5", OUTCOME,100.0));
        account.addTransaction(new Transaction(LocalDate.now(),"Transaction 6", OUTCOME,100.0));
        assertEquals(500.0,account.getBalance());
    }

    @Test
    public void should_add_one_income_five_outcomes_with_initial_balance(){
        Account account = new Account(545.00);
        account.addTransaction(new Transaction(LocalDate.now(),"Transaction 1", INCOME,1000.0));
        account.addTransaction(new Transaction(LocalDate.now(),"Transaction 2", OUTCOME,100.0));
        account.addTransaction(new Transaction(LocalDate.now(),"Transaction 3", OUTCOME,100.0));
        account.addTransaction(new Transaction(LocalDate.now(),"Transaction 4", OUTCOME,100.0));
        account.addTransaction(new Transaction(LocalDate.now(),"Transaction 5", OUTCOME,100.0));
        account.addTransaction(new Transaction(LocalDate.now(),"Transaction 6", OUTCOME,100.0));
        assertEquals(1045.0,account.getBalance());
    }
}
