package ru.slisarenko.model.entity;

import java.math.BigDecimal;
import java.util.Currency;
import ru.slisarenko.model.interfaces.Closable;
import ru.slisarenko.model.interfaces.Depositable;
import ru.slisarenko.model.interfaces.Withdrawable;

public abstract class Card extends BaseBankProduct  implements Depositable, Withdrawable, Closable {
    public Card(String name, Currency currency) {
        super(name, currency);
    }

    @Override
    public void deposit(BigDecimal amount) {
        incrementBalance(amount);
    }

    @Override
    public void withdraw(BigDecimal amount) {
       decrementBalance(amount);
    }

    @Override
    public void close(){
        markClosed();
    }
}
