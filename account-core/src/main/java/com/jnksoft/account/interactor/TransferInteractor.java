package com.jnksoft.account.interactor;

import com.jnksoft.account.boundary.Callback;
import com.jnksoft.account.boundary.Interactor;
import com.jnksoft.account.boundary.ds.exceptions.BusinessException;
import com.jnksoft.account.boundary.ds.exceptions.FeedbackItem;
import com.jnksoft.account.boundary.ds.input.InputPort;
import com.jnksoft.account.boundary.ds.input.TransferInputPort;
import com.jnksoft.account.boundary.ds.model.AccountBalance;
import com.jnksoft.account.boundary.ds.output.OutputPort;
import com.jnksoft.account.boundary.ds.output.TransferOutputPort;
import com.jnksoft.account.gateway.AccountRetrieverGateway;
import com.jnksoft.account.gateway.AccountSaveGateway;
import com.jnksoft.account.gateway.TransactionSaveGateway;
import com.jnksoft.account.model.Account;
import com.jnksoft.account.model.Transaction;
import com.jnksoft.account.model.enums.TransactionType;
import org.joda.time.LocalDateTime;

import java.util.*;

public class TransferInteractor implements Interactor {

    private AccountSaveGateway accountSaveGateway;
    private AccountRetrieverGateway accountRetrieverGateway;
    private TransactionSaveGateway transactionSaveGateway;

    public TransferInteractor(AccountSaveGateway accountSaveGateway,
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
            TransferInputPort transferInputPort = (TransferInputPort) input;
            try {
                Account fromAccount = accountRetrieverGateway.findById(transferInputPort.fromAccountId);
                if(fromAccount == null)
                    callback.execute(mapToOutputPortFailAccountNotFound(transferInputPort.fromAccountId));

                findTransactionsToAffect(fromAccount,transferInputPort.date);

                Account toAccount = accountRetrieverGateway.findById(transferInputPort.toAccountId);
                if(toAccount == null)
                    callback.execute(mapToOutputPortFailAccountNotFound(transferInputPort.toAccountId));

                findTransactionsToAffect(toAccount,transferInputPort.date);

                postTransaction(fromAccount,new Transaction(transferInputPort.date
                        , fromAccount.getDescription()+"->"+toAccount.getDescription()
                        , TransactionType.EXPENSE
                        ,transferInputPort.amount));
                postTransaction(toAccount,new Transaction(transferInputPort.date
                        ,fromAccount.getDescription()+"->"+toAccount.getDescription()
                        ,TransactionType.INCOME
                        ,transferInputPort.amount));

                callback.execute(mapToOutputPortSuccess(fromAccount,toAccount));
            }
            catch (RuntimeException runtimeException){
                callback.execute(mapToOutputPortFail(runtimeException));
            }
        }
        catch (BusinessException businessException){
            callback.execute(mapToOutputPortFail(businessException));
        }
    }

    private void findTransactionsToAffect(Account account, LocalDateTime date){
        List<Transaction> transactions = accountRetrieverGateway.findTopTransactions(account.getId(),date);
        if(transactions == null)
            transactions = new ArrayList<>();

        Transaction bottomTransaction = accountRetrieverGateway.findBottomTransaction(account.getId(),date);
        if(bottomTransaction != null)
            transactions.add(bottomTransaction);

        account.setTransactions(transactions);
    }

    private Account postTransaction(Account account,Transaction transaction){
        account.postTransaction(transaction);
        account.getTransactions().stream().forEach(t -> transactionSaveGateway.save(t));
        return accountSaveGateway.save(account);
    }

    private TransferOutputPort mapToOutputPortSuccess(Account fromAccount,Account toAccount){
        TransferOutputPort transferOutputPort = new TransferOutputPort();
        transferOutputPort.success = Boolean.TRUE;
        AccountBalance fromAccountBalance = new AccountBalance();
        fromAccountBalance.id = fromAccount.getId();
        fromAccountBalance.description = fromAccount.getDescription();
        fromAccountBalance.balance = fromAccount.getBalance();
        AccountBalance toAccountBalance = new AccountBalance();
        toAccountBalance.id = toAccount.getId();
        toAccountBalance.description = toAccount.getDescription();
        toAccountBalance.balance = toAccount.getBalance();
        transferOutputPort.fromAccount = fromAccountBalance;
        transferOutputPort.toAccount = toAccountBalance;
        return transferOutputPort;
    }

    private OutputPort mapToOutputPortFail(RuntimeException runtimeException){
        OutputPort outputPort = new OutputPort();
        outputPort.success = Boolean.FALSE;
        outputPort.runtimeException = runtimeException;
        return outputPort;
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
}
