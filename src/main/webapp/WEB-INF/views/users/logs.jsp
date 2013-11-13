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
Page <c:out value="${logEntryResult.page}"/> out of <c:out value="${logEntryResult.totalPages}"/>
<table border="1">
	<tr>
		<td>User ID</td>
		<td>Tag</td>
		<td>Value</td>
		<td>Application ID</td>
		<td>Time</td>
		<td>Session ID</td>
	</tr>
	<c:forEach items="${logEntryResult.results}" var="log">
		<tr>
			<td>${log.userId}</td>
			<td>${log.tag}</td>
			<td>${log.value}</td>
			<td>${log.applicationId}</td>
			<td>${log.timestamp}</td>
			<td>${log.sessionId}</td>
		</tr>
	</c:forEach>
</table>
</body>
</html>