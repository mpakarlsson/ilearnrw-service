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
		<div class="panel panel-default col-xs-12">
		<form method="POST" class="form-signin" action="login" role="form">
			<div class="form-group">
				<h2 class="form-signin-heading">Sign in</h2>
			</div>
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
		</form>
		</div>
	</div>
</body>
</html>