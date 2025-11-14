package com.skillbox.controller;

import com.skillbox.controller.option.GroupOption;

public class GroupMenuController extends AbstractMenuController<GroupOption> {

    public GroupMenuController() {
        super(GroupOption.class, "Выберете способ группировки транзакций");
    }

    public GroupOption getOption() {
        while (true) {
            GroupOption option = selectMenu();
            switch (option) {
                case EXIT:
                    return GroupOption.EXIT;
                case GROUP_BY_MOUNT:
                    groupByMount();
                    return GroupOption.GROUP_BY_MOUNT;
                case GROUP_BY_YEARS:
                    groupByYears();
                    return GroupOption.GROUP_BY_YEARS;
                case GROUP_BY_DAYS_OF_WEEK:
                    groupByDaysOfWeek();
                    return GroupOption.GROUP_BY_DAYS_OF_WEEK;
                case GROUP_BY_CATEGORY:
                    groupByCategory();
                    return GroupOption.GROUP_BY_CATEGORY;
                case GROUP_BY_INCOME_AND_EXPENSE:
                    groupByIncomeAndExpense();
                    return GroupOption.GROUP_BY_INCOME_AND_EXPENSE;
                case GROUP_BY_ACCOUNT_TYPE:
                    groupByAccountType();
                    return GroupOption.GROUP_BY_ACCOUNT_TYPE;
                case GROUP_BY_USER_ID:
                    groupByUserId();
                    return GroupOption.GROUP_BY_USER_ID;
            }
        }
    }

    private void groupByMount() {
        System.out.println("GROUP_BY_MOUNT");
    }

    private void groupByYears() {
        System.out.println("GROUP_BY_YEARS");
    }

    private void groupByDaysOfWeek() {
        System.out.println("GROUP_BY_DAYS_OF_WEEK");
    }

    private void groupByCategory() {
        System.out.println("GROUP_BY_CATEGORY");
    }

    private void groupByIncomeAndExpense() {
        System.out.println("GROUP_BY_INCOME_AND_EXPENSE");
    }

    private void groupByAccountType() {
        System.out.println("GROUP_BY_ACCOUNT_TYPE");
    }

    private void groupByUserId() {
        System.out.println("GROUP_BY_USER_ID");
    }
}
