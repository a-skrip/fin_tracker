package com.skillbox.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@ToString(callSuper = true)
public class TransactionCommentable extends Transaction implements Commentable{

    private List<String> comments;

    public TransactionCommentable(int accountId,
                                  int transactionId,
                                  LocalDateTime date,
                                  String category,
                                  BigDecimal amount,
                                  TransactionType type) {
        super(accountId, transactionId, date, category, amount.setScale(2, RoundingMode.HALF_UP), type);
    }

    @Override
    public List<String> getComments() {
        return comments;
    }
}
