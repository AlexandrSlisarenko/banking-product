package ru.slisarenko.model.factory;

import ru.slisarenko.model.entity.CreditCard;
import ru.slisarenko.model.entity.DebitCard;
import ru.slisarenko.model.entity.FixPercentDeposit;

public class StandardBankProduct implements BankProductFactory{
    @Override
    public CreditCard createCreditCard(CreateProductData createProductData) {
        return new CreditCard(createProductData.name().name(),
                createProductData.currency(),
                createProductData.percent(),
                createProductData.start());
    }

    @Override
    public DebitCard createDebitCard(CreateProductData createProductData) {
        return new DebitCard(createProductData.name().name(),
                createProductData.currency());
    }

    @Override
    public FixPercentDeposit createFixPercentDeposit(CreateProductData createProductData) {
        return new FixPercentDeposit(createProductData.name().name(),
                createProductData.currency(),
                createProductData.percent()
        );
    }
}
