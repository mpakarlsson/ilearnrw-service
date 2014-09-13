<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<!DOCTYPE html>
<html>

<head>

<meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">

<title>Assign students to teacher</title>

<jsp:include page="../includes/includes.jsp"></jsp:include>
<body>
	<div id="wrapper">
		<jsp:include page="../includes/navigation.jsp"></jsp:include>
		<div id="page-wrapper">
			<div class="row">
				<div class="col-lg-12">
					<h1 class="page-header">
						Assigned students for
						<c:out value="${teacherStudentForm.teacher.username}"></c:out>
					</h1>
				</div>
			</div>
			<form:form action="assign" method="POST"
				modelAttribute="teacherStudentForm" class="form-horizontal"
				role="form">
				<div class="row">
					<c:if test="${not empty teacherStudentForm.allStudents}">
						<div class="row">
							<div class="panel panel-info">
								<div class="panel-heading">Students</div>
								<div class="panel-body">
									<fieldset>
										<label class="col-sm-2 control-label" for="roles">Students</label>
										<div id="roles" class="col-sm-10">
											<form:checkboxes element="div class='checkbox'"
												items="${teacherStudentForm.allStudents}"
												path="selectedStudents" itemLabel="username" itemValue="id"></form:checkboxes>
										</div>
									</fieldset>
								</div>
							</div>
						</div>
					</c:if>
				</div>
				<button type="submit" class="btn btn-primary">Save assignments</button>
			<sec:authorize
				ifAnyGranted="PERMISSION_ADMIN,PERMISSION_EXPERT">
				<a class="btn btn-success"
					href="${pageContext.request.contextPath}/apps/users/new"> New
					User </a>
			</sec:authorize>
			</form:form>
		</div>
	</div>
</body>
</html>
