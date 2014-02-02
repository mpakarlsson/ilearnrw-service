package com.ilearnrw.common.security;

import org.springframework.security.core.Authentication;

public interface PermissionChecker {
    boolean isAllowed(Authentication authentication, Object targetDomainObject);
}
