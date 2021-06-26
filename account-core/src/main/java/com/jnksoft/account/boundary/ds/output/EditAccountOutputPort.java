package com.jnksoft.account.boundary.ds.output;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public class EditAccountOutputPort extends OutputPort {
    public String id;
    public String description;
    public Double balance;
}
