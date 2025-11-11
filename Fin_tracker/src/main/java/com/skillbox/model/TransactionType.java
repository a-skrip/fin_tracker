package com.skillbox.model;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public enum TransactionType {
    REGULAR("Regular"),
    TAXABLE("Taxable"),
    RECURRENT("Recurrent"),
    FOREIGN_CURRENT("ForeignCurrency"),
    COMMENTABLE("Commentable");

    private final String type;
    TransactionType(String type) {
        this.type = type;
    }
    private static final Map<String, TransactionType> MAP = Arrays.stream(TransactionType.values())
            .collect(Collectors.toMap(TransactionType::getType, Function.identity()));

    public static TransactionType of(String transactionType) {
        return MAP.get(transactionType);
    }

    public String getType() {
        return type;
    }
}
