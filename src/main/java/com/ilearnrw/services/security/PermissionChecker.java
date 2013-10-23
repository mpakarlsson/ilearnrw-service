package com.ilearnrw.services.security;

import org.springframework.security.core.Authentication;

public interface PermissionChecker {
    boolean isAllowed(Authentication authentication, Object targetDomainObject);
}
