package com.jnksoft.account.boundary;

import com.jnksoft.account.boundary.ds.output.OutputPort;

@FunctionalInterface
public interface Callback {
    void execute(OutputPort output);
}
