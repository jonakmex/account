package com.jnksoft.account.factory;

import com.jnksoft.account.boundary.Interactor;
import com.jnksoft.account.gateway.AccountRetrieverGateway;
import com.jnksoft.account.gateway.AccountSaveGateway;
import com.jnksoft.account.gateway.TransactionSaveGateway;
import com.jnksoft.account.interactor.CreateAccountInteractor;
import com.jnksoft.account.interactor.EditAccountInteractor;
import com.jnksoft.account.interactor.PostTransactionInteractor;
import com.jnksoft.account.interactor.TransferInteractor;

import java.util.Map;

public class InteractorFactory {
    private Map<String,Object> context;

    public InteractorFactory(Map<String, Object> context) {
        this.context = context;
    }

    public  Interactor make(String name){
        if("CreateAccountInteractor".equals(name))
            return makeCreateAccountInteractor();
        if("PostTransactionInteractor".equals(name))
            return makePostTransactionInteractor();
        if("TransferInteractor".equals(name))
            return makeTransferInteractor();
        if("EditAccountInteractor".equals(name))
            return makeEditAccountInteractor();

        return null;
    }

    private Interactor makeEditAccountInteractor() {
        AccountSaveGateway accountSaveGateway= (AccountSaveGateway) context.get("accountSaveGateway");
        AccountRetrieverGateway accountRetrieverGateway= (AccountRetrieverGateway) context.get("accountRetrieverGateway");
        TransactionSaveGateway transactionSaveGateway = (TransactionSaveGateway) context.get("transactionSaveGateway");
        return new EditAccountInteractor(accountSaveGateway,accountRetrieverGateway,transactionSaveGateway);
    }

    private Interactor makeTransferInteractor() {
        AccountSaveGateway accountSaveGateway= (AccountSaveGateway) context.get("accountSaveGateway");
        AccountRetrieverGateway accountRetrieverGateway= (AccountRetrieverGateway) context.get("accountRetrieverGateway");
        TransactionSaveGateway transactionSaveGateway = (TransactionSaveGateway) context.get("transactionSaveGateway");
        return new TransferInteractor(accountSaveGateway,accountRetrieverGateway,transactionSaveGateway);
    }

    private Interactor makePostTransactionInteractor() {
        AccountSaveGateway accountSaveGateway= (AccountSaveGateway) context.get("accountSaveGateway");
        AccountRetrieverGateway accountRetrieverGateway= (AccountRetrieverGateway) context.get("accountRetrieverGateway");
        TransactionSaveGateway transactionSaveGateway = (TransactionSaveGateway) context.get("transactionSaveGateway");
        return new PostTransactionInteractor(accountSaveGateway,accountRetrieverGateway,transactionSaveGateway);
    }

    private Interactor makeCreateAccountInteractor() {
        AccountSaveGateway accountSaveGateway = (AccountSaveGateway) context.get("accountSaveGateway");
        return new CreateAccountInteractor(accountSaveGateway);
    }
}
