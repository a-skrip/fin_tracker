package com.skillbox.model;

import com.skillbox.exception.UncorrectedDataTimeException;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAmount;

@Getter
@Setter
@ToString
public class TransactionRecurrent extends Transaction implements Recurring {

    private RecurrencePattern pattern;
    private int repeat;

    public TransactionRecurrent(int accountId,
                                int transactionId,
                                LocalDateTime date,
                                String category,
                                BigDecimal amount,
                                TransactionType type,
                                RecurrencePattern pattern,
                                int repeat
    ) {
        super(accountId, transactionId, date, category, amount, type);
        this.pattern = pattern;
        this.repeat = repeat;
    }


    //TODO Реализовать методы
    @Override
    public LocalDateTime getNextOccurrence(LocalDateTime dateTime) {
        LocalDateTime result = null;
        LocalDateTime start = this.getDate();
        if (dateTime.isBefore(start)) {
            throw new UncorrectedDataTimeException("Переданная дата раньше начала повторения транзакции!");
        }
        for (int i = 0; i < this.repeat; i++) {
            start = start.plus(this.pattern.getDuration());
            if (dateTime.isBefore(start)) {
                result = start;
                break;
            }
        }
        return result;
    }

    @Override
    public LocalDateTime getPreviousOccurrence(LocalDateTime dateTime) {
        LocalDateTime result = this.getDate();
        int repeat = this.repeat;

        if (result.isAfter(dateTime)) {
            throw new UncorrectedDataTimeException("Переданная дата позже начала повторения транзакции!");
        }
        for (int i = 1; i < repeat; i++) {
            LocalDateTime next = result.plus(this.pattern.getDuration());
            if (next.isAfter(dateTime)) {
                return result;
            }
            result = next;
        }
        return result;
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
