<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Create user</title>
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/apps/resources/css/style.css"></link>
</head>
<body>
	<div class="form-container">
		<form:form action="${pageContext.servletContext.contextPath}/apps/users/new" method="POST" modelAttribute="user">
		<fieldset>
			<legend>
				User details
			</legend>
			<span>
				<label >Username</label>
				<form:input path="username" />
				<form:errors path="username" class="error"/>
			</span>
			<span>
				<label >Password</label>
				<form:input path="password" />
				<form:errors path="password" class="error"/>
			</span>
			<span>
				<label >Enabled</label>
				<form:checkbox path="enabled"/>
			</span>
		</fieldset>
		<span class="buttonrow">
			<input type="submit" value="Submit"/>
		</span>
		</form:form>
	</div>
</body>
</html>
