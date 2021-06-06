package com.jnksoft.account.interactor;

import com.jnksoft.account.boundary.Callback;
import com.jnksoft.account.boundary.Interactor;
import com.jnksoft.account.boundary.ds.exceptions.BusinessException;
import com.jnksoft.account.boundary.ds.exceptions.FeedbackItem;
import com.jnksoft.account.boundary.ds.input.EditAccountInputPort;
import com.jnksoft.account.boundary.ds.input.InputPort;
import com.jnksoft.account.boundary.ds.output.CreateAccountOutputPort;
import com.jnksoft.account.boundary.ds.output.EditAccountOutputPort;
import com.jnksoft.account.boundary.ds.output.OutputPort;
import com.jnksoft.account.gateway.AccountRetrieverGateway;
import com.jnksoft.account.gateway.AccountSaveGateway;
import com.jnksoft.account.gateway.TransactionSaveGateway;
import com.jnksoft.account.model.Account;
import com.jnksoft.account.model.Transaction;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EditAccountInteractor implements Interactor {
    private AccountSaveGateway accountSaveGateway;
    private AccountRetrieverGateway accountRetrieverGateway;
    private TransactionSaveGateway transactionSaveGateway;

    public EditAccountInteractor(AccountSaveGateway accountSaveGateway,
                              AccountRetrieverGateway accountRetrieverGateway,
                              TransactionSaveGateway transactionSaveGateway) {
        this.accountSaveGateway = accountSaveGateway;
        this.accountRetrieverGateway = accountRetrieverGateway;
        this.transactionSaveGateway = transactionSaveGateway;
    }

    @Override
    public void execute(InputPort input, Callback callback) {
        try {
            input.validate();
            EditAccountInputPort editAccountInputPort = (EditAccountInputPort) input;
            Account account = accountRetrieverGateway.findById(editAccountInputPort.accountId);
            if(account == null) {
                callback.execute(mapToOutputPortFailAccountNotFound(editAccountInputPort.accountId));
                return;
            }
            try {
                account.setDescription(editAccountInputPort.description);
                if(isInitialBalanceChanged(editAccountInputPort, account)){
                    editInitialBalance(account,editAccountInputPort.initialBalance);
                }
                account.getTransactions().stream().forEach(transaction -> transactionSaveGateway.save(transaction));
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

    private boolean isInitialBalanceChanged(EditAccountInputPort editAccountInputPort, Account account) {
        return editAccountInputPort.initialBalance != null && account.getInitialBalance() != editAccountInputPort.initialBalance;
    }

    private void editInitialBalance(Account account, Double initialBalance) {
        account.setInitialBalance(initialBalance);
        List<Transaction> transactions = accountRetrieverGateway.findTransactions();
        account.setTransactions(transactions);
        account.totalize();
    }

    private OutputPort mapToOutputPortSuccess(Account account){
        EditAccountOutputPort editAccountOutputPort = new EditAccountOutputPort();
        editAccountOutputPort.success = Boolean.TRUE;
        editAccountOutputPort.id = account.getId();
        editAccountOutputPort.description = account.getDescription();
        editAccountOutputPort.balance = account.getBalance();
        return editAccountOutputPort;
    }

    private OutputPort mapToOutputPortFailAccountNotFound(String accountId){
        OutputPort outputPort = new OutputPort();
        outputPort.success = Boolean.FALSE;
        Map<String,String> params = new HashMap<>();
        params.put("accountId",accountId);
        FeedbackItem feedbackItem = FeedbackItem.make("accountId","MSG_ERR_002",params);
        outputPort.runtimeException = BusinessException.make(Arrays.asList(feedbackItem));
        return outputPort;
    }

    private OutputPort mapToOutputPortFail(RuntimeException runtimeException){
        OutputPort outputPort = new OutputPort();
        outputPort.success = Boolean.FALSE;
        outputPort.runtimeException = runtimeException;
        return outputPort;
    }
}
