package com.supplychainx.exception;

/**
 * Exception levée lorsqu'un utilisateur n'est pas autorisé à effectuer une action
 */
public class UnauthorizedException extends RuntimeException {
    
    public UnauthorizedException(String message) {
        super(message);
    }
}
