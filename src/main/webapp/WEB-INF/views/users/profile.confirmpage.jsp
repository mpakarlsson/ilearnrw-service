<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>User profile</title>
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/apps/resources/css/style.css"></link>
</head>
<body>
	<div class="form-container">
	<form:form
		action="${pageContext.servletContext.contextPath}/apps/users/${userId}/${nextPage}"
		method="GET" modelAttribute="profile">
		
		<legend>Profile Initialization for ${username}</legend>
		${ profile.getUserProblems().getProblemDescription(category, index) }
		<%Integer all = 0, correct = 0;%>
		<table width="100%">
       <c:forEach items="${wordlist}" var="res" varStatus="inner">
					<%all++;%>
            <tr>
                <td>${res}</td>
                <td  style="text-align:left"> 
                
                <c:choose>
			      <c:when test="${succeedlist.contains(res)}">correct
					<%correct++;%>
			      </c:when>
			
			      <c:otherwise>fail
			      <br />
			      </c:otherwise>
				</c:choose>
				</td>
            </tr>
        </c:forEach>
    </table>
		Score:<%out.print(correct+" / "+all);%> </br>
       <input type="hidden" name="difficulty" value="${difficulty}">
       <input type="hidden" name="category" value="${category}">
       <input type="hidden" name="index" value="${index}">
       <input type="hidden" name="start" value="${start}">
       <input type="hidden" name="end" value="${end}">
		<input type="submit" value="Continue" />
	</form:form>
	</div>
</body>
</html>
