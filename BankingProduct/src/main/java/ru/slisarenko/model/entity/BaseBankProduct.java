package ru.slisarenko.model.entity;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.Objects;
import lombok.Getter;
import ru.slisarenko.model.exceptions.InsufficientFundsException;

@Getter
public abstract class BaseBankProduct {
    private String name;
    private Currency currency;
    private BigDecimal balance;
    private boolean isClosed;

    public BaseBankProduct(String name, Currency currency) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("name must not be blank");
        }
        this.name = name;
        this.currency = Objects.requireNonNull(currency, "currency must not be null");
        this.balance = BigDecimal.ZERO;
        this.isClosed = false;
    }

    public void incrementBalance(BigDecimal amount) {
        validateAmount(amount);
        checkNotClosed();
        balance = balance.add(amount);
    }

    public void decrementBalance(BigDecimal amount) {
        validateAmount(amount);
        checkNotClosed();
        if (balance.compareTo(amount) < 0) {
            throw new InsufficientFundsException(
                    "Insufficient funds: balance=" + balance + ", required=" + amount);
        }
        balance = balance.subtract(amount);
    }

    public void markClosed() {
        checkNotClosed();
        this.isClosed = true;
    }

    public void validateAmount(BigDecimal amount) {
        if (amount == null) {
            throw new IllegalArgumentException("amount must not be null");
        }
        if (amount.signum() <= 0) {
            throw new IllegalArgumentException("amount must be positive: " + amount);
        }
    }

    private void checkNotClosed() {
        if (this.isClosed) {
            throw new IllegalStateException("Product is closed: " + this.name);
        }
    }




}
