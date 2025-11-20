package com.skillbox.model;

import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

/**
 * Класс представляющий собой счет в банке
 */
@Setter
@ToString
public class Account implements AccountInfo, BalanceOperations, AccountStatement {

    private int accountId;
    private int userId;
    private AccountType accountType;
    private List<Transaction> transactions = new ArrayList<>();
    private BigDecimal balance;


    @Override
    public int getAccountId() {
        return accountId;
    }

    @Override
    public int getUserId() {
        return userId;
    }

    @Override
    public AccountType getAccountType() {
        return accountType;
    }

    @Override
    public List<Transaction> getTransactions() {
        return this.transactions;
    }

    @Override
    public BigDecimal getBalance() {
        BigDecimal balance = BigDecimal.ZERO;
        for (Transaction transaction : transactions) {
            balance = balance.add(transaction.getAmount()).setScale(2, RoundingMode.HALF_UP);
        }
        return balance;
    }

    @Override
    public void addTransaction(Transaction transaction) {
        if (this.accountId == transaction.getAccountId()) {
            transactions.add(transaction);
        }

    }
}
