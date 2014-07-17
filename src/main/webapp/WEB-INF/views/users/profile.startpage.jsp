<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>User profile</title>
<!--  <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/apps/resources/css/style.css"></link>  -->

    <link href="${pageContext.request.contextPath}/apps/resources/libs/sb-admin-v2/css/plugins/morris/morris-0.4.3.min.css" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/apps/resources/libs/sb-admin-v2/css/plugins/timeline/timeline.css" rel="stylesheet">
</head>
<body>
	<div class="form-container">
	<form:form
		action="${pageContext.servletContext.contextPath}/apps/users/${userId}/initialize"
		method="GET" modelAttribute="profile">
		
		<legend>Profile Initialization for ${username}</legend>
		
		<table border="1">
                <td>Problem</td>
                <td> 
                </td>  
                <td  style="text-align:left">   Set </td>
       <c:forEach items="${profile.getUserProblems().getProblems().getProblemsIndex()}" var="res" varStatus="inner">
            <tr>
                <td  style="text-align:left">   ${res.getUri()} : ${res.getType().getUrl()} </td>
                <td> 
                <a href="${pageContext.servletContext.contextPath}/apps/users/${userId}/initialize?difficulty=0&category=${inner.index}&start=0&end=${profile.getUserProblems().getProblems().getRowLength(inner.index)}">
                Start Test
                </a>
                </td>  
                <td  style="text-align:left">   ${categoriesSet.contains(inner.index)} </td>
            </tr>
        </c:forEach>
    </table>
       <input type="hidden" name="difficulty" value="0">
		<input type="submit" value="Continue" />
	</form:form>
	</div>
</body>
</html>
