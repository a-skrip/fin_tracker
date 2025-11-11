package com.skillbox.repository;

import com.skillbox.model.Account;
import com.skillbox.model.AccountType;
import com.skillbox.exception.ParseLineFormatException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;


public class AccountRepositoryImpl implements AccountRepository {
    private final List<Account> accounts = new ArrayList<>();
    private final String fileName;

    public AccountRepositoryImpl(String fileName) {
        this.fileName = fileName;
    }

    @Override
    public List<Account> readAll() {
        try {
            List<String> allLines = Files.readAllLines(Path.of(fileName));
            for (int i = 0; i < allLines.size(); i++) {
                String[] split = allLines.get(i).split(",");
                if (split.length != 3) {
                    throw new ParseLineFormatException("Не корректная строка - " + (i + 1) + " несоответствие количества аргументов");
                }
                int accountId = Integer.parseInt(split[0]);
                AccountType accountType = AccountType.of(Integer.parseInt(split[1]));
                int userId = Integer.parseInt(split[2]);

                Account account = new Account();
                account.setAccountId(accountId);
                account.setAccountType(accountType);
                account.setUserId(userId);
                accounts.add(account);
            }
        } catch (ParseLineFormatException e) {
            System.out.println(e.getMessage());
        } catch (IOException e) {
            System.out.println("Файл не найден " + e.getMessage());
        }

        return accounts;
    }
}
