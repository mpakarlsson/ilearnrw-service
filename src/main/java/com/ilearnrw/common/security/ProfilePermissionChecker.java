package com.ilearnrw.common.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;

import com.ilearnrw.common.security.users.model.User;
import com.ilearnrw.common.security.users.services.TeacherStudentService;
import com.ilearnrw.common.security.users.services.UserService;

public class ProfilePermissionChecker implements PermissionChecker {

	@Autowired
	private TeacherStudentService teacherStudentService;

	@Autowired
	private UserService userService;

	@Override
	public boolean isAllowed(Authentication authentication,
			Object targetDomainObject) {

		boolean isAllowed = false;
		if (targetDomainObject instanceof String) {
			String userId = (String) targetDomainObject;
			User user = userService.getUser(Integer.parseInt(userId));
			String loggedInUser = (String) authentication.getPrincipal();
			isAllowed = (loggedInUser.compareTo(user.getUsername()) == 0 || teacherStudentService
					.isUserStudentOfTeacher(user.getUsername(), loggedInUser));
		}

		return isAllowed;
	}

}
