<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<html>
<head>
<title>Login</title>
</head>
<body>

	<h2>Login Information</h2>
	<c:if test="${not empty error}">
		<div class="errorblock">
			Your login attempt was not successful, try again.<br /> Caused :
			${error}
		</div>
	</c:if>
	<form method="POST" action="<c:url value='login' />">
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
</body>
</html>