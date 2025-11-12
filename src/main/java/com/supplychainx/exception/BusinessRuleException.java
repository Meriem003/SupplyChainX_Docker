package com.supplychainx.exception;

/**
 * Exception levée lorsqu'une règle métier est violée
 */
public class BusinessRuleException extends RuntimeException {
    
    public BusinessRuleException(String message) {
        super(message);
    }
}
