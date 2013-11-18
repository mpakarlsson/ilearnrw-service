<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Create roles</title>
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/apps/resources/css/style.css"></link>
</head>
<body>
<div class="form-container">
	<form:form action="${pageContext.servletContext.contextPath}/apps/roles/new" method="POST" modelAttribute="role">
		<fieldset>
			<legend>
				Role details
			</legend>
			<span>
				<label >Role name</label>
				<form:input path="name" />
				<form:errors path="name" class="error"/>
			</span>
		</fieldset>
		<span class="buttonrow">
			<input type="submit" value="Submit"/>
		</span>
	</form:form>
</div>
</body>
</html>