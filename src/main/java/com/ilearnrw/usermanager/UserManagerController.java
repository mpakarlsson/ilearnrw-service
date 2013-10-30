package com.ilearnrw.usermanager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.security.core.token.KeyBasedPersistenceTokenService;
import org.springframework.security.core.token.Token;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;

import com.ilearnrw.usermanager.model.Permission;
import com.ilearnrw.usermanager.model.Role;
import com.ilearnrw.usermanager.model.User;

@Controller
@SessionAttributes("token")
public class UserManagerController {

	private static Logger LOG = Logger.getLogger(UserManagerController.class);
	
	private List<User> userList;
	private List<Role> roleList;
	private List<Permission> permissionList;
	
	
	@Autowired
	private KeyBasedPersistenceTokenService tokenService;
	
	@Autowired
	@Qualifier("dataSource")
	private DataSource dSource;
	
	@RequestMapping(value = "/home")
	public ModelAndView home() {

		LOG.info("Returning home view");
		String serverTime = (new Date()).toString();
		ModelAndView modelView = new ModelAndView("home");
		modelView.addObject("serverTime", serverTime);
		return modelView;
	}
	
	@RequestMapping(value = "/login", method = RequestMethod.GET)
	public @ResponseBody
	ModelAndView login(
			@RequestParam(value = "username", required=false) String username,
			@RequestParam(value = "pass", required=false) String pass, 
			HttpServletResponse httpResponse) {

			ModelAndView modelAndView = new ModelAndView("login");
			return modelAndView;
	}
	
	@RequestMapping(value = "/check", method = RequestMethod.POST)
	public @ResponseBody
	ModelAndView check(
			@RequestParam(value = "username", required=true) String username,
			@RequestParam(value = "pass", required=true) String pass, 
			HttpServletResponse httpResponse) {

			ModelAndView modelAndView = new ModelAndView("redirect:panel");
			if (username != null)
			{
				Token authToken = tokenService.allocateToken(username);
				LOG.debug(authToken.getKey().toString());
				modelAndView.addObject("token", authToken.getKey().toString());
			}
			return modelAndView;
	}
	
	@RequestMapping(value = "/panel", method = RequestMethod.GET)
	public @ResponseBody
	ModelAndView panel() {		    
			return panelAction(null, null, null, null, null, null, null, null);
	}
	
	@RequestMapping(value = "/panel", method = RequestMethod.POST)
	public @ResponseBody
	ModelAndView panelAction(
			@RequestParam("action") String action,
			@RequestParam(value = "user_id", required=false) Integer user_id,
			@RequestParam(value = "username", required=false) String username,
			@RequestParam(value = "password", required=false) String password,
			@RequestParam(value = "role_id", required=false) Integer role_id,
			@RequestParam(value = "role_name", required=false) String role_name,
			@RequestParam(value = "permission_id", required=false) Integer permission_id,
			@RequestParam(value = "permission_name", required=false) String permission_name
			) {

			userList = null;
			roleList = null;
			permissionList = null;
			
			ModelAndView modelAndView = new ModelAndView("panel");
			if (action == null) {
				//nothing
			} else if (action.equals("add-user")) {
				addUser(username, password, modelAndView);
			} else if (action.equals("edit-user")) {
				editUser(user_id, modelAndView);
			} else if (action.equals("delete-user")) {
				deleteUser(user_id, modelAndView);
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
				userList = getUserList();
			if (roleList == null)
				roleList = getRoleList();
			if (permissionList == null)
				permissionList = getPermissionList();
			modelAndView.addObject("users", userList);
			modelAndView.addObject("roles", roleList);
			modelAndView.addObject("permissions", permissionList);
			return modelAndView;
	}
	
	List<User> getUserList()
	{
		JdbcTemplate template = new JdbcTemplate(dSource);
		return template.query("select id, username, password from users", new BeanPropertyRowMapper<User>(User.class));
	}

	private List<Role> getRoleList() {
		JdbcTemplate template = new JdbcTemplate(dSource);
		return template.query("select id, name from roles", new BeanPropertyRowMapper<Role>(Role.class));
	}
	
	private List<Permission> getPermissionList() {
		JdbcTemplate template = new JdbcTemplate(dSource);
		return template.query("select id, name from permissions", new BeanPropertyRowMapper<Permission>(Permission.class));
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
				} catch (SQLException e) {}
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
				} catch (SQLException e) {}
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
				} catch (SQLException e) {}
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
				} catch (SQLException e) {}
			}
		}
	}
	
	
	private void addUser(String username, String password, ModelAndView modelAndView)
	{
		String sql = "INSERT INTO users (username, password, enabled) VALUES (?, ?, 1)";
		Connection conn = null;
		try {
			conn = dSource.getConnection();
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setString(1, username);
			ps.setString(2, password);
			int n = ps.executeUpdate();
			ps.close();
			modelAndView.addObject("info", n + " rows added.");
		} catch (SQLException e) {
			throw new RuntimeException(e);
 
		} finally {
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {}
			}
		}
	}

	private void editUser(Integer user_id, ModelAndView modelAndView) {
		modelAndView.addObject("edituser", true);
		modelAndView.addObject("user_id", user_id);
		
		String sql = "SELECT username, password FROM users WHERE id=?";
		Connection conn = null;
		try {
			conn = dSource.getConnection();
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setInt(1, user_id);
			ResultSet results = ps.executeQuery();
			results.next();
			String username = results.getString("username");
			String password = results.getString("password");
			modelAndView.addObject("username", username);
			modelAndView.addObject("password", password);
			roleList = getRoleListByUser(user_id);
			ps.close();
		} catch (SQLException e) {
			throw new RuntimeException(e);
 
		} finally {
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {}
			}
		}
	}
	
	private List<Role> getRoleListByUser(final Integer user_id) {
		JdbcTemplate template = new JdbcTemplate(dSource);
		String sql = "select (rm.members_id=?) as hasRole, id, name from roles, role_members rm where rm.roles_id = roles.id";
		return template.query(sql,
				new PreparedStatementSetter() {
			public void setValues(PreparedStatement preparedStatement) throws
			SQLException {
				preparedStatement.setInt(1, user_id);
			}
		},
		new BeanPropertyRowMapper<Role>(Role.class));
	}

	private void updateUser(Integer user_id, String username, String password, ModelAndView modelAndView) {
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
				} catch (SQLException e) {}
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
				} catch (SQLException e) {}
			}
		}
	}
	
}