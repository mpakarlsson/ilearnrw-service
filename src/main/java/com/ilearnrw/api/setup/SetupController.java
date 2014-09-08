package com.ilearnrw.api.setup;

import ilearnrw.utils.LanguageCode;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ilearnrw.api.profileAccessUpdater.IProfileProvider;
import com.ilearnrw.api.profileAccessUpdater.IProfileProvider.ProfileProviderException;
import com.ilearnrw.common.security.users.model.Permission;
import com.ilearnrw.common.security.users.model.Role;
import com.ilearnrw.common.security.users.model.User;
import com.ilearnrw.common.security.users.services.ExpertTeacherService;
import com.ilearnrw.common.security.users.services.PermissionService;
import com.ilearnrw.common.security.users.services.RoleService;
import com.ilearnrw.common.security.users.services.TeacherStudentService;
import com.ilearnrw.common.security.users.services.UserService;

@Controller
public class SetupController {

	@Autowired
	UserService userService;

	@Autowired
	RoleService roleService;

	@Autowired
	PermissionService permissionService;
	
	@Autowired
	TeacherStudentService teacherStudentService;
	
	@Autowired
	ExpertTeacherService expertTeacherService;

	@Autowired
	IProfileProvider profileProvider;

	private static Logger LOG = Logger.getLogger(SetupController.class);

	private List<String> outputLog = new ArrayList<String>();

	@RequestMapping(value = "/setup", method = RequestMethod.GET)
	public @ResponseBody
	List<String> setup() {
		createUsersRolesPermissionsTeacherStudents();
		createProfiles();
		return outputLog;
	}

	private void createProfiles() {
		List<User> users = userService.getUserList();

		profileProvider.createTables();

		for (User user : users) {
			try {
				profileProvider.deleteProfile(user.getId());
			} catch (ProfileProviderException e) {
				LOG.debug(String.format(
						"Exception when deleting profile for user %s: %s",
						user.getUsername(), e.getMessage()));
			}
			try {
				profileProvider.createProfile(user.getId(),
						LanguageCode.fromString(user.getLanguage()));
				outputLog.add(String.format(
						"Profile created for user %s (Language=%s)",
						user.getUsername(), user.getLanguage()));
			} catch (ProfileProviderException e) {
				outputLog.add(String.format(
						"Exception on profile for user %s: %s",
						user.getUsername(), e.getMessage()));
			}
		}

	}

	private void createUsersRolesPermissionsTeacherStudents() {
		List<User> users = new ArrayList<User>();

		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		try {

			User admin = new User();
			admin.setUsername("admin");
			admin.setPassword("admin");
			admin.setBirthdate(new Date());
			admin.setLanguage("EN");
			admin.setGender("M");
			admin.setEnabled(true);
			users.add(admin);

			User joe_t = createBoy("joe_t");
			joe_t.setBirthdate(df.parse("2008-05-01"));
			joe_t.setLanguage("EN");
			users.add(joe_t);

			User sue_t = createGirl("sue_t");
			sue_t.setBirthdate(df.parse("2009-04-18"));
			sue_t.setLanguage("EN");
			users.add(sue_t);

			User maria_b = createBoy("Maria_Begineraki");
			maria_b.setBirthdate(df.parse("2007-04-21"));
			maria_b.setLanguage("GR");
			users.add(maria_b);

			User giorgos_e = createGirl("Giorgos_Expertidis");
			giorgos_e.setBirthdate(df.parse("2009-09-11"));
			giorgos_e.setLanguage("GR");
			users.add(giorgos_e);

			User englishteacher = new User();
			englishteacher.setUsername("englishteacher");
			englishteacher.setPassword("test");
			englishteacher.setBirthdate(new Date());
			englishteacher.setLanguage("EN");
			englishteacher.setGender("F");
			englishteacher.setEnabled(true);
			users.add(englishteacher);
			
			User greekteacher = new User();
			greekteacher.setUsername("greekteacher");
			greekteacher.setPassword("test");
			greekteacher.setBirthdate(new Date());
			greekteacher.setLanguage("GR");
			greekteacher.setGender("F");
			greekteacher.setEnabled(true);
			users.add(greekteacher);
			
			User expert = new User();
			expert.setUsername("expert");
			expert.setPassword("test");
			expert.setBirthdate(new Date());
			expert.setLanguage("EN");
			expert.setGender("M");
			expert.setEnabled(true);
			users.add(expert);

			createUsers(users);

			createDefaultPermissions();

			createDefaultRoles();

			List<Role> adminRole = new ArrayList<Role>();
			adminRole.add(roleService.getRole("ROLE_ADMIN"));
			roleService.setRoleList(admin, adminRole);
			outputLog.add("Added roles for admin");

			List<Role> studentRole = new ArrayList<Role>();
			studentRole.add(roleService.getRole("ROLE_STUDENT"));
			roleService.setRoleList(joe_t, studentRole);
			roleService.setRoleList(sue_t, studentRole);
			roleService.setRoleList(maria_b, studentRole);
			roleService.setRoleList(giorgos_e, studentRole);
			outputLog.add("Added roles for students");

			List<Role> teacherRole = new ArrayList<Role>();
			teacherRole.add(roleService.getRole("ROLE_TEACHER"));
			roleService.setRoleList(englishteacher, teacherRole);
			roleService.setRoleList(greekteacher, teacherRole);
			outputLog.add("Added roles for teachers");
			
			List<Role> expertRole = new ArrayList<Role>();
			expertRole.add(roleService.getRole("ROLE_EXPERT"));
			roleService.setRoleList(expert, expertRole);
			outputLog.add("Added roles for expert");

			permissionService.setPermissionList(roleService
					.getRole("ROLE_ADMIN"), Arrays.asList(
					permissionService.getPermission("PERMISSION_DEFAULT"),
					permissionService.getPermission("PERMISSION_ADMIN")));
			permissionService.setPermissionList(roleService
					.getRole("ROLE_STUDENT"), Arrays.asList(
					permissionService.getPermission("PERMISSION_DEFAULT"),
					permissionService.getPermission("PERMISSION_STUDENT")));
			permissionService.setPermissionList(roleService
					.getRole("ROLE_TEACHER"), Arrays.asList(
					permissionService.getPermission("PERMISSION_DEFAULT"),
					permissionService.getPermission("PERMISSION_TEACHER")));
			permissionService.setPermissionList(roleService
					.getRole("ROLE_EXPERT"), Arrays.asList(
					permissionService.getPermission("PERMISSION_DEFAULT"),
					permissionService.getPermission("PERMISSION_EXPERT")));
			
			teacherStudentService.setStudentList(englishteacher, Arrays.asList(joe_t, sue_t));
			teacherStudentService.setStudentList(greekteacher, Arrays.asList(maria_b, giorgos_e));
			expertTeacherService.setTeacherList(expert, Arrays.asList(englishteacher, greekteacher));
			
		} catch (Exception e) {
			outputLog.add("Exception: " + e.getMessage());
		}

	}

	private User createGirl(String name) {
		User girl = new User();
		girl.setEnabled(true);
		girl.setGender("F");
		girl.setUsername(name);
		girl.setPassword("test");
		return girl;
	}

	private User createBoy(String name) {
		User boy = new User();
		boy.setEnabled(true);
		boy.setGender("M");
		boy.setUsername(name);
		boy.setPassword("test");
		return boy;
	}

	private List<Permission> createDefaultPermissions() {
		List<Permission> permissions = new ArrayList<Permission>();

		Permission perm = new Permission("1");
		perm.setName("PERMISSION_DEFAULT");
		permissions.add(perm);

		perm = new Permission("2");
		perm.setName("PERMISSION_ADMIN");
		permissions.add(perm);

		perm = new Permission("3");
		perm.setName("PERMISSION_TEACHER");
		permissions.add(perm);

		perm = new Permission("4");
		perm.setName("PERMISSION_STUDENT");
		permissions.add(perm);
		
		perm = new Permission("5");
		perm.setName("PERMISSION_EXPERT");
		permissions.add(perm);

		for (Permission p : permissions) {
			Permission existingPermission = permissionService.getPermission(p
					.getName());

			if (existingPermission == null) {
				try {
					permissionService.insertData(p);
					outputLog.add(String.format("Added permission: %s",
							p.getName()));
				} catch (Exception e) {
					outputLog.add(String.format(
							"Exception when adding permission %s: %s",
							p.getName(), e.getMessage()));
				}
			}
		}

		return permissions;
	}

	private List<Role> createDefaultRoles() {
		List<Role> roles = new ArrayList<Role>();
		Role role = new Role();
		role.setName("ROLE_STUDENT");
		roles.add(role);

		role = new Role();
		role.setName("ROLE_ADMIN");
		roles.add(role);

		role = new Role();
		role.setName("ROLE_TEACHER");
		roles.add(role);
		
		role = new Role();
		role.setName("ROLE_EXPERT");
		roles.add(role);

		for (Role r : roles) {
			try {
				Role existingRole = roleService.getRole(r.getName());
				if (existingRole == null) {
					roleService.insertData(r);
					outputLog.add(String.format("Added role: %s", r.getName()));
				}

			} catch (Exception e) {
				outputLog.add(String.format("Exception on role %s: %s",
						r.getName(), e.getMessage()));
			}
		}

		return roles;

	}

	private void createUsers(List<User> users) {
		for (User u : users) {
			try {
				User existingUser = userService.getUserByUsername(u
						.getUsername());
				userService.deleteData(existingUser.getId());
				profileProvider.deleteProfile(existingUser.getId());
			} catch (Exception ex) {
				LOG.debug(String.format("Could not delete user %s",
						u.getUsername()));
			} catch (ProfileProviderException e) {
				LOG.debug(String.format(
						"Exception when deleting profile for user %s",
						u.getUsername()));
			}
			try {
				userService.insertData(u);
				outputLog.add(String.format("Inserted user %s!",
						u.getUsername()));
			} catch (Exception e) {
				outputLog.add(String.format("Exception on user %s: %s",
						u.getUsername(), e.getMessage()));
			}
		}

	}
}