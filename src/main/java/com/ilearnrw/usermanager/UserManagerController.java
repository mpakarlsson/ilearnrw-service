package com.ilearnrw.usermanager;

import ilearnrw.user.profile.UserProfile;
import ilearnrw.utils.LanguageCode;

import java.security.Principal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.sql.DataSource;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementSetter;
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
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.ilearnrw.services.profileAccessUpdater.IProfileProvider;
import com.ilearnrw.services.profileAccessUpdater.IProfileProvider.ProfileProviderException;
import com.ilearnrw.services.security.Tokens;
import com.ilearnrw.usermanager.model.Permission;
import com.ilearnrw.usermanager.model.Role;
import com.ilearnrw.usermanager.model.User;

@Controller
public class UserManagerController {

	private static Logger LOG = Logger.getLogger(UserManagerController.class);

	private List<User> userList;
	private List<Role> roleList;
	private List<Permission> permissionList;

	@Autowired
	@Qualifier("dataSource")
	private DataSource dSource;

	@Autowired
	private UserService userService;

	@Autowired
	IProfileProvider profileProvider;

	@Autowired
	private AuthenticatedRestClient restClient;

	@RequestMapping(value = "/panel", method = RequestMethod.GET)
	public @ResponseBody
	ModelAndView panel() {
		return panelAction(null, null, null, null, null, null, null, null);
	}

	@RequestMapping(value = "/panel", method = RequestMethod.POST)
	public @ResponseBody
	ModelAndView panelAction(
			@RequestParam("action") String action,
			@RequestParam(value = "user_id", required = false) Integer user_id,
			@RequestParam(value = "username", required = false) String username,
			@RequestParam(value = "password", required = false) String password,
			@RequestParam(value = "role_id", required = false) Integer role_id,
			@RequestParam(value = "role_name", required = false) String role_name,
			@RequestParam(value = "permission_id", required = false) Integer permission_id,
			@RequestParam(value = "permission_name", required = false) String permission_name) {

		userList = null;
		roleList = null;
		permissionList = null;

		ModelAndView modelAndView = new ModelAndView("panel");
		if (action == null) {
			// nothing
		} else if (action.equals("commit-user-edit")) {
			updateUser(user_id, username, password, modelAndView);
		} else if (action.equals("add-role")) {
			addRole(role_name, modelAndView);
		} else if (action.equals("edit-role")) {
			editRole(role_id, modelAndView);
		} else if (action.equals("delete-role")) {
			deleteRole(role_id, modelAndView);
		} else if (action.equals("add-permission")) {
			addPermission(permission_name, modelAndView);
		} else if (action.equals("edit-permission")) {
			editPermission(permission_id, modelAndView);
		} else if (action.equals("delete-permission")) {
			deletePermission(permission_id, modelAndView);
		} else {
			throw new RuntimeException("Incorrect action: " + action);
		}

		if (userList == null)
			userList = userService.getUserList();
		if (roleList == null)
			roleList = getRoleList();
		if (permissionList == null)
			permissionList = getPermissionList();
		modelAndView.addObject("users", userList);
		modelAndView.addObject("roles", roleList);
		modelAndView.addObject("permissions", permissionList);
		return modelAndView;
	}

	private List<Role> getRoleList() {
		JdbcTemplate template = new JdbcTemplate(dSource);
		return template.query("select id, name from roles",
				new BeanPropertyRowMapper<Role>(Role.class));
	}

	private List<Permission> getPermissionList() {
		JdbcTemplate template = new JdbcTemplate(dSource);
		return template.query("select id, name from permissions",
				new BeanPropertyRowMapper<Permission>(Permission.class));
	}

	private void deletePermission(Integer permission_id,
			ModelAndView modelAndView) {
		String sql = "DELETE FROM permissions WHERE id=?;";
		Connection conn = null;
		try {
			conn = dSource.getConnection();
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setInt(1, permission_id);
			int n = ps.executeUpdate();
			ps.close();
			modelAndView.addObject("info", n + " rows deleted.");
		} catch (SQLException e) {
			throw new RuntimeException(e);

		} finally {
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
				}
			}
		}
	}

	private void editPermission(Integer permission_id, ModelAndView modelAndView) {
		// TODO Auto-generated method stub

	}

	private void addPermission(String permission_name, ModelAndView modelAndView) {
		String sql = "INSERT INTO permissions (name) VALUES (?)";
		Connection conn = null;
		try {
			conn = dSource.getConnection();
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setString(1, permission_name);
			int n = ps.executeUpdate();
			ps.close();
			modelAndView.addObject("info", n + " rows added.");
		} catch (SQLException e) {
			throw new RuntimeException(e);

		} finally {
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
				}
			}
		}
	}

	private void deleteRole(Integer role_id, ModelAndView modelAndView) {
		String sql = "DELETE FROM roles WHERE id=?;";
		Connection conn = null;
		try {
			conn = dSource.getConnection();
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setInt(1, role_id);
			int n = ps.executeUpdate();
			ps.close();
			modelAndView.addObject("info", n + " rows deleted.");
		} catch (SQLException e) {
			throw new RuntimeException(e);

		} finally {
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
				}
			}
		}
	}

	private void editRole(Integer role_id, ModelAndView modelAndView) {
		// TODO Auto-generated method stub

	}

	private void addRole(String role_name, ModelAndView modelAndView) {
		String sql = "INSERT INTO roles (name) VALUES (?)";
		Connection conn = null;
		try {
			conn = dSource.getConnection();
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setString(1, role_name);
			int n = ps.executeUpdate();
			ps.close();
			modelAndView.addObject("info", n + " rows added.");
		} catch (SQLException e) {
			throw new RuntimeException(e);

		} finally {
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
				}
			}
		}
	}

	private List<Role> getRoleListByUser(final Integer user_id) {
		JdbcTemplate template = new JdbcTemplate(dSource);
		String sql = "select (rm.members_id=?) as hasRole, id, name from roles, role_members rm where rm.roles_id = roles.id";
		return template.query(sql, new PreparedStatementSetter() {
			public void setValues(PreparedStatement preparedStatement)
					throws SQLException {
				preparedStatement.setInt(1, user_id);
			}
		}, new BeanPropertyRowMapper<Role>(Role.class));
	}

	private void updateUser(Integer user_id, String username, String password,
			ModelAndView modelAndView) {
		String sql = "UPDATE users SET username=?, password=? WHERE id=?;";
		Connection conn = null;
		try {
			conn = dSource.getConnection();
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setString(1, username);
			ps.setString(2, password);
			ps.setInt(3, user_id);
			int n = ps.executeUpdate();
			ps.close();
			modelAndView.addObject("info", n + " rows edited.");
		} catch (SQLException e) {
			throw new RuntimeException(e);

		} finally {
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
				}
			}
		}
	}

	private void deleteUser(Integer user_id, ModelAndView modelAndView) {
		String sql = "DELETE FROM users WHERE id=?;";
		Connection conn = null;
		try {
			conn = dSource.getConnection();
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setInt(1, user_id);
			int n = ps.executeUpdate();
			ps.close();
			modelAndView.addObject("info", n + " rows deleted.");
		} catch (SQLException e) {
			throw new RuntimeException(e);

		} finally {
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
				}
			}
		}
	}

	@RequestMapping(value = "/home")
	public ModelAndView home(Principal principal, HttpServletRequest request) {

		LOG.info("Returning home view");
		String serverTime = (new Date()).toString();
		ModelAndView modelView = new ModelAndView("home");
		modelView.addObject("serverTime", serverTime);
		modelView.addObject("username",
				request.getSession().getAttribute("user"));
		modelView.addObject("principal", principal.getName());

		return modelView;
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
			request.getSession().setAttribute("tokens", tokens);

		} catch (Exception e) {
			model.addAttribute("error", e.getMessage());
			return "login";
		}
		return "redirect:/apps/home";
	}

	@RequestMapping(value = "/users/{id}/form", method = RequestMethod.GET)
	@Transactional(readOnly = true)
	public String viewUpdateForm(@PathVariable int id, ModelMap model) {
		User user = userService.getUser(id);

		model.put("user", user);
		return "users/form.update";
	}

	// @Autowired
	// private Validator validator;

	@RequestMapping(value = "users/{id}", method = RequestMethod.POST)
	@Transactional
	public String updateUser(@PathVariable int id,
			@ModelAttribute("user") User user, BindingResult result) {
		user.setId(id);

		// validator.validate(article, result);
		if (result.hasErrors())
			return "users/form.update";

		userService.updateData(user);
		return "redirect:/apps/panel";
	}

	@RequestMapping(value = "users/{id}/delete", method = RequestMethod.GET)
	@Transactional
	public String deleteUser(@PathVariable int id,
			@ModelAttribute("user") User user, BindingResult result) {

		userService.deleteData(id);
		return "redirect:/apps/panel";
	}

	@RequestMapping(value = "users/form", method = RequestMethod.GET)
	@Transactional(readOnly = true)
	public String viewInsertForm(@ModelAttribute("user") User user) {
		return "users/form.insert";
	}

	@RequestMapping(value = "users/{userId}/profile", method = RequestMethod.GET)
	@Transactional(readOnly = true)
	public String viewProfile(@PathVariable String userId, ModelMap model)
			throws ProfileProviderException {
		UserProfile profile = null;
		try {
			profile = profileProvider.getProfile(userId);
		} catch (ProfileProviderException e) {
			LOG.error(e);
		}

		if (profile == null) {
			profileProvider.createProfile(userId, LanguageCode.EN);
			profile = profileProvider.getProfile(userId);
		}

		model.put("userId", userId);
		model.put("profile", profile);
		model.put("problems", profile.getUserSeveritiesToProblems().getUserSeverities().getIndices());
		
		return "users/profile";
	}

	@RequestMapping(value = "users/{userId}/profile", method = RequestMethod.POST)
	@Transactional(readOnly = true)
	public String updateProfile(@ModelAttribute("profile") UserProfile profile, @PathVariable String userId) throws ProfileProviderException {
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
}