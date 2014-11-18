<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>

<meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">

<jsp:include page="../includes/includes.jsp"></jsp:include>
<script src="${pageContext.request.contextPath}/apps/resources/libs/sb-admin-v2/js/screening/get_post_functions.js"></script>
<script src="${pageContext.request.contextPath}/apps/resources/libs/sb-admin-v2/js/screening/test_page.js"></script>
<script src="${pageContext.request.contextPath}/apps/resources/libs/sb-admin-v2/js/screening/server_requests.js"></script>

<title>Screening Test Creator</title>

<link
	href="${pageContext.request.contextPath}/apps/resources/libs/sb-admin-v2/css/screening/screening_style.css"
	rel="stylesheet">
<link
	href="${pageContext.request.contextPath}/apps/resources/libs/sb-admin-v2/css/plugins/timeline/timeline.css"
	rel="stylesheet">

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
							<i class="fa fa-bar-chart-o fa-fw"></i> Test for ${username}

						</div>
						<div class="panel-body">


							<c:forEach items="${screeningTest.getSortedQuestionsListByType()}" var="questions" varStatus="outer">
							
								<c:choose>
									<c:when test="${outer.index<=0 || !screeningTest.getSortedQuestionsListByType().get(outer.index-1).getQuestion().equalsIgnoreCase(questions.getQuestion())}">
									<hr><h4> ${questions.getQuestion() }</h4>
									</c:when>
								</c:choose>
								<hr>
								${profileClusters.getClusterHTMLDescription(questions.getParentCluster()) }
								<%-- <c:forEach items="${profileClusters.getClusterProblems(questions.getParentCluster())}" var="res" varStatus="inner">
									<i> ${res.getProblemDescription().getHumanReadableDescription() }</i><br>
								</c:forEach> --%>
								<c:forEach items="${questions.getRelatedWords()}" var="relatedWords" varStatus="inner">
									<div class="word" data-word="${relatedWords}"
										data-cluster="${questions.getParentCluster()}" style="padding: 3px;background-color:${inner.index % 2 == 0 ? '#F8F8F8' : '#ebebeb '};">
										<strong style="text-decoration: underline;">${relatedWords} </strong><hr style="margin: 5px 0 5px 0;"> <form>
										<input type="radio" name="ans" value="WORD_SUCCESS" style="margin-left:10px">Correct
										<input type="radio" name="ans" value="WORD_FAILED" style="margin-left:10px">Incorrect
										<input type="radio" name="ans" value="WORD_NOT_ANSWERED" style="margin-left:10px">Not Answered
										<input type="radio" name="ans" value="WORD_NOT_SEEN" style="margin-left:10px" checked>Not Seen
										</form>
									</div>
								</c:forEach>
							</c:forEach>

							<button onclick="sendStudentsAnswers('${username}', '${userid}')">Submit
								Data</button>


						</div>
					</div>
				</div>
			</div>
		</div>

	</div>

</body>
</html>
