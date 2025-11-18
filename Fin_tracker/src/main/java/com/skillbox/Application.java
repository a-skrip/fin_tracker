package com.skillbox;


import com.skillbox.controller.MainMenuController;
import com.skillbox.controller.dto.TransactionFilterDto;
import com.skillbox.model.Account;
import com.skillbox.model.Transaction;
import com.skillbox.data.repository.*;
import com.skillbox.exception.NumberParametersException;
import com.skillbox.service.TransactionService;
import com.skillbox.service.TransactionServiceImpl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Application {

    public static void main(String[] args) {
        // проверка аргументов командной строки
        if (args.length < 3) {
            // TODO: создайте собственное исключение для обработки этой бизнес-ошибки
            throw new NumberParametersException("Необходимо указать имена файлов для входных данных аккаунтов и транзакций, а также для выходного файла.");
        }
        // имя входного файла с информацией об аккаунтах
        String accountFilename = args[0];
        // имя входного файла с информацией о транзакциях
        String transactionFilename = args[1];
        // имя выходного файла для записи результата
        String outputFilename = args[2];

        // TODO: исправьте инициализацию сервисов
        AccountRepository accountReader = new AccountRepositoryImpl(accountFilename);
        List<Account> accounts = accountReader.readAll();

        TransactionRepository transactionReader = new TransactionRepositoryImpl(transactionFilename);
        List<Transaction> transactions = transactionReader.readAll();

        accountReader.setTransaction(transactions);


        TransactionService transactionService = new TransactionServiceImpl(transactionReader, accountReader);

        AnalyticRepository saver = null;
        new MainMenuController(transactionService, saver).start();
    }

}


