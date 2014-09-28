<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<!DOCTYPE html>
<html>

<head>

<meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">

<title>Manage roles</title>

<jsp:include page="../includes/includes.jsp"></jsp:include>

</head>

<body>
	<div id="wrapper">
		<jsp:include page="../includes/navigation.jsp"></jsp:include>
		<div id="page-wrapper">
			<form:form action="new" method="POST" modelAttribute="role">
			    <spring:bind path="name">
				<div class="form-group ${status.error ? 'has-error' : ''}">
						<label class="control-label" for="inputName">Role
							name</label>
						<div class="controls">
							<form:input type="text" id="inputName" class="form-control"
								placeholder="Role name" path="name" required="true"/>
								<form:errors path="name" class="help-block" for="inputName" />
						</div>
				</div>
				</spring:bind>
				<button type="submit" class="btn btn-primary">Submit</button>
			</form:form>
		</div>
	</div>
</body>