package com.jnksoft.account.boundary.ds.input;

import com.jnksoft.account.boundary.ds.exceptions.BusinessException;
import com.jnksoft.account.boundary.ds.exceptions.FeedbackItem;
import com.jnksoft.account.model.enums.TransactionType;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.joda.time.LocalDateTime;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
@EqualsAndHashCode(callSuper=false)
public class PostTransactionInputPort extends InputPort {
    public String accountId;
    public LocalDateTime date;
    public String description;
    public Double amount;
    public TransactionType type;

    @Override
    public void validate() {
        List<FeedbackItem> feedback = new ArrayList<>();

        if(accountId == null){
            Map<String,String> params = new HashMap<>();
            params.put("accountId",accountId);
            feedback.add(FeedbackItem.make("accountId","MSG_ERR_001",params));
        }

        if(amount == null){
            Map<String,String> params = new HashMap<>();
            params.put("amount",amount.toString());
            feedback.add(FeedbackItem.make("amount","MSG_ERR_001",params));
        }

        if(type == null){
            Map<String,String> params = new HashMap<>();
            params.put("type",type.toString());
            feedback.add(FeedbackItem.make("type","MSG_ERR_001",params));
        }

        if(description == null || description.length() < 2){
            Map<String,String> params = new HashMap<>();
            params.put("description",description);
            feedback.add(FeedbackItem.make("description","MSG_ERR_001",params));
        }

        if(!feedback.isEmpty())
            throw BusinessException.make(feedback);
    }
}
