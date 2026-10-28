package ru.slisarenko.model.exceptions;

public class CreditRestrictionException extends RuntimeException {
    public CreditRestrictionException(String message) {
        super(message);
    }
}
