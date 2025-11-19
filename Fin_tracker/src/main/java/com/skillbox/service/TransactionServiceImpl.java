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
        Analytic analytic = new Analytic();

        Map<String, List<Transaction>> result = new HashMap<>();
        List<Transaction> filteredTransaction = allTransaction.stream()
                .filter(transactionFilter.buildPredicate())
                .toList();

        if (groupOption == null /*&& aggregateOption == null*/) {
            result = filteredTransaction.stream()
                    .collect(Collectors.toMap(
                            tr -> String.valueOf(tr.getTransactionId()),
                            tr -> Collections.singletonList(tr)
                    ));
            analytic.setFilterDescription(buildDescriptionFilter(transactionFilter));
//            analytic.printAnalytic(result);
        }
        if (groupOption != null ) {
            switch (groupOption) {
                case EXIT -> {
                    result = filteredTransaction.stream()
                            .collect(Collectors.toMap(
                                    tr -> String.valueOf(tr.getTransactionId()),
                                    Collections::singletonList
                            ));
                    analytic.setFilterDescription(buildDescriptionFilter(transactionFilter));
                    analytic.setGroupOption(groupOption.getName());
//                    analytic.printAnalytic(result);

                }
                case GROUP_BY_MOUNT -> {
                    result = filteredTransaction.stream()
                            .collect(Collectors.groupingBy(tr -> tr.getDate().getMonth().toString()));
                    analytic.setFilterDescription(buildDescriptionFilter(transactionFilter));
                    analytic.setGroupOption(groupOption.getName());
//                    analytic.printAnalytic(result);
                }
                case GROUP_BY_YEARS -> {
                    result = filteredTransaction.stream()
                            .collect(Collectors.groupingBy(tr -> String.valueOf(tr.getDate().getYear())));
                    analytic.setFilterDescription(buildDescriptionFilter(transactionFilter));
                    analytic.setGroupOption(groupOption.getName());
//                    analytic.printAnalytic(result);
                }
                case GROUP_BY_DAYS_OF_WEEK -> {
                    result = filteredTransaction.stream()
                            .collect(Collectors.groupingBy(tr -> tr.getDate().getDayOfWeek().toString()));
                    analytic.setFilterDescription(buildDescriptionFilter(transactionFilter));
                    analytic.setGroupOption(groupOption.getName());
//                    analytic.printAnalytic(result);
                }
                case GROUP_BY_CATEGORY -> {
                    result = filteredTransaction.stream()
                            .collect(Collectors.groupingBy(Transaction::getCategory));
                    analytic.setFilterDescription(buildDescriptionFilter(transactionFilter));
                    analytic.setGroupOption(groupOption.getName());
//                    analytic.printAnalytic(result);
                }
                case GROUP_BY_INCOME_AND_EXPENSE -> {
                    result = filteredTransaction.stream()
                            .collect(Collectors.groupingBy(
                                    tr -> tr.getAmount().compareTo(BigDecimal.ZERO) >= 0 ? "Доходы" : "Расходы"));
                    analytic.setFilterDescription(buildDescriptionFilter(transactionFilter));
                    analytic.setGroupOption(groupOption.getName());
//                    analytic.printAnalytic(result);
                }
                case GROUP_BY_ACCOUNT_TYPE -> {
                    result = filteredTransaction.stream()
                            .collect(Collectors.groupingBy(tr -> String.valueOf(tr.getAccountId())));
                    analytic.setFilterDescription(buildDescriptionFilter(transactionFilter));
                    analytic.setGroupOption(groupOption.getName());
//                    analytic.printAnalytic(result);
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
                    analytic.setFilterDescription(buildDescriptionFilter(transactionFilter));
                    analytic.setGroupOption(groupOption.getName());
//                    analytic.printAnalytic(result);
                }
            }
        }
        analytic.setCalculationDate(LocalDateTime.now());
        analytic.setData(result);

        return analytic;

    }

    private String buildDescriptionFilter(TransactionFilterDto filterDto) {
        StringBuilder builder = new StringBuilder();
        if (filterDto.getStartDate() != null) {
            builder.append(", начальная дата: ").append(filterDto.getStartDate());
        }
        if (filterDto.getEndDate() != null) {
            builder.append(", конечная дата: ").append(filterDto.getEndDate());
        }
        if (filterDto.getMinAmount() != null) {
            builder.append(", минимальная сумма: ").append(filterDto.getMinAmount());
        }
        if (filterDto.getMaxAmount() != null) {
            builder.append(", максимальная сумма сумма: ").append(filterDto.getMaxAmount());
        }
        if (filterDto.getCategoryToken() != null) {
            builder.append(", категория: ").append(filterDto.getCategoryToken());
        }
        if (filterDto.getCommentToken() != null) {
            builder.append(", комментарий: ").append(filterDto.getCommentToken());
        }
        return builder.toString();
    }
}



