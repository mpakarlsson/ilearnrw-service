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
<script src="${pageContext.request.contextPath}/apps/resources/libs/sb-admin-v2/js/screening/test_page.js"></script>

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

							<c:forEach items="${profileClusters.getClustersNumbers()}"
								var="cluster" varStatus="inner">
								<h3>Cluster ${cluster }</h3>
								<ol>
									<c:forEach
										items="${screeningTest.getClusterQuestions(cluster)}"
										var="questions" varStatus="inner">
										<li>${questions.getQuestion() }</li>

										<c:forEach items="${questions.getRelatedWords()}"
											var="relatedWords" varStatus="inner">
											<div class="word" data-word="${relatedWords}"
												data-cluster="${cluster}" style="padding: 3px; height: 30px; background-color:${inner.index % 2 == 0 ? '#F8F8F8' : '#FFFFFF '};">
												<strong>${relatedWords} </strong> Displayed <input
															type="checkbox" name="displayed" checked> Correct 
															<input type="checkbox" name="correct"><br>
											</div>
										</c:forEach>

									</c:forEach>
								</ol>
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
