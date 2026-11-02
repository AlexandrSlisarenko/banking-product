package ru.slisarenko.model.entity;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.Currency;
import lombok.Getter;
import ru.slisarenko.model.exceptions.CreditRestrictionException;
import ru.slisarenko.model.interfaces.BalanceInquirable;
import ru.slisarenko.model.interfaces.Depositable;
import ru.slisarenko.model.interfaces.Closable;

@Getter
public abstract class Deposit extends BaseBankProduct implements Depositable, Closable, BalanceInquirable {

    private BigDecimal percent;
    private LocalDate dateOpen;

    public Deposit(String name, Currency currency, BigDecimal percent) {
        super(name, currency);
        if (percent == null || percent.compareTo(BigDecimal.ONE) < 0 || percent.compareTo(BigDecimal.valueOf(100)) > 0) {
            throw new CreditRestrictionException("Returns the percent function of this must be >= 1 and <= 100");
        }
        dateOpen = LocalDate.now();
        this.percent = percent;
    }

    @Override
    public void close() {
        markClosed();
    }

    @Override
    public void deposit(BigDecimal amount) {
        incrementBalance(amount);
    }

    @Override
    public BigDecimal getBalancePercent() {
        return super.getBalance().multiply(percent.divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP));
    }
}
