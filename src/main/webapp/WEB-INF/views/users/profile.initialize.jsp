<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>User profile</title>
<!--  <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/apps/resources/css/style.css"></link>  -->

    <link href="${pageContext.request.contextPath}/apps/resources/libs/sb-admin-v2/css/plugins/morris/morris-0.4.3.min.css" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/apps/resources/libs/sb-admin-v2/css/plugins/timeline/timeline.css" rel="stylesheet">
</head>
<body>
	<div class="form-container">
	<form:form
		action="${pageContext.servletContext.contextPath}/apps/users/${userId}/confirmanswers"
		method="GET" modelAttribute="profile">
		
		<legend>Profile Initialization for ${username}</legend>
		<form:hidden path="language" />
		${ profile.getUserProblems().getProblemDescription(category, index) }
		<table width="100%">
       <c:forEach items="${result}" var="res" varStatus="inner">
            <tr>
                <td>Word: </td>
                <td> ${res.getAnnotatedWord().toString()} </td>
                <td> <input type="checkbox" name="succeed" value="${res.getAnnotatedWord().toString()}">
                Correct
                <br>
                <input type="hidden" name="words" value="${res.getAnnotatedWord().toString()}">
                </td>  
            </tr>
        </c:forEach>
    </table>
       <input type="hidden" name="category" value="${category}">
       <input type="hidden" name="index" value="${index}">
       <input type="hidden" name="start" value="${start}">
       <input type="hidden" name="end" value="${end}">
       <input type="hidden" name="difficulty" value="${difficulty}">
		<input type="submit" value="Continue" />
	</form:form>
	</div>
</body>
</html>
