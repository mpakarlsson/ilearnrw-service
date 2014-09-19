<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Screening Test Creator</title>
	
<link href="${pageContext.request.contextPath}/apps/resources/libs/sb-admin-v2/css/screening/screening_style.css" rel="stylesheet">

</head>
<body>
<div style="width:595px;">

	<c:forEach items="${screeningTest.getSortedQuestionsListByType()}" var="questions" varStatus="inner">
	
		<c:choose>
			<c:when test="${inner.index<=0 || !screeningTest.getSortedQuestionsListByType().get(inner.index-1).getQuestion().equalsIgnoreCase(questions.getQuestion())}">
			<strong> ${questions.getQuestion() }</strong><br>
			</c:when>
		</c:choose>
		<c:choose>
			<c:when test="${questions.isAttachRelWords() && questions.getRelatedWords().size()>1}">
				<c:forEach items="${questions.getRelatedWords()}" end="${questions.getRelatedWords().size()-2}" var="relatedWords" varStatus="inner">
						${relatedWords}, 
				</c:forEach>
				${questions.getRelatedWords().get(questions.getRelatedWords().size()-1)}
			</c:when>
			<c:otherwise>
				${questions.getRelatedWords().get(0)}
			</c:otherwise>
		</c:choose>
		<br>
	</c:forEach>

</div>

</body>
</html>
