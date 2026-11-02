package ru.slisarenko.tests;

import java.math.BigDecimal;
import java.math.RoundingMode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.slisarenko.fixtures.BankProductFixtures;
import ru.slisarenko.model.entity.FixPercentDeposit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("Вклад")
public class FixPercentDepositTest {
    private FixPercentDeposit deposit;

    @BeforeEach
    void setUp() {
        deposit = BankProductFixtures.depositEUR();
    }


    @Test
    @DisplayName("пополнение увеличивает баланс")
    void checkSumWithPercent() {
        BigDecimal addingSum = BigDecimal.valueOf(10000);
        deposit.deposit(addingSum);
        BigDecimal expected = addingSum.multiply(deposit.getPercent().divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP)).add(addingSum);



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
