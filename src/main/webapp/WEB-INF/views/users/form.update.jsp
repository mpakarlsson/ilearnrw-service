<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
</head>
<body>
	<form:form action="${pageContext.servletContext.contextPath}/apps/users/${user.id}" method="POST" modelAttribute="user">
		<label >Username</label>
		<form:input path="username" />
		<label >Password</label>
		<form:input path="password" />
		<label >Enabled</label>
		<form:checkbox path="enabled"/>
	<input type="submit" value="Submit"/>
	</form:form>
</body>
</html>