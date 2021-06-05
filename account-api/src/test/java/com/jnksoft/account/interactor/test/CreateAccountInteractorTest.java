package com.jnksoft.account.interactor.test;

import com.jnksoft.account.boundary.Interactor;
import com.jnksoft.account.boundary.ds.exceptions.BusinessException;
import com.jnksoft.account.boundary.ds.factory.InputPortFactory;
import com.jnksoft.account.boundary.ds.output.CreateAccountOutputPort;
import com.jnksoft.account.factory.InteractorFactory;
import com.jnksoft.account.gateway.AccountSaveGateway;
import com.jnksoft.account.model.Account;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class CreateAccountInteractorTest {
    @Test
    public void should_execute_interactor(){
        Account mockSaved = new Account();
        String id = "1010101010";
        String description = "Account Test";
        mockSaved.setId(id);
        mockSaved.setDescription(description);

        AccountSaveGateway accountSaveGateway = mock(AccountSaveGateway.class);
        when(accountSaveGateway.save(any())).thenReturn(mockSaved);
        Map<String,Object> context = new HashMap<>();
        context.put("accountSaveGateway",accountSaveGateway);
        InteractorFactory interactorFactory = new InteractorFactory(context);
        Interactor createAccountInteractor = interactorFactory.make("CreateAccountInteractor");
        Map<String,Object> params = new HashMap<>();
        params.put("initialBalance",100.0);
        params.put("description","Test");
        createAccountInteractor.execute(InputPortFactory.make("CreateAccountInputPort", params), (response) -> {
            if(response.success){
                CreateAccountOutputPort outputPort = (CreateAccountOutputPort) response;
                System.out.println(outputPort.id);
                assertEquals(id,outputPort.id);
            }
        });
    }

    @Test
    public void should_throw_exception_on_desc(){
        AccountSaveGateway accountSaveGateway = mock(AccountSaveGateway.class);
        Map<String,Object> context = new HashMap<>();
        context.put("accountSaveGateway",accountSaveGateway);
        InteractorFactory interactorFactory = new InteractorFactory(context);
        Interactor createAccountInteractor = interactorFactory.make("CreateAccountInteractor");
        Map<String,Object> params = new HashMap<>();
        params.put("initialBalance",100.0);
        params.put("description","A");
        createAccountInteractor.execute(InputPortFactory.make("CreateAccountInputPort", params), (response) -> {
            assertFalse(response.success);
            assertTrue(response.runtimeException instanceof BusinessException);
            BusinessException businessException = (BusinessException) response.runtimeException;
            businessException.feedback.stream().forEach(feedbackItem -> {
                System.out.println(feedbackItem.getField()+"|"+feedbackItem.getCode()+"|"+feedbackItem.getParams().get("description"));
            });
        });
    }
}
