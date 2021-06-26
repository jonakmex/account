package com.jnksoft.account.boundary.ds.output;

import lombok.Data;

@Data
public class OutputPort {
    public Boolean success;
    public RuntimeException runtimeException;
}
