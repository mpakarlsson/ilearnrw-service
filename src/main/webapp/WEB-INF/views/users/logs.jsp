<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>View user logs</title>
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/apps/resources/css/style.css"></link>
</head>
<body>
<div class="form-container">
	<fieldset>
		<legend>
			Page
		</legend>
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
	</fieldset>
	<fieldset>
		<legend>
			Current filters
		</legend>
		<c:if test="${ empty sessionScope.tags and empty sessionScope.applicationId and empty sessionScope.timestart and empty sessionScope.timeend}">
			There are no current filters applied.
		</c:if>
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
	</fieldset>
	<fieldset>
		<legend>
			New filters
		</legend>
		<form:form action="" method="POST">
			<label>Tags:</label><input type="text" name="tags" value="<c:out value="${sessionScope.tags}"/>"/><br/>
			<label>Application ID:</label><input type="text" name="applicationId" value="<c:out value="${sessionScope.applicationId}"/>"/><br/>
			<label>Time start:</label><input type="text" name="timestart" value="<c:out value="${sessionScope.timestart}"/>"/><br/>
			<label>Time end:</label><input type="text" name="timeend" value="<c:out value="${sessionScope.timeend}"/>"/><br/>
			<input type="submit" value="Apply filters">
		</form:form>
	</fieldset>
	<fieldset>
		<legend>
			Logs
		</legend>
		<c:choose>
		    <c:when test="${ not empty logEntryResult.results}">
				<table border="1">
					<tr>
						<th>Username</th>
						<th>Tag</th>
						<th>Value</th>
						<th>Application ID</th>
						<th>Time</th>
					</tr>
					<c:forEach items="${logEntryResult.results}" var="log">
						<tr>
							<td>${log.username}</td>
							<td>${log.tag}</td>
							<td>${log.value}</td>
							<td>${log.applicationId}</td>
							<td>${log.timestamp}</td>
						</tr>
					</c:forEach>
				</table>
		    </c:when>
		    <c:otherwise>
		        There are no logs for the current user with the current filters.
		    </c:otherwise>
		</c:choose>
	</fieldset>
</div>
</body>
</html>