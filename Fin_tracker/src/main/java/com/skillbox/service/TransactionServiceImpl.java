package com.skillbox.service;

import com.skillbox.controller.dto.TransactionFilterDto;
import com.skillbox.controller.option.AggregateOption;
import com.skillbox.controller.option.GroupOption;
import com.skillbox.model.Analytic;
import com.skillbox.model.Transaction;
import com.skillbox.data.repository.TransactionRepository;
import com.skillbox.model.TransactionType;

import java.time.LocalDateTime;
import java.util.List;

public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository repository;

    public TransactionServiceImpl(TransactionRepository repository) {
        this.repository = repository;
    }

    @Override
    public Analytic calculateAnalytics(TransactionFilterDto transactionFilter,
                                       GroupOption groupOption,
                                       AggregateOption aggregateOption) {

        List<Transaction> allTransaction = repository.getAllTransaction();

        List<String> list = allTransaction.stream()
                .filter(transactionFilter.buildPredicate())
                .map(transaction -> transaction.getDate().toString())
                .toList();

        Analytic analytic = new Analytic();
        analytic.setTransactions(list);
        return analytic;
    }
}
