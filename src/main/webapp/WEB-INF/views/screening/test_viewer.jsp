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
<li><a href="#">View Full Test</a></li>
<li>Go to a cluster
<select name="forma"  onchange="location = this.options[this.selectedIndex].value;">

 <option value="screening">Select Cluster</option>
<c:forEach items="${profileClusters.getClustersNumbers()}" var="res" varStatus="inner">
 <option value="screening?cluster=${res}">Cluster ${res}</option>
</c:forEach>
</select>
</li>
</ul>

<div id="testView">

</div>



<script>
function test(t){
	var element = document.createElement('div');
	element.setAttribute("id", 'question');
	element.setAttribute('class', 'question_box');
	element.innerHTML = '<label>Question</label>';
	element.innerHTML = (JSON.parse(t)).questions[0].clusterQuestions[1].question;
	document.getElementById('testView').appendChild(element);
	
}
</script>
<script>
test('${screeningTest}');
</script>

</div>	

</body>
</html>
