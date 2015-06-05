package com.ilearnrw.common.security;
/*
 * Copyright (c) 2015, iLearnRW. Licensed under Modified BSD Licence. See licence.txt for details.
 */
import org.springframework.security.core.Authentication;

public interface PermissionChecker {
    boolean isAllowed(Authentication authentication, Object targetDomainObject);
}
