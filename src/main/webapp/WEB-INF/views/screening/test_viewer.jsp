<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Screening Test Creator</title>
	
<link href="${pageContext.request.contextPath}/apps/resources/libs/sb-admin-v2/css/screening/screening_style.css" rel="stylesheet">

</head>
<body>
<div style="width:595px;">

<!-- 
<c:forEach items="${profileClusters.getClustersNumbers()}" var="cluster" varStatus="inner">
	<h3>Cluster ${cluster } </h3> -->
	<ol>
	<%Integer current = 0; %>
	<c:forEach items="${screeningTest.getClusterQuestions(cluster)}" var="questions" varStatus="inner">
	<li> <strong>${questions.getQuestion() } </strong><!-- <br>${questions.getId() }  --><br></li>
		<c:choose>
			<c:when test="${questions.isAttachRelWords()}">
				<c:forEach items="${questions.getRelatedWords()}" end="${questions.getRelatedWords().size()-2}" var="relatedWords" varStatus="inner">
						${relatedWords}, 
				</c:forEach>
				${questions.getRelatedWords().get(questions.getRelatedWords().size()-1)}
			</c:when>
		</c:choose>
	</c:forEach>
	</ol>
	
<!-- </c:forEach> -->

</div>

</body>
</html>
