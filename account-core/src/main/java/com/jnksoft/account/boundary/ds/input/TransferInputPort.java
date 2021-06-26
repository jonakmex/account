package com.jnksoft.account.boundary.ds.input;

import com.jnksoft.account.boundary.ds.exceptions.BusinessException;
import com.jnksoft.account.boundary.ds.exceptions.FeedbackItem;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.joda.time.LocalDateTime;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
@EqualsAndHashCode(callSuper=false)
public class TransferInputPort extends InputPort {
    public String fromAccountId;
    public String toAccountId;
    public Double amount;
    public LocalDateTime date;

    @Override
    public void validate() {
        List<FeedbackItem> feedback = new ArrayList<>();

        if(fromAccountId == null){
            Map<String,String> params = new HashMap<>();
            params.put("fromAccountId",fromAccountId);
            feedback.add(FeedbackItem.make("fromAccountId","MSG_ERR_001",params));
        }

        if(toAccountId == null){
            Map<String,String> params = new HashMap<>();
            params.put("toAccountId",toAccountId);
            feedback.add(FeedbackItem.make("toAccountId","MSG_ERR_001",params));
        }

        if(amount == null){
            Map<String,String> params = new HashMap<>();
            params.put("amount",amount.toString());
            feedback.add(FeedbackItem.make("amount","MSG_ERR_001",params));
        }

        if(date == null){
            Map<String,String> params = new HashMap<>();
            feedback.add(FeedbackItem.make("date","MSG_ERR_001"));
        }

        if(!feedback.isEmpty())
            throw BusinessException.make(feedback);
    }
}
