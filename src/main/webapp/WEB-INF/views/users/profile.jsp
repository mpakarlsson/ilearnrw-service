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
		<legend>Preferences</legend>
		<label>Font size</label>
		<form:input path="preferences.fontSize" />
		<form:hidden path="userProblems.problems" />
		<form:hidden path="userProblems.userSeverities.indices" />
		<input type="submit" value="Submit" />
		</fieldset>
		<fieldset>
		<legend>Severities</legend>
		<table border="1">
		<c:forEach var="row"
			items="${profile.userProblems.userSeverities.severities}"
			varStatus="statusRow">
			<tr>
			<td>
			<label>Index <c:out value="${statusRow.index}"/> = </label>
			</td>
			<td>
			<form:input
					path="userProblems.userSeverities.indices[${statusRow.index}]" size="2"/>
			</td>
			<td>
			<label>Severities:</label>
			<form:hidden
				path="userProblems.userSeverities.severities[${statusRow.index}]" />
			</td>
			<c:forEach var="col" items="${row}" varStatus="statusCol">
				<td>
				<form:input
					path="userProblems.userSeverities.severities[${statusRow.index}][${statusCol.index}]" size="2" style="width: 20px"/>
				</td>
			</c:forEach>
			</tr>
		</c:forEach>
		</table>
		</fieldset>

	</form:form>
	</div>
</body>
</html>
