package ru.slisarenko.tests;

import java.math.BigDecimal;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.slisarenko.fixtures.BankProductFixtures;
import ru.slisarenko.model.entity.Deposit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("Вклад")
public class DepositTest {
    private Deposit deposit;

    @BeforeEach
    void setUp() {
        deposit = BankProductFixtures.depositRUB();
    }

    @Test
    @DisplayName("пополнение увеличивает баланс")
    void depositIncreasesBalance() {
        BigDecimal expected = new BigDecimal("10000.00");
        deposit.deposit(expected);

        assertEquals(expected, deposit.getBalance());
    }

    @Test
    @DisplayName("закрытие помечает продукт закрытым")
    void closeMarksClosed() {
        deposit.close();

        assertTrue(deposit.isClosed());
    }

    @Test
    @DisplayName("повторное закрытие отклоняется")
    void closeTwiceRejected() {
        deposit.close();

        assertThrows(IllegalStateException.class, () -> deposit.close());
    }

    @Test
    @DisplayName("пополнение закрытого вклада отклоняется")
    void depositIntoClosedRejected() {
        deposit.close();

        assertThrows(IllegalStateException.class, () -> deposit.deposit(new BigDecimal("100.00")), "closed");
    }
}
