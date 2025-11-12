package com.skillbox.model;

import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

/**
 * Класс, хранящий результаты расчета аналитики транзакций
 */
// TODO: реализуйте класс для хранения аналитики
@Setter

public class Analytic {
    private List<String> transactions = new ArrayList<>();

    @Override
    public String toString() {
        return transactions.toString();
    }

}
