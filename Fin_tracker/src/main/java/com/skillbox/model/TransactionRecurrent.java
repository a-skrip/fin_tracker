package com.skillbox.model;

import com.skillbox.exception.UncorrectedDataTimeException;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ToString(callSuper = true)
public class TransactionRecurrent extends Transaction implements Recurring {

    private RecurrencePattern pattern;
    private int repeat;
    private final List<LocalDateTime> datesTransaction = new ArrayList<>();

    public TransactionRecurrent(int accountId,
                                int transactionId,
                                LocalDateTime date,
                                String category,
                                BigDecimal amount,
                                TransactionType type,
                                RecurrencePattern pattern,
                                int repeat
    ) {
        super(accountId, transactionId, date, category, amount.setScale(2, RoundingMode.HALF_UP), type);
        this.pattern = pattern;
        this.repeat = repeat;
    }

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
        for (int i = 0; i < repeat; i++) {
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
        BigDecimal startAmount = this.getAmount();
        LocalDateTime start = this.getDate();
        BigDecimal result = startAmount;
        for (int i = 0; i < this.repeat; i++) {
            LocalDateTime next = start.plus(this.pattern.getDuration());
            if (next.isBefore(dateTime)) {
                result = result.add(startAmount);
            }
        }
        return result;
    }

    @Override
    public boolean isExecutedBetween(LocalDateTime startDate, LocalDateTime endDate) {
        createDatesList();
        for (LocalDateTime dateTime : datesTransaction) {
            boolean isAfter = startDate == null || dateTime.isAfter(startDate);
            boolean isBefore = endDate == null || dateTime.isBefore(endDate);
            if (isAfter && isBefore) {
                return true;
            }
        }
        return false;
    }

    private void createDatesList() {
        LocalDateTime start = this.getDate();
        for (int i = 0; i <= this.repeat; i++) {
            datesTransaction.add(start);
            start = start.plus(this.pattern.getDuration());
        }
    }

}
