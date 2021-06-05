package com.jnksoft.account.boundary.ds.output;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public class PostTransactionOutputPort extends OutputPort{
    public String accountId;
    public String description;
    public Double balance;
}
