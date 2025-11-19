package com.skillbox.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;
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

}
