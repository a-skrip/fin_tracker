package com.skillbox.data.repository;

import com.skillbox.exception.ParseLineFormatException;
import com.skillbox.model.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RequiredArgsConstructor

public class TransactionRepositoryImpl implements TransactionRepository {
    private final List<Transaction> transactions = new ArrayList<>();
    private final String fileName;


    @Override
    public List<Transaction> readAll() {
        try {
            List<String> lines = Files.readAllLines(Path.of(fileName));
            for (String line : lines) {
                String[] values = splitLine(line);
                TransactionType type = TransactionType.of(values[5]);
                switch (type) {
                    case REGULAR -> transactions.add(createTransactionRegular(values));
                    case TAXABLE -> transactions.add(createTransactionTaxable(values));
                    case FOREIGN_CURRENT -> transactions.add(createTransactionForeignCurrency(values));
                    case COMMENTABLE -> transactions.add(createTransactionCommentable(values));
                    case RECURRENT -> transactions.add(createTransactionRecurrent(values));
                }
            }
        } catch (
                ParseLineFormatException e) {
            System.out.println("Не корректная строка -   несоответствие количества аргументов");
        } catch (
                IOException e) {
            System.out.println("Файл не найден " + e.getMessage());
        }
        return transactions;
    }

    private static TransactionRegular createTransactionRegular(String[] values) {
        return new TransactionRegular(
                Integer.parseInt(values[0]),
                Integer.parseInt(values[1]),
                LocalDateTime.parse(values[2]),
                values[3],
                BigDecimal.valueOf(Double.parseDouble(values[4])),
                TransactionType.of(values[5])
        );
    }

    private static TransactionTaxable createTransactionTaxable(String[] values) {
        TransactionTaxable transaction = new TransactionTaxable(
                Integer.parseInt(values[0]),
                Integer.parseInt(values[1]),
                LocalDateTime.parse(values[2]),
                values[3],
                BigDecimal.valueOf(Double.parseDouble(values[4])),
                TransactionType.of(values[5]),
                BigDecimal.valueOf(Double.parseDouble(values[6]))
        );
        transaction.setAmount(transaction.calculateTax());
        return transaction;
    }

    private static TransactionForeignCurrency createTransactionForeignCurrency(String[] values) {
        BigDecimal course = BigDecimal.valueOf(Double.parseDouble(values[6]));
        TransactionForeignCurrency transaction = new TransactionForeignCurrency(
                Integer.parseInt(values[0]),
                Integer.parseInt(values[1]),
                LocalDateTime.parse(values[2]),
                values[3],
                BigDecimal.valueOf(Double.parseDouble(values[4])),
                TransactionType.of(values[5]),
                BigDecimal.valueOf(Double.parseDouble(values[6]))
        );
        transaction.setAmount(transaction.convertToBaseCurrency(course));
        return transaction;
    }

    private static TransactionCommentable createTransactionCommentable(String[] values) {
        List<String> comments = Arrays.stream(values[6].split(";"))
                .toList();
        TransactionCommentable transaction = new TransactionCommentable(
                Integer.parseInt(values[0]),
                Integer.parseInt(values[1]),
                LocalDateTime.parse(values[2]),
                values[3],
                BigDecimal.valueOf(Double.parseDouble(values[4])),
                TransactionType.of(values[5]));
        transaction.setComments(comments);
        return transaction;
    }

    private static TransactionRecurrent createTransactionRecurrent(String[] values) {
        String[] fields = values[6].split(";");
        RecurrencePattern pattern = RecurrencePattern.of(fields[0]);
        int duration = Integer.parseInt(fields[1]);

        return new TransactionRecurrent(
                Integer.parseInt(values[0]),
                Integer.parseInt(values[1]),
                LocalDateTime.parse(values[2]),
                values[3],
                BigDecimal.valueOf(Double.parseDouble(values[4])),
                TransactionType.of(values[5]),
                pattern,
                duration
        );
    }

    public List<Transaction> getAllTransaction() {
        return this.transactions;
    }

    private static String[] splitLine(String line) {
        return line.split(",");
    }
}
