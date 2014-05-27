package com.ilearnrw.app.usermanager;

import ilearnrw.user.profile.UserProfile;
import ilearnrw.utils.LanguageCode;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.sql.DataSource;
import javax.validation.Valid;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.concurrent.ConcurrentMapCache;
import org.springframework.context.ApplicationContext;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ilearnrw.api.datalogger.model.LogEntryResult;
import com.ilearnrw.api.profileAccessUpdater.IProfileProvider;
import com.ilearnrw.api.profileAccessUpdater.IProfileProvider.ProfileProviderException;
import com.ilearnrw.app.usermanager.form.RoleForm;
import com.ilearnrw.app.usermanager.form.TeacherStudentForm;
import com.ilearnrw.app.usermanager.form.UserForm;
import com.ilearnrw.common.AuthenticatedRestClient;
import com.ilearnrw.common.security.Tokens;
import com.ilearnrw.common.security.users.model.Permission;
import com.ilearnrw.common.security.users.model.Role;
import com.ilearnrw.common.security.users.model.User;
import com.ilearnrw.common.security.users.services.PermissionService;
import com.ilearnrw.common.security.users.services.RoleService;
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
	IProfileProvider profileProvider;

	@Autowired
	private AuthenticatedRestClient restClient;

	@RequestMapping(value = "/panel", method = RequestMethod.GET)
	public String panel(ModelMap modelMap) {
		modelMap.addAttribute("teachers",
				teacherStudentService.getTeacherList());
		modelMap.addAttribute("users", userService.getUserList());
		modelMap.addAttribute("roles", roleService.getRoleList());
		modelMap.addAttribute("permissions",
				permissionService.getPermissionList());
		return "main/panel";
	}

	@RequestMapping(value = "/logout")
	public String logout(HttpServletRequest request) {
		SecurityContextHolder.getContext().setAuthentication(null);
		request.getSession().invalidate();
		return "main/login";
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

			request.getSession().setAttribute("user", username);

		} catch (Exception e) {
			model.addAttribute("error", e.getMessage());
			return "main/login";
		}
		return "redirect:/apps/panel";
	}

	/* Users logs */
	
	@RequestMapping(value = "users/manage")
	public String manageUsers(ModelMap modelMap) {
		modelMap.addAttribute("users", userService.getUserList());
		return "users/manage";
	}

	@RequestMapping(value = "users/{username}/logs/page/{page}", method = RequestMethod.GET)
	public String viewLogs(@PathVariable String username,
			@PathVariable String page, ModelMap model,
			HttpServletRequest request) {

		Map<String, String> args = new HashMap<String, String>();
		args.put("username", username);
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

	@RequestMapping(value = "users/{username}/logs/page/{page}", method = RequestMethod.POST)
	public String viewLogsFiltered(
			@PathVariable String username,
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

		return viewLogs(username, page, model, request);
	}

	/* Users profile */

	@RequestMapping(value = "users/{username}/profile", method = RequestMethod.GET)
	@Transactional(readOnly = true)
	public String viewProfile(@PathVariable String username, ModelMap model)
			throws ProfileProviderException, Exception {
		Integer userId = userService.getUserByUsername(username).getId();
		UserProfile profile = null;
		try {
			profile = profileProvider.getProfile(userId);
		} catch (ProfileProviderException e) {
			LOG.error(e);
		}

		if (profile == null) {
			throw new Exception("No profile available.");
		}

		model.put("userId", userId);
		model.put("profile", profile);
		return "users/profile";
	}

	@RequestMapping(value = "users/{username}/profile", method = RequestMethod.POST)
	@Transactional(readOnly = true)
	public String updateProfile(@ModelAttribute("profile") UserProfile profile,
			@PathVariable String username) throws ProfileProviderException {
		Integer userId = userService.getUserByUsername(username).getId();
		profileProvider.updateProfile(userId, profile);
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
		UserForm userForm = new UserForm();
		User user = userService.getUser(id);
		List<Role> allRoles = roleService.getRoleList();
		List<Role> selectedRoles = roleService.getRoleList(user);
		userForm.setUser(user);
		userForm.setAllRoles(allRoles);
		userForm.setSelectedRoles(selectedRoles);

		model.put("userform", userForm);
		return "users/form.update";
	}

	@RequestMapping(value = "users/{id}/edit", method = RequestMethod.POST)
	@Transactional
	public String updateUser(@PathVariable int id,
			@Valid @ModelAttribute("userform") UserForm userForm,
			BindingResult result, ModelMap model) {
		User user = userForm.getUser();
		user.setId(id);
		userForm.setUser(user);

		if (result.hasErrors()) {
			List<Role> allRoles = roleService.getRoleList();
			userForm.setAllRoles(allRoles);
			return "users/form.update";
		}

		userService.updateData(userForm.getUser());
		roleService
				.setRoleList(userForm.getUser(), userForm.getSelectedRoles());
		return "redirect:/apps/panel";
	}

	@RequestMapping(value = "users/{id}/delete", method = RequestMethod.GET)
	@Transactional
	public String deleteUser(@PathVariable int id) {
		userService.deleteData(id);
		return "redirect:/apps/panel";
	}

	@RequestMapping(value = "users/new", method = RequestMethod.GET)
	@Transactional(readOnly = true)
	public String viewUserInsertForm(@ModelAttribute("user") User user) {
		return "users/form.insert";
	}

	@RequestMapping(value = "users/new", method = RequestMethod.POST)
	@Transactional
	public String insertUser(@Valid @ModelAttribute("user") User user,
			BindingResult result, ModelMap model) throws ProfileProviderException {

		if (userService.getUserByUsername(user.getUsername()) != null)
			result.rejectValue("username", "username.exists");
		if (result.hasErrors())
			return "users/form.insert";
		int userId = userService.insertData(user);
		profileProvider.createProfile(userId, LanguageCode.fromString(user.getLanguage()));

		return "redirect:/apps/panel";
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
		RoleForm roleForm = new RoleForm();
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
			@Valid @ModelAttribute("roleform") RoleForm roleForm,
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
		return "redirect:/apps/panel";
	}

	@RequestMapping(value = "roles/{id}/delete", method = RequestMethod.GET)
	@Transactional
	public String deleteRole(@PathVariable int id) {
		roleService.deleteData(id);
		return "redirect:/apps/panel";
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
		return "redirect:/apps/panel";
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
		return "redirect:/apps/panel";
	}

	@RequestMapping(value = "permissions/{id}/delete", method = RequestMethod.GET)
	@Transactional
	public String deletePermission(@PathVariable int id) {
		permissionService.deleteData(id);
		return "redirect:/apps/panel";
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
		return "redirect:/apps/panel";
	}

	/* Teachers */

	@RequestMapping(value = "teachers/{id}/assign", method = RequestMethod.GET)
	@Transactional(readOnly = true)
	public String viewTeachersAssignForm(@PathVariable int id, ModelMap model) {
		User teacher = userService.getUser(id);
		TeacherStudentForm teacherStudentForm = new TeacherStudentForm();
		teacherStudentForm.setTeacher(teacher);
		teacherStudentForm.setAllStudents(teacherStudentService
				.getStudentList());
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
			BindingResult result, ModelMap model) {
		User teacher = userService.getUser(id);
		teacherStudentService.setStudentList(teacher,
				teacherStudentForm.getSelectedStudents());
		return "redirect:/apps/panel";
	}

}
