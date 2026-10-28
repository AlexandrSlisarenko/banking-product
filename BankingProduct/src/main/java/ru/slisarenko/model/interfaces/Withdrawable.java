package ru.slisarenko.model.interfaces;

import java.math.BigDecimal;

public interface Withdrawable {
    void withdraw(BigDecimal amount);
}
