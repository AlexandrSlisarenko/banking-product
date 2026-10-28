package ru.slisarenko.model.entity;

import java.math.BigDecimal;
import java.util.Currency;
import ru.slisarenko.model.interfaces.Depositable;
import ru.slisarenko.model.interfaces.Closable;

public class Deposit extends BaseBankProduct implements Depositable, Closable{
    public Deposit(String name, Currency currency) {
        super(name, currency);
    }

    @Override
    public void close() {
        markClosed();
    }

    @Override
    public void deposit(BigDecimal amount) {
        incrementBalance(amount);
    }
}
