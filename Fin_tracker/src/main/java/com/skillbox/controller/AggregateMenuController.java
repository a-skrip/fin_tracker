package com.skillbox.controller;

import com.skillbox.controller.option.AggregateOption;

public class AggregateMenuController extends AbstractMenuController<AggregateOption> {

    public AggregateMenuController() {
        super(AggregateOption.class, "Выберите опцию группировки транзакций: ");
    }

    public AggregateOption selectAggregation() {
        return selectMenu();
    }
}
