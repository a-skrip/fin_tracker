package com.skillbox.repository;

import com.skillbox.exception.ParseLineFormatException;
import com.skillbox.model.Transaction;
import com.skillbox.model.TransactionForeignCurrency;
import com.skillbox.model.TransactionRegular;
import com.skillbox.model.TransactionTaxable;
import lombok.RequiredArgsConstructor;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static com.skillbox.model.TransactionType.*;

@RequiredArgsConstructor
public class TransactionRepositoryImpl implements TransactionRepository {
    private final List<Transaction> transactions = new ArrayList<>();
    private final String fileName;

    @Override
    public List<Transaction> readAll() {
        try {
            List<String> lines = Files.readAllLines(Path.of(fileName));
            for (String line : lines) {
                String[] split = line.split(",");
                int accountId = Integer.parseInt(split[0]);
                int transactionId = Integer.parseInt(split[1]);
                LocalDateTime date = LocalDateTime.parse(split[2]);
                String category = split[3];
                BigDecimal amount = BigDecimal.valueOf(Double.parseDouble(split[4]));
                String type = split[5];
                if (split.length == 6) {
                    Transaction transaction = new TransactionRegular(accountId, transactionId, date, category, amount, REGULAR);
                    transactions.add(transaction);
                } else if (split.length == 7) {
                    if (type.equals(TAXABLE.getType())) {
                        BigDecimal tax = BigDecimal.valueOf(Double.parseDouble(split[6]));
                        TransactionTaxable transaction = new TransactionTaxable(
                                accountId, transactionId, date, category, amount, TAXABLE, tax);
                        transaction.setAmount(transaction.calculateTax());
                        transactions.add(transaction);
                    } else if (type.equals(FOREIGN_CURRENT.getType())) {
                        BigDecimal course = BigDecimal.valueOf(Double.parseDouble(split[6]));
                        TransactionForeignCurrency transaction = new TransactionForeignCurrency(
                                accountId, transactionId, date, category, amount, FOREIGN_CURRENT, course);
                        BigDecimal resultAfterConvert = transaction.convertToBaseCurrency(course);
                        transaction.setAmount(resultAfterConvert);
                        transactions.add(transaction);
                    }
                }
            }
        } catch (ParseLineFormatException e) {
            System.out.println("Не корректная строка -   несоответствие количества аргументов");
        } catch (IOException e) {
            System.out.println("Файл не найден " + e.getMessage());
        }
        return transactions;
    }
}
