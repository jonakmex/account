package com.jnksoft.account.boundary.ds.factory;

import com.jnksoft.account.boundary.ds.input.CreateAccountInputPort;
import com.jnksoft.account.boundary.ds.input.InputPort;

import java.util.Map;

public class InputPortFactory {
    public static InputPort make(String name, Map<String,Object> params){
        if("CreateAccountInputPort".equals(name))
            return makeCreateAccountInputPort(params);
        return null;
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
