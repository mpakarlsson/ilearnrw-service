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
	$(document)
			.ready(
					function() {
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
							$("#birthdayDiv").hide();
						} else {
							$("#studentDetails").show();
							$("#studentDetails :input").prop('disabled', false);
							$("#birthdayDiv").show();
						}

						$("#btn-changepassword")
								.click(
										function() {
											$("#changebtn").click(function(){
												$.ajax(
														{
															url : "${pageContext.request.contextPath}/apps/users/${userform.user.id}/changepassword",
															type : 'POST',
															data : {
																password : $(
																		"#newpassword")
																		.val()
															}

														})
												.always(
														function(data) {
															$('#changepasswordstatus')
																	.html(data);
															$('#changepasswordstatus')
																	.removeClass("hidden");
															$("#newpassword")
																	.val("");
														});
												
											});
											$("#dialog-changepassword").modal({});
										});

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
							<spring:bind path="user.birthdate">
								<div id="birthdayDiv">
									<div class="form-group ${status.error ? 'has-error' : ''}">
										<label class="col-sm-2 control-label" for="birthdate">Birth
											Date</label>
										<div class="col-sm-10" id="birthdatepicker">
											<form:input type="text" id="datepicker" path="user.birthdate" />
											<form:errors path="user.birthdate" class="help-block"
												for="birthdate" />
										</div>
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
										<form:select path="user.gender" class="form-control">
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
										<form:select path="user.language" class="form-control">
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
											Select a school or type in a new one: <select
												id="schoolSelect" class="form-control">
												<option value=""></option>
												<c:forEach items="${schools}" var="s">
													<option value="${s.name}">${s.name}</option>
												</c:forEach>
											</select>
											<form:input type="text" id="school"
												class="form-control col-sm-9" placeholder="new School"
												path="studentDetails.school" required="true" />
											<form:errors path="studentDetails.school" class="help-block"
												for="school" />
										</div>
									</div>
									<div class="form-group ${status.error ? 'has-error' : ''}">
										<label class="col-sm-2 control-label">Classroom</label>
										<div class="col-sm-10">
											Select a class or type in a new one: <select
												id="classroomSelect" class="form-control">
												<option value=""></option>
												<c:forEach items="${classRooms}" var="s">
													<option value="${s.name}">${s.name}</option>
												</c:forEach>
											</select>
											<form:input type="text" id="classRoom"
												class="form-control col-sm-9" placeholder="New classroom"
												path="studentDetails.classRoom" required="true" />
											<form:errors path="studentDetails.classRoom"
												class="help-block" for="classRoom" />
										</div>
									</div>
									<div class="form-group ${status.error ? 'has-error' : ''}">
										<label class="col-sm-2 control-label">Teacher</label>
										<div class="col-sm-10">
											<form:select id="teacherSelect"
												path="studentDetails.teacherId" class="form-control">
												<form:option value="" label="" />
												<form:options items="${teachersList}" itemLabel="username"
													itemValue="id" />
											</form:select>
											<form:errors path="studentDetails.teacherId"
												class="help-block" for="teacherSelect" />
										</div>
									</div>

								</div>
							</spring:bind>
						</div>
					</div>
				</div>
				<div class="row">
					<div class="col-md-4">
						<button type="submit" class="btn btn-primary">Save</button>
						<button type="button" id="btn-changepassword"
							class="btn btn-default">Change password</button>
					</div>
					<div class="col-md-8">
						<div class="alert alert-info hidden" id="changepasswordstatus"></div>
					</div>
				</div>
			</form:form>
		</div>
	</div>
	
<div id="dialog-changepassword" class="modal fade bs-example-modal-sm" tabindex="-1" role="dialog" aria-labelledby="mySmallModalLabel" aria-hidden="true">
  <div class="modal-dialog modal-sm">
    <div class="modal-content">
		<div class="modal-header">
          <button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">�</span><span class="sr-only">Close</span></button>
          <h4 class="modal-title" id="mySmallModalLabel">Change password</h4>
        </div>
        <div class="modal-body">
			<div class="form-group">
				<label>Enter a new password</label> <input id="newpassword"
					type="password" class="form-control" placeholder="New password"></input>
	
			</div>
			<button type="button" id="changebtn" class="btn btn-primary" data-dismiss="modal">Change</button>
	        <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
        </div>
    </div>
  </div>
</div>
</body>
</html>
