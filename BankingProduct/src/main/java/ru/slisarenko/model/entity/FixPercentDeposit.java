package ru.slisarenko.model.entity;

import java.math.BigDecimal;
import java.util.Currency;

public class FixPercentDeposit extends Deposit{
    public FixPercentDeposit(String name, Currency currency, BigDecimal percent) {
        super(name, currency, percent);
    }
}
