<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Edit roles</title>
<jsp:include page="../includes/includes.jsp"></jsp:include>
</head>
<body>
	<div id="wrapper">
		<jsp:include page="../includes/navigation.jsp"></jsp:include>
		<div id="page-wrapper">
			<form:form
				action="${pageContext.servletContext.contextPath}/apps/roles/${roleform.role.id}/edit"
				method="POST" modelAttribute="roleform">
				<div class="form-group">
					<span> <label class="control-label">Role name</label> <form:input
							class="form-control" path="role.name" /> <form:errors
							path="role.name" class="error" />
					</span>
				</div>
				<div class="form-group">
					<c:if test="${not empty roleform.allPermissions}">
						<form:checkboxes items="${roleform.allPermissions}"
							path="selectedPermissions" itemLabel="name" itemValue="id"></form:checkboxes>

					</c:if>
				</div>
				<button type="submit" class="btn btn-primary">Submit</button>
			</form:form>
		</div>
	</div>
</body>
</html>