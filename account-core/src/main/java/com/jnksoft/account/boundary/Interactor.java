package com.jnksoft.account.boundary;

import com.jnksoft.account.boundary.ds.input.InputPort;

public interface Interactor {
    void execute(InputPort input, Callback callback);
}
