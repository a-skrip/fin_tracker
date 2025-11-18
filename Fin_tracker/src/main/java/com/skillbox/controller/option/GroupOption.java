package com.skillbox.controller.option;

import lombok.Getter;

@Getter
public enum GroupOption implements MenuOption {
    // TODO: исправьте реализацию перечисления для выбора полей группировки по образцу класса SearchOption
    EXIT("Вернуться назад - без группировки"),
    GROUP_BY_MOUNT("Группировка по месяцам"),
    GROUP_BY_YEARS("Группировка по годам"),
    GROUP_BY_DAYS_OF_WEEK("Группировка по дням недели"),
    GROUP_BY_CATEGORY("Группировка по категориям"),
    GROUP_BY_INCOME_AND_EXPENSE("Доходы/Расходы"),
    GROUP_BY_ACCOUNT_TYPE("Группировка по типу счёта"),
    GROUP_BY_USER_ID("Группировка по пользователю");

    private final String name;

    GroupOption(String name) {
        this.name = name;
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
