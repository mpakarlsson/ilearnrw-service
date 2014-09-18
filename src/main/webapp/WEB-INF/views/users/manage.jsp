<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>

<head>

<meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">

<title>Manage users</title>

<jsp:include page="../includes/includes.jsp"></jsp:include>
<script>
	$(function() {
		$('.fa').tooltip();
		$("#usertable").dataTable();
	});
</script>
</head>

<body>

	<div id="wrapper">

		<jsp:include page="../includes/navigation.jsp"></jsp:include>

		<div id="page-wrapper">
			<div class="row">
				<div class="col-lg-12">
					<h1 class="page-header">Manage users</h1>
				</div>
			</div>
			<div class="row col-xs-12">
				<div class="panel panel-default">
					<div class="panel-heading">Hover over the icons to see the
				user's role. Teachers can be assigned students in the actions menu.
				Experts can be assigned teachers also in the actions menu.</div>
					<div class="panel-body">
						<table id="usertable"
							class="table table-striped table-bordered table-condensed table-hover">
							<thead>
								<tr>
									<th>Username</th>
									<th>Status</th>
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
														<li><a
															href="${pageContext.request.contextPath}/apps/users/${o.user.id}/delete"><i
																class="fa fa-trash-o fa-fw"></i> Delete</a></li>
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

</body>