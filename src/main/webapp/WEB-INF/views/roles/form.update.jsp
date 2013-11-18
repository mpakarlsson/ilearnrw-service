<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Edit roles</title>
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/apps/resources/css/style.css"></link>
</head>
<body>
<div class="form-container">
	<form:form action="${pageContext.servletContext.contextPath}/apps/roles/${roleform.role.id}/edit" method="POST" modelAttribute="roleform">
		<fieldset>
			<legend>
				Role details
			</legend>
			<span>
				<label >Role name</label>
				<form:input path="role.name" />
				<form:errors path="role.name" class="error"/>
			</span>
		</fieldset>
		<fieldset>
			<legend>
				Role permissions
			</legend>
			<c:if test="${not empty roleform.allPermissions}">
				<form:checkboxes items="${roleform.allPermissions}" path="selectedPermissions" itemLabel="name" itemValue="id"></form:checkboxes>
			</c:if>
		</fieldset>
		<span class="buttonrow">
			<input type="submit" value="Submit"/>
		</span>
	</form:form>
</div>
</body>
</html>