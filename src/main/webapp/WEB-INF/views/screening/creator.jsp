<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
	
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>

<meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
    
<jsp:include page="../includes/includes.jsp"></jsp:include>

<title>Screening Test Creator</title>
		
<script src="${pageContext.request.contextPath}/apps/resources/libs/sb-admin-v2/js/typeahead.js"></script>
<script src="${pageContext.request.contextPath}/apps/resources/libs/sb-admin-v2/js/screening/chris_typeahead.js"></script>
<script src="${pageContext.request.contextPath}/apps/resources/libs/sb-admin-v2/js/screening/get_post_functions.js"></script>
<script src="${pageContext.request.contextPath}/apps/resources/libs/sb-admin-v2/js/screening/create_page.js"></script>
<script src="${pageContext.request.contextPath}/apps/resources/libs/sb-admin-v2/js/screening/server_requests.js"></script>
<script src="${pageContext.request.contextPath}/apps/resources/libs/sb-admin-v2/js/screening/jquery.selectric.js"></script>
	
<link href="${pageContext.request.contextPath}/apps/resources/libs/sb-admin-v2/css/screening/screening_style.css" rel="stylesheet">
<link href="${pageContext.request.contextPath}/apps/resources/libs/sb-admin-v2/css/screening/selectric.css" rel="stylesheet">
<link href="${pageContext.request.contextPath}/apps/resources/libs/sb-admin-v2/css/plugins/timeline/timeline.css" rel="stylesheet">

</head>
<body>



    <div id="wrapper">

        <jsp:include page="../includes/navigation.jsp"></jsp:include>

        <div id="page-wrapper">
            <div class="row">
                <div class="col-lg-12">
                <sec:authorize ifAnyGranted="PERMISSION_ADMIN">
        			<h1 class="page-header">Screening test page</h1>
    			</sec:authorize>
                <sec:authorize ifAnyGranted="PERMISSION_TEACHER">
        			<h1 class="page-header">Screening test page</h1>
    			</sec:authorize>
    			<sec:authorize ifAnyGranted="PERMISSION_STUDENT">
        			<h1 class="page-header">Screening test page</h1>
    			</sec:authorize>
                </div>
            </div>
            <div class="row">
                <div class="col-lg-12">
                    <div class="panel panel-default">
                        <div class="panel-heading">
                            <i class="fa fa-bar-chart-o fa-fw"></i> ${fname} | 
                            <a href="${pageContext.request.contextPath}/apps/screening?fname=${fname}">View Full Test</a>
                     		 | 
                            <a href="${pageContext.request.contextPath}/apps/testviewer?fname=${fname}" target="_blank">View Printable Test</a>
                     		 | 
							<span>Go to a cluster
							<select name="forma" onchange="location = this.options[this.selectedIndex].value;">
							<option value="screening">Select Cluster</option>
							<c:forEach items="${profileClusters.getClustersNumbers()}" var="res" varStatus="inner">
							 <option value="screening?fname=${fname}&cluster=${res}">Cluster ${res}</option>
							</c:forEach>
							</select>
							</span>
                            
                        </div>
                        <div class="panel-body">


							<c:choose>
								<c:when test="${profileClusters.getClusterProblems(cluster) != null}">
									<h3>Cluster ${cluster } (${clusterCategories })</h3>
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
										<h3>Cluster ${cluster } (${clustersCategoriesMap.get(cluster) }) </h3>
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
								loadClusterParameters('\'${fname}\'', '${cluster}', '${clusterDescriptions}', '${clustersQuestions}', '${clusterWords}', '${wordsInsideCategory}', '${activeQuestions}');
								loadAddQuestionField('\'${fname}\'', '${cluster}');
							}
							</script>


                        </div>
                    </div>
                </div>
            </div>
        </div>

    </div>


</body>
</html>
