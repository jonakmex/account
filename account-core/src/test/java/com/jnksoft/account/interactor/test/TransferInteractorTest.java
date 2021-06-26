package com.jnksoft.account.interactor.test;

import com.jnksoft.account.boundary.Interactor;
import com.jnksoft.account.boundary.ds.factory.InputPortFactory;
import com.jnksoft.account.boundary.ds.input.InputPort;
import com.jnksoft.account.boundary.ds.output.TransferOutputPort;
import com.jnksoft.account.factory.InteractorFactory;
import com.jnksoft.account.gateway.AccountRetrieverGateway;
import com.jnksoft.account.gateway.AccountSaveGateway;
import com.jnksoft.account.gateway.TransactionSaveGateway;
import com.jnksoft.account.model.Account;
import com.jnksoft.account.model.Transaction;
import org.joda.time.LocalDateTime;
import org.junit.jupiter.api.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.util.*;

import static com.jnksoft.account.model.enums.TransactionType.EXPENSE;
import static com.jnksoft.account.model.enums.TransactionType.INCOME;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class TransferInteractorTest {
    @Test
    public void should_transfer_amount() throws InterruptedException {
        Account fromAccount = new Account();
        fromAccount.setId("1");
        fromAccount.setDescription("Account 1");
        fromAccount.postTransaction(new Transaction(LocalDateTime.now(),"Transaction 1", INCOME,1000.0));
        Thread.sleep(1);
        fromAccount.postTransaction(new Transaction(LocalDateTime.now(),"Transaction 2", EXPENSE,100.0));
        Thread.sleep(1);
        fromAccount.postTransaction(new Transaction(LocalDateTime.now(),"Transaction 3", EXPENSE,100.0));
        Thread.sleep(1);
        fromAccount.postTransaction(new Transaction(LocalDateTime.now(),"Transaction 4", EXPENSE,100.0));
        Thread.sleep(1);
        fromAccount.postTransaction(new Transaction(LocalDateTime.now(),"Transaction 5", EXPENSE,100.0));
        Thread.sleep(1);
        fromAccount.postTransaction(new Transaction(LocalDateTime.now(),"Transaction 6", EXPENSE,100.0));


        Account toAccount = new Account();
        toAccount.setId("2");
        toAccount.setDescription("Account 2");
        toAccount.postTransaction(new Transaction(LocalDateTime.now(),"Transaction 1", INCOME,2000.0));
        Thread.sleep(1);
        toAccount.postTransaction(new Transaction(LocalDateTime.now(),"Transaction 2", EXPENSE,100.0));
        Thread.sleep(1);
        toAccount.postTransaction(new Transaction(LocalDateTime.now(),"Transaction 3", EXPENSE,100.0));
        Thread.sleep(1);
        toAccount.postTransaction(new Transaction(LocalDateTime.now(),"Transaction 4", EXPENSE,100.0));
        Thread.sleep(1);
        toAccount.postTransaction(new Transaction(LocalDateTime.now(),"Transaction 5", EXPENSE,100.0));
        Thread.sleep(1);
        toAccount.postTransaction(new Transaction(LocalDateTime.now(),"Transaction 6", EXPENSE,100.0));

        List<Account> accounts = Arrays.asList(fromAccount,toAccount);

        AccountRetrieverGateway accountRetrieverGateway = mock(AccountRetrieverGateway.class);
        when(accountRetrieverGateway.findById(any())).thenAnswer((Answer<Account>) invocation -> {
            Object[] args = invocation.getArguments();
            String id = args[0].toString();
            Optional<Account> t = accounts.stream().filter(account -> id.equals(account.getId())).findFirst();
            if(t.isPresent())
                return t.get();
            return null;
        });
        when(accountRetrieverGateway.findTopTransactions(any(),any())).thenAnswer(new Answer<List<Transaction>>() {
            @Override
            public List<Transaction> answer(InvocationOnMock invocation) throws Throwable {
                Object[] args = invocation.getArguments();
                String id = args[0].toString();
                Optional<Account> t = accounts.stream().filter(account -> id.equals(account.getId())).findFirst();
                if(t.isPresent())
                    return t.get().getTopTransactions((LocalDateTime) args[1]);
                return null;
            }
        });
        when(accountRetrieverGateway.findBottomTransaction(any(),any())).thenAnswer((Answer<Transaction>) invocation -> {
            Object[] args = invocation.getArguments();
            String id = args[0].toString();
            Optional<Account> t = accounts.stream().filter(account -> id.equals(account.getId())).findFirst();
            if(t.isPresent()) {
                Optional<Transaction> bottomTransaction = t.get().getBottomTransaction((LocalDateTime) args[1]);
                if(bottomTransaction.isPresent())
                    return bottomTransaction.get();
            }
            return null;
        });

        TransactionSaveGateway transactionSaveGateway = mock(TransactionSaveGateway.class);
        AccountSaveGateway accountSaveGateway = mock(AccountSaveGateway.class);
        when(accountSaveGateway.save(any())).thenAnswer((Answer<Account>) invocation -> {
            Object[] args = invocation.getArguments();
            return (Account) args[0];
        });


        Map<String,Object> context = new HashMap<>();
        context.put("accountRetrieverGateway",accountRetrieverGateway);
        context.put("transactionSaveGateway",transactionSaveGateway);
        context.put("accountSaveGateway",accountSaveGateway);
        InteractorFactory interactorFactory = new InteractorFactory(context);
        Interactor transferInteractor = interactorFactory.make("TransferInteractor");


        Map<String,Object> params = new HashMap<>();
        params.put("fromAccountId","1");
        params.put("toAccountId","2");
        params.put("amount",100.0);
        params.put("date", LocalDateTime.now());
        InputPort inputPort = InputPortFactory.make("TransferInputPort",params);

        transferInteractor.execute(inputPort,(response)->{
            assertTrue(response.success);
            TransferOutputPort transferOutputPort = (TransferOutputPort) response;
            assertEquals(400,transferOutputPort.fromAccount.balance);
            assertEquals(1600,transferOutputPort.toAccount.balance);
        });

    }
}
