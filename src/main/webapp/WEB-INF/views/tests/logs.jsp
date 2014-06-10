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

<jsp:include page="../includes/includes.jsp"></jsp:include>

<script>
	function ConvertFormToJSON(form){
	    var array = jQuery(form).serializeArray();
	    var json = {};
	    
	    jQuery.each(array, function() {
	        json[this.name] = this.value || '';
	    });
	    
	    return json;
	}

	$(document).ready(
			function() {

				$('#submit').click(
						function() {
							var serialized = JSON.stringify(ConvertFormToJSON($("#form")));
						    var $newDiv = $("<div/>").addClass(
											"alert alert-info alert-dismissable").css("word-wrap","break-word").html(serialized).append('<button class="close" aria-hidden="true" data-dismiss="alert" type="button">×</button>');
									$("#querypanel").append($newDiv);
							$.ajax({
								'username' : 'api',
								'password' : 'api',
								'contentType': "application/json",
								'url' : $("#site").val(),
								'type' : 'POST',
								'data' : serialized,
								'success' : function(data) {
									var $newDiv = $("<div/>").addClass(
											"alert alert-success alert-dismissable").html("Returned data: " + data).append('<button class="close" aria-hidden="true" data-dismiss="alert" type="button">×</button>');
									$("#querypanel").append($newDiv);
								},
								'error' : function(xhr, status, error) {
									var $newDiv = $("<div/>").addClass(
											"alert alert-danger alert-dismissable").html(error).append('<button class="close" aria-hidden="true" data-dismiss="alert" type="button">×</button>');
									$("#querypanel").append($newDiv);
								},
							});

							return false;
						});
			});
</script>

</head>

<body>

	<div id="wrapper">

		<jsp:include page="../includes/navigation.jsp"></jsp:include>

		<div id="page-wrapper">
			<div class="col-sm-6">
				<div class="panel panel-default">
					<div class="panel-heading">Basic Form Elements</div>
					<div class="panel-body">
						<div class="row">
							<div class="col-sm-12">
								<form id="form" class="form form-horizontal" role="form">
									<div class="form-group">
										<label class="col-sm-2 control-label">Site</label>
										<div class="col-sm-10 ">
											<input class="form-control" id="site"
												value="${pageContext.request.contextPath}/logs">
											<p class="col-sm-10 help-block">Url to POST to.</p>
										</div>
									</div>
									<div class="form-group">
										<label class="col-sm-2 control-label">Username</label>
										<div class="col-sm-10 ">
											<input class="form-control" name="username"
												value="${pageContext.request.userPrincipal.name}">
										</div>
									</div>
									<div class="form-group">
										<label class="col-sm-2 control-label">Application Id</label>
										<div class="col-sm-10">
											<div class="radio">
												<label> <input name="applicationId"
													type="radio" value="GAME_WORLD">GAME_WORLD
												</label>
											</div>
											<div class="radio">
												<label> <input name="applicationId" type="radio"
													value="DROP_CHOPS">DROP_CHOPS
												</label>
											</div>
											<div class="radio">
												<label> <input name="applicationId" type="radio" checked="checked"
													value="SERENADE_HERO">SERENADE_HERO
												</label>
											</div>
										</div>
									</div>
									<div class="form-group">
										<label class="col-sm-2 control-label">Tag</label>
										<div class="col-sm-10 ">
											<select name="tag" class="col-sm-10 form-control">
												<option>LEARN_SESSION_START</option>
												<option>LEARN_SESSION_END</option>
												<option>APP_SESSION_START</option>
												<option>APP_SESSION_END</option>
												<option>APP_ROUND_SESSION_START</option>
												<option>APP_ROUND_SESSION_END</option>
												<option>WORD_SELECTED</option>
												<option>WORD_DISPLAYED</option>
												<option selected="selected" >WORD_SUCCESS</option>
												<option>WORD_FAILED</option>
												<option>PROFILE_UPDATE</option>
												<option>LOGIN</option>
												<option>LOGOUT</option>
											</select>
										</div>
									</div>
									<div class="form-group">
										<label class="col-sm-2 control-label">Value</label>
										<div class="col-sm-10 ">
											<input class="form-control" name="value" value="Testing">
										</div>
									</div>
									<div class="form-group">
										<label class="col-sm-2 control-label">Duration</label>
										<div class="col-sm-10 ">
											<input class="form-control" name="duration" value="1.0">
										</div>
									</div>
									<div class="form-group">
										<label class="col-sm-2 control-label">Level</label>
										<div class="col-sm-10 ">
											<input class="form-control" name="level" value="1">
										</div>
									</div>
									<div class="form-group">
										<label class="col-sm-2 control-label">Mode</label>
										<div class="col-sm-10 ">
											<input class="form-control" name="mode" value="ADVENTURE">
										</div>
									</div>
									<div class="form-group">
										<label class="col-sm-2 control-label">Problem category</label>
										<div class="col-sm-10 ">
											<input class="form-control" name="problem_category" value="1">
										</div>
									</div>
									<div class="form-group">
										<label class="col-sm-2 control-label">Problem index</label>
										<div class="col-sm-10 ">
											<input class="form-control" name="problem_index" value="1">
										</div>
									</div>
									<div class="form-group">
										<label class="col-sm-2 control-label">Word</label>
										<div class="col-sm-10 ">
											<input class="form-control" name="word" value="hello">
										</div>
									</div>
									<div class="form-group">
										<label class="col-sm-2 control-label">Timestamp</label>
										<div class="col-sm-10 ">
											<input class="form-control" name="timestamp" value="2014-12-31T11:59:01">
										</div>
									</div>
									<button id="submit" type="button" class="btn btn-default">Submit</button>
								</form>
							</div>
						</div>
						<!-- /.row (nested) -->
					</div>
					<!-- /.panel-body -->
				</div>
			</div>
			<div class="col-sm-6">
				<div class="panel panel-default">
					<div class="panel-heading">Query responses</div>
					<div id="querypanel" class="panel-body">
					</div>
				</div>
			</div>
		</div>

	</div>

</body>