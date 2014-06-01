<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>

<head>

<meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">

<title>Test logs</title>
<script>
	$(document)
			.ready(
					function() {
						$('#loginForm')
								.submit(
										function() {
											$
													.ajax({
														'password' : 'api',
														'username' : 'api',
														'url' : $("#site")
																.val(),
														'type' : 'POST',
														'success' : function() {
															window.location = 'http://www.website.com/basic-auth-file.php';
														},
														'error' : function() {
															alert('Bad Login Details');
														},
													});

											return false;
										});
					});
</script>

<jsp:include page="../includes/includes.jsp"></jsp:include>

</head>

<body>

	<div id="wrapper">

		<jsp:include page="../includes/navigation.jsp"></jsp:include>

		<div id="page-wrapper">
			<div class="panel panel-default">
				<div class="panel-heading">Basic Form Elements</div>
				<div class="panel-body">
					<div class="row">
						<div class="col-sm-12">
							<form id="loginForm" class="form form-horizontal" role="form">
								<div class="form-group">
									<label class="col-sm-2 control-label">Site</label>
									<div class="col-sm-10 ">
										<input class="form-control"
											value="http://api.ilearnrw.eu/ilearnrw/logs">
										<p class="col-sm-10 help-block">Url to POST to.</p>
									</div>
								</div>
								<div class="form-group">
									<label class="col-sm-2 control-label">Username</label>
									<div class="col-sm-10 ">
									<input
										class="form-control"
										value="${pageContext.request.userPrincipal.name}">
									</div>
								</div>
								<div class="form-group">
									<label class="col-sm-2 control-label">applicationId</label>
									<div class="col-sm-10">
										<div class="radio">
											<label> <input name="applicationId"
												id="optionsRadios1" value="GAME_WORLD" checked="checked" type="radio">GAME_WORLD
											</label>
										</div>
										<div class="radio">
											<label> <input name="applicationId"
												id="DROP_CHOPS" value="option2" type="radio">DROP_CHOPS
											</label>
										</div>
										<div class="radio">
											<label> <input name="applicationId"
												id="SERENADE_HERO" value="option3" type="radio">SERENADE_HERO
											</label>
										</div>
									</div>
								</div>
								<div class="form-group">
									<label class="col-sm-2">Site</label> <input
										class="col-sm-10 form-control"
										value="http://api.ilearnrw.eu/ilearnrw/logs">
									<p class="col-sm-10 help-block">Url to POST to.</p>
								</div>
								<div class="form-group">
									<label>Text Input with Placeholder</label> <input
										class="form-control" placeholder="Enter text">
								</div>
								<div class="form-group">
									<label>Static Control</label>
									<p class="form-control-static">email@example.com</p>
								</div>
								<div class="form-group">
									<label>File input</label> <input type="file">
								</div>
								<div class="form-group">
									<label>Text area</label>
									<textarea class="form-control" rows="3"></textarea>
								</div>
								<div class="form-group">
									<label>Checkboxes</label>
									<div class="checkbox">
										<label> <input value="" type="checkbox">Checkbox
											1
										</label>
									</div>
									<div class="checkbox">
										<label> <input value="" type="checkbox">Checkbox
											2
										</label>
									</div>
									<div class="checkbox">
										<label> <input value="" type="checkbox">Checkbox
											3
										</label>
									</div>
								</div>
								<div class="form-group">
									<label>Inline Checkboxes</label> <label class="checkbox-inline">
										<input type="checkbox">1
									</label> <label class="checkbox-inline"> <input type="checkbox">2
									</label> <label class="checkbox-inline"> <input type="checkbox">3
									</label>
								</div>
								<div class="form-group">
									<label>Radio Buttons</label>
									<div class="radio">
										<label> <input name="optionsRadios"
											id="optionsRadios1" value="option1" checked="" type="radio">Radio
											1
										</label>
									</div>
									<div class="radio">
										<label> <input name="optionsRadios"
											id="optionsRadios2" value="option2" type="radio">Radio
											2
										</label>
									</div>
									<div class="radio">
										<label> <input name="optionsRadios"
											id="optionsRadios3" value="option3" type="radio">Radio
											3
										</label>
									</div>
								</div>
								<div class="form-group">
									<label>Inline Radio Buttons</label> <label class="radio-inline">
										<input name="optionsRadiosInline" id="optionsRadiosInline1"
										value="option1" checked="" type="radio">1
									</label> <label class="radio-inline"> <input
										name="optionsRadiosInline" id="optionsRadiosInline2"
										value="option2" type="radio">2
									</label> <label class="radio-inline"> <input
										name="optionsRadiosInline" id="optionsRadiosInline3"
										value="option3" type="radio">3
									</label>
								</div>
								<div class="form-group">
									<label>Selects</label> <select class="form-control">
										<option>1</option>
										<option>2</option>
										<option>3</option>
										<option>4</option>
										<option>5</option>
									</select>
								</div>
								<div class="form-group">
									<label>Multiple Selects</label> <select multiple=""
										class="form-control">
										<option>1</option>
										<option>2</option>
										<option>3</option>
										<option>4</option>
										<option>5</option>
									</select>
								</div>
								<button type="submit" class="btn btn-default">Submit
									Button</button>
								<button type="reset" class="btn btn-default">Reset
									Button</button>
							</form>
						</div>
					</div>
					<!-- /.row (nested) -->
				</div>
				<!-- /.panel-body -->
			</div>
		</div>

	</div>

</body>