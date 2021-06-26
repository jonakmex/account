package com.jnksoft.account.boundary.ds.exceptions;

import lombok.Data;

import java.util.List;
@Data
public class BusinessException extends RuntimeException {
    public List<FeedbackItem> feedback;

    public static BusinessException make(List<FeedbackItem> feedback){
        BusinessException businessException = new BusinessException();
        businessException.feedback = feedback;
        return businessException;
    }
}
