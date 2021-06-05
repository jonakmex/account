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
public class CreateAccountInputPort extends InputPort {
    public Double initialBalance;
    public String description;

    @Override
    public void validate() {
        List<FeedbackItem> feedback = new ArrayList<>();

        if(description == null || description.length() < 2){
            Map<String,String> params = new HashMap<>();
            params.put("description",description);
            feedback.add(FeedbackItem.make("description","MSG_ERR_001",params));
        }

        if(!feedback.isEmpty())
            throw BusinessException.make(feedback);
    }
}
