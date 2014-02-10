package com.ilearnrw.api.setup;

import ilearnrw.languagetools.greek.GreekDictionaryLoader;
import ilearnrw.textclassification.Word;
import ilearnrw.textclassification.greek.GreekWord;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ilearnrw.common.security.users.model.Permission;
import com.ilearnrw.common.security.users.model.Role;
import com.ilearnrw.common.security.users.model.User;
import com.ilearnrw.common.security.users.services.PermissionService;
import com.ilearnrw.common.security.users.services.RoleService;
import com.ilearnrw.common.security.users.services.UserService;

@Controller
public class SetupController {

	@Autowired
	UserService userService;

	@Autowired
	RoleService roleService;

	@Autowired
	PermissionService permissionService;

	private static Logger LOG = Logger.getLogger(SetupController.class);

	private List<String> outputLog = new ArrayList<String>();
	
	@RequestMapping(value = "/setup", method = RequestMethod.GET)
	public @ResponseBody
	List<String> setup() {
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

			User chris_t = createBoy("chris_t");
			chris_t.setBirthdate(df.parse("2007-04-21"));
			chris_t.setLanguage("GR");
			users.add(chris_t);

			User xena_t = createGirl("xena_t");
			xena_t.setBirthdate(df.parse("2009-09-11"));
			xena_t.setLanguage("GR");
			users.add(xena_t);

			User teacher = new User();
			teacher.setUsername("teacher");
			teacher.setPassword("test");
			teacher.setBirthdate(new Date());
			teacher.setLanguage("EN");
			teacher.setGender("F");
			teacher.setEnabled(true);
			users.add(teacher);

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
			roleService.setRoleList(chris_t, studentRole);
			roleService.setRoleList(xena_t, studentRole);
			outputLog.add("Added roles for students");

			List<Role> teacherRole = new ArrayList<Role>();
			teacherRole.add(roleService.getRole("ROLE_TEACHER"));
			roleService.setRoleList(teacher, teacherRole);
			outputLog.add("Added roles for teacher");

			
			permissionService.setPermissionList(roleService.getRole("ROLE_ADMIN"), permissionService.getPermissionList());
			permissionService.setPermissionList(roleService.getRole("ROLE_STUDENT"), permissionService.getPermissionList());
			permissionService.setPermissionList(roleService.getRole("ROLE_TEACHER"), permissionService.getPermissionList());

		} catch (Exception e) {
			outputLog.add("Exception: " + e.getMessage());
		}

		return outputLog;
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
		perm.setName("DEFAULT");
		permissions.add(perm);

		for (Permission p : permissions) {
			permissionService.insertData(p);
			outputLog.add(String.format("Added permission: %s", p.getName()));
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

		for (Role r : roles) {
			try {
				roleService.insertData(r);
				outputLog.add(String.format("Added role: %s", r.getName()));
				
			} catch (Exception e) {
				outputLog.add(String.format("Exception on role %s: %s", r.getName(), e.getMessage()));
			}
		}
		
		return roles;

	}

	private void createUsers(List<User> users) {
		for (User u : users) {
			try {
				userService.insertData(u);
				outputLog.add(String.format("Inserted user %s!", u.getUsername()));
			} catch (Exception e) {
				outputLog.add(String.format("Exception on user %s: %s",
						u.getUsername(), e.getMessage()));
			}
		}

	}
}