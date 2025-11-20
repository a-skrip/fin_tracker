package com.skillbox.model;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Абстрактный класс, представляющий собой транзакцию
 */
@Getter
@Setter

public abstract class Transaction {

    private int accountId;
    private int transactionId;
    private LocalDateTime date;
    private String category;
    private BigDecimal amount;
    private TransactionType type;

    public Transaction(int accountId,
                       int transactionId,
                       LocalDateTime date,
                       String category,
                       BigDecimal amount,
                       TransactionType type) {
        this.accountId = accountId;
        this.transactionId = transactionId;
        this.date = date;
        this.category = category;
        this.amount = amount;
        this.type = type;
    }

    @Override
    public String toString() {
        return " accountId= " + accountId + " transactionId= " + transactionId + " date " + date + " category " + category + " amount " + amount + " type " + type;

    }
}
