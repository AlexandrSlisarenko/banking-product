package ru.slisarenko.tests;

import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import ru.slisarenko.fixtures.BankProductFixtures;
import ru.slisarenko.model.entity.BaseBankProduct;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DisplayName("Контракт BankProduct для всех продуктов")
public class BankProductContractTest {
    static Stream<BaseBankProduct> allProducts() {
        return Stream.of(
                BankProductFixtures.debitCardRUB(),
                BankProductFixtures.creditCardRUB(),
                BankProductFixtures.depositRUB()
        );
    }

    @ParameterizedTest(name = "[{index}] {0}")
    @MethodSource("allProducts")
    @DisplayName("валюта и имя задаются при создании")
    void exposesMetadata(BaseBankProduct product) {
        assertNotNull(product.getCurrency());
        assertFalse(product.getName().isBlank());
    }

    @ParameterizedTest(name = "[{index}] {0}")
    @MethodSource("allProducts")
    @DisplayName("новый продукт не закрыт")
    void isOpenByDefault(BaseBankProduct product) {
        assertFalse(product.isClosed());
    }
}
