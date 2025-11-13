package com.skillbox.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;

@Getter
@Setter
@ToString(callSuper = true)
public class TransactionTaxable extends Transaction implements Taxable {

    private  BigDecimal tax;

    public TransactionTaxable(int accountId,
                              int transactionId,
                              LocalDateTime date,
                              String category,
                              BigDecimal amount,
                              TransactionType type,
                              BigDecimal tax
    ) {
        super(accountId, transactionId, date, category, amount.setScale(2, RoundingMode.HALF_UP), type);
        this.tax = tax;
    }

    @Override
    public BigDecimal calculateTax() {
        BigDecimal amount = this.getAmount();
        BigDecimal multiply = amount.multiply(this.tax);
        return amount.subtract(multiply).setScale(2, RoundingMode.HALF_UP);
    }
}
