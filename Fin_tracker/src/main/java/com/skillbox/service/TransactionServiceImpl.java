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
import java.math.MathContext;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static com.skillbox.model.Analytic.buildDescriptionFilter;

@Getter
@Setter
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository transactionRepository;
    private final AccountRepository accountRepository;

    public TransactionServiceImpl(TransactionRepository transactionRepository,
                                  AccountRepository accountRepository) {
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
        Map<String, BigDecimal> aggregateResult;
        List<Transaction> filteredTransaction = allTransaction.stream()
                .filter(transactionFilter.buildPredicate())
                .toList();

        if (groupOption == null) {
            result = filteredTransaction.stream()
                    .collect(Collectors.toMap(
                            tr -> String.valueOf(tr.getTransactionId()),
                            tr -> Collections.singletonList(tr)
                    ));
            analytic.setFilterDescription(buildDescriptionFilter(transactionFilter));
        }
        if (groupOption != null) {
            switch (groupOption) {
                case EXIT -> {
                    result = filteredTransaction.stream()
                            .collect(Collectors.toMap(
                                    tr -> String.valueOf(tr.getTransactionId()),
                                    Collections::singletonList
                            ));
                    analytic.setFilterDescription(buildDescriptionFilter(transactionFilter));
                    analytic.setGroupOption(groupOption.getName());

                }
                case GROUP_BY_MOUNT -> {
                    result = groupingByMonth(filteredTransaction);
                    analytic.setFilterDescription(buildDescriptionFilter(transactionFilter));
                    analytic.setGroupOption(groupOption.getName());
                }
                case GROUP_BY_YEARS -> {
                    result = groupingByYears(filteredTransaction);
                    analytic.setFilterDescription(buildDescriptionFilter(transactionFilter));
                    analytic.setGroupOption(groupOption.getName());
                }
                case GROUP_BY_DAYS_OF_WEEK -> {
                    result = groupingByDaysOfWeek(filteredTransaction);
                    analytic.setFilterDescription(buildDescriptionFilter(transactionFilter));
                    analytic.setGroupOption(groupOption.getName());
                }
                case GROUP_BY_CATEGORY -> {
                    result = groupingByCategory(filteredTransaction);
                    analytic.setFilterDescription(buildDescriptionFilter(transactionFilter));
                    analytic.setGroupOption(groupOption.getName());
                }
                case GROUP_BY_INCOME_AND_EXPENSE -> {
                    result = groupingByIncomeAndExpense(filteredTransaction);
                    analytic.setFilterDescription(buildDescriptionFilter(transactionFilter));
                    analytic.setGroupOption(groupOption.getName());
                }
                case GROUP_BY_ACCOUNT_TYPE -> {
                    result = groupingByAccountType(filteredTransaction);
                    analytic.setFilterDescription(buildDescriptionFilter(transactionFilter));
                    analytic.setGroupOption(groupOption.getName());
                }
                case GROUP_BY_USER_ID -> {
                    result = mapAccountWithTransactions(allAccounts, transactionFilter);
                    analytic.setFilterDescription(buildDescriptionFilter(transactionFilter));
                    analytic.setGroupOption(groupOption.getName());
                }
            }
        }
        if (aggregateOption != null) {
            switch (aggregateOption) {
                case SUM -> {
                    aggregateResult = result.entrySet().stream()
                            .collect(Collectors.toMap(
                                    Map.Entry::getKey,
                                    entry -> calculateSum(entry.getValue())
                            ));
                    analytic.setAggregateOption(aggregateOption.getName());
                    analytic.setAggregateData(aggregateResult);
                }
                case AVERAGE -> {
                    aggregateResult = result.entrySet().stream()
                                    .collect(Collectors.toMap(
                                            Map.Entry::getKey,
                                            entry -> calculateAverage(entry.getValue())
                                    ));
                    analytic.setAggregateOption(aggregateOption.getName());
                    analytic.setAggregateData(aggregateResult);
                }
                case COUNT -> {
                    aggregateResult = result.entrySet().stream()
                                    .collect(Collectors.toMap(
                                            Map.Entry::getKey,
                                            entry -> calculateCount(entry.getValue())
                                    ));
                    analytic.setAggregateOption(aggregateOption.getName());
                    analytic.setAggregateData(aggregateResult);
                }
            }
        }
        analytic.setCalculationDate(LocalDateTime.now());
        analytic.setData(result);

        return analytic;

    }

    private Map<String, List<Transaction>> mapAccountWithTransactions(List<Account> accountList,
                                                                      TransactionFilterDto filterDto) {
        Map<String, List<Transaction>> resultMap = new HashMap<>();
        for (Account account : accountList) {
            String userId = String.valueOf(account.getUserId());
            List<Transaction> accountTransaction = account.getTransactions();
            List<Transaction> filteredAccountTransaction = accountTransaction.stream()
                    .filter(filterDto.buildPredicate())
                    .toList();
            if (resultMap.containsKey(userId)) {
                List<Transaction> existTransactions = resultMap.get(userId);
                List<Transaction> updateListTransaction = new ArrayList<>();
                updateListTransaction.addAll(existTransactions);
                updateListTransaction.addAll(filteredAccountTransaction);
                resultMap.put(userId, updateListTransaction);
            } else {
                resultMap.put(String.valueOf(account.getUserId()), filteredAccountTransaction);
            }
        }
        return resultMap;
    }

    private Map<String, List<Transaction>> groupingByAccountType(List<Transaction> transactionList) {
        return transactionList.stream()
                .collect(Collectors.groupingBy(tr -> String.valueOf(tr.getAccountId())));
    }

    private Map<String, List<Transaction>> groupingByIncomeAndExpense(List<Transaction> transactionList) {
        return transactionList.stream()
                .collect(Collectors.groupingBy(
                        tr -> tr.getAmount().compareTo(BigDecimal.ZERO) >= 0 ? "Доходы" : "Расходы"));
    }

    private Map<String, List<Transaction>> groupingByCategory(List<Transaction> transactionList) {
        return transactionList.stream()
                .collect(Collectors.groupingBy(Transaction::getCategory));
    }

    private Map<String, List<Transaction>> groupingByDaysOfWeek(List<Transaction> transactionList) {
        return transactionList.stream()
                .collect(Collectors.groupingBy(tr -> tr.getDate().getDayOfWeek().toString()));
    }

    private Map<String, List<Transaction>> groupingByYears(List<Transaction> transactionList) {
        return transactionList.stream()
                .collect(Collectors.groupingBy(tr -> String.valueOf(tr.getDate().getYear())));
    }

    private Map<String, List<Transaction>> groupingByMonth(List<Transaction> transactionList) {
        return transactionList.stream()
                .collect(Collectors.groupingBy(tr -> tr.getDate().getMonth().toString()));
    }

    private BigDecimal calculateSum(List<Transaction> transactionList) {
        BigDecimal result = BigDecimal.ZERO;
        for (Transaction transaction : transactionList) {
            BigDecimal amount = transaction.getAmount();
            result = result.add(amount);
        }
        return result;
    }

    private BigDecimal calculateAverage(List<Transaction> transactionList) {
        BigDecimal sum = calculateSum(transactionList);
        return sum.divide(BigDecimal.valueOf(transactionList.size()), MathContext.DECIMAL128);
    }

    private BigDecimal calculateCount(List<Transaction> transactionList) {
        return BigDecimal.valueOf(transactionList.size());
    }


}



