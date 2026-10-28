package ru.slisarenko.model.entity;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Currency;
import lombok.Getter;
import ru.slisarenko.model.exceptions.CreditRestrictionException;

@Getter
public class CreditCard extends Card{
    private final BigDecimal interestRate;
    private final BigDecimal startBalance;

    public CreditCard(String name, Currency currency, BigDecimal interestRate, BigDecimal startBalance) {
        super(name, currency);
        if (interestRate == null || interestRate.signum() < 0) {
            throw new CreditRestrictionException("interestRate must be >= 0");
        }
        this.interestRate = interestRate;
        this.startBalance = startBalance;
        incrementBalance(startBalance);
    }

    public void deposit(BigDecimal amount) {
        validateAmount(amount);
        BigDecimal dept = getDebtPercent();
        if (amount.compareTo(dept) < 0) {
            throw new CreditRestrictionException("minimum payment must be no less than " + dept);
        }

        BigDecimal remainingAmount = amount.subtract(getDebtPercent());

        if (startBalance.compareTo(getBalance().add(remainingAmount)) < 0) {
            throw new IllegalArgumentException("balance plus remainingAmount greater than startBalance");
        } else if (remainingAmount.compareTo(BigDecimal.ZERO) > 0) {
            super.deposit(remainingAmount);
        }
    }

    public BigDecimal getDebtPercent() {
        return (startBalance.subtract(getBalance()).multiply(interestRate)).setScale(2, RoundingMode.HALF_UP);
    }


}
