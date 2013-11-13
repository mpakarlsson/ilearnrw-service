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
	<c:if test="${logEntryResult.page > 1}">
		<a href="<c:out value="${logEntryResult.page - 1}"/>">
			Previous page
		</a>
	</c:if>
	Page <c:out value="${logEntryResult.page}"/> out of <c:out value="${logEntryResult.totalPages}"/>
	<c:if test="${logEntryResult.page < logEntryResult.totalPages}">
		<a href="<c:out value="${logEntryResult.page + 1}"/>">
			Next page
		</a>
	</c:if>
	Current filters are {<br/>
	<c:if test="${not empty sessionScope.tags}">
		Tags: <c:out value="${sessionScope.tags}"/><br/>
	</c:if>
	<c:if test="${not empty sessionScope.applicationId}">
		Application ID: <c:out value="${sessionScope.applicationId}"/><br/>
	</c:if>
	<c:if test="${not empty sessionScope.timestart}">
		Time start: <c:out value="${sessionScope.timestart}"/><br/>
	</c:if>
	<c:if test="${not empty sessionScope.timeend}">
		Time end: <c:out value="${sessionScope.timeend}"/><br/>
	</c:if>
	<c:if test="${not empty sessionScope.sessionId}">
		Session ID: <c:out value="${sessionScope.sessionId}"/><br/>
	</c:if>
	}
<form:form action="" method="POST">
	<label>Tags:</label><input type="text" name="tags" value="<c:out value="${sessionScope.tags}"/>"/><br/>
	<label>Application ID:</label><input type="text" name="applicationId" value="<c:out value="${sessionScope.applicationId}"/>"/><br/>
	<label>Time start:</label><input type="text" name="timestart" value="<c:out value="${sessionScope.timestart}"/>"/><br/>
	<label>Time end:</label><input type="text" name="timeend" value="<c:out value="${sessionScope.timeend}"/>"/><br/>
	<label>Session ID:</label><input type="text" name="sessionId" value="<c:out value="${sessionScope.sessionId}"/>"/><br/>
	<input type="submit" value="Apply filters">
</form:form>
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