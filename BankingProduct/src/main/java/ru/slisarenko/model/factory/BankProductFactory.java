package ru.slisarenko.model.factory;

import ru.slisarenko.model.entity.CreditCard;
import ru.slisarenko.model.entity.DebitCard;
import ru.slisarenko.model.entity.FixPercentDeposit;

public interface BankProductFactory {
    CreditCard createCreditCard(CreateProductData createProductData);
    DebitCard createDebitCard(CreateProductData createProductData);
    FixPercentDeposit createFixPercentDeposit(CreateProductData createProductData);
}
