<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
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
				<label >Birth date</label>
				<fmt:formatDate value="${user.birthdate}" var="dateString" pattern="yyyy/MM/dd" />
				<form:input path="birthdate" value="${dateString}" />
				<form:errors path="birthdate" class="error"/>
			</span>
			<span>
				<label >Enabled</label>
				<form:checkbox path="enabled"/>
			</span>
		</fieldset>
		<fieldset>
			<legend>
				Gender
			</legend>
			<span>
				<label >Male</label>
				<form:radiobutton path="gender" value="M" />
				<form:errors path="gender" class="error"/>
			</span>
			<span>
				<label >Female</label>
				<form:radiobutton path="gender" value="F" />
				<form:errors path="gender" class="error"/>
			</span>
		</fieldset>
		<fieldset>
			<legend>
				Language
			</legend>
			<span>
				<label >English</label>
				<form:radiobutton path="language" value="EN" />
				<form:errors path="language" class="error"/>
			</span>
			<span>
				<label >Greek</label>
				<form:radiobutton path="language" value="GR" />
				<form:errors path="language" class="error"/>
			</span>
		</fieldset>
		<span class="buttonrow">
			<input type="submit" value="Submit"/>
		</span>
		</form:form>
	</div>
</body>
</html>
