package com.skillbox.controller.dto;

import com.skillbox.model.Commentable;
import com.skillbox.model.Recurring;
import com.skillbox.model.Transaction;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;

/**
 * Класс для хранения фильтра по транзакциям.
 */
@Getter
@Setter
@RequiredArgsConstructor
@AllArgsConstructor

public class TransactionFilterDto {

    private LocalDate startDate;
    private LocalDate endDate;
    private String commentToken;
    private BigDecimal minAmount;
    private BigDecimal maxAmount;
    private String categoryToken;

    /**
     * Создает предикат для фильтрации транзакций по диапазону дат. Также вернет те Recurring транзакции, которые будут
     * или были выполнены в указанный диапазон дат
     *
     * @return Предикат для фильтрации транзакций по диапазону дат.
     */
    private Predicate<Transaction> datePredicate() {
        return transaction -> {
            LocalDateTime date = transaction.getDate(); // TODO: здесь необходимо получить дату транзакции
            LocalDateTime start = startDate == null ? null : startDate.atStartOfDay();
            LocalDateTime end = endDate == null ? null : endDate.atStartOfDay();
            return (start == null || !date.isBefore(start)) &&
                    (end == null || !date.isAfter(end))
                    || (transaction instanceof Recurring && ((Recurring) transaction).isExecutedBetween(start, end));
        };
    }

    /**
     * Создает предикат для фильтрации транзакций по комментарию или его части. Фильтруются только транзакции,
     * имплементирующие интерфейс Commentable. Если токен пустой или null, то возвращается предикат, который всегда
     * вернет true
     *
     * @return Предикат для фильтрации транзакций по комментарию.
     */
    private Predicate<Transaction> commentPredicate() {
        // TODO: реализуйте метод, возвращающий предикат для фильтрации транзакций по комментарию
        return transaction -> {
            if (commentToken == null || commentToken.trim().isEmpty()) {
                return true;
            }
            String token = commentToken.trim().toLowerCase();

            if (transaction instanceof Commentable commentable) {
                List<String> comments = commentable.getComments();
                for (String comment : comments) {
                    if (comment.contains(token)) {
                        return true;
                    }
                }
            }
            return false;
        };

    }

    /**
     * Создает предикат для фильтрации транзакций по диапазону суммы.
     *
     * @return Предикат для фильтрации транзакций по диапазону суммы.
     */
    private Predicate<Transaction> amountPredicate() {
        // TODO: реализуйте метод, возвращающий предикат для фильтрации транзакций по диапазону суммы

        return transaction -> {
            BigDecimal transactionAmount = transaction.getAmount();
            if (transactionAmount == null) {
                return false;
            }

            boolean minCondition = true;
            if (minAmount != null) {
                int compare = transactionAmount.compareTo(minAmount);
                minCondition = compare >= 0;
            }
            boolean maxCondition = true;
            if (maxAmount != null) {
                int compare = maxAmount.compareTo(transactionAmount);
                maxCondition = compare >= 0;
            }
            return minCondition && maxCondition;

        };
    }

    /**
     * Создает предикат для фильтрации транзакций по категории.
     *
     * @return Предикат для фильтрации транзакций по категории.
     */
    private Predicate<Transaction> categoryPredicate() {
        // TODO: реализуйте метод, возвращающий предикат для фильтрации транзакций по категории
        return transaction -> {
            if (categoryToken == null) {
                return true;
            }
            String category = transaction.getCategory();
            return Objects.equals(categoryToken, category);
        };
    }

    /**
     * Собирает предикат для фильтрации транзакции.
     *
     * @return Предикат для фильтрации транзакции.
     */
    public Predicate<Transaction> buildPredicate() {
        return categoryPredicate()
                .and(amountPredicate())
                .and(commentPredicate())
                .and(datePredicate());
//                .and(amountPredicate());
    }
}
