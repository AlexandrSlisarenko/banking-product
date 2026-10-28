package ru.slisarenko.tests;

import java.math.BigDecimal;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import ru.slisarenko.fixtures.BankProductFixtures;
import ru.slisarenko.model.entity.CreditCard;
import ru.slisarenko.model.exceptions.CreditRestrictionException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class CreditCardTest {
    private CreditCard card;

    @BeforeEach
    void setUp() {
        card = BankProductFixtures.creditCardRUB();
    }

    @Test
    @DisplayName("процентная ставка доступна через геттер")
    void exposesInterestRate() {
        BigDecimal expected = new BigDecimal("0.14");
        assertEquals(expected, card.getInterestRate());
    }

    @ParameterizedTest
    @ValueSource(strings = {"-0.01", "-10"})
    @DisplayName("создание с отрицательной ставкой отклоняется")
    void rejectsNegativeRate(String rate) {
        assertThrows(CreditRestrictionException.class, () -> new CreditCard("Кредитная", BankProductFixtures.RUB, new BigDecimal(rate), new BigDecimal("1.00")));
    }

    @Nested
    @DisplayName("Списание")
    class WithdrawOperations {

        @Test
        @DisplayName("долг с процентами")
        void overBalanceCreatesDebt() {

            card.withdraw(new BigDecimal("800.00"));

            assertEquals("200.00", card.getBalance().toString());
            assertEquals("112.00", card.getDebtPercent().toString());
        }
    }

    @Nested
    @DisplayName("Пополнение")
    class DepositOperations {

        @Test
        @DisplayName("сначала гасит долг, остаток идёт на баланс")
        void repaysDebtFirst() {
            card.withdraw(new BigDecimal("800.00"));
            card.deposit(new BigDecimal("500.00"));

            assertEquals("588.00", card.getBalance().toString());
        }

        @Test
        @DisplayName("пополнение меньше процентов по догу — ошибка")
        void partialRepaymentError() {
            card.withdraw(new BigDecimal("800.00"));

            assertThrows(CreditRestrictionException.class, () -> card.deposit(new BigDecimal("100")));
        }

        @ParameterizedTest
        @CsvSource({
                "112.00, 200.00",
                "115.00, 203.00"
        })
        @DisplayName("пополнение >= процентов по догу")
        void partialRepayment(String payment, String balance) {
            card.withdraw(new BigDecimal("800.00"));
            card.deposit(new BigDecimal(payment));

            assertEquals(balance, card.getBalance().toString());
        }
    }
}
