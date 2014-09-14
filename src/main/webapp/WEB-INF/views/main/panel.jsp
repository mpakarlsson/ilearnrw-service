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
					<sec:authorize ifAnyGranted="PERMISSION_ADMIN">
					</sec:authorize>
					<sec:authorize ifAnyGranted="PERMISSION_TEACHER">
					</sec:authorize>
					<sec:authorize ifAnyGranted="PERMISSION_STUDENT">
						<h1 class="page-header">Student statistics</h1>
					</sec:authorize>
				</div>
			</div>
			<div class="row">
				<div class="col-lg-6 box">
					<div class="row">
						<img
							src="${pageContext.request.contextPath}/apps/resources/webapp/images/test.png" />
					</div>
					<div class="row">Print paper-based screening test</div>
				</div>
				<div class="col-lg-6 box">
					<div class="row">
						<img
							src="${pageContext.request.contextPath}/apps/resources/webapp/images/screening.png" />
					</div>
					<div class="row">Access online screening test</div>
				</div>
			</div>
			<div class="row">
				<div class="col-lg-6 box">
					<div class="row">
						<img
							src="${pageContext.request.contextPath}/apps/resources/webapp/images/placeholder.png" />
					</div>
					<div class="row">View reader user guide</div>
				</div>
				<div class="col-lg-6 box">
					<div class="row">
						<img
							src="${pageContext.request.contextPath}/apps/resources/webapp/images/placeholder.png" />
					</div>
					<div class="row">View game user guide</div>
				</div>
			</div>
		</div>

	</div>

</body>

</html>
