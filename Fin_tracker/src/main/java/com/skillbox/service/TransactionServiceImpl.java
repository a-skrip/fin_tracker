package com.skillbox.service;

import com.skillbox.controller.dto.TransactionFilterDto;
import com.skillbox.controller.option.AggregateOption;
import com.skillbox.controller.option.GroupOption;
import com.skillbox.model.Analytic;
import com.skillbox.model.Transaction;
import com.skillbox.data.repository.TransactionRepository;
import com.skillbox.model.TransactionType;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;

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

        Map<String, List<Transaction>> collect = allTransaction.stream()
                .filter(transactionFilter.buildPredicate())
                .collect(Collectors.groupingBy(groupOption.getGroupFunction())
//                        switch (groupOption) {
//                            case GROUP_BY_MOUNT -> tr.getDate().getMonth().toString();
//                            default -> tr.toString();
//
//                        }

                );

        for (Map.Entry<String, List<Transaction>> group : collect.entrySet()) {
            System.out.println(group);
        }

        Analytic analytic = new Analytic();
//        analytic.setTransactions(list);
        return analytic;
    }
}
