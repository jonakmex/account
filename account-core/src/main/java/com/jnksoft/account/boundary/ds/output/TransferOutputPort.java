package com.jnksoft.account.boundary.ds.output;

import com.jnksoft.account.boundary.ds.model.AccountBalance;

public class TransferOutputPort extends OutputPort {
    public AccountBalance fromAccount;
    public AccountBalance toAccount;
}
