package com.skillbox.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;

@Getter
@Setter
@ToString
public class TransactionRegular extends Transaction{

    public TransactionRegular(int accountId,
                              int transactionId,
                              LocalDateTime date,
                              String category,
                              BigDecimal amount,
                              TransactionType type) {
        super(accountId, transactionId, date, category, amount.setScale(2, RoundingMode.HALF_UP), type);
    }
}
