package com.supplychainx.security;

import com.supplychainx.common.entity.User;
import com.supplychainx.common.enums.UserRole;
import com.supplychainx.exception.UnauthorizedException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.lang.reflect.Method;

/**
 * Aspect AOP pour gérer la sécurité de l'application
 * Intercepte les méthodes annotées avec @RequiresAuth ou @RequiresRole
 * et vérifie l'authentification et les permissions
 */
@Aspect
@Component
@RequiredArgsConstructor
public class SecurityAspect {

    private final AuthenticationService authenticationService;

    /**
     * Intercepte toutes les méthodes annotées avec @RequiresAuth
     * Vérifie que l'utilisateur est authentifié via les headers email et password
     */
    @Before("@annotation(com.supplychainx.security.RequiresAuth)")
    public void checkAuthentication(JoinPoint joinPoint) {
        // Récupérer la requête HTTP actuelle
        HttpServletRequest request = getCurrentRequest();
        
        // Récupérer email et password depuis les headers
        String email = request.getHeader("email");
        String password = request.getHeader("password");
        
        // Authentifier l'utilisateur
        authenticationService.authenticate(email, password);
    }

    /**
     * Intercepte toutes les méthodes annotées avec @RequiresRole
     * Vérifie que l'utilisateur est authentifié ET qu'il a le bon rôle
     */
    @Before("@annotation(com.supplychainx.security.RequiresRole)")
    public void checkAuthorization(JoinPoint joinPoint) {
        // Récupérer la requête HTTP actuelle
        HttpServletRequest request = getCurrentRequest();
        
        // Récupérer email et password depuis les headers
        String email = request.getHeader("email");
        String password = request.getHeader("password");
        
        // Authentifier l'utilisateur
        User user = authenticationService.authenticate(email, password);
        
        // Récupérer les rôles requis depuis l'annotation
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        RequiresRole requiresRole = method.getAnnotation(RequiresRole.class);
        UserRole[] requiredRoles = requiresRole.value();
        
        // Vérifier si l'utilisateur a l'un des rôles requis
        authenticationService.checkRole(user, requiredRoles);
    }

    /**
     * Récupère la requête HTTP actuelle
     */
    private HttpServletRequest getCurrentRequest() {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes == null) {
            throw new UnauthorizedException("Impossible de récupérer la requête HTTP");
        }
        return attributes.getRequest();
    }
}
