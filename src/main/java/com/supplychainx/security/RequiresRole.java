package com.supplychainx.security;

import com.supplychainx.common.enums.UserRole;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation pour spécifier les rôles autorisés à accéder à une méthode
 * Exemple: @RequiresRole({UserRole.ADMIN, UserRole.CHEF_PRODUCTION})
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface RequiresRole {
    UserRole[] value(); // Les rôles autorisés
}
