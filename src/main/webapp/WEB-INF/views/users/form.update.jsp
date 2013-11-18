<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Edit user</title>
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/apps/resources/css/style.css"></link>
</head>
<body>
	<div class="form-container">
		<form:form action="${pageContext.servletContext.contextPath}/apps/users/${userform.user.id}/edit" method="POST" modelAttribute="userform">
		<fieldset>
			<legend>
				User details
			</legend>
			<span>
				<label >Username</label>
				<form:input path="user.username" />
				<form:errors path="user.username" class="error"/>
			</span>
			<span>
				<label >Password</label>
				<form:input path="user.password" />
				<form:errors path="user.password" class="error"/>
			</span>
			<span>
				<label >Enabled</label>
				<form:checkbox path="user.enabled"/>
			</span>
		</fieldset>
		<fieldset>
			<legend>
				User roles
			</legend>
			<c:if test="${not empty userform.allRoles}">
				<form:checkboxes items="${userform.allRoles}" path="selectedRoles" itemLabel="name" itemValue="id"></form:checkboxes>
			</c:if>
		</fieldset>
		<span class="buttonrow">
			<input type="submit" value="Submit"/>
		</span>
		</form:form>
	</div>
</body>
</html>
