<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Assign students to teacher</title>
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/apps/resources/css/style.css"></link>
</head>
<body>
	<div class="form-container">
		<form:form action="${pageContext.servletContext.contextPath}/apps/teachers/${teacherStudentForm.teacher.id}/assign" method="POST" modelAttribute="teacherStudentForm">
		<fieldset>
			<legend>
				Assigned students for <c:out value="${teacherStudentForm.teacher.username}"></c:out>
			</legend>
			<c:if test="${not empty teacherStudentForm.allStudents}">
				<form:checkboxes items="${teacherStudentForm.allStudents}" path="selectedStudents" itemLabel="username" itemValue="id"></form:checkboxes>
			</c:if>
		</fieldset>
		<span class="buttonrow">
			<input type="submit" value="Submit"/>
		</span>
		</form:form>
	</div>
</body>
</html>
