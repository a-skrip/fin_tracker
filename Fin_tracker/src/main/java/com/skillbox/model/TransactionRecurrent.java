package com.skillbox.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@ToString
public class TransactionRecurrent extends Transaction implements Recurring {

    private RecurrencePattern pattern;
    private int repeat;

    public TransactionRecurrent(int accountId, int transactionId, LocalDateTime date, String category, BigDecimal amount, TransactionType type) {
        super(accountId, transactionId, date, category, amount, type);
    }


    //TODO Реализовать методы
    @Override
    public LocalDateTime getNextOccurrence(LocalDateTime dateTime) {
        return null;
    }

    @Override
    public LocalDateTime getPreviousOccurrence(LocalDateTime dateTime) {
        return null;
    }

    @Override
    public BigDecimal getTransactionAmount(LocalDateTime dateTime) {
        return null;
    }

    @Override
    public boolean isExecutedBetween(LocalDateTime startDate, LocalDateTime endDate) {
        return false;
    }
}
