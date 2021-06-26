package com.jnksoft.account.interactor;

import com.jnksoft.account.boundary.Callback;
import com.jnksoft.account.boundary.Interactor;
import com.jnksoft.account.boundary.ds.exceptions.BusinessException;
import com.jnksoft.account.boundary.ds.input.CreateAccountInputPort;
import com.jnksoft.account.boundary.ds.input.InputPort;
import com.jnksoft.account.boundary.ds.output.CreateAccountOutputPort;
import com.jnksoft.account.boundary.ds.output.OutputPort;
import com.jnksoft.account.gateway.AccountSaveGateway;
import com.jnksoft.account.model.Account;

public class CreateAccountInteractor implements Interactor {

    private AccountSaveGateway accountSaveGateway;

    public CreateAccountInteractor(AccountSaveGateway accountSaveGateway) {
        this.accountSaveGateway = accountSaveGateway;
    }

    @Override
    public void execute(InputPort input, Callback callback) {
        try {
            input.validate();
            CreateAccountInputPort createAccountInputPort = (CreateAccountInputPort) input;
            Account account = mapFromInputPort(createAccountInputPort);
            try {
                account = accountSaveGateway.save(account);
                callback.execute(mapToOutputPortSuccess(account));
            }
            catch (RuntimeException runtimeException){
                callback.execute(mapToOutputPortFail(runtimeException));
            }
        }
        catch (BusinessException businessException){
            callback.execute(mapToOutputPortFail(businessException));
        }
    }

    private Account mapFromInputPort(CreateAccountInputPort createAccountInputPort){
        Account account = new Account();
        account.setInitialBalance(createAccountInputPort.initialBalance);
        account.setDescription(createAccountInputPort.description);
        return account;
    }

    private OutputPort mapToOutputPortSuccess(Account account){
        CreateAccountOutputPort createAccountOutputPort = new CreateAccountOutputPort();
        createAccountOutputPort.success = Boolean.TRUE;
        createAccountOutputPort.id = account.getId();
        createAccountOutputPort.description = account.getDescription();
        return createAccountOutputPort;
    }

    private OutputPort mapToOutputPortFail(RuntimeException runtimeException){
        CreateAccountOutputPort createAccountOutputPort = new CreateAccountOutputPort();
        createAccountOutputPort.success = Boolean.FALSE;
        createAccountOutputPort.runtimeException = runtimeException;
        return createAccountOutputPort;
    }
}
