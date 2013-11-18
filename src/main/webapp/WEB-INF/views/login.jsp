<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<html>
<head>
<title>Login</title>
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/apps/resources/css/style.css"></link>
</head>
<body>
<div class="form-container">
	<h2>Login Information</h2>
	<form method="POST" action="<c:url value='login' />">
		<c:if test="${not empty error}">
			<span class="error">
				Your login attempt was not successful, try again. Caused : ${error} 
			</span>
		</c:if>
		<table>
			<tr>
				<td><label id="username">Username</label></td>
				<td><input type="text" name="username" /></td>
			</tr>
			<tr>
				<td><label id="pass">Password</label></td>
				<td><input type="password" name="pass" /></td>
			</tr>
			<tr>
				<td colspan="2"><input type="submit" value="Submit" /></td>
			</tr>
		</table>
	</form>
</div>
</body>
</html>