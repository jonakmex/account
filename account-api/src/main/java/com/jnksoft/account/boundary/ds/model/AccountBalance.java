package com.jnksoft.account.boundary.ds.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public class AccountBalance {
    public String id;
    public String description;
    public Double balance;
}
