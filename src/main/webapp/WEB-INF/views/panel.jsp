<?xml version="1.0" encoding="ISO-8859-1" ?>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1" />
<title>Welcome page</title>
<style>
#wrapper {
	width: 100%;
}

#users {
	background-color: #ddf;
	float: left;
	width: 30%;
	min-height: 400px;
}

#roles {
	background-color: #dfd;
	float: left;
	width: 40%;
	min-height: 400px;
}

#permissions {
	background-color: #fdd;
	float: right;
	width: 30%;
	min-height: 400px;
}
</style>
</head>
<body>
	<h1>Welcome page</h1>
	<p>Info: ${info}</p>
	<div id="wrapper">
		<div id="users">
			<c:if test="${edituser}">
	Edit an existing user:
	<form action="panel" method="post">
					<input type="hidden" name="action" value="commit-user-edit" /> <input
						type="hidden" name="user_id" value="${user_id}" /> New user:<input
						type="text" name="username" value="${username}" /><br /> New
					password:<input type="text" name="password" value="${password}" /><br />
					<input type="submit" value="Save changes" /><br />
				</form>
			</c:if>
			<c:if test="${not empty users}">
				<table border="1">
					<tr>
						<td>Username</td>
						<td>Password</td>
						<td>Enabled</td>
						<td></td>
						<td></td>
					</tr>
					<c:forEach items="${users}" var="o">
						<tr>
							<td>${o.username}</td>
							<td>${o.password}</td>
							<td>${o.enabled}</td>
							<td><a href='<c:url value='users/${o.id}/form'/>'>Edit</a></td>
							<td><a href='<c:url value='users/${o.id}/delete'/>'>Delete</a>
							</td>
							<td><a href='<c:url value='users/${o.id}/profile'/>'>View Profile</a>
							</td>
						</tr>
					</c:forEach>
				</table>
			</c:if>

			Add a new user: <a href='<c:url value='users/form'/>'>Add</a>
		</div>

		<div id="roles">
			<c:if test="${editrole}">
	Edit an existing role:
	<form action="panel" method="post">
					<input type="hidden" name="action" value="commit-role-edit" /> <input
						type="hidden" name="role_id" value="${role_id}" /> New name:<input
						type="text" name="role_name" value="${role_name}" /><br /> <input
						type="submit" value="Save changes" /><br />
				</form>
			</c:if>
			<c:if test="${not empty roles}">
				<table border="1">
					<tr>
						<c:if test="${edituser}">
							<td>User has role</td>
						</c:if>
						<td>Role name</td>
					</tr>
					<c:forEach items="${roles}" var="o">
						<tr>
							<c:if test="${edituser}">
								<c:choose>
									<c:when test="${o.hasRole}">
										<td><input type="checkbox" checked="checked" /></td>
									</c:when>
									<c:otherwise>
										<td><input type="checkbox" /></td>
									</c:otherwise>
								</c:choose>
							</c:if>
							<td>${o.name}</td>
							<td>
								<form action="panel" method="post">
									<input type="hidden" name="action" value="edit-role" /> <input
										type="hidden" name="role_id" value="${o.id}" /> <input
										type="submit" value="Edit" />
								</form>
							</td>
							<td>
								<form action="panel" method="post">
									<input type="hidden" name="action" value="delete-role" /> <input
										type="hidden" name="role_id" value="${o.id}" /> <input
										type="submit" value="Delete" />
								</form>
							</td>
						</tr>
					</c:forEach>
				</table>
			</c:if>

			Add a new role:
			<form action="panel" method="post">
				<input type="hidden" name="action" value="add-role" /> Name:<input
					type="text" name="role_name" /><br /> <input type="submit"
					value="Submit" />
			</form>
		</div>

		<div id="permissions">
			<c:if test="${editpermission}">
	Edit an existing permission:
	<form action="panel" method="post">
					<input type="hidden" name="action" value="commit-permission-edit" />
					<input type="hidden" name="permission_id" value="${permission_id}" />
					New name:<input type="text" name="permission_name"
						value="${permission_name}" /><br /> <input type="submit"
						value="Save changes" /><br />
				</form>
			</c:if>
			<c:if test="${not empty permissions}">
				<table border="1">
					<tr>
						<td>Permission name</td>
					</tr>
					<c:forEach items="${permissions}" var="o">
						<tr>
							<td>${o.name}</td>
							<td>
								<form action="panel" method="post">
									<input type="hidden" name="action" value="edit-permission" />
									<input type="hidden" name="permission_id" value="${o.id}" /> <input
										type="submit" value="Edit" />
								</form>
							</td>
							<td>
								<form action="panel" method="post">
									<input type="hidden" name="action" value="delete-permission" />
									<input type="hidden" name="permission_id" value="${o.id}" /> <input
										type="submit" value="Delete" />
								</form>
							</td>
						</tr>
					</c:forEach>
				</table>
			</c:if>

			Add a new permission:
			<form action="panel" method="post">
				<input type="hidden" name="action" value="add-permission" />
				Permission name:<input type="text" name="permission_name" /><br />
				<input type="submit" value="Submit" />
			</form>
		</div>
	</div>
	<a href="home">Home page</a>
	<br />
	<a href='<c:url value='logout'/>'>Logout</a>

	<c:forEach items="${sessionScope}" var="attr">
    ${attr.key}=${attr.value}<br />
	</c:forEach>
</body>
</html>