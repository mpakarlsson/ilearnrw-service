package com.ilearnrw.usermanager;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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

import com.ilearnrw.services.security.Tokens;
import com.ilearnrw.usermanager.form.RoleForm;
import com.ilearnrw.usermanager.form.UserForm;
import com.ilearnrw.usermanager.model.Permission;
import com.ilearnrw.usermanager.model.Role;
import com.ilearnrw.usermanager.model.User;
import com.ilearnrw.usermanager.services.PermissionService;
import com.ilearnrw.usermanager.services.RoleService;
import com.ilearnrw.usermanager.services.UserService;

@Controller
public class UserManagerController {

	private static Logger LOG = Logger.getLogger(UserManagerController.class);

	@Autowired
	@Qualifier("dataSource")
	private DataSource dSource;

	@Autowired
	private UserService userService;
	
	@Autowired
	private RoleService roleService;

	@Autowired
	private PermissionService permissionService;

	@RequestMapping(value = "/panel", method = RequestMethod.GET)
	public String panel(ModelMap modelMap) {
		modelMap.addAttribute("users", userService.getUserList());
		modelMap.addAttribute("roles", roleService.getRoleList());
		modelMap.addAttribute("permissions", permissionService.getPermissionList());
		return "panel";
	}

	@RequestMapping(value = "/home")
	public String home(Principal principal, HttpServletRequest request, ModelMap model) {
		LOG.info("Returning home view");
		String serverTime = (new Date()).toString();
		model.addAttribute("serverTime", serverTime);
		model.addAttribute("username", request.getSession().getAttribute("user"));
		model.addAttribute("principal", principal.getName());
		return "home";
	}

	@RequestMapping(value = "/logout")
	public String logout(HttpServletRequest request) {
		SecurityContextHolder.getContext().setAuthentication(null);
		request.getSession().invalidate();
		return "login";
	}

	@RequestMapping(value = "/login")
	public String login() {
		return "login";
	}

	@RequestMapping(value = "/loginfailed", method = RequestMethod.GET)
	public String loginerror(ModelMap model) {
		model.addAttribute("error", "true");
		return "login";
	}

	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public String login(@RequestParam("username") String username,
			@RequestParam("pass") String pass, ModelMap model,
			HttpServletRequest request) {

		AuthenticatedRestClient restClient = new AuthenticatedRestClient();

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
			return "login";
		}
		return "redirect:/apps/home";
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
			@Valid @ModelAttribute("userform") UserForm userForm, BindingResult result, ModelMap model) {
		User user = userForm.getUser();
		user.setId(id);
		userForm.setUser(user);

		if (result.hasErrors())
		{
			List<Role> allRoles = roleService.getRoleList();
			userForm.setAllRoles(allRoles);
		    return "users/form.update";
		}

		userService.updateData(userForm.getUser());
		roleService.setRoleList(userForm.getUser(), userForm.getSelectedRoles());
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
			BindingResult result, ModelMap model) {
		
		if (result.hasErrors())
			return "users/form.insert";
		userService.insertData(user);

		model.put("message", "inserted");
		return "redirect:/apps/panel";
	}
	
	/* Roles */
	
	@RequestMapping(value = "/roles/{id}/edit", method = RequestMethod.GET)
	@Transactional(readOnly = true)
	public String viewRoleUpdateForm(@PathVariable int id, ModelMap model) {
		RoleForm roleForm = new RoleForm();
		Role role = roleService.getRole(id);
		List<Permission> allPermissions = permissionService.getPermissionList();
		List<Permission> selectedPermissions = permissionService.getPermissionList(role);
		roleForm.setRole(role);
		roleForm.setAllPermissions(allPermissions);
		roleForm.setSelectedPermissions(selectedPermissions);
		model.put("roleform", roleForm);
		return "roles/form.update";
	}

	@RequestMapping(value = "roles/{id}/edit", method = RequestMethod.POST)
	@Transactional
	public String updateRole(@PathVariable int id,
			@Valid @ModelAttribute("roleform") RoleForm roleForm, BindingResult result, ModelMap model) {
		Role role = roleForm.getRole();
		role.setId(id);
		roleForm.setRole(role);

		if (result.hasErrors())
		{
			List<Permission> allPermissions = permissionService.getPermissionList();
			roleForm.setAllPermissions(allPermissions);
		    return "roles/form.update";
		}

		roleService.updateData(roleForm.getRole());
		permissionService.setPermissionList(roleForm.getRole(), roleForm.getSelectedPermissions());
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
			@Valid @ModelAttribute("permission") Permission permission, BindingResult result, ModelMap model) {
		
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
	public String viewPermissionInsertForm(@ModelAttribute("permission") Permission permission) {
		return "permissions/form.insert";
	}

	@RequestMapping(value = "permissions/new", method = RequestMethod.POST)
	@Transactional
	public String insertPermission(@Valid @ModelAttribute("permission") Permission permission,
			BindingResult result, ModelMap model) {
		
		if (result.hasErrors())
			return "permissions/form.insert";
		permissionService.insertData(permission);

		model.put("message", "inserted");
		return "redirect:/apps/panel";
	}
}