<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>

<head>

<meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">

<title>iLearnRW</title>

<jsp:include page="../includes/includes.jsp"></jsp:include>
<style>
.box {
	border: 1px solid #bbb;
	text-align: center;
	height: 180px;
	padding: 10px;
}
</style>
</head>

<body>

	<div id="wrapper">

		<jsp:include page="../includes/navigation.jsp"></jsp:include>

		<div id="page-wrapper">
			<div class="row">
				<div class="col-lg-12">
					<h1 class="page-header">Online Management System Dashboard</h1>
				</div>
			</div>
			<div class="row">
				<div class="col-lg-6 box">
					<div class="row">
						<a
							href="${pageContext.request.contextPath}/apps/resources/webapp/pdf/Game User Guide Nov11.pdf">
							<img
							src="${pageContext.request.contextPath}/apps/resources/webapp/images/placeholder.png" />
						</a>
					</div>
					<div class="row">
						<a
							href="${pageContext.request.contextPath}/apps/resources/webapp/pdf/Game User Guide Nov11.pdf">View
							game user guide</a>
					</div>
				</div>
				<div class="col-lg-6 box">
					<div class="row">
						<a
							href="${pageContext.request.contextPath}/apps/resources/webapp/pdf/iLearnRW Reader User Guide 3Dec.pdf">
							<img
							src="${pageContext.request.contextPath}/apps/resources/webapp/images/placeholder.png" />
						</a>
					</div>
					<div class="row">
						<a
							href="${pageContext.request.contextPath}/apps/resources/webapp/pdf/iLearnRW Reader User Guide 3Dec.pdf">View
							reader user guide</a>
					</div>
				</div>
				<sec:authorize ifAnyGranted="PERMISSION_ADMIN,PERMISSION_EXPERT">
				<div class="col-lg-6 box">
					<div class="row">
						<a
							href="${pageContext.request.contextPath}/apps/screening">
							<img
							src="${pageContext.request.contextPath}/apps/resources/webapp/images/screening.png" />
						</a>
					</div>
					<div class="row">
						<a
							href="${pageContext.request.contextPath}/apps/screening">Screening
							tests</a>
					</div>
				</div>
				</sec:authorize>
			</div>
		</div>

	</div>

</body>

</html>
