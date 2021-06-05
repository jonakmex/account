package com.jnksoft.account.test;

import com.jnksoft.account.model.Account;
import com.jnksoft.account.model.Transaction;
import org.apache.log4j.Logger;
import org.joda.time.LocalDateTime;
import org.junit.jupiter.api.Test;

import java.util.Comparator;
import java.util.Optional;

import static com.jnksoft.account.model.enums.TransactionType.INCOME;
import static com.jnksoft.account.model.enums.TransactionType.OUTCOME;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


public class AccountTest {
    private static final Logger logger = Logger.getLogger(AccountTest.class);
    @Test
    public void should_create_an_account(){
        Account account = new Account();
    }

    @Test
    public void should_add_funds(){
        Account account = new Account();
        Transaction transaction = new Transaction(LocalDateTime.now(),"Description", INCOME,100.0);
        account.postTransaction(transaction);
        assertEquals(100.0,account.getBalance());
    }
    @Test
    public void should_add_one_income_five_outcomes(){
        Account account = new Account();
        account.postTransaction(new Transaction(LocalDateTime.now(),"Transaction 1", INCOME,1000.0));
        account.postTransaction(new Transaction(LocalDateTime.now(),"Transaction 2", OUTCOME,100.0));
        account.postTransaction(new Transaction(LocalDateTime.now(),"Transaction 3", OUTCOME,100.0));
        account.postTransaction(new Transaction(LocalDateTime.now(),"Transaction 4", OUTCOME,100.0));
        account.postTransaction(new Transaction(LocalDateTime.now(),"Transaction 5", OUTCOME,100.0));
        account.postTransaction(new Transaction(LocalDateTime.now(),"Transaction 6", OUTCOME,100.0));
        account.getTransactions()
                .stream()
                .sorted(Comparator.comparing(Transaction::getDate).reversed())
                .forEach(t -> System.out.println(t.getDescription()+" | "+t.getAddAmount()+ "|"+t.getBalance() + "|"+ t.getDate()));
        System.out.println("Balance:"+account.getBalance());
        assertEquals(500.0,account.getBalance());
    }

    @Test
    public void should_add_one_income_five_outcomes_with_initial_balance(){
        Account account = new Account(545.00);
        account.postTransaction(new Transaction(LocalDateTime.now(),"Transaction 1", INCOME,1000.0));
        account.postTransaction(new Transaction(LocalDateTime.now(),"Transaction 2", OUTCOME,100.0));
        account.postTransaction(new Transaction(LocalDateTime.now(),"Transaction 3", OUTCOME,100.0));
        account.postTransaction(new Transaction(LocalDateTime.now(),"Transaction 4", OUTCOME,100.0));
        account.postTransaction(new Transaction(LocalDateTime.now(),"Transaction 5", OUTCOME,100.0));
        account.postTransaction(new Transaction(LocalDateTime.now(),"Transaction 6", OUTCOME,100.0));
        assertEquals(1045.0,account.getBalance());
    }

    @Test
    public void should_keep_balance_on_transaction_init_balance_0(){
        Account account = new Account();
        account.postTransaction(new Transaction(LocalDateTime.now(),"Transaction 1", INCOME,1000.0));
        account.postTransaction(new Transaction(LocalDateTime.now(),"Transaction 2", OUTCOME,100.0));
        account.postTransaction(new Transaction(LocalDateTime.now(),"Transaction 3", OUTCOME,100.0));
        account.postTransaction(new Transaction(LocalDateTime.now(),"Transaction 4", OUTCOME,100.0));
        account.postTransaction(new Transaction(LocalDateTime.now(),"Transaction 5", OUTCOME,100.0));
        account.postTransaction(new Transaction(LocalDateTime.now(),"Transaction 6", OUTCOME,100.0));
        assertEquals(500.0,account.getBalance());
    }


    @Test
    public void should_post_transaction_at_the_beginning() throws InterruptedException {
        Account account = new Account(1000.00);
        account.postTransaction(new Transaction(LocalDateTime.now(),"Transaction 1", OUTCOME,100.0));
        Thread.sleep(1);
        account.postTransaction(new Transaction(LocalDateTime.now(),"Transaction 2", OUTCOME,100.0));
        Thread.sleep(1);
        account.postTransaction(new Transaction(LocalDateTime.now(),"Transaction 3", OUTCOME,100.0));

        // Insert in at beginning
        account.postTransaction(new Transaction(LocalDateTime.now().minusDays(1),"Transaction 0", OUTCOME,100.0));

        account.getTransactions()
                .stream()
                .sorted(Comparator.comparing(Transaction::getDate).reversed())
                .forEach(t -> System.out.println(t.getDescription()+" | "+t.getAmount()+ "|"+t.getBalance() + "|"+ t.getDate()));

        Optional<Transaction> head = account.getHeadTransaction();
        assertTrue(head.isPresent());
        assertEquals(600.0,head.get().getBalance());
    }

    @Test
    public void should_post_transaction_in_the_middle() throws InterruptedException {
        Account account = new Account(1000.00);
        account.postTransaction(new Transaction(LocalDateTime.now().minusDays(3),"Transaction 1", OUTCOME,100.0));
        account.postTransaction(new Transaction(LocalDateTime.now().minusDays(2),"Transaction 2", OUTCOME,100.0));
        account.postTransaction(new Transaction(LocalDateTime.now().minusDays(1),"Transaction 3", OUTCOME,100.0));

        account.getTransactions()
                .stream()
                .sorted(Comparator.comparing(Transaction::getDate).reversed())
                .forEach(t -> System.out.println(t.getDescription()+" | "+t.getAddAmount()+ "|"+t.getBalance() + "|"+ t.getDate()));
        System.out.println("Balance:"+account.getBalance());

        // Insert in the middle
        account.postTransaction(new Transaction(LocalDateTime.now().minusDays(2),"Transaction 0", OUTCOME,100.0));

        account.getTransactions()
                .stream()
                .sorted(Comparator.comparing(Transaction::getDate).reversed())
                .forEach(t -> System.out.println(t.getDescription()+" | "+t.getAddAmount()+ "|"+t.getBalance() + "|"+ t.getDate()));
        System.out.println("Balance:"+account.getBalance());

        Optional<Transaction> head = account.getHeadTransaction();
        assertTrue(head.isPresent());
        assertEquals(600.0,head.get().getBalance());
        assertEquals(600.0,account.getBalance());
    }

    @Test
    public void should_remove_transaction_at_beginning(){
        Account account = new Account(1000.00);
        Transaction t01 = new Transaction(LocalDateTime.now().minusDays(3),"Transaction 1", OUTCOME,100.0);
        t01.setId("1");
        Transaction t02 = new Transaction(LocalDateTime.now().minusDays(2),"Transaction 2", OUTCOME,100.0);
        t02.setId("2");
        Transaction t03 = new Transaction(LocalDateTime.now().minusDays(1),"Transaction 3", OUTCOME,100.0);
        t03.setId("3");

        account.postTransaction(t01);
        account.postTransaction(t02);
        account.postTransaction(t03);

        account.getTransactions()
                .stream()
                .sorted(Comparator.comparing(Transaction::getDate).reversed())
                .forEach(t -> System.out.println(t.getDescription()+" | "+t.getAddAmount()+ "|"+t.getBalance() + "|"+ t.getDate()));
        System.out.println("Balance:"+account.getBalance());

        account.undoTransaction("2");

        account.getTransactions()
                .stream()
                .sorted(Comparator.comparing(Transaction::getDate).reversed())
                .forEach(t -> System.out.println(t.getDescription()+" | "+t.getAddAmount()+ "|"+t.getBalance() + "|"+ t.getDate()));
        System.out.println("Balance:"+account.getBalance());

        Optional<Transaction> head = account.getHeadTransaction();
        assertTrue(head.isPresent());
        assertEquals("Transaction 3",head.get().getDescription());
        assertEquals(800,head.get().getBalance());
        assertEquals(800.0,account.getBalance());
    }
}
