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

<title>Edit user</title>

<jsp:include page="../includes/includes.jsp"></jsp:include>

<script type="text/javascript">
	$(document).ready(function() {
		$("#datepicker").datepicker({
			changeMonth : true,
			changeYear : true,
			dateFormat : "dd.mm.yy"
		});
		$("#schoolSelect").change(function() {
			$("#school").val($(this).val());
		});
		$("#classroomSelect").change(function() {
			$("#classRoom").val($(this).val());
		});

		if ("${userform.role}" !== "ROLE_STUDENT") {
			$("#studentDetails").hide();
			$("#studentDetails :input").prop('disabled', true);
		} else {
			$("#studentDetails").show();
			$("#studentDetails :input").prop('disabled', false);
		}

	});
</script>

</head>

<body>
	<div id="wrapper">
		<jsp:include page="../includes/navigation.jsp"></jsp:include>
		<div id="page-wrapper">
			<div class="row">
				<div class="col-lg-2">
					<c:choose>
						<c:when test="${userform.role == 'ROLE_ADMIN'}">
							<img
								src="${pageContext.request.contextPath}/apps/resources/webapp/images/admin_head.png" />
						</c:when>
						<c:when test="${userform.role == 'ROLE_EXPERT'}">
							<img
								src="${pageContext.request.contextPath}/apps/resources/webapp/images/expert_head.png" />
						</c:when>
						<c:when test="${userform.role == 'ROLE_TEACHER'}">
							<img
								src="${pageContext.request.contextPath}/apps/resources/webapp/images/teacher_head.png" />
						</c:when>
						<c:otherwise>
							<img
								src="${pageContext.request.contextPath}/apps/resources/webapp/images/student_head.png" />
						</c:otherwise>

					</c:choose>
				</div>
				<div class="col-lg-10">
					<h1 class="page-header">Edit user</h1>
				</div>
			</div>
			<form:form action="edit" method="POST" modelAttribute="userform"
				class="form-horizontal" role="form">
				<div class="row">
					<div class="panel panel-info">
						<div class="panel-heading">User details</div>
						<div class="panel-body">
							<spring:bind path="user.username">
								<div class="form-group ${status.error ? 'has-error' : ''}">
									<label class="col-sm-2 control-label" for="username">Username</label>
									<div class="col-sm-10">
										<form:input type="text" id="username"
											class="form-control col-sm-9" placeholder="Username"
											path="user.username" required="true" />
										<form:errors path="user.username" class="help-block"
											for="username" />
									</div>
								</div>
							</spring:bind>

							<spring:bind path="user.password">
								<div class="form-group ${status.error ? 'has-error' : ''}">
									<label class="col-sm-2 control-label" for="password">New
										password</label>
									<div class="col-sm-10">
										<form:input type="text" id="password"
											class="form-control col-sm-9" placeholder="Password"
											path="user.password" required="true" />
										<form:errors path="user.password" class="help-block"
											for="password" />
									</div>
								</div>
							</spring:bind>

							<spring:bind path="user.birthdate">
								<div class="form-group ${status.error ? 'has-error' : ''}">
									<label class="col-sm-2 control-label" for="birthdate">Birth
										Date</label>
									<div class="col-sm-10" id="birthdatepicker">
										<fieldset id="birthdate" class='birthday-picker'>
											<form:input type="text" id="datepicker" path="user.birthdate" />
										</fieldset>
										<form:errors path="user.birthdate" class="help-block"
											for="birthdate" />
									</div>
								</div>
							</spring:bind>

							<div class="form-group">
								<div class="col-sm-offset-2 col-sm-10">
									<div class="checkbox">
										<label> <form:checkbox path="user.enabled"
												checked="true" /> Enabled
										</label>
									</div>
								</div>
							</div>

							<spring:bind path="user.gender">
								<div class="form-group ${status.error ? 'has-error' : ''}">
									<label class="col-sm-2 control-label">Gender</label>
									<div class="col-sm-10">
										<form:select path="user.gender">
											<form:option value="M" label="Male" />
											<form:option value="F" label="Female" />
										</form:select>
										<form:errors path="user.gender" class="help-block"
											for="gender" />
									</div>
								</div>
							</spring:bind>

							<spring:bind path="user.language">
								<div class="form-group ${status.error ? 'has-error' : ''}">
									<label class="col-sm-2 control-label">Language</label>
									<div class="col-sm-10">
										<form:select path="user.language">
											<form:option value="EN" label="English" />
											<form:option value="GR" label="Greek" />
										</form:select>
										<form:errors path="user.language" class="help-block"
											for="language" />
									</div>
								</div>
							</spring:bind>
							<spring:bind path="role">
								<form:hidden path="role" />
							</spring:bind>
							<spring:bind path="studentDetails">
								<div id="studentDetails">
									<div class="form-group ${status.error ? 'has-error' : ''}">
										<label class="col-sm-2 control-label">School</label>
										<div class="col-sm-10">
											<form:input type="text" id="school"
												class="form-control col-sm-9" placeholder="School"
												path="studentDetails.school" required="true" />
											Type a new school or select from an existing one: <select
												id="schoolSelect">
												<option value="" ></option>
												<c:forEach items="${schools}" var="s">
													<option value="${s.name}">${s.name}</option>
												</c:forEach>
											</select>
											<form:errors path="studentDetails.school" class="help-block"
												for="school" />
										</div>
									</div>
									<div class="form-group ${status.error ? 'has-error' : ''}">
										<label class="col-sm-2 control-label">Classroom</label>
										<div class="col-sm-10">
											<form:input type="text" id="classRoom"
												class="form-control col-sm-9" placeholder="Classroom"
												path="studentDetails.classRoom" required="true" />
											Type a new class room or select from an existing one: <select
												id="classroomSelect">
												<option value=""></option>
												<c:forEach items="${classRooms}" var="s">
													<option value="${s.name}">${s.name}</option>
												</c:forEach>
											</select>
											<form:errors path="studentDetails.classRoom"
												class="help-block" for="classRoom" />
										</div>
										<div class="form-group ${status.error ? 'has-error' : ''}">
											<label class="col-sm-2 control-label">Teacher</label>
											<div class="col-sm-10">
												<form:select id="teacherSelect"
													path="studentDetails.teacherId">
													<form:option value="" label="" />
													<form:options items="${teachersList}" />
												</form:select>
												<form:errors path="studentDetails.teacherId"
													class="help-block" for="teacherSelect" />
											</div>
										</div>
									</div>
								</div>
							</spring:bind>
						</div>
					</div>
				</div>
				<button type="submit" class="btn btn-primary">Submit</button>
			</form:form>
		</div>
	</div>
</body>
</html>
