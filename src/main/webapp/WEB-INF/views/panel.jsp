<?xml version="1.0" encoding="ISO-8859-1" ?>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1" />
<title>Admin Panel</title>
<link rel="stylesheet" type="text/css"
	href="${pageContext.request.contextPath}/apps/resources/css/style.css"></link>
</head>
<body>
	<img src="${pageContext.servletContext.contextPath}/apps/resources/images/Logo.gif"/>
	<h1>Administration Panel</h1>
	<div id="wrapper">
		<fieldset id="users">
			<legend> Users </legend>
			<c:if test="${not empty users}">
				<table border="1">
					<tr>
						<th>Username</th>
						<th>Enabled</th>
						<th></th>
						<th></th>
						<th></th>
						<th></th>
					</tr>
					<c:forEach items="${users}" var="o">
						<tr>
							<td>${o.username}</td>
							<td>${o.enabled}</td>
							<td><a href='<c:url value='users/${o.id}/edit'/>'>Edit</a></td>
							<td><a href='<c:url value='users/${o.id}/delete'/>'>Delete</a></td>
							<td><a href='<c:url value='users/${o.id}/profile'/>'>View
									Profile</a></td>
							<td><a
								href='<c:url value='users/${o.username}/logs/page/1'/>'>View
									Logs</a></td>
						</tr>
					</c:forEach>
				</table>
			</c:if>
			<a href='<c:url value='users/new'/>'>Add a new user</a>
		</fieldset>

		<fieldset id="roles">
			<legend> Roles </legend>
			<c:if test="${not empty roles}">
				<table border="1">
					<tr>
						<th>Role name</th>
						<th></th>
						<th></th>
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
			<a href='<c:url value='roles/new'/>'>Add a new role</a>
		</fieldset>

		<fieldset id="permissions">
			<legend> Permissions </legend>
			<c:if test="${not empty permissions}">
				<table border="1">
					<tr>
						<th>Permission name</th>
						<th></th>
						<th></th>
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
			<a href='<c:url value='permissions/new'/>'>Add a new permission</a>
		</fieldset>

		<fieldset id="teachers">
			<legend> Teachers </legend>
			<c:choose>
				<c:when test="${not empty teachers}">
					<table border="1">
						<tr>
							<th>Teacher username</th>
							<th>Assign students</th>
						</tr>
						<c:forEach items="${teachers}" var="o">
							<tr>
								<td>${o.username}</td>
								<td><a href='<c:url value='teachers/${o.id}/assign'/>'>Assign
										students</a></td>
							</tr>
						</c:forEach>
					</table>
				</c:when>
				<c:otherwise>
					There are no users that have role 'ROLE_TEACHER'
				</c:otherwise>
			</c:choose>
		</fieldset>
	</div>
	<div style="clear: both">
		<a href='<c:url value='logout'/>'>Logout</a>
	</div>
	<c:forEach items="${sessionScope}" var="attr">
    ${attr.key}=${attr.value}<br />
	</c:forEach>
</body>
</html>
