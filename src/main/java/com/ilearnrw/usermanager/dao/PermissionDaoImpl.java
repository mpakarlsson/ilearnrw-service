package com.ilearnrw.usermanager.dao;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import com.ilearnrw.usermanager.model.Permission;
import com.ilearnrw.usermanager.model.Role;

public class PermissionDaoImpl implements PermissionDao {

	@Autowired
	private DataSource dataSource;

	@Override
	public List<Permission> getPermissionList() {
		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
		return jdbcTemplate.query("select * from permissions", new BeanPropertyRowMapper<Permission>(Permission.class));
	}

	@Override
	public Permission getPermission(int id) {
		JdbcTemplate template = new JdbcTemplate(dataSource);
		Permission permission = template.queryForObject("select * from permissions where id=?",
				new Object[] { id },
				new BeanPropertyRowMapper<Permission>(Permission.class));
		return permission;
	}

	@Override
	public int insertData(Permission permission) {
		JdbcTemplate template = new JdbcTemplate(dataSource);
		return template.update("insert into permissions (name) values (?)", permission.getName());
	}

	@Override
	public void updateData(Permission permission) {
		JdbcTemplate template = new JdbcTemplate(dataSource);
		template.update("update permissions set name=? where id=?", permission.getName(), permission.getId());
	}

	@Override
	public void deleteData(int id) {
		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
		jdbcTemplate.update("delete from permissions where id=?", id );
	}

	@Override
	public List<Permission> getPermissionList(Role role) {
		List<Permission> permissionList = new ArrayList<Permission>();
		String sql = "select p.id, p.name from permissions p, role_permissions rp "
				+ "where p.id = rp.permissions_id and rp.roles_id = ?";
		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
		permissionList = jdbcTemplate.query(sql, new Object[] { role.getId() },
				new BeanPropertyRowMapper<Permission>(Permission.class));
		return permissionList;
	}

	@Override
	public void setPermissionList(final Role role, final List<Permission> permissions) {
		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
		jdbcTemplate.update("delete from role_permissions where roles_id=?", new Object[] { role.getId() });
		
		String sql = "insert into role_permissions (roles_id, permissions_id) values (?,?);";
		jdbcTemplate.batchUpdate(sql , new BatchPreparedStatementSetter() {
			
			@Override
			public void setValues(PreparedStatement pStatement, int index) throws SQLException {
				Permission permission = permissions.get(index);
				pStatement.setInt(1, role.getId());
				pStatement.setInt(2, permission.getId());
			}
			
			@Override
			public int getBatchSize() {
				return permissions.size();
			}
		});
	}

}
