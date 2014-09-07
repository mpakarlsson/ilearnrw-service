<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Screening Test Creator</title>
		
<script src="${pageContext.request.contextPath}/apps/resources/libs/sb-admin-v2/js/jquery-1.10.2.js"></script>
<script src="${pageContext.request.contextPath}/apps/resources/libs/sb-admin-v2/js/typeahead.js"></script>
<script src="${pageContext.request.contextPath}/apps/resources/libs/sb-admin-v2/js/screening/chris_typeahead.js"></script>
<script src="${pageContext.request.contextPath}/apps/resources/libs/sb-admin-v2/js/screening/create_page.js"></script>
	
<link href="${pageContext.request.contextPath}/apps/resources/libs/sb-admin-v2/css/screening/screening_style.css" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/apps/resources/libs/sb-admin-v2/css/plugins/timeline/timeline.css" rel="stylesheet">

</head>
<body>

<div id="testara">
</div>
<script>
suggestWords('testara', 'itsme', '${clusterWords}');
gogo('itsme');
</script>

<div id="navcontainer">
<ul id="navlist">
<li><a href="${pageContext.request.contextPath}/apps/screening">Home</a></li>
<c:choose>
	<c:when test="${fname != null}">
		<li><a href="${pageContext.request.contextPath}/apps/screening?fname=${fname}">View Full Test</a></li>
		<li>Go to a cluster
		<select name="forma"  onchange="location = this.options[this.selectedIndex].value;">
		
		 <option value="screening">Select Cluster</option>
		<c:forEach items="${profileClusters.getClustersNumbers()}" var="res" varStatus="inner">
		 <option value="screening?fname=${fname}&cluster=${res}">Cluster ${res}</option>
		</c:forEach>
		</select>
		</li>
	</c:when>
</c:choose>
</ul>
</div>

<c:choose>
	<c:when test="${profileClusters.getClusterProblems(cluster) != null}">

		<h2>Test name: ${fname} </h2>
		<h3>Cluster ${cluster } </h3>
		<ul>
		<c:forEach items="${profileClusters.getClusterProblems(cluster)}" var="res" varStatus="inner">
		<li> Pr[${1+res.getCategory()}, ${1+res.getIndex()}]: ${res.getProblemDescription().getHumanReadableDescription() }</li>
		</c:forEach>
		</ul>

    </c:when>
	<c:when test="${fname != null}">
	
		<h2>Test name: ${fname} </h2>
	    <%Integer current = 0;%>
		<c:forEach items="${profileClusters.getClustersNumbers()}" var="cluster" varStatus="inner">
			<h3>Cluster ${cluster } </h3>
			<ul>
			<c:forEach items="${screeningTest.getClusterQuestions(cluster)}" var="questions" varStatus="inner">
			<li> <% current++; out.print(current); %> <strong>Question:</strong> ${questions.getQuestion() } <!-- <br>${questions.getId() }  --><br></li>
				<!-- <c:forEach items="${questions.getRelatedWords()}" var="relatedWords" varStatus="inner">
				${relatedWords}
				</c:forEach> -->
			</c:forEach>
			</ul>
		</c:forEach>

    </c:when>

	<c:otherwise>
	<div id="filenamesListDiv"></div>
		<script>
			filenamesList('${screeningTestList}');
		</script>	
    </c:otherwise>
   
    
</c:choose>



<div id="questionsListDiv">

</div>

<div id="addQuestionsDiv">

</div>

<script>
if ('${showAll}' == 'false'){
	loadClusterParameters('\'${fname}\'', '${cluster}', '${ clustersQuestions}', '${clusterWords}');
	loadAddQuestionField('\'${fname}\'', '${cluster}');
}
</script>


</body>
</html>
