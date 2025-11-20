package com.skillbox.controller;

import com.skillbox.controller.dto.TransactionFilterDto;
import com.skillbox.controller.option.SearchOption;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

/**
 * Консольный контроллер для управления навигацией по функционалу поиска транзакций.
 */

public class SearchMenuController extends AbstractMenuController<SearchOption> {

    public SearchMenuController() {
        super(SearchOption.class, "Выберите способ поиска транзакции");
    }

    public TransactionFilterDto getTransactionFilter() {
        TransactionFilterDto filter = new TransactionFilterDto();
        while (true) {
            SearchOption option = selectMenu();
            switch (option) {
                case EXIT:
                    return filter;
                case ALL_TRANSACTION:
                    return new TransactionFilterDto();
                case SEARCH_BY_CATEGORY:
                    filter = inputCategory(filter);
                    break;
                case SEARCH_BY_DATES:
                    filter = inputDates(filter);
                    break;
                case SEARCH_BY_AMOUNT:
                    filter = inputAmount(filter);
                    break;
                case SEARCH_BY_COMMENT:
                    filter = inputComment(filter);
                    break;
                default:
                    throw new IllegalStateException("Unexpected value: " + option);
            }
        }
    }

    private TransactionFilterDto inputComment(TransactionFilterDto filter) {
        System.out.println("Введите комментарий для поиска");
        String line = scanner.next();
        filter.setCommentToken(line);
        return filter;
    }

    private TransactionFilterDto inputAmount(TransactionFilterDto filter) {
        System.out.println("Введите минимальную сумму:");
        String minAmount = scanner.next();

        if (minAmount.isEmpty()) {
            System.out.println("Минимальное значение не установлено");
            filter.setMinAmount(null);
        } else {
            try {
                filter.setMinAmount(BigDecimal.valueOf(Double.parseDouble(minAmount)));
            } catch (NumberFormatException e) {
                System.err.println("Неправильный ввод - " + e.getMessage() + " , минимальная сумма не установлена! ");
            }
        }

        System.out.println("Введите максимальную сумму:");
        String maxAmount = scanner.next();
        if (maxAmount.isEmpty()) {
            System.out.println("Максимальное значение не установлено");
            filter.setMaxAmount(null);
        } else {
            try {
                filter.setMaxAmount(BigDecimal.valueOf(Double.parseDouble(maxAmount)));
            } catch (NumberFormatException e) {
                System.err.println("Неправильный ввод - " + e.getMessage() + " максимальная сумма не установлена! ");
            }
        }
        return filter;
    }

    private TransactionFilterDto inputDates(TransactionFilterDto filter) {
        System.out.println("Введите дату начала (yyyy-MM-dd): ");
        String startDate = scanner.next();
        DateTimeFormatter pattern = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        if (startDate.isEmpty()) {
            filter.setStartDate(null);
        } else {
            try {
                LocalDate parseStartDate = LocalDate.parse(startDate, pattern);
                filter.setStartDate(parseStartDate);
            } catch (DateTimeParseException e) {
                System.err.println("Неверно введена дата! Дата начала не установлена");
                filter.setStartDate(null);
            }
        }

        System.out.println("Введите дату окончания: ");
        String endDate = scanner.next();
        if (endDate.isEmpty()) {
            filter.setEndDate(null);
        } else {
            try {
                LocalDate parseEndDate = LocalDate.parse(endDate, pattern);
                filter.setEndDate(parseEndDate.plusDays(1));
            } catch (DateTimeParseException e) {
                System.err.println("Неверно введена дата! Дата окончания не установлена");
                filter.setEndDate(null);
            }
        }
        return filter;
    }

    private TransactionFilterDto inputCategory(TransactionFilterDto filter) {
        System.out.println("Введите категорию");
        String line = scanner.next();
        filter.setCategoryToken(line);
        return filter;
    }


}
