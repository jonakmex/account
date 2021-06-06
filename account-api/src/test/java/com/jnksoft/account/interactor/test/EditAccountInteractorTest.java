package com.jnksoft.account.interactor.test;

import com.jnksoft.account.boundary.Interactor;
import com.jnksoft.account.boundary.ds.factory.InputPortFactory;
import com.jnksoft.account.boundary.ds.input.InputPort;
import com.jnksoft.account.boundary.ds.output.EditAccountOutputPort;
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

public class EditAccountInteractorTest {
    @Test
    public void should_edit_description() throws InterruptedException {
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

        Interactor editAccountInteractor = interactorFactory.make("EditAccountInteractor");
        Map<String,Object> params = new HashMap<>();
        params.put("accountId","1234567890");
        String newDescription = "Account A";
        params.put("description","Account A");
        InputPort inputPort = InputPortFactory.make("EditAccountInputPort",params);

        editAccountInteractor.execute(inputPort,(response)->{
            assertTrue(response.success);
            EditAccountOutputPort editAccountOutputPort = (EditAccountOutputPort) response;
            assertEquals(newDescription,editAccountOutputPort.description);
        });
    }

    @Test
    public void should_edit_initial_balance() throws InterruptedException {
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
        when(accountRetrieverGateway.findTransactions()).thenReturn(account.getTransactions());

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

        Interactor editAccountInteractor = interactorFactory.make("EditAccountInteractor");
        Map<String,Object> params = new HashMap<>();
        params.put("accountId","1234567890");
        params.put("initialBalance",250);
        InputPort inputPort = InputPortFactory.make("EditAccountInputPort",params);

        editAccountInteractor.execute(inputPort,(response)->{
            assertTrue(response.success);
            EditAccountOutputPort editAccountOutputPort = (EditAccountOutputPort) response;
            assertEquals(750,editAccountOutputPort.balance);
        });
    }
}
