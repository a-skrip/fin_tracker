package com.skillbox.model;

import lombok.*;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;

@Getter
@Setter
@ToString
public class TransactionForeignCurrency extends Transaction implements CurrencyConvertible{

    private  BigDecimal course;

    public TransactionForeignCurrency(int accountId,
                                      int transactionId,
                                      LocalDateTime date,
                                      String category,
                                      BigDecimal amount,
                                      TransactionType type,
                                      BigDecimal course) {
        super(accountId, transactionId, date, category, amount.setScale(2, RoundingMode.HALF_UP), type);
        this.course = course;
    }

    @Override
    public BigDecimal convertToBaseCurrency(BigDecimal amount) {
        return this.getAmount().multiply(amount).setScale(2, RoundingMode.HALF_UP);
    }

}
