package com.jnksoft.account.boundary.ds.factory;

import com.jnksoft.account.boundary.ds.input.CreateAccountInputPort;
import com.jnksoft.account.boundary.ds.input.InputPort;
import com.jnksoft.account.boundary.ds.input.PostTransactionInputPort;
import com.jnksoft.account.model.enums.TransactionType;
import org.joda.time.LocalDateTime;

import java.util.Map;

public class InputPortFactory {
    public static InputPort make(String name, Map<String,Object> params){
        if("CreateAccountInputPort".equals(name))
            return makeCreateAccountInputPort(params);
        if("PostTransactionInputPort".equals(name))
            return makePostTransactionInputPort(params);
        return null;
    }

    private static InputPort makePostTransactionInputPort(Map<String, Object> params) {
        PostTransactionInputPort postTransactionInputPort = new PostTransactionInputPort();
        if(params.get("accountId") != null)
            postTransactionInputPort.accountId = params.get("accountId").toString();
        if(params.get("date") != null)
            postTransactionInputPort.date = (LocalDateTime) params.get("date");
        if(params.get("description") != null)
            postTransactionInputPort.description = params.get("accountId").toString();
        if(params.get("amount") != null)
            postTransactionInputPort.amount = Double.valueOf(params.get("amount").toString());
        if(params.get("type") != null)
            postTransactionInputPort.type = TransactionType.valueOf(params.get("type").toString());

        return postTransactionInputPort;
    }

    private static InputPort makeCreateAccountInputPort(Map<String, Object> params) {
        CreateAccountInputPort createAccountInputPort = new CreateAccountInputPort();
        if(params.get("description") != null)
            createAccountInputPort.description = params.get("description").toString();
        if(params.get("initialBalance") != null)
            createAccountInputPort.initialBalance = Double.valueOf(params.get("initialBalance").toString());

        return createAccountInputPort;
    }

}
