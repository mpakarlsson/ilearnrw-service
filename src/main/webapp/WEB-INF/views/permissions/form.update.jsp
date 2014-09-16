<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Edit permission</title>
<jsp:include page="../includes/includes.jsp"></jsp:include>
</head>
<body>
	<div class="wrapper">
		<jsp:include page="../includes/navigation.jsp"></jsp:include>
		<div id="page-wrapper">
			<form:form
				action="${pageContext.servletContext.contextPath}/apps/permissions/${permission.id}/edit"
				method="POST" modelAttribute="permission">
				<div class="form-group">
					<span> <label class="control-label">Permission name</label> <form:input
							path="name" class="form-control"/> 
							<form:errors path="name" class="error" />
					</span>
				</div>
				<button type="submit" class="btn btn-primary">Submit</button>
			</form:form>
		</div>
	</div>
</body>
</html>