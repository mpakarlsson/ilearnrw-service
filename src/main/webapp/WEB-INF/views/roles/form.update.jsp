<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
<style>
  .error {
      color: #EF1313;
      font-style: italic;
  }
  form > span {
    float: left;
    clear: left;
    display:inline-block;
  }
</style>
</head>
<body>
	<form:form action="${pageContext.servletContext.contextPath}/apps/roles/${roleform.role.id}/edit" method="POST" modelAttribute="roleform">
	<span>
		<label >Role name</label>
		<form:input path="role.name" />
		<form:errors path="role.name" cssclass="error"/>
	</span>
	<c:if test="${not empty roleform.allPermissions}">
		<form:checkboxes items="${roleform.allPermissions}" path="selectedPermissions" itemLabel="name" itemValue="id"></form:checkboxes>
	</c:if>
	<span>
		<input type="submit" value="Submit"/>
	</span>
	</form:form>
</body>
</html>