package com.skillbox.model;

import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;
import java.util.List;

/**
 * Класс представляющий собой счет в банке
 */
// TODO: Исправьте этот класс, он не должен быть абстрактным
@Setter
@ToString
public class Account implements AccountInfo, BalanceOperations, AccountStatement {

    private int accountId;
    private int userId;
    private AccountType accountType;
    private List<Transaction> transactions;
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
        return null;
    }

    @Override
    public BigDecimal getBalance() {
        return null;
    }

    @Override
    public void addTransaction(Transaction transaction) {

    }
}
