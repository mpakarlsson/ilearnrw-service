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
	width: 25%;
	min-height: 400px;
}

#roles {
	background-color: #dfd;
	float: left;
	width: 25%;
	min-height: 400px;
}

#permissions {
	background-color: #fdd;
	float: left;
	width: 25%;
	min-height: 400px;
}

#teachers {
	background-color: #fdf;
	float: right;
	width: 25%;
	min-height: 400px;
}
</style>
</head>
<body>
	<h1>Welcome page</h1>
	<p>Info: ${info}</p>
	<div id="wrapper">
		<div id="users">
			<c:if test="${not empty users}">
				<table border="1">
					<tr>
						<td>Username</td>
						<td>Password</td>
						<td>Enabled</td>
						<td></td>
						<td></td>
						<td></td>
						<td></td>
					</tr>
					<c:forEach items="${users}" var="o">
						<tr>
							<td>${o.username}</td>
							<td>${o.password}</td>
							<td>${o.enabled}</td>
							<td><a href='<c:url value='users/${o.id}/edit'/>'>Edit</a></td>
							<td><a href='<c:url value='users/${o.id}/delete'/>'>Delete</a></td>
							<td><a href='<c:url value='users/${o.id}/profile'/>'>View Profile</a></td>
							<td><a href='<c:url value='users/${o.id}/logs/page/1'/>'>View Logs</a></td>
						</tr>
					</c:forEach>
				</table>
			</c:if>

			Add a new user: <a href='<c:url value='users/new'/>'>Add</a>
		</div>

		<div id="roles">
			<c:if test="${not empty roles}">
				<table border="1">
					<tr>
						<td>Role name</td>
						<td></td>
						<td></td>
					</tr>
					<c:forEach items="${roles}" var="o">
						<tr>
							<td>${o.name}</td>
							<td><a href='<c:url value='roles/${o.id}/edit'/>'>Edit</a></td>
							<td><a href='<c:url value='roles/${o.id}/delete'/>'>Delete</a></td>
						</tr>
					</c:forEach>
				</table>
			</c:if>

			Add a new role: <a href='<c:url value='roles/new'/>'>Add</a>
		</div>

		<div id="permissions">
			<c:if test="${not empty permissions}">
				<table border="1">
					<tr>
						<td>Permission name</td>
						<td></td>
						<td></td>
					</tr>
					<c:forEach items="${permissions}" var="o">
						<tr>
							<td>${o.name}</td>
							<td><a href='<c:url value='permissions/${o.id}/edit'/>'>Edit</a></td>
							<td><a href='<c:url value='permissions/${o.id}/delete'/>'>Delete</a></td>
						</tr>
					</c:forEach>
				</table>
			</c:if>

			Add a new permission: <a href='<c:url value='permissions/new'/>'>Add</a>
		</div>
		
		<div id="teachers">
			<c:if test="${not empty teachers}">
				<table border="1">
					<tr>
						<td>Username</td>
						<td>Assign students</td>
					</tr>
					<c:forEach items="${teachers}" var="o">
						<tr>
							<td>${o.username}</td>
							<td><a href='<c:url value='teachers/${o.id}/assign'/>'>Assign students</a></td>
						</tr>
					</c:forEach>
				</table>
			</c:if>
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
