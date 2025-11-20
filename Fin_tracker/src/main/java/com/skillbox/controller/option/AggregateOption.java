package com.skillbox.controller.option;

import lombok.Getter;

@Getter
public enum AggregateOption implements MenuOption {
    SUM("Подсчет суммы транзакций"),
    AVERAGE("Подсчет среднего значения транзакций"),
    COUNT("Подсчет количества транзакций");

    private final String name;

    AggregateOption(String name) {
        this.name = name;
    }

    @Override
    public int getOption() {
        return ordinal();
    }

    @Override
    public String getName() {
        return this.name;
    }
}
