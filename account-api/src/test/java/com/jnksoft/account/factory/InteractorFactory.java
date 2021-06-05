package com.jnksoft.account.factory;

import com.jnksoft.account.boundary.Interactor;
import com.jnksoft.account.gateway.AccountSaveGateway;
import com.jnksoft.account.interactor.CreateAccountInteractor;

import java.util.Map;

public class InteractorFactory {
    private Map<String,Object> context;

    public InteractorFactory(Map<String, Object> context) {
        this.context = context;
    }

    public  Interactor make(String name){
        if("CreateAccountInteractor".equals(name))
            return makeCreateAccountInteractor();

        return null;
    }

    private Interactor makeCreateAccountInteractor() {
        AccountSaveGateway accountSaveGateway = (AccountSaveGateway) context.get("accountSaveGateway");
        return new CreateAccountInteractor(accountSaveGateway);
    }
}
