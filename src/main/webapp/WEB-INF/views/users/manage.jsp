<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>

<head>

<meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">

<sec:authorize var="isExpert" ifAnyGranted="PERMISSION_EXPERT"/>

<title>
<c:choose>
<c:when test="${isExpert}">Teachers and Students</c:when>
<c:otherwise>Manage Users</c:otherwise>
</c:choose></title>

<jsp:include page="../includes/includes.jsp"></jsp:include>
<script>
	$(function() {
		$('.fa').tooltip();

		$('#usertable thead th')
				.each(
						function() {
							var title = $('#usertable thead th').eq(
									$(this).index()).text();
							$(this)
									.html(
											'<div class=""><input type="text" style="width:100%;font-weight:normal;font-size:smaller;margin-bottom:5px;border:1px solid lightgray" placeholder="Search '
													+ title
													+ '" /></div>'
													+ title + '');
						});

		var table = $("#usertable").DataTable({
			'paging' : false,
			'searching' : true
		});

		// Apply the search

		$('#usertable thead th input').each(function(idx) {
			$(this).on('keyup change', function() {
				table.column(idx).search(this.value).draw();
			});
		});

		$(".deleteLink").click(function(e) {
			e.preventDefault();
			var targetUrl = $(this).attr("href");
			$("#deletebtn").click(function() {
				window.location.href = targetUrl;
				$("dialog-confirm").modal("hide");
				
			});
			$("#dialog-confirm").modal({});
		});

	});
</script>
</head>

<body>

	<div id="wrapper">

		<jsp:include page="../includes/navigation.jsp"></jsp:include>

		<div id="page-wrapper">
			<div class="row">
				<div class="col-lg-12">
					<h1 class="page-header">
					<c:choose>
					<c:when test="${isExpert}">Teachers and Students</c:when>
					<c:otherwise>Manage Users</c:otherwise>
					</c:choose>
					</h1>
				</div>
			</div>
			<div class="row col-xs-12">
				<div class="panel panel-default">
					<div class="panel-heading">Hover over the icons to see the
						user's role. Teachers can be assigned students in the actions
						menu. Experts can be assigned teachers also in the actions menu.</div>
					<div class="panel-body">
						<table id="usertable"
							class="table table-striped table-bordered table-condensed table-hover">
							<thead>
								<tr>
									<th>Username</th>
									<th style="width: 60px">Status</th>
									<th>Role</th>
									<th>Lang.</th>
									<th>Gender</th>
									<th>School</th>
									<th>Class</th>
									<th>Teacher</th>
									<th>Actions</th>
								</tr>
							</thead>
							<tbody>
								<c:forEach items="${users}" var="o">
									<tr class="list-users">
										<td><c:choose>
												<c:when test="${o.role eq 'none'}">
													<i class="fa fa-question fa-fw" title="What?"></i>
												</c:when>
												<c:when test="${o.role eq 'ROLE_ADMIN'}">
													<i class="fa fa-cogs fa-fw" title="Admin"></i>
												</c:when>
												<c:when test="${o.role eq 'ROLE_EXPERT'}">
													<i class="fa fa-coffee fa-fw" title="Expert"></i>
												</c:when>
												<c:when test="${o.role eq 'ROLE_TEACHER'}">
													<i class="fa fa-book fa-fw" title="Teacher"></i>
												</c:when>
												<c:when test="${o.role eq 'ROLE_STUDENT'}">
													<i class="fa fa-star fa-fw" title="Student"></i>
												</c:when>
												<c:otherwise>
													<i class="fa fa-exclamation fa-fw" title="This is a bug"></i>
												</c:otherwise>
											</c:choose> ${o.user.username}</td>
										<c:choose>
											<c:when test="${o.user.enabled}">
												<td><span class="label label-success"> Active</span></td>
											</c:when>
											<c:otherwise>
												<td><span class="label label-danger"> Inactive</span></td>
											</c:otherwise>
										</c:choose>
										<td>${o.role}</td>
										<td>${o.user.language}</td>
										<td>${o.user.gender}</td>
										<td>${o.studentDetails.school}</td>
										<td>${o.studentDetails.classRoom}</td>
										<td><c:set var="foundTeacherAsString">${o.studentDetails.teacherId}</c:set>
											${teachersList[foundTeacherAsString]}</td>
										<td>
											<div class="btn-group">
												<a class="btn btn-default btn-xs dropdown-toggle"
													data-toggle="dropdown" href="#">Actions <span
													class="caret"></span></a>
												<ul class="dropdown-menu pull-right">
													<c:choose>
														<%--
														<c:when test="${o.role eq 'ROLE_EXPERT'}">
															<sec:authorize ifAnyGranted="PERMISSION_ADMIN">
																<li><a
																	href="${pageContext.request.contextPath}/apps/experts/${o.user.id}/assign"><i
																		class="fa fa-briefcase fa-fw"></i> Assign teachers to
																		this expert</a></li>
															</sec:authorize>
														</c:when>
														 
														<c:when test="${o.role eq 'ROLE_TEACHER'}">
															<sec:authorize
																ifAnyGranted="PERMISSION_ADMIN,PERMISSION_EXPERT">
																<li><a
																	href="${pageContext.request.contextPath}/apps/teachers/${o.user.id}/assign"><i
																		class="fa fa-plus-square fa-fw"></i> Assign students
																		to this teacher</a></li>
															</sec:authorize>
														</c:when>
														--%>
														<c:when test="${o.role eq 'ROLE_STUDENT'}">
															<sec:authorize
																ifAnyGranted="PERMISSION_EXPERT,PERMISSION_TEACHER">
																<li><a
																	href="${pageContext.request.contextPath}/apps/${o.user.id}/screeningtest"><i
																		class="fa fa-location-arrow fa-fw"></i> Screening Test</a></li>
															</sec:authorize>
															<li><a
																href="${pageContext.request.contextPath}/apps/users/${o.user.id}/profile"><i
																	class="fa fa-user fa-fw"></i> Profile</a></li>
														</c:when>
													</c:choose>
													<li><a
														href="${pageContext.request.contextPath}/apps/users/${o.user.id}/edit"><i
															class="fa fa-pencil fa-fw"></i> Edit</a></li>
													<sec:authorize
														ifAnyGranted="PERMISSION_ADMIN,PERMISSION_EXPERT">
														<li><a class="deleteLink"
															href="${pageContext.request.contextPath}/apps/users/${o.user.id}/delete"><i
																class="fa fa-trash-o fa-fw "></i> Delete</a></li>
													</sec:authorize>
													<li><a
														href="${pageContext.request.contextPath}/apps/users/${o.user.id}/logs/page/1"><i
															class="fa fa-file-text-o fa-fw"></i> View Logs</a></li>
												</ul>
											</div>
										</td>
									</tr>
								</c:forEach>
							</tbody>
						</table>
					</div>
				</div>
			</div>
			<sec:authorize ifAnyGranted="PERMISSION_ADMIN,PERMISSION_EXPERT">
				<a class="btn btn-success"
					href="${pageContext.request.contextPath}/apps/users/new"> New
					User </a>
			</sec:authorize>
		</div>

	</div>
<div id="dialog-confirm" class="modal fade bs-example-modal-sm" tabindex="-1" role="dialog" aria-labelledby="mySmallModalLabel" aria-hidden="true">
  <div class="modal-dialog modal-sm">
    <div class="modal-content">
		<div class="modal-header">
          <button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">×</span><span class="sr-only">Close</span></button>
          <h4 class="modal-title" id="mySmallModalLabel">Delete?</h4>
        </div>
        <div class="modal-body">
        <p>
			<span class="ui-icon ui-icon-alert"
				style="float: left; margin: 0 7px 20px 0;"></span>Are you sure you
			want to delete this item?
		</p>
		<button type="button" id="deletebtn" class="btn btn-primary" data-dismiss="modal">Delete</button>
        <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
		
        </div>
    </div>
  </div>
</div>
	
</body>