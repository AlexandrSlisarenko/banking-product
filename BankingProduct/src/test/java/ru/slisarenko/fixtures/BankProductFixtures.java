package ru.slisarenko.fixtures;

import java.math.BigDecimal;
import java.util.Currency;
import ru.slisarenko.model.entity.CreditCard;
import ru.slisarenko.model.entity.DebitCard;
import ru.slisarenko.model.entity.Deposit;

public class BankProductFixtures {
    public static final Currency RUB = Currency.getInstance("RUB");
    public static final Currency USD = Currency.getInstance("USD");
    public static final Currency EUR = Currency.getInstance("EUR");

    private BankProductFixtures() {}

    public static DebitCard debitCardRUB() {
        return new DebitCard("Дебетовая карта", RUB);
    }

    public static CreditCard creditCardRUB() {
        return new CreditCard("Кредитная карта", USD, new BigDecimal("0.14"), new BigDecimal("1000.00"));
    }

    public static Deposit depositRUB() {
        return new Deposit("Вклад", EUR);
    }
}
