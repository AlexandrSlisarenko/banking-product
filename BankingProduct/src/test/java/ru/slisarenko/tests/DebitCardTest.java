package ru.slisarenko.tests;

import java.math.BigDecimal;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;
import ru.slisarenko.fixtures.BankProductFixtures;
import ru.slisarenko.model.entity.DebitCard;
import ru.slisarenko.model.exceptions.InsufficientFundsException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DisplayName("Дебетовая карта")
public class DebitCardTest {
    private DebitCard card;

    @BeforeEach
    void setUp() {
        card = BankProductFixtures.debitCardRUB();
    }

    @Test
    @DisplayName("возвращает валюту, заданную при создании")
    void returnsCurrency() {
        assertEquals(BankProductFixtures.RUB, card.getCurrency());
    }

    @ParameterizedTest
    @NullSource
    @ValueSource(strings = {" "})
    @DisplayName("создание с недопустимым именем отклоняется")
    void rejectsInvalidName(String name) {
        assertThrows(IllegalArgumentException.class, () -> new DebitCard(name, BankProductFixtures.RUB));
    }

    @Nested
    @DisplayName("Пополнение")
    class DepositOperations {

        @Test
        @DisplayName("увеличивает баланс на сумму")
        void increasesBalance() {
            BigDecimal expected = new BigDecimal("500.50");
            card.deposit(expected);

            assertEquals(expected, card.getBalance());
        }

        @ParameterizedTest
        @ValueSource(strings = {"0", "-0.01", "-100.00"})
        @DisplayName("неположительная сумма отклоняется")
        void rejectsNonPositive(String amount) {
            assertThrows(IllegalArgumentException.class, () -> card.deposit(new BigDecimal(amount)));
        }

        @Test
        @DisplayName("null отклоняется")
        void rejectsNull() {
            assertThrows(IllegalArgumentException.class, () -> card.deposit(null));
        }

        @Test
        @DisplayName("баланс не меняется при отказе")
        void balanceUnchangedOnReject() {
            assertThrows(IllegalArgumentException.class, () -> card.deposit(new BigDecimal("-1")));
            assertEquals(BigDecimal.ZERO, card.getBalance());
        }
    }

    @Nested
    @DisplayName("Списание")
    class WithdrawOperations {

        @Test
        @DisplayName("уменьшает баланс на сумму")
        void decreasesBalance() {
            card.deposit(new BigDecimal("1000.00"));
            card.withdraw(new BigDecimal("300.00"));

            assertEquals(new BigDecimal("700.00"), card.getBalance());
        }

        @Test
        @DisplayName("позволяет списать весь баланс")
        void allowsWithdrawAll() {
            card.deposit(new BigDecimal("100.00"));
            card.withdraw(new BigDecimal("100.00"));

            assertEquals("0.00", card.getBalance().toString());
        }

        @Test
        @DisplayName("бросает InsufficientFundsException при нехватке средств")
        void insufficientFunds() {
            card.deposit(new BigDecimal("100.00"));
            assertThrows(InsufficientFundsException.class, () -> card.withdraw(new BigDecimal("100.01")), "Insufficient funds");
        }

        @Test
        @DisplayName("баланс не меняется при отказе")
        void balanceUnchangedOnInsufficientFunds() {
            BigDecimal expected = new BigDecimal("100.00");
            card.deposit(expected);

            assertThrows(InsufficientFundsException.class, () -> card.withdraw(new BigDecimal("100.01")));

            assertEquals(expected, card.getBalance());
        }
    }

}
