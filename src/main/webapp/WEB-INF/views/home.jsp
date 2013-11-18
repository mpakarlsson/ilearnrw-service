<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page session="false" %>
<html>
<head>
<title>Home</title>
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/apps/resources/css/style.css"></link>
</head>
<body>
<h1>
	Hello world!  
</h1>

<P>  The time on the server is ${serverTime}. </P>

<a href='<c:url value='panel'/>'>Control panel</a>

<P>  Current user: ${username}. </P>
<P>  Current principal: ${principal}. </P>

<a href='<c:url value='logout'/>'>Logout</a>

</body>
</html>
