package com.skillbox.data.repository;

import com.skillbox.model.Account;
import com.skillbox.model.Transaction;

import java.util.List;

/**
 * Интерфейс для чтения аккаунтов (счетов) пользователей
 */
public interface AccountRepository {

    /**
     * Читает все записи со счетами
     * @return список счетов
     */
    List<Account> readAll();

    List<Account> getAllAccounts();

    void setTransaction(List<Transaction> transactions);
}
