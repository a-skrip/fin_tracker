package com.skillbox.service;

import com.skillbox.controller.dto.TransactionFilterDto;
import com.skillbox.controller.option.AggregateOption;
import com.skillbox.controller.option.GroupOption;
import com.skillbox.data.repository.AccountRepository;
import com.skillbox.data.repository.TransactionRepository;
import com.skillbox.model.Account;
import com.skillbox.model.Analytic;
import com.skillbox.model.Transaction;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Getter
@Setter
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository transactionRepository;
    private final AccountRepository accountRepository;


    public TransactionServiceImpl(TransactionRepository transactionRepository, AccountRepository accountRepository) {
        this.transactionRepository = transactionRepository;
        this.accountRepository = accountRepository;
    }


    @Override
    public Analytic calculateAnalytics(TransactionFilterDto transactionFilter,
                                       GroupOption groupOption,
                                       AggregateOption aggregateOption) {

        List<Transaction> allTransaction = transactionRepository.getAllTransaction();
        List<Account> allAccounts = accountRepository.getAllAccounts();

        Map<String, List<Transaction>> result = new HashMap<>();
        List<Transaction> filteredTransaction = allTransaction.stream()
                .filter(transactionFilter.buildPredicate())
                .toList();

        if (groupOption == null) {
            result = filteredTransaction.stream()
                    .collect(Collectors.toMap(
                            tr -> String.valueOf(tr.getTransactionId()),
                            tr -> Collections.singletonList(tr)
                    ));
        }
        if (groupOption != null) {
            switch (groupOption) {
                case EXIT -> {
                    result = filteredTransaction.stream()
                            .collect(Collectors.toMap(
                                    tr -> String.valueOf(tr.getTransactionId()),
                                    tr -> Collections.singletonList(tr)
                            ));

                }
                case GROUP_BY_MOUNT -> {
                    result = filteredTransaction.stream()
                            .collect(Collectors.groupingBy(tr -> tr.getDate().getMonth().toString()));
                }
                case GROUP_BY_YEARS -> {
                    result = filteredTransaction.stream()
                            .collect(Collectors.groupingBy(tr -> String.valueOf(tr.getDate().getYear())));
                }
                case GROUP_BY_DAYS_OF_WEEK -> {
                    result = filteredTransaction.stream()
                            .collect(Collectors.groupingBy(tr -> tr.getDate().getDayOfWeek().toString()));
                }
                case GROUP_BY_CATEGORY -> {
                    result = filteredTransaction.stream()
                            .collect(Collectors.groupingBy(Transaction::getCategory));
                }
                case GROUP_BY_INCOME_AND_EXPENSE -> {
                    result = filteredTransaction.stream()
                            .collect(Collectors.groupingBy(
                                    tr -> tr.getAmount().compareTo(BigDecimal.ZERO) >= 0 ? "Доходы" : "Расходы"));
                }
                case GROUP_BY_ACCOUNT_TYPE -> {
                    result = filteredTransaction.stream()
                            .collect(Collectors.groupingBy(tr -> String.valueOf(tr.getAccountId())));
                }
                case GROUP_BY_USER_ID -> {
                    Map<String, List<Transaction>> tempMap = new HashMap<>();
                    for (Account account : allAccounts) {
                        String userId = String.valueOf(account.getUserId());
                        List<Transaction> accountTransaction = account.getTransactions();
                        List<Transaction> filteredAccountTransaction = accountTransaction.stream()
                                .filter(transactionFilter.buildPredicate())
                                .toList();
                        if (tempMap.containsKey(userId)) {
                            List<Transaction> existTransactions = tempMap.get(userId);

                            List<Transaction> updateListTransaction = new ArrayList<>();
                            updateListTransaction.addAll(existTransactions);
                            updateListTransaction.addAll(filteredAccountTransaction);
                            tempMap.put(userId, updateListTransaction);

                        } else {

                            tempMap.put(String.valueOf(account.getUserId()), filteredAccountTransaction);
                        }
                    }
                    result = tempMap;
                }
            }

        }
        Analytic analytic = new Analytic();
        analytic.setCalculationDate(LocalDateTime.now());
        analytic.setGroupedData(result);
        System.out.println(transactionFilter.getCategoryToken());
        System.out.println(transactionFilter.getCommentToken());
        System.out.println(transactionFilter.getEndDate());
        System.out.println(transactionFilter.getStartDate());
        System.out.println(transactionFilter.getMinAmount());
        System.out.println(transactionFilter.getMaxAmount());
        return analytic;

    }
}



