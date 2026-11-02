package ru.slisarenko.fixtures;

import java.math.BigDecimal;
import java.util.Currency;
import ru.slisarenko.model.NamesProduct;
import ru.slisarenko.model.entity.CreditCard;
import ru.slisarenko.model.entity.DebitCard;
import ru.slisarenko.model.entity.Deposit;
import ru.slisarenko.model.entity.FixPercentDeposit;
import ru.slisarenko.model.factory.CreateProductData;
import ru.slisarenko.model.factory.StandardBankProduct;

public class BankProductFixtures {
    public static final Currency RUB = Currency.getInstance("RUB");
    public static final Currency USD = Currency.getInstance("USD");
    public static final Currency EUR = Currency.getInstance("EUR");

    private static final StandardBankProduct standardBankProduct = new StandardBankProduct();

    private BankProductFixtures() {}

    public static DebitCard debitCardRUB() {
        CreateProductData data = new CreateProductData(NamesProduct.DEBIT_CARD, RUB, null, null);
        return standardBankProduct.createDebitCard(data);
    }

    public static CreditCard creditCardUSD() {
        CreateProductData data = new CreateProductData(NamesProduct.CREDIT_CARD, USD, BigDecimal.valueOf(14), new BigDecimal("1000.00"));
        return standardBankProduct.createCreditCard(data);
    }

    public static FixPercentDeposit depositEUR() {
        CreateProductData data = new CreateProductData(NamesProduct.FIX_PERCENT_DEPOSIT, EUR, BigDecimal.valueOf(5), null);
        return standardBankProduct.createFixPercentDeposit(data);
    }
}
