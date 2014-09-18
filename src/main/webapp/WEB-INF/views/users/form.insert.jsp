<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ page import="java.util.Date" %>
<%@ page import="java.text.SimpleDateFormat" %>
<!DOCTYPE html>
<html>

<head>

<meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">

<title>Add user</title>

<jsp:include page="../includes/includes.jsp"></jsp:include>
<script type="text/javascript">
	$(document).ready(
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
				if ($("#roleSelect").val() !== "student") {
					$("#studentDetails").hide();
					$("#studentDetails :input").prop('disabled', true);
					$("#birthdayDiv").hide();
				} else {
					$("#studentDetails").show();
					$("#studentDetails :input").prop('disabled', false);
					$("#birthdayDiv").show();
				}

				$("#headImage").attr(
						'src',
						'${pageContext.request.contextPath}/apps/resources/webapp/images/'
								+ $("#roleSelect").val() + '_head.png');

				$("#roleSelect").change(
						function() {
							$("#headImage").attr(
									'src',
									'${pageContext.request.contextPath}/apps/resources/webapp/images/'
											+ $(this).val() + '_head.png');
							if ($(this).val() === "student") {
								$("#studentDetails").show();
								$("#studentDetails :input").prop('disabled',
										false);
								$("#birthdayDiv").show();
							} else {
								$("#studentDetails").hide();
								$("#studentDetails :input").prop('disabled',
										true);
								$("#birthdayDiv").hide();
							}

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
					<img id="headImage"
						src="${pageContext.request.contextPath}/apps/resources/webapp/images/student_head.png" />
				</div>
				<div class="col-lg-10">
					<h1 class="page-header">New user</h1>
				</div>
			</div>
			<div class="row">
				<div class="panel panel-info">
					<div class="panel-heading">User details</div>
					<div class="panel-body">
						<form:form action="new" method="POST" modelAttribute="userform"
							class="form-horizontal" role="form">
							<spring:bind path="role">
								<div class="form-group ${status.error ? 'has-error' : ''}">
									<label class="col-sm-2 control-label">Role</label>
									<div class="col-sm-10">
										<form:select id="roleSelect" path="role" multiple="false"
											class="form-control">
											<form:option value="student" label="Student" />
											<sec:authorize
												ifAnyGranted="PERMISSION_ADMIN,PERMISSION_EXPERT">
												<form:option value="teacher" label="Teacher" />
											</sec:authorize>
											<sec:authorize ifAnyGranted="PERMISSION_ADMIN">
												<form:option value="expert" label="Expert" />
												<form:option value="admin" label="Admin" />
											</sec:authorize>
										</form:select>
										<form:errors path="role" class="help-block" for="role" />
									</div>
								</div>
							</spring:bind>

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
									<label class="col-sm-2 control-label" for="password">Password</label>
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
									<div id="birthdayDiv"
										class="form-group ${status.error ? 'has-error' : ''}">

										<fieldset id="birthdate" class='birthday-picker'>
											<label class="col-sm-2 control-label" for="birthdate">Birth
												Date</label>
											<div class="col-sm-10" id="birthdatepicker">
												<form:input type="text" id="datepicker"
													path="user.birthdate" value='<%=(new SimpleDateFormat("dd.MM.yyyy")).format(new Date())%>'/>
												<form:errors path="user.birthdate" class="help-block"
													for="birthdate" />

											</div>
										</fieldset>
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

							<div id="studentDetails">
								
								<spring:bind path="studentDetails">

									<div class="form-group ${status.error ? 'has-error' : ''}">
										<label class="col-sm-2 control-label">School</label>
										<div class="col-sm-10">
											Type a new school or select from an existing one: <select
												id="schoolSelect" class="form-control">
												<option value=""></option>
												<c:forEach items="${schools}" var="s">
													<option value="${s}">${s}</option>
												</c:forEach>
											</select>
											<form:input type="text" id="school"
												class="form-control col-sm-9" placeholder="School"
												path="studentDetails.school" required="true" />
											<form:errors path="studentDetails.school" class="help-block"
												for="school" />
										</div>
									</div>
									<div class="form-group ${status.error ? 'has-error' : ''}">
										<label class="col-sm-2 control-label">Classroom</label>
										<div class="col-sm-10">
											Type a new class room or select from an existing one: <select
												id="classroomSelect" class="form-control">
												<option value="" label=""></option>
												<c:forEach items="${classRooms}" var="s">
													<option value="${s}">${s}</option>
												</c:forEach>
											</select>
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
								</spring:bind>
							</div>

							<button type="submit" class="btn btn-primary">Save</button>
						</form:form>
					</div>
				</div>
			</div>
		</div>
	</div>
</body>