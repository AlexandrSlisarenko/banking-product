package ru.slisarenko.model.factory;

import java.math.BigDecimal;
import java.util.Currency;
import ru.slisarenko.model.NamesProduct;

public record CreateProductData(NamesProduct name, Currency currency, BigDecimal percent, BigDecimal start) {
}
