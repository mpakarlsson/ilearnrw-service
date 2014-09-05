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

</head>

<body>

	<div id="wrapper">

		<jsp:include page="../includes/navigation.jsp"></jsp:include>

		<div id="page-wrapper">
			<div class="row">
				<div class="col-lg-12">
					<h1 class="page-header">Manage students</h1>
				</div>
			</div>
			<table class="table table-striped table-bordered table-condensed">
				<thead>
					<tr>
						<th>Username</th>
						<th></th>
					</tr>
				</thead>
				<tbody>
					<c:forEach items="${students}" var="o">
						<tr class="list-users">
							<td>${o.username}</td>
							<td>
								<div class="btn-group">
									<a class="btn btn-default btn-xs dropdown-toggle"
										data-toggle="dropdown" href="#">Actions <span
										class="caret"></span></a>
									<ul class="dropdown-menu pull-right">
										<li><a href="${pageContext.request.contextPath}/apps/users/${o.id}/profile"><i class="icon-user"></i>
												Edit Profile</a></li>
										<li><a href="${pageContext.request.contextPath}/apps/users/${o.id}/logs/page/1"><i
												class="icon-user"></i> View Logs</a></li>
										<li><a href="${pageContext.request.contextPath}/apps/users/${o.id}/stats"><i
												class="icon-user"></i> View Statistics</a></li>
									</ul>
								</div>
							</td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
		</div>

	</div>

</body>