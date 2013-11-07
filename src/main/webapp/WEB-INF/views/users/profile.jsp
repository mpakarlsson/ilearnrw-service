<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title></title>
</head>
<body>
	<form:form
		action="${pageContext.servletContext.contextPath}/apps/users/${userId}/profile"
		method="POST" modelAttribute="profile">
		
		<form:hidden path="problemsMatrix.userSeverities.length" />
		
		<label>Language</label>
		<form:input path="language" />
		<label>Font size</label>
		<form:input path="preferences.fontSize" />
		<form:hidden path="problemsMatrix.problems" />
		<form:hidden path="problemsMatrix.userSeverities.indices" />
		
		<input type="submit" value="Submit" />
		<br/>
		<c:forEach var="row"
			items="${profile.problemsMatrix.userSeverities.severities}"
			varStatus="statusRow">
			<label>Index <c:out value="${statusRow.index}"/> = </label>
			<form:input
					path="problemsMatrix.userSeverities.indices[${statusRow.index}]" size="2"/>
			<label>Severities:</label>
			<form:hidden
				path="problemsMatrix.userSeverities.severities[${statusRow.index}]" />
			<c:forEach var="col" items="${row}" varStatus="statusCol">
				<form:input
					path="problemsMatrix.userSeverities.severities[${statusRow.index}][${statusCol.index}]" size="2" style="width: 20px"/>
			</c:forEach>
			<br/>
		</c:forEach>

	</form:form>
</body>
</html>