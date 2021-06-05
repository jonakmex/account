package com.jnksoft.account.interactor;

import com.jnksoft.account.boundary.Callback;
import com.jnksoft.account.boundary.Interactor;
import com.jnksoft.account.boundary.ds.exceptions.BusinessException;
import com.jnksoft.account.boundary.ds.exceptions.FeedbackItem;
import com.jnksoft.account.boundary.ds.input.InputPort;
import com.jnksoft.account.boundary.ds.input.PostTransactionInputPort;
import com.jnksoft.account.boundary.ds.output.OutputPort;
import com.jnksoft.account.boundary.ds.output.PostTransactionOutputPort;
import com.jnksoft.account.gateway.AccountRetrieverGateway;
import com.jnksoft.account.gateway.AccountSaveGateway;
import com.jnksoft.account.gateway.TransactionSaveGateway;
import com.jnksoft.account.model.Account;
import com.jnksoft.account.model.Transaction;

import java.util.*;

public class PostTransactionInteractor implements Interactor {

    private AccountSaveGateway accountSaveGateway;
    private AccountRetrieverGateway accountRetrieverGateway;
    private TransactionSaveGateway transactionSaveGateway;

    public PostTransactionInteractor(AccountSaveGateway accountSaveGateway,
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
            PostTransactionInputPort postTransactionInputPort = (PostTransactionInputPort) input;
            try {
                Account account = accountRetrieverGateway.findById(postTransactionInputPort.accountId);
                if(account == null)
                    callback.execute(mapToOutputPortFailAccountNotFound(postTransactionInputPort.accountId));

                List<Transaction> transactions = accountRetrieverGateway.findTopTransactions(account.getId(),postTransactionInputPort.getDate());
                if(transactions == null)
                    transactions = new ArrayList<>();

                Transaction bottomTransaction = accountRetrieverGateway.findBottomTransaction(account.getId(),postTransactionInputPort.getDate());
                if(bottomTransaction != null)
                    transactions.add(bottomTransaction);

                account.setTransactions(transactions);
                account.postTransaction(new Transaction(postTransactionInputPort.date
                        ,postTransactionInputPort.description
                        ,postTransactionInputPort.type
                        ,postTransactionInputPort.amount));

                account.getTransactions().stream().forEach(transaction -> {
                    transactionSaveGateway.save(transaction);
                });
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

    private PostTransactionOutputPort mapToOutputPortSuccess(Account account){
        PostTransactionOutputPort postTransactionOutputPort = new PostTransactionOutputPort();
        postTransactionOutputPort.success = Boolean.TRUE;
        postTransactionOutputPort.accountId = account.getId();
        postTransactionOutputPort.description = account.getDescription();
        postTransactionOutputPort.balance = account.getBalance();
        return postTransactionOutputPort;
    }
}
