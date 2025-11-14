package com.skillbox.controller.option;

import com.skillbox.model.Transaction;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.function.Function;

@Getter
public enum GroupOption implements MenuOption {
    // TODO: исправьте реализацию перечисления для выбора полей группировки по образцу класса SearchOption
    EXIT("Вернуться назад - без группировки"),
    GROUP_BY_MOUNT("Группировка по месяцам", tr -> tr.getDate().getMonth().toString()),
    GROUP_BY_YEARS("Группировка по годам", tr -> String.valueOf(tr.getDate().getYear())),
    GROUP_BY_DAYS_OF_WEEK("Группировка по дням недели", tr -> tr.getDate().getDayOfWeek().toString()),
    GROUP_BY_CATEGORY("Группировка по категориям", tr -> tr.getCategory().toString()),

    GROUP_BY_INCOME_AND_EXPENSE("Доходы/Расходы", tr -> tr.getAmount().compareTo(BigDecimal.ZERO) >= 0 ? "Доходы" : "Расходы"),
    GROUP_BY_ACCOUNT_TYPE("Группировка по типу счёта", tr -> tr.getDate().toString()),
    GROUP_BY_USER_ID("Группировка по пользователю", tr -> tr.getDate().toString());

    private final String name;
    private Function<Transaction, String> groupFunction;

    GroupOption(String name) {
        this.name = name;
    }

    GroupOption(String name, Function<Transaction, String> function) {
        this.name = name;
        this.groupFunction = function;
    }

    public static GroupOption of(int option) {
        return OptionUtils.of(GroupOption.class, option);
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
