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

<title>Manage users</title>

<jsp:include page="../includes/includes.jsp"></jsp:include>

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
						<form:form action="new" method="POST" modelAttribute="user"
							class="form-horizontal" role="form">
							<spring:bind path="username">
								<div class="form-group ${status.error ? 'has-error' : ''}">
									<label class="col-sm-2 control-label" for="username">Username</label>
									<div class="col-sm-10">
										<form:input type="text" id="username"
											class="form-control col-sm-9" placeholder="Username"
											path="username" required="true" />
										<form:errors path="username" class="help-block" for="username" />
									</div>
								</div>
							</spring:bind>

							<spring:bind path="password">
								<div class="form-group ${status.error ? 'has-error' : ''}">
									<label class="col-sm-2 control-label" for="password">Password</label>
									<div class="col-sm-10">
										<form:input type="text" id="password"
											class="form-control col-sm-9" placeholder="Password"
											path="password" required="true" />
										<form:errors path="password" class="help-block" for="password" />
									</div>
								</div>
							</spring:bind>

							<spring:bind path="birthdate">
								<div class="form-group ${status.error ? 'has-error' : ''}">
									<label class="col-sm-2 control-label" for="birthdate">Birth
										Date</label>
									<div class="col-sm-10">
										<form:input type="text" id="birthdate" class="form-control"
											placeholder="year/month/day" path="birthdate" required="true" />
										<form:errors path="birthdate" class="help-block"
											for="birthdate" />
									</div>
								</div>
							</spring:bind>

							<div class="form-group">
								<div class="col-sm-offset-2 col-sm-10">
									<div class="checkbox">
										<label> <form:checkbox path="enabled" checked="true" />
											Enabled
										</label>
									</div>
								</div>
							</div>

							<spring:bind path="gender">
								<div class="form-group ${status.error ? 'has-error' : ''}">
									<label class="col-sm-2 control-label">Gender</label>
									<div class="col-sm-10">
										<fieldset>
											<div class="radio">
												<label> <form:radiobutton name="gender"
														path="gender" value="M" />Male
												</label>
											</div>
											<div class="radio">
												<label> <form:radiobutton name="gender"
														path="gender" value="F" />Female
												</label>
											</div>
										</fieldset>
										<form:errors path="gender" class="help-block" for="gender" />
									</div>
								</div>
							</spring:bind>

							<spring:bind path="language">
								<div class="form-group ${status.error ? 'has-error' : ''}">
									<label class="col-sm-2 control-label">Language</label>
									<div class="col-sm-10">
										<fieldset>
											<div class="radio">
												<label> <form:radiobutton name="language"
														path="language" value="EN" />English
												</label>
											</div>
											<div class="radio">
												<label> <form:radiobutton name="language"
														path="language" value="GR" />Greek
												</label>
											</div>
										</fieldset>
										<form:errors path="language" class="help-block" for="language" />
									</div>
								</div>
							</spring:bind>

							<button type="submit" class="btn btn-primary">Submit</button>
						</form:form>
					</div>
				</div>
			</div>
			</div>
		</div>
	</div>
</body>