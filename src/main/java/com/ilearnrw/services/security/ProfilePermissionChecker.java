package com.ilearnrw.services.security;

import org.springframework.security.core.Authentication;

public class ProfilePermissionChecker implements PermissionChecker {

	@Override
	public boolean isAllowed(Authentication authentication,
			Object targetDomainObject) {

		boolean isAllowed = false;
		if (targetDomainObject instanceof Integer) {
			int profileId = (Integer)targetDomainObject;
			
			// just as demo. Here we would need to check again if current user
			// has the right to the given profile
			if (profileId == 1)
			{
				isAllowed = true;
			}
		}

		return isAllowed;
	}

}
