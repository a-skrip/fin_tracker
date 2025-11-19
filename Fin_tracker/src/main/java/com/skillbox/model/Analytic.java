package com.skillbox.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.skillbox.controller.dto.TransactionFilterDto;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Класс, хранящий результаты расчета аналитики транзакций
 */
// TODO: реализуйте класс для хранения аналитики
@Setter
@Getter
public class Analytic {

    private LocalDateTime calculationDate;
    private String filterDescription;
    private String groupOption;
    private String aggregateOption;

    private Map<String, List<Transaction>> data;
    private Map<String, BigDecimal> aggregateData;


    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("==================================================").append("\n");
        builder.append("Дата расчета аналитики: ").append(calculationDate).append("\n");
        builder.append("Фильтр: ").append(filterDescription).append("\n");
        builder.append("Группировка: ").append(groupOption).append("\n");
        builder.append("Агрегация: ").append(aggregateOption).append("\n");
        builder.append("--------------------------------------------------").append("\n");
        builder.append("        АНАЛИТИКА: ").append("\n");
        if (aggregateOption == null) {
            for (Map.Entry<String, List<Transaction>> entry : data.entrySet()) {
                List<Transaction> value = entry.getValue();
                builder.append(entry.getKey()).append(": ");
                for (Transaction tr : value) {
                    builder.append(tr.getAmount()).append(", ");
                }
                builder.append("\n");
            }
        } else {
            for (Map.Entry<String, BigDecimal> entry : aggregateData.entrySet()) {
                builder.append(entry.getKey()).append(": ").append(entry.getValue()).append("\n");
            }
        }
        return builder.toString();
    }
    public static String buildDescriptionFilter(TransactionFilterDto filterDto) {
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
            builder.append(", максимальная сумма: ").append(filterDto.getMaxAmount());
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
