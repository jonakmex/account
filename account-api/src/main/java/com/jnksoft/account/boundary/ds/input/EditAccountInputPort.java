package com.jnksoft.account.boundary.ds.input;

import com.jnksoft.account.boundary.ds.exceptions.BusinessException;
import com.jnksoft.account.boundary.ds.exceptions.FeedbackItem;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
@EqualsAndHashCode(callSuper=false)
public class EditAccountInputPort extends InputPort {
    public String accountId;
    public String description;
    public Double initialBalance;

    @Override
    public void validate() {
        List<FeedbackItem> feedback = new ArrayList<>();

        if(accountId == null || accountId.length() < 2){
            Map<String,String> params = new HashMap<>();
            params.put("accountId", accountId);
            feedback.add(FeedbackItem.make("accountId","MSG_ERR_001",params));
        }

        if(description != null && description.length() < 2){
            Map<String,String> params = new HashMap<>();
            params.put("description",description);
            feedback.add(FeedbackItem.make("description","MSG_ERR_001",params));
        }


        if(!feedback.isEmpty())
            throw BusinessException.make(feedback);
    }
}
