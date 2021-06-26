package com.jnksoft.account.boundary.ds.input;

import lombok.Data;

@Data
public abstract class InputPort {

    public abstract void validate();
}
