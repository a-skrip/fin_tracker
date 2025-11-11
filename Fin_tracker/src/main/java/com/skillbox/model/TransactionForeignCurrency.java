package com.skillbox.model;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@ToString(callSuper = true)
public class TransactionForeignCurrency extends Transaction implements CurrencyConvertible{

    private  BigDecimal course;

    public TransactionForeignCurrency(int accountId,
                                      int transactionId,
                                      LocalDateTime date,
                                      String category,
                                      BigDecimal amount,
                                      TransactionType type,
                                      BigDecimal course) {
        super(accountId, transactionId, date, category, amount, type);
        this.course = course;
    }

    @Override
    public BigDecimal convertToBaseCurrency(BigDecimal amount) {
        return this.getAmount().multiply(amount);
    }

}
