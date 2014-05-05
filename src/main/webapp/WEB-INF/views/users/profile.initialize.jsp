<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>User profile</title>
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/apps/resources/css/style.css"></link>
</head>
<body>
	<div class="form-container">
	<form:form
		action="${pageContext.servletContext.contextPath}/apps/users/${userId}/profile"
		method="POST" modelAttribute="profile">
		
		<form:hidden path="userProblems.userSeverities.length" />
		<fieldset>
		<legend>Profile Initialization for ${username}</legend>
		<label>${profile.getUserProblems().getNumerOfRows()} Categories</label>
		<form:input path="preferences.fontSize" />
		<form:hidden path="language" />
		<input type="submit" value="Submit" />
		</fieldset>
		${pageContext.request.contextPath}

	</form:form>
	</div>
</body>
</html>
