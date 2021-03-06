<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>User profile</title>
<jsp:include page="../includes/includes.jsp"></jsp:include>
<style>
.progress {
	cursor: pointer;
    position: relative;
}

.progress span {
    position: absolute;
    display: block;
    left: 1em;
    width: 100%;
    color: darkgray;
    text-align: left;
 }

.problemsblock {
	border: 1px solid #aaa;
	background: #eef;
	margin: 10px;
}

.problemdescription {
	border: 1px solid #aaa;
	margin: 5px;
	padding: 0px;
	cursor: pointer;
}

.problemdescription-text {
	text-align: center;
}

.problemdescription .problemstatus-binary-0 {
	background: #0F0;
	height: 2px;
}

.problemdescription .problemstatus-binary-1 {
	background: #F00;
	height: 2px;
}

.problemdescription .problemstatus-zeroToThree-0 {
	background: #0F0;
	height: 2px;
}

.problemdescription .problemstatus-zeroToThree-1 {
	background: #FF0;
	height: 2px;
}

.problemdescription .problemstatus-zeroToThree-2 {
	background: #F70;
	height: 2px;
}

.problemdescription .problemstatus-zeroToThree-3 {
	background: #F00;
	height: 2px;
}
</style>
<script>
	$(function() {
		$('#independentMenu').click(function() {
			$("#independent-profile").show();
			$("#supervised-profile").hide();

		});
		$('#supervisedMenu').click(function() {
			$("#independent-profile").hide();
			$("#supervised-profile").show();

		});
		$('.progress-bar').each(function() {
			  var min = $(this).attr('aria-valuemin');
			  var max = $(this).attr('aria-valuemax');
			  var now = $(this).attr('aria-valuenow');
			  var siz = (now-min)*100/(max-min);
			  $(this).css('width', siz+'%');
			});
		$(".progress").each(
				function(i) {
					$(this).click(
							function() {
								$(
										'#problemsblock'
												+ $(this).data('index') + '-'
												+ $(this).data('type'))
										.toggle();
							});
				});
		$(".problemdescription").click(
				function() {
					var text = $(this).data("problem");

					$("#dialog #dialog-value-level-zeroToThree").hide();
					$("#dialog #dialog-value-level-binary").hide();
					$(
							"#dialog #dialog-value-level-"
									+ $(this).data("severitytype")).val(
							$(this).data("level")).show();

					$("#dialog #dialog-description").html(text);
					$("#dialog").data("type", $(this).data("type"));
					$("#dialog").data("index", $(this).data("index"));
					$("#dialog").data("category", $(this).data("category"));
					<%--
					$("#dialog #dialog-save").click(function() {
						$.get('profile/set', {
							'level' : $("#dialog #dialog-value-level").val(),
							'type' : $("#dialog").data("type"),
							'category' : $("#dialog").data("category"),
							'index' : $("#dialog").data("index")
						}, function(data) {
							$("#dialog").modal("hide");
						});
					});
					 --%>
					$("#dialog").modal({});

				});

	});
</script>

</head>
<body>
<div class="modal fade" id="dialog" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
  <div class="modal-dialog">
    <div class="modal-content">
      <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">&times;</span><span class="sr-only">Close</span></button>
        <h4 class="modal-title" id="myModalLabel">Problem details</h4>
      </div>
      <div class="modal-body">
		<div class="row">
			<div id="dialog-description" class="col-md-12 well"></div>
		</div>
        <div class="row">
			<div id="dialog-value" class="col-md-12 well">
				Current skill level: <select id="dialog-value-level-zeroToThree"
					disabled>
					<option value="0">Completed</option>
					<option value="1">Almost fine</option>
					<option value="2">Needs work</option>
					<option value="3">Needs more work</option>
				</select> <select id="dialog-value-level-binary" disabled>
					<option value="0">Completed</option>
					<option value="1">Needs work</option>
				</select>
			</div>
		</div>

      </div>
      <div class="modal-footer">
		<%-- we should not allow direct altering of profile entries
			<sec:authorize ifAnyGranted="PERMISSION_ADMIN,PERMISSION_EXPERT">
				<button type="submit" class="btn btn-primary" id="dialog-save">Save</button>
			</sec:authorize>
        <button type="button" class="btn btn-primary">Save changes</button>
		 --%>
        <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
      </div>
    </div>
  </div>
</div>

	<div id="wrapper">

		<jsp:include page="../includes/navigation.jsp"></jsp:include>

		<div id="page-wrapper">
			<div class="row">
				<div class="col-lg-10">
					<h1 class="">Student profile: ${student.username}</h1>
				</div>
				<div class="col-lg-2">
					<ul class="nav navbar-nav">
						<li class="dropdown"><a class="dropdown-toggle"
							data-toggle="dropdown" href="#">Profile type <span
								class="caret"></span>
						</a>
							<ul class="dropdown-menu" role="menu">
								<li><a href="#" id="independentMenu">Independent</a></li>
								<li><a href="#" id="supervisedMenu">With teacher</a></li>
							</ul></li>
					</ul>
				</div>
			</div>

			<div id="independent-profile" class="row">
				<div class="panel panel-default">
					<div class="panel-heading">Profile "Independent"</div>
					<div class="panel-body">
						<c:forEach var="row"
							items="${profile.userProblems.problems.problemsIndex}"
							varStatus="statusRow">
							<div class="row">
								<div class="col-md-6">${row.uri}</div>
								<div class="col-md-6">
									<div class="progress"
										data-index="${statusRow.index}" data-type="independent"
										data-severitytype="${row.severityType }">
										
										
										<div class="progress-bar" role="progressbar" 
											aria-valuenow="${(profile.userProblems.userSeverities.systemIndices[statusRow.index] / fn:length(profile.userProblems.userSeverities.severities[statusRow.index])) * 100 + 90}"
											aria-valuemin="0" aria-valuemax="100">
											<span class="" >${profile.userProblems.userSeverities.systemIndices[statusRow.index]}
											out of
											${fn:length(profile.userProblems.userSeverities.severities[statusRow.index])}
											skills accomplished</span>
											</div>
										
									</div>
								</div>
							</div>
							<div id="problemsblock${statusRow.index}-independent"
								class="row problemsblock" style="display: none">
								<c:forEach var="col"
									items="${profile.userProblems.userSeverities.severities[statusRow.index]}"
									varStatus="statusCol">
									<div class="col-xs-10 col-md-2 problemdescription"
										data-problem="${profile.userProblems.problems.problems[statusRow.index][statusCol.index]}"
										data-category="${statusRow.index }"
										data-index="${statusCol.index }" data-level="${col }"
										data-severitytype="${row.severityType }"
										data-type="independent">

										<div class="problemstatus-${row.severityType }-${col}"></div>
										<div class="problemdescription-text">
											<c:forEach var="desc"
												items="${profile.userProblems.problems.problems[statusRow.index][statusCol.index].descriptions}"
												varStatus="statusDesc">${desc} (${col })</c:forEach>
										</div>
									</div>

								</c:forEach>
							</div>
						</c:forEach>
					</div>
				</div>
			</div>
			<div id="supervised-profile" class="row" style="display: none">
				<div class="panel panel-default">
					<div class="panel-heading">Profile "With teacher"</div>
					<div class="panel-body">
						<c:forEach var="row"
							items="${profile.userProblems.problems.problemsIndex}"
							varStatus="statusRow">
							<div class="row">
								<div class="col-md-6">${row.uri}</div>
								<div class="col-md-6">
									<div class="progress"
										data-index="${statusRow.index}" data-type="supervised"
										data-severitytype="${row.severityType }">
										
										
										<div class="progress-bar" role="progressbar" 
											aria-valuenow="${(profile.userProblems.userSeverities.systemIndices[statusRow.index] / fn:length(profile.userProblems.userSeverities.severities[statusRow.index])) * 100 + 90}"
											aria-valuemin="0" aria-valuemax="100">
											<span class="" >${profile.userProblems.userSeverities.systemIndices[statusRow.index]}
											out of
											${fn:length(profile.userProblems.userSeverities.severities[statusRow.index])}
											skills accomplished</span>
											</div>
										
									</div>
								</div>
							</div>
							<div id="problemsblock${statusRow.index}-supervised"
								class="row problemsblock" style="display: none">
								<c:forEach var="col"
									items="${profile.userProblems.userSeverities.severities[statusRow.index]}"
									varStatus="statusCol">
									<div class="col-xs-10 col-md-2 problemdescription"
										data-problem="${profile.userProblems.problems.problems[statusRow.index][statusCol.index]}"
										data-category="${statusRow.index }"
										data-index="${statusCol.index }" data-level="${col }"
										data-severitytype="${row.severityType }"
										data-type="supervised">

										<div class="problemstatus-${row.severityType }-${col}"></div>
										<div class="problemdescription-text">
											<c:forEach var="desc"
												items="${profile.userProblems.problems.problems[statusRow.index][statusCol.index].descriptions}"
												varStatus="statusDesc">${desc} (${col })</c:forEach>
										</div>
									</div>

								</c:forEach>
							</div>
						</c:forEach>
					</div>
				</div>
			</div>

			<div class="row">
				<div class="panel panel-default">
					<div class="panel-heading">Preferences</div>
					<div class="panel-body">
						<form:form
							action="${pageContext.servletContext.contextPath}/apps/users/${student.id}/profile"
							method="POST" modelAttribute="profile" class="form-horizontal">

							<form:hidden path="userProblems.userSeverities.length" />
							<div class="form-group">
								<label class="col-sm-2 control-label">Tricky Words</label>
								<div class="col-sm-10">
									<form:input path="userProblems.trickyWords" />
								</div>
							</div>
							<div class="form-group">
								<label class="col-sm-2 control-label">Font size</label>
								<div class="col-sm-10">
									<form:input path="preferences.fontSize" />
								</div>
							</div>
							<form:hidden path="language" />

							<sec:authorize ifAnyGranted="PERMISSION_ADMIN,PERMISSION_EXPERT,PERMISSION_TEACHER">
								<button type="submit" class="btn btn-primary">Save</button>
							</sec:authorize>
						</form:form>
					</div>
				</div>
			</div>
		</div>
	</div>
</body>
</html>
