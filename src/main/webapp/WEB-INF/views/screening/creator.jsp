<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Screening Test Creator</title>
	
	<script src="${pageContext.request.contextPath}/apps/resources/libs/sb-admin-v2/js/screening/create_page.js"></script>
	
<link href="${pageContext.request.contextPath}/apps/resources/libs/sb-admin-v2/css/screening/screening_style.css" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/apps/resources/libs/sb-admin-v2/css/plugins/timeline/timeline.css" rel="stylesheet">

</head>
<body>

${showAll}

<div id="navcontainer">
<ul id="navlist">
<li><a href="${pageContext.request.contextPath}/apps/testviewer">View Full Test</a></li>
<li>Go to a cluster
<select name="forma"  onchange="location = this.options[this.selectedIndex].value;">

 <option value="screening">Select Cluster</option>
<c:forEach items="${profileClusters.getClustersNumbers()}" var="res" varStatus="inner">
 <option value="screening?cluster=${res}">Cluster ${res}</option>
</c:forEach>
</select>
</li>
</ul>
</div>	

<h3>Cluster ${cluster } </h3>
<ul>
<c:forEach items="${profileClusters.getClusterProblems(cluster)}" var="res" varStatus="inner">
<li> Pr[${1+res.getCategory()}, ${1+res.getIndex()}]: ${res.getProblemDescription().getHumanReadableDescription() }</li>
</c:forEach>
</ul>

<div id="questionsListDiv">

</div>

<div id="addQuestionsDiv">

</div>

<script>
if ('${showAll}' == 'true'){
	alert('hmmm');
}
else {
	loadClusterQuestions('${ clustersQuestions}');
	loadAddQuestionField('${cluster}');
}
</script>


</body>
</html>
