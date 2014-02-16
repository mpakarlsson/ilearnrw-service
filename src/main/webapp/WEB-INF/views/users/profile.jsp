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
		<form:hidden path="language" />
		</fieldset>
		<fieldset>
		<legend>Severities</legend>
		<!-- form:hidden path="userProblems.problems" /-->
		<form:hidden path="userProblems.userSeverities.systemIndices" />
		<form:hidden path="userProblems.userSeverities.teacherIndices" />
		<table border="1">
		<tr>
			<th>Row</th>
			<th>System Index</th>
			<th>Teacher Index</th>
			<th colspan="1000">Severities</th>
		</tr>
		<c:forEach var="row"
			items="${profile.userProblems.userSeverities.severities}"
			varStatus="statusRow">
			<tr>
			<td>
				<c:out value="${statusRow.index}"/>
			</td>
			<td>
			<form:input
					path="userProblems.userSeverities.systemIndices[${statusRow.index}]" size="2"/>
			</td>
			<td>
			<form:input
					path="userProblems.userSeverities.teacherIndices[${statusRow.index}]" size="2"/>
			</td>
			<td>
			Severities:
			</td>
			<form:hidden
				path="userProblems.userSeverities.severities[${statusRow.index}]" />
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
		<fieldset>
		<legend>Tricky Words</legend>
		<label>Words</label>
		<form:input path="userProblems.trickyWords" />
		</fieldset>
		<span class="buttonrow">
		    <input type="submit" value="Submit"></input>
		</span>
	</form:form>
	</div>
</body>
</html>
