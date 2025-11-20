package com.skillbox;


import com.skillbox.controller.MainMenuController;
import com.skillbox.data.repository.*;
import com.skillbox.exception.NumberParametersException;
import com.skillbox.model.Transaction;
import com.skillbox.service.TransactionService;
import com.skillbox.service.TransactionServiceImpl;

import java.util.List;

public class Application {

    public static void main(String[] args) {
        // проверка аргументов командной строки
        if (args.length < 3) {
            throw new NumberParametersException(
                    "Необходимо указать имена файлов для входных данных аккаунтов и транзакций, а также для выходного файла."
            );
        }
        String accountFilename = args[0];
        String transactionFilename = args[1];
        String outputFilename = args[2];

        AccountRepository accountReader = new AccountRepositoryImpl(accountFilename);
        accountReader.readAll();

        TransactionRepository transactionReader = new TransactionRepositoryImpl(transactionFilename);
        List<Transaction> transactions = transactionReader.readAll();

        accountReader.setTransaction(transactions);

        TransactionService transactionService = new TransactionServiceImpl(transactionReader, accountReader);

        AnalyticRepository saver = new AnalyticRepositoryImpl(outputFilename);

        new MainMenuController(transactionService, saver).start();
    }

}


