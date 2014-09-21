<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<html>
<head>
<title>Login</title>
<jsp:include page="../includes/includes.jsp"></jsp:include>
</head>
<body>
	<div class="container">
		<c:if test="${not empty error}">
			<div class="alert alert-danger">
				<a class="close" data-dismiss="alert">×</a> <strong>Error!</strong>
				${error}
			</div>
		</c:if>
		<div class="panel panel-default">
			<form method="POST" class="form-signin" action="login">
				<div class="panel-heading">
					<img style="float: right"
						src="${pageContext.request.contextPath}/apps/resources/webapp/images/Logo-crop-border.png" />
					<h1 class="form-signin-heading">Welcome to iLearnRW Online
						Management System</h1>
					<span>Please enter your details below</span>
				</div>
				<div class="panel-body">
					<div class="form-group">
						<input name="username" type="text" class="form-control"
							placeholder="Username">
					</div>
					<div class="form-group">
						<input name="pass" type="password" class="form-control"
							placeholder="Password">
					</div>
					<div class="form-group">
						<button class="btn btn-large btn-primary" type="submit">Sign
							in</button>
					</div>
				</div>
			</form>
		</div>
	</div>
</body>
</html>