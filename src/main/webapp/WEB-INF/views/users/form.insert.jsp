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

<title>Add user</title>

<jsp:include page="../includes/includes.jsp"></jsp:include>
<script src="${pageContext.request.contextPath}/apps/resources/webapp/js/bday-picker.js"></script>
<script type="text/javascript">
  $(document).ready(function(){
    $("#birthdatepicker").birthdaypicker({});
  });
</script>

</head>

<body>
	<div id="wrapper">
		<jsp:include page="../includes/navigation.jsp"></jsp:include>
		<div id="page-wrapper">
		<div class="container master-container">
			<div class="row">
				<div class="panel panel-info">
					<div class="panel-heading">New user</div>
					<div class="panel-body">
						<form:form action="new" method="POST" modelAttribute="userform"
							class="form-horizontal" role="form">
							<spring:bind path="user.username">
								<div class="form-group ${status.error ? 'has-error' : ''}">
									<label class="col-sm-2 control-label" for="username">Username</label>
									<div class="col-sm-10">
										<form:input type="text" id="username"
											class="form-control col-sm-9" placeholder="Username"
											path="user.username" required="true" />
										<form:errors path="user.username" class="help-block" for="username" />
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
										<form:errors path="user.password" class="help-block" for="password" />
									</div>
								</div>
							</spring:bind>

							<spring:bind path="user.birthdate">
								<div class="form-group ${status.error ? 'has-error' : ''}">
									<label class="col-sm-2 control-label" for="birthdate">Birth
										Date</label>
									<div class="col-sm-10" id="birthdatepicker">
										<fieldset id="birthdate" class='birthday-picker'>
											<div class="col-sm-3">
												<form:select type="text" id="date" class="form-control"
													placeholder="day" path="birthdate.date" required="true" />
											</div>
											<div class="col-sm-3">
											<form:select type="text" id="month" class="form-control"
												placeholder="month" path="birthdate.month" required="true" />
											</div>
											<div class="col-sm-4">
											<form:select type="text" id="year" class="form-control"
												placeholder="year" path="birthdate.year" required="true" />
											</div>
										</fieldset>
										<form:errors path="birthdate" class="help-block"
											for="birthdate" />
									</div>
								</div>
							</spring:bind>

							<div class="form-group">
								<div class="col-sm-offset-2 col-sm-10">
									<div class="checkbox">
										<label> <form:checkbox path="user.enabled" checked="true" />
											Enabled
										</label>
									</div>
								</div>
							</div>

							<spring:bind path="user.gender">
								<div class="form-group ${status.error ? 'has-error' : ''}">
									<label class="col-sm-2 control-label">Gender</label>
									<div class="col-sm-10">
										<fieldset>
											<div class="radio">
												<label> <form:radiobutton name="gender"
														path="user.gender" value="M" />Male
												</label>
											</div>
											<div class="radio">
												<label> <form:radiobutton name="gender"
														path="user.gender" value="F" />Female
												</label>
											</div>
										</fieldset>
										<form:errors path="user.gender" class="help-block" for="gender" />
									</div>
								</div>
							</spring:bind>

							<spring:bind path="user.language">
								<div class="form-group ${status.error ? 'has-error' : ''}">
									<label class="col-sm-2 control-label">Language</label>
									<div class="col-sm-10">
										<fieldset>
											<div class="radio">
												<label> <form:radiobutton name="language"
														path="user.language" value="EN" />English
												</label>
											</div>
											<div class="radio">
												<label> <form:radiobutton name="language"
														path="user.language" value="GR" />Greek
												</label>
											</div>
										</fieldset>
										<form:errors path="user.language" class="help-block" for="language" />
									</div>
								</div>
							</spring:bind>
							
							<div class="form-group">
								<div class="col-sm-offset-2 col-sm-10">
									<div class="checkbox">
										<label> <form:checkbox path="teacher" />
											Is teacher
										</label>
									</div>
								</div>
							</div>

							<button type="submit" class="btn btn-primary">Submit</button>
						</form:form>
					</div>
				</div>
			</div>
			</div>
		</div>
	</div>
</body>