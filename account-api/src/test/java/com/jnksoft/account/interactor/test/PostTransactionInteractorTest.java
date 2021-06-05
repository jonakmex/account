package com.jnksoft.account.interactor.test;

import com.jnksoft.account.boundary.Interactor;
import com.jnksoft.account.boundary.ds.factory.InputPortFactory;
import com.jnksoft.account.boundary.ds.input.InputPort;
import com.jnksoft.account.boundary.ds.output.PostTransactionOutputPort;
import com.jnksoft.account.factory.InteractorFactory;
import com.jnksoft.account.gateway.AccountRetrieverGateway;
import com.jnksoft.account.gateway.AccountSaveGateway;
import com.jnksoft.account.gateway.TransactionSaveGateway;
import com.jnksoft.account.model.Account;
import com.jnksoft.account.model.Transaction;
import com.jnksoft.account.model.enums.TransactionType;
import org.joda.time.LocalDateTime;
import org.junit.jupiter.api.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.jnksoft.account.model.enums.TransactionType.EXPENSE;
import static com.jnksoft.account.model.enums.TransactionType.INCOME;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class PostTransactionInteractorTest {
    @Test
    public void should_post_transaction() throws InterruptedException {
        Account account = new Account();
        account.postTransaction(new Transaction(LocalDateTime.now(),"Transaction 1", INCOME,1000.0));
        Thread.sleep(1);
        account.postTransaction(new Transaction(LocalDateTime.now(),"Transaction 2", EXPENSE,100.0));
        Thread.sleep(1);
        account.postTransaction(new Transaction(LocalDateTime.now(),"Transaction 3", EXPENSE,100.0));
        Thread.sleep(1);
        account.postTransaction(new Transaction(LocalDateTime.now(),"Transaction 4", EXPENSE,100.0));
        Thread.sleep(1);
        account.postTransaction(new Transaction(LocalDateTime.now(),"Transaction 5", EXPENSE,100.0));
        Thread.sleep(1);
        account.postTransaction(new Transaction(LocalDateTime.now(),"Transaction 6", EXPENSE,100.0));

        AccountRetrieverGateway accountRetrieverGateway = mock(AccountRetrieverGateway.class);
        when(accountRetrieverGateway.findById(any())).thenReturn(account);
        when(accountRetrieverGateway.findTopTransactions(any(),any())).thenAnswer((Answer<List<Transaction>>) invocation -> {
            Object[] args = invocation.getArguments();
            return account.getTopTransactions((LocalDateTime)args[1]);
        });

        when(accountRetrieverGateway.findBottomTransaction(any(),any())).thenAnswer((Answer<Transaction>) invocation -> {
            Object[] args = invocation.getArguments();
            Optional<Transaction> bottomTransaction = account.getBottomTransaction((LocalDateTime)args[1]);
            if(bottomTransaction.isPresent())
                return bottomTransaction.get();
            else
                return null;
        });

        TransactionSaveGateway transactionSaveGateway = mock(TransactionSaveGateway.class);
        AccountSaveGateway accountSaveGateway = mock(AccountSaveGateway.class);
        when(accountSaveGateway.save(any())).thenReturn(account);

        Map<String,Object> context = new HashMap<>();
        context.put("accountRetrieverGateway",accountRetrieverGateway);
        context.put("transactionSaveGateway",transactionSaveGateway);
        context.put("accountSaveGateway",accountSaveGateway);
        InteractorFactory interactorFactory = new InteractorFactory(context);
        Interactor postTransactionInteractor = interactorFactory.make("PostTransactionInteractor");
        Map<String,Object> params = new HashMap<>();
        params.put("accountId","1234567890");
        params.put("date",LocalDateTime.now());
        params.put("description","Transaction 7");
        params.put("amount",100.0);
        params.put("type", "EXPENSE");
        InputPort inputPort = InputPortFactory.make("PostTransactionInputPort",params);

        postTransactionInteractor.execute(inputPort,(response)->{
            assertTrue(response.success);
            PostTransactionOutputPort postTransactionOutputPort = (PostTransactionOutputPort) response;
            assertEquals(400.0,postTransactionOutputPort.balance);
        });
    }

    @Test
    public void should_post_transaction_at_the_beginning() throws InterruptedException {
        Account account = new Account(1000.00);
        account.postTransaction(new Transaction(LocalDateTime.now(),"Transaction 1", EXPENSE,100.0));
        Thread.sleep(1);
        account.postTransaction(new Transaction(LocalDateTime.now(),"Transaction 2", EXPENSE,100.0));
        Thread.sleep(1);
        account.postTransaction(new Transaction(LocalDateTime.now(),"Transaction 3", EXPENSE,100.0));

        AccountRetrieverGateway accountRetrieverGateway = mock(AccountRetrieverGateway.class);
        when(accountRetrieverGateway.findById(any())).thenReturn(account);
        when(accountRetrieverGateway.findTopTransactions(any(),any())).thenAnswer((Answer<List<Transaction>>) invocation -> {
            Object[] args = invocation.getArguments();
            return account.getTopTransactions((LocalDateTime)args[1]);
        });

        when(accountRetrieverGateway.findBottomTransaction(any(),any())).thenAnswer((Answer<Transaction>) invocation -> {
            Object[] args = invocation.getArguments();
            Optional<Transaction> bottomTransaction = account.getBottomTransaction((LocalDateTime)args[1]);
            if(bottomTransaction.isPresent())
                return bottomTransaction.get();
            else
                return null;
        });

        TransactionSaveGateway transactionSaveGateway = mock(TransactionSaveGateway.class);
        AccountSaveGateway accountSaveGateway = mock(AccountSaveGateway.class);
        when(accountSaveGateway.save(any())).thenReturn(account);

        Map<String,Object> context = new HashMap<>();
        context.put("accountRetrieverGateway",accountRetrieverGateway);
        context.put("transactionSaveGateway",transactionSaveGateway);
        context.put("accountSaveGateway",accountSaveGateway);
        InteractorFactory interactorFactory = new InteractorFactory(context);
        Interactor postTransactionInteractor = interactorFactory.make("PostTransactionInteractor");
        Map<String,Object> params = new HashMap<>();
        params.put("accountId","1234567890");
        params.put("date",LocalDateTime.now().minusDays(1));
        params.put("description","Transaction 0");
        params.put("amount",100.0);
        params.put("type", "EXPENSE");
        InputPort inputPort = InputPortFactory.make("PostTransactionInputPort",params);

        postTransactionInteractor.execute(inputPort,(response)->{
            assertTrue(response.success);
            PostTransactionOutputPort postTransactionOutputPort = (PostTransactionOutputPort) response;
            assertEquals(600.0,postTransactionOutputPort.balance);
        });
    }

    @Test
    public void should_post_transaction_in_the_middle() throws InterruptedException {
        Account account = new Account(1000.00);
        account.postTransaction(new Transaction(LocalDateTime.now().minusDays(3),"Transaction 1", EXPENSE,100.0));
        account.postTransaction(new Transaction(LocalDateTime.now().minusDays(2),"Transaction 2", EXPENSE,100.0));
        account.postTransaction(new Transaction(LocalDateTime.now().minusDays(1),"Transaction 3", EXPENSE,100.0));

        AccountRetrieverGateway accountRetrieverGateway = mock(AccountRetrieverGateway.class);
        when(accountRetrieverGateway.findById(any())).thenReturn(account);
        when(accountRetrieverGateway.findTopTransactions(any(),any())).thenAnswer((Answer<List<Transaction>>) invocation -> {
            Object[] args = invocation.getArguments();
            return account.getTopTransactions((LocalDateTime)args[1]);
        });

        when(accountRetrieverGateway.findBottomTransaction(any(),any())).thenAnswer((Answer<Transaction>) invocation -> {
            Object[] args = invocation.getArguments();
            Optional<Transaction> bottomTransaction = account.getBottomTransaction((LocalDateTime)args[1]);
            if(bottomTransaction.isPresent())
                return bottomTransaction.get();
            else
                return null;
        });

        TransactionSaveGateway transactionSaveGateway = mock(TransactionSaveGateway.class);
        AccountSaveGateway accountSaveGateway = mock(AccountSaveGateway.class);
        when(accountSaveGateway.save(any())).thenReturn(account);

        Map<String,Object> context = new HashMap<>();
        context.put("accountRetrieverGateway",accountRetrieverGateway);
        context.put("transactionSaveGateway",transactionSaveGateway);
        context.put("accountSaveGateway",accountSaveGateway);
        InteractorFactory interactorFactory = new InteractorFactory(context);
        Interactor postTransactionInteractor = interactorFactory.make("PostTransactionInteractor");
        Map<String,Object> params = new HashMap<>();
        params.put("accountId","1234567890");
        params.put("date",LocalDateTime.now().minusDays(2));
        params.put("description","Transaction 0");
        params.put("amount",100.0);
        params.put("type", "EXPENSE");
        InputPort inputPort = InputPortFactory.make("PostTransactionInputPort",params);

        postTransactionInteractor.execute(inputPort,(response)->{
            assertTrue(response.success);
            PostTransactionOutputPort postTransactionOutputPort = (PostTransactionOutputPort) response;
            assertEquals(600.0,postTransactionOutputPort.balance);
        });
    }
}
