package com.skillbox.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * Класс, хранящий результаты расчета аналитики транзакций
 */
// TODO: реализуйте класс для хранения аналитики
@Setter
@Getter
@ToString
public class Analytic {

    private LocalDateTime calculationDate;
    private String filterDescription;
    private String groupOption;
    private String aggregateOption;

    private Map<String, List<Transaction>> groupedData;


}
