package com.ilearnrw.app.usermanager;

import ilearnrw.user.profile.UserProfile;
import ilearnrw.utils.LanguageCode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.sql.DataSource;
import javax.validation.Valid;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.ilearnrw.api.datalogger.model.LogEntryResult;
import com.ilearnrw.api.profileAccessUpdater.IProfileProvider;
import com.ilearnrw.api.profileAccessUpdater.IProfileProvider.ProfileProviderException;
import com.ilearnrw.app.usermanager.form.ExpertTeacherForm;
import com.ilearnrw.app.usermanager.form.RolePermissionsForm;
import com.ilearnrw.app.usermanager.form.TeacherStudentForm;
import com.ilearnrw.app.usermanager.form.UserNewForm;
import com.ilearnrw.common.AuthenticatedRestClient;
import com.ilearnrw.common.security.Tokens;
import com.ilearnrw.common.security.users.model.Permission;
import com.ilearnrw.common.security.users.model.Role;
import com.ilearnrw.common.security.users.model.StudentDetails;
import com.ilearnrw.common.security.users.model.User;
import com.ilearnrw.common.security.users.services.ExpertTeacherService;
import com.ilearnrw.common.security.users.services.PermissionService;
import com.ilearnrw.common.security.users.services.RoleService;
import com.ilearnrw.common.security.users.services.StudentDetailsService;
import com.ilearnrw.common.security.users.services.TeacherStudentService;
import com.ilearnrw.common.security.users.services.UserService;

@Controller
public class UserManagerController {

	private static Logger LOG = Logger.getLogger(UserManagerController.class);

	@Autowired
	@Qualifier("usersDataSource")
	private DataSource dSource;

	@Autowired
	private UserService userService;

	@Autowired
	private RoleService roleService;

	@Autowired
	private PermissionService permissionService;

	@Autowired
	private TeacherStudentService teacherStudentService;
	
	@Autowired
	private ExpertTeacherService expertTeacherService;

	@Autowired
	private StudentDetailsService studentDetailsService;

	@Autowired
	IProfileProvider profileProvider;

	@Autowired
	private AuthenticatedRestClient restClient;

	@RequestMapping(value = "/panel", method = RequestMethod.GET)
	public String panel(ModelMap modelMap) {
		return "main/panel";
	}
	
	@RequestMapping(value = "users/{id}/stats", method = RequestMethod.GET)
	public String stats(ModelMap modelMap) {
		return "statistics/stats";
	}

	@RequestMapping(value = "/logout")
	public String logout(HttpServletRequest request) {
		SecurityContextHolder.getContext().setAuthentication(null);
		request.getSession().invalidate();
		return "redirect:/apps/login";
	}

	@RequestMapping(value = "/login")
	public String login() {
		return "main/login";
	}

	@RequestMapping(value = "/loginfailed", method = RequestMethod.GET)
	public String loginerror(ModelMap model) {
		model.addAttribute("error", "true");
		return "main/login";
	}

	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public String login(@RequestParam("username") String username,
			@RequestParam("pass") String pass, ModelMap model,
			HttpServletRequest request) {

		try {
			Tokens tokens = restClient.getTokens(username, pass);

			List<String> roles = restClient.getRoles(tokens.getAuthToken());

			List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
			for (String role : roles) {
				authorities.add(new SimpleGrantedAuthority(role));
			}
			Authentication userAuthentication = new UsernamePasswordAuthenticationToken(
					username, tokens.getAuthToken(), authorities);

			SecurityContextHolder.getContext().setAuthentication(
					userAuthentication);

			request.getSession().setAttribute("username", username);
			User user = userService.getUserByUsername(username);
			request.getSession().setAttribute("userid", user.getId());
			request.getSession().setAttribute("students", teacherStudentService.getStudentList(user));

		} catch (Exception e) {
			model.addAttribute("error", e.getMessage());
			return "main/login";
		}
		return "redirect:/apps/panel";
	}

	/* Users logs */
	
	@RequestMapping(value = "users/manage")
	public String manageUsers(ModelMap modelMap, HttpServletRequest request, ModelMap model) {
		if (request.isUserInRole("PERMISSION_ADMIN"))
			modelMap.addAttribute("users", getUsersManagedByAdmin());
		else if (request.isUserInRole("PERMISSION_EXPERT"))
			modelMap.addAttribute("users", getUsersManagedByExpert());
		else if (request.isUserInRole("PERMISSION_TEACHER"))
			modelMap.addAttribute("users", getUsersManagedByTeacher());
		model.put("teachersList", roleService.getUsersWithRole("ROLE_TEACHER"));
		return "users/manage";
	}

	private List<UserNewForm> getUsersManagedByAdmin() {
		List<User> users = userService.getUserList();
		List<UserNewForm> userRoles = new ArrayList<UserNewForm>();
		for (User user : users)
		{
			List<Role> roles = roleService.getRoleList(user);
			StudentDetails sd = studentDetailsService.getStudentDetails(user.getId());
			if (roles.size() == 1)
				userRoles.add(new UserNewForm(user, roles.get(0).getName(), sd));
			else {
				userRoles.add(new UserNewForm(user, "none", sd));
			}
		}
		return userRoles;
	}
	
	private List<UserNewForm> getUsersManagedByExpert() {
		User current = userService.getUserByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
		List<User> teachers = expertTeacherService.getTeacherList(current);
		List<User> users = new ArrayList<User>();
		for (User teacher : teachers)
		{
			users.add(teacher);
			users.addAll(teacherStudentService.getStudentList(teacher));
		}
		users.addAll(teacherStudentService.getUnassignedStudentsList());
		List<UserNewForm> userRoles = new ArrayList<UserNewForm>();
		for (User user : users)
		{
			List<Role> roles = roleService.getRoleList(user);
			StudentDetails sd = studentDetailsService.getStudentDetails(user.getId());
			if (roles.size() == 1)
				userRoles.add(new UserNewForm(user, roles.get(0).getName(), sd));
			else {
				userRoles.add(new UserNewForm(user, "none", sd));
			}
		}
		return userRoles;
	}
	
	private List<UserNewForm> getUsersManagedByTeacher() {
		User current = userService.getUserByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
		List<User> users = teacherStudentService.getStudentList(current);
		List<UserNewForm> userRoles = new ArrayList<UserNewForm>();
		for (User user : users)
		{
			List<Role> roles = roleService.getRoleList(user);
			StudentDetails sd = studentDetailsService.getStudentDetails(user.getId());
			if (roles.size() == 1)
				userRoles.add(new UserNewForm(user, roles.get(0).getName(), sd));
			else {
				userRoles.add(new UserNewForm(user, "none", sd));
			}
		}
		return userRoles;
	}
	
	@RequestMapping(value = "users/{id}/logs/page/{page}", method = RequestMethod.GET)
	public String viewLogs(@PathVariable int id,
			@PathVariable String page, ModelMap model,
			HttpServletRequest request) {

		Map<String, String> args = new HashMap<String, String>();
		User user = userService.getUser(id);
		args.put("username", user.getUsername());
		args.put("page", page);
		for (String param : Arrays.asList("timestart", "timeend", "tags",
				"applicationId"))
			if (request.getSession().getAttribute(param) != null)
				args.put(param,
						(String) request.getSession().getAttribute(param));
		LogEntryResult result = restClient.getLogs(args);
		model.addAttribute("logEntryResult", result);
		return "users/logs";
	}

	@RequestMapping(value = "users/{id}/logs/page/{page}", method = RequestMethod.POST)
	public String viewLogsFiltered(
			@PathVariable int id,
			@PathVariable String page,
			@RequestParam(value = "timestart", required = false) String timestart,
			@RequestParam(value = "timeend", required = false) String timeend,
			@RequestParam(value = "tags", required = false) String tags,
			@RequestParam(value = "applicationId", required = false) String applicationId,
			ModelMap model, HttpServletRequest request) {
		Map<String, String> map = new HashMap<String, String>();
		map.put("timestart", timestart);
		map.put("timeend", timeend);
		map.put("tags", tags);
		map.put("applicationId", applicationId);
		for (Map.Entry<String, String> entry : map.entrySet()) {
			if (entry.getValue() != null && !entry.getValue().isEmpty())
				request.getSession().setAttribute(entry.getKey(),
						entry.getValue());
			else
				request.getSession().removeAttribute(entry.getKey());
		}

		return viewLogs(id, page, model, request);
	}

	/* Users profile */

	@RequestMapping(value = "users/{id}/profile", method = RequestMethod.GET)
	@Transactional(readOnly = true)
	public String viewProfile(@PathVariable int id, ModelMap model)
			throws ProfileProviderException, Exception {
		UserProfile profile = null;
		try {
			profile = profileProvider.getProfile(id);
		} catch (ProfileProviderException e) {
			LOG.error(e);
		}

		if (profile == null) {
			throw new Exception("No profile available.");
		}

		model.put("userId", id);
		model.put("profile", profile);
		return "users/profile";
	}

	@RequestMapping(value = "users/{id}/profile", method = RequestMethod.POST)
	@Transactional(readOnly = true)
	public String updateProfile(@ModelAttribute("profile") UserProfile profile,
			@PathVariable int id) throws ProfileProviderException {
		profileProvider.updateProfile(id, profile);
		return "redirect:/apps/panel";
	}

	@RequestMapping(value = "users", method = RequestMethod.POST)
	@Transactional
	public String insert(@ModelAttribute("user") User user,
			BindingResult result, ModelMap model) {
		if (result.hasErrors())
			return "users/form.update";

		userService.insertData(user);

		model.put("message", "inserted");
		return "redirect:/apps/panel";
	}

	/* Users */

	@RequestMapping(value = "/users/{id}/edit", method = RequestMethod.GET)
	@Transactional(readOnly = true)
	public String viewUserUpdateForm(@PathVariable int id, ModelMap model) {
		UserNewForm userForm = new UserNewForm();
		User user = userService.getUser(id);
		user.setPassword("");

		List<Role> selectedRoles = roleService.getRoleList(user);
		userForm.setUser(user);
		userForm.setRole(selectedRoles.get(0).getName());
		userForm.setStudentDetails(studentDetailsService.getStudentDetails(user.getId()));

		model.put("userform", userForm);
		model.put("schools", studentDetailsService.getSchools());
		model.put("classRooms", studentDetailsService.getClassRooms());
		model.put("teachersList", roleService.getUsersWithRole("ROLE_TEACHER"));

		return "users/form.update";
	}

	@RequestMapping(value = "users/{id}/edit", method = RequestMethod.POST)
	@Transactional
	public String updateUser(@PathVariable int id,
			@Valid @ModelAttribute("userform") UserNewForm userForm,
			BindingResult result, ModelMap model) {
		User user = userForm.getUser();
		user.setId(id);
		userForm.setUser(user);
		if (user.getBirthdate().after(new Date()))
			result.rejectValue("birthdate", "birthdate.invalid");
		else {
			Calendar calendar = Calendar.getInstance();
			calendar.add(Calendar.YEAR, -100);
			if (user.getBirthdate().before(calendar.getTime()))
				result.rejectValue("birthdate", "birthdate.invalid");
		}
		if (result.hasErrors()) {
			return "users/form.update";
		}

		userService.updateData(userForm.getUser());
		StudentDetails sd = userForm.getStudentDetails();
		sd.setStudentId(user.getId());
		studentDetailsService.updateData(userForm.getStudentDetails());
		return "redirect:/apps/users/manage";
	}

	@RequestMapping(value = "users/{id}/delete", method = RequestMethod.GET)
	@Transactional
	public String deleteUser(@PathVariable int id) {
		userService.deleteData(id);
		return "redirect:/apps/users/manage";
	}

	@RequestMapping(value = "users/new", method = RequestMethod.GET)
	@Transactional(readOnly = true)
	public String viewUserInsertForm(@ModelAttribute("userform") UserNewForm form, ModelMap model) {
		model.put("schools", studentDetailsService.getSchools());
		model.put("classRooms", studentDetailsService.getClassRooms());
		model.put("teachersList", roleService.getUsersWithRole("ROLE_TEACHER"));
		return "users/form.insert";
	}

	@RequestMapping(value = "users/new", method = RequestMethod.POST)
	@Transactional
	public String insertUser(@Valid @ModelAttribute("userform") UserNewForm form,
			BindingResult result, HttpServletRequest request, ModelMap model) throws ProfileProviderException {

		User current = userService.getUserByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
		User user = form.getUser();
		if (user.getBirthdate().after(new Date()))
			result.rejectValue("birthdate", "birthdate.invalid");
		else {
			Calendar calendar = Calendar.getInstance();
			calendar.add(Calendar.YEAR, -100);
			if (user.getBirthdate().before(calendar.getTime()))
				result.rejectValue("birthdate", "birthdate.invalid");
		}
		if (userService.getUserByUsername(user.getUsername()) != null)
			result.rejectValue("user.username", "user.username.exists");
		if (result.hasErrors())
			return "users/form.insert";
		int userId = userService.insertData(user);
		if (form.getRole().equals("admin"))
		{
			roleService.setRoleList(user, Arrays.asList(roleService.getRole("ROLE_ADMIN")));
		}
		else if (form.getRole().equals("expert"))
		{
			roleService.setRoleList(user, Arrays.asList(roleService.getRole("ROLE_EXPERT")));
		}
		else if (form.getRole().equals("teacher"))
		{
			roleService.setRoleList(user, Arrays.asList(roleService.getRole("ROLE_TEACHER")));
			if (request.isUserInRole("PERMISSION_EXPERT"))
				expertTeacherService.assignTeacherToExpert(current, user);
		}
		else if (form.getRole().equals("student"))
		{
			roleService.setRoleList(user, Arrays.asList(roleService.getRole("ROLE_STUDENT")));
			if (request.isUserInRole("PERMISSION_TEACHER"))
				teacherStudentService.assignStudentToTeacher(current, user);
				StudentDetails sd = form.getStudentDetails();
				sd.setStudentId(user.getId());
				studentDetailsService.insertData(sd);
		}
		profileProvider.createProfile(userId, LanguageCode.fromString(user.getLanguage()));
		

		return "redirect:/apps/users/manage";
	}

	/* Roles */

	@RequestMapping(value = "roles/manage")
	public String manageRoles(ModelMap modelMap) {
		modelMap.addAttribute("roles", roleService.getRoleList());
		return "roles/manage";
	}
	
	@RequestMapping(value = "/roles/{id}/edit", method = RequestMethod.GET)
	@Transactional(readOnly = true)
	public String viewRoleUpdateForm(@PathVariable int id, ModelMap model) {
		RolePermissionsForm roleForm = new RolePermissionsForm();
		Role role = roleService.getRole(id);
		List<Permission> allPermissions = permissionService.getPermissionList();
		List<Permission> selectedPermissions = permissionService
				.getPermissionList(role);
		roleForm.setRole(role);
		roleForm.setAllPermissions(allPermissions);
		roleForm.setSelectedPermissions(selectedPermissions);
		model.put("roleform", roleForm);
		return "roles/form.update";
	}

	@RequestMapping(value = "roles/{id}/edit", method = RequestMethod.POST)
	@Transactional
	public String updateRole(@PathVariable int id,
			@Valid @ModelAttribute("roleform") RolePermissionsForm roleForm,
			BindingResult result, ModelMap model) {
		Role role = roleForm.getRole();
		role.setId(id);
		roleForm.setRole(role);

		if (result.hasErrors()) {
			List<Permission> allPermissions = permissionService
					.getPermissionList();
			roleForm.setAllPermissions(allPermissions);
			return "roles/form.update";
		}

		roleService.updateData(roleForm.getRole());
		permissionService.setPermissionList(roleForm.getRole(),
				roleForm.getSelectedPermissions());
		return "redirect:/apps/roles/manage";
	}

	@RequestMapping(value = "roles/{id}/delete", method = RequestMethod.GET)
	@Transactional
	public String deleteRole(@PathVariable int id) {
		roleService.deleteData(id);
		return "redirect:/apps/roles/manage";
	}

	@RequestMapping(value = "roles/new", method = RequestMethod.GET)
	@Transactional(readOnly = true)
	public String viewRoleInsertForm(@ModelAttribute("role") Role role) {
		return "roles/form.insert";
	}

	@RequestMapping(value = "roles/new", method = RequestMethod.POST)
	@Transactional
	public String insertRole(@Valid @ModelAttribute("role") Role role,
			BindingResult result, ModelMap model) {

		if (result.hasErrors())
			return "roles/form.insert";
		roleService.insertData(role);

		model.put("message", "inserted");
		return "redirect:/apps/roles/manage";
	}

	/* Permissions */
	
	@RequestMapping(value = "permissions/manage")
	public String managePermissions(ModelMap modelMap) {
		modelMap.addAttribute("permissions", permissionService.getPermissionList());
		return "permissions/manage";
	}

	@RequestMapping(value = "/permissions/{id}/edit", method = RequestMethod.GET)
	@Transactional(readOnly = true)
	public String viewPermissionUpdateForm(@PathVariable int id, ModelMap model) {
		Permission permission = permissionService.getPermission(id);
		model.put("permission", permission);
		return "permissions/form.update";
	}

	@RequestMapping(value = "permissions/{id}/edit", method = RequestMethod.POST)
	@Transactional
	public String updatePermission(@PathVariable int id,
			@Valid @ModelAttribute("permission") Permission permission,
			BindingResult result, ModelMap model) {

		if (result.hasErrors())
			return "permissions/form.update";
		permissionService.updateData(permission);
		return "redirect:/apps/permissions/manage";
	}

	@RequestMapping(value = "permissions/{id}/delete", method = RequestMethod.GET)
	@Transactional
	public String deletePermission(@PathVariable int id) {
		permissionService.deleteData(id);
		return "redirect:/apps/permissions/manage";
	}

	@RequestMapping(value = "permissions/new", method = RequestMethod.GET)
	@Transactional(readOnly = true)
	public String viewPermissionInsertForm(
			@ModelAttribute("permission") Permission permission) {
		return "permissions/form.insert";
	}

	@RequestMapping(value = "permissions/new", method = RequestMethod.POST)
	@Transactional
	public String insertPermission(
			@Valid @ModelAttribute("permission") Permission permission,
			BindingResult result, ModelMap model) {

		if (result.hasErrors())
			return "permissions/form.insert";
		permissionService.insertData(permission);

		model.put("message", "inserted");
		return "redirect:/apps/permissions/manage";
	}

	/* Teachers */

	@RequestMapping(value = "teachers/{id}/assign", method = RequestMethod.GET)
	@Transactional(readOnly = true)
	public String viewTeachersAssignForm(@PathVariable int id, ModelMap model) {
		User teacher = userService.getUser(id);
		TeacherStudentForm teacherStudentForm = new TeacherStudentForm();
		teacherStudentForm.setTeacher(teacher);
		List<User> unassignedStudentsList = teacherStudentService
				.getUnassignedStudentsList();
		unassignedStudentsList.addAll(teacherStudentService.getStudentList(teacher));
		teacherStudentForm.setAllStudents(unassignedStudentsList);
		teacherStudentForm.setSelectedStudents(teacherStudentService
				.getStudentList(teacher));
		model.put("teacherStudentForm", teacherStudentForm);
		return "teachers/assign";
	}

	@RequestMapping(value = "teachers/{id}/assign", method = RequestMethod.POST)
	@Transactional
	public String viewTeachersAssignForm(
			@PathVariable int id,
			@ModelAttribute("teacherStudentForm") TeacherStudentForm teacherStudentForm,
			BindingResult result, ModelMap model, HttpServletRequest request) {
		User teacher = userService.getUser(id);
		teacherStudentService.setStudentList(teacher,
				teacherStudentForm.getSelectedStudents());
		request.getSession().setAttribute("students", teacherStudentService.getStudentList(teacher));
		return "redirect:/apps/users/manage";
	}
	
	/* Experts */
	
	@RequestMapping(value = "experts/{id}/assign", method = RequestMethod.GET)
	@Transactional(readOnly = true)
	public String viewExpertsAssignForm(@PathVariable int id, ModelMap model) {
		User expert = userService.getUser(id);
		ExpertTeacherForm expertTeacherForm = new ExpertTeacherForm();
		expertTeacherForm.setExpert(expert);
		List<User> unassignedTeachersList = expertTeacherService
				.getUnassignedTeachersList();
		unassignedTeachersList.addAll(expertTeacherService.getTeacherList(expert));
		expertTeacherForm.setAllTeachers(unassignedTeachersList);
		expertTeacherForm.setSelectedTeachers(expertTeacherService
				.getTeacherList(expert));
		model.put("expertTeacherForm", expertTeacherForm);
		return "experts/assign";
	}

	@RequestMapping(value = "experts/{id}/assign", method = RequestMethod.POST)
	@Transactional
	public String viewExpertsAssignForm(
			@PathVariable int id,
			@ModelAttribute("expertTeacherForm") ExpertTeacherForm expertTeacherForm,
			BindingResult result, ModelMap model, HttpServletRequest request) {
		User expert = userService.getUser(id);
		expertTeacherService.setTeacherList(expert,
				expertTeacherForm.getSelectedTeachers());
		request.getSession().setAttribute("teachers", expertTeacherService.getTeacherList(expert));
		return "redirect:/apps/users/manage";
	}

}
