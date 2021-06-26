package com.jnksoft.account.boundary.ds.exceptions;

import lombok.Data;

import java.util.Map;
@Data
public class FeedbackItem {
    public String field;
    public String code;
    public Map<String,String> params;

    public static FeedbackItem make(String field,String code,Map<String,String> params){
        FeedbackItem feedbackItem = new FeedbackItem();
        feedbackItem.field = field;
        feedbackItem.code = code;
        feedbackItem.params = params;
        return feedbackItem;
    }
    public static FeedbackItem make(String field,String code){
        FeedbackItem feedbackItem = new FeedbackItem();
        feedbackItem.field = field;
        feedbackItem.code = code;
        return feedbackItem;
    }
}
