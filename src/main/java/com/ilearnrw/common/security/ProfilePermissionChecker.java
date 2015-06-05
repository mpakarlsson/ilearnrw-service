package com.ilearnrw.common.security;
/*
 * Copyright (c) 2015, iLearnRW. Licensed under Modified BSD Licence. See licence.txt for details.
 */
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;

import com.ilearnrw.common.security.users.model.User;
import com.ilearnrw.common.security.users.services.ExpertTeacherService;
import com.ilearnrw.common.security.users.services.TeacherStudentService;
import com.ilearnrw.common.security.users.services.UserService;

public class ProfilePermissionChecker implements PermissionChecker {

	@Autowired
	private ExpertTeacherService expertTeacherService;

	@Autowired
	private TeacherStudentService teacherStudentService;

	@Autowired
	private UserService userService;

	@Override
	public boolean isAllowed(Authentication authentication,
			Object targetDomainObject) {

		boolean isAllowed = false;
		if (targetDomainObject instanceof Integer) {
			int userId = (Integer) targetDomainObject;
			User user = userService.getUser(userId);
			String loggedInUser = (String) authentication.getPrincipal();
			isAllowed = (loggedInUser.compareTo(user.getUsername()) == 0
					|| teacherStudentService.isUserStudentOfTeacher(
							user.getUsername(), loggedInUser) || expertTeacherService
					.isUserAssignedToExpert(teacherStudentService
							.getTeacherOfStudent(user).getUsername(),
							loggedInUser));
		}

		return isAllowed;
	}

}
