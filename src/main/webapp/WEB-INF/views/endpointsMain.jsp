<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1" />
<title>Endpoints</title>
<link rel="stylesheet" type="text/css"
	href="${pageContext.request.contextPath}/apps/resources/css/style.css"></link>
<style>
th,td {
	text-align: left;
}
</style>
</head>
<body>
	<div id="wrapper">
		<h1>ILearnRW Endpoints</h1>
		<fieldset id="users">
			<legend> Endpoints </legend>
			<ul>
			<li><a href="${pageContext.request.contextPath}/endpoints-api">API endpoints</a><br/></li>
			<li><a href="${pageContext.request.contextPath}/apps/endpoints">Web application endpoints</a></li>
			</ul>
		</fieldset>
	</div>
</body>
</html>