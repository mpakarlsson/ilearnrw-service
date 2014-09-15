<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Edit permission</title>
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/apps/resources/css/style.css"></link>
</head>
<body>
<div class="form-container">
	<form:form action="${pageContext.servletContext.contextPath}/apps/permissions/${permission.id}/edit" method="POST" modelAttribute="permission">
		<fieldset>
			<legend>
				Permission details
			</legend>
			<span>
				<label >Permission name</label>
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