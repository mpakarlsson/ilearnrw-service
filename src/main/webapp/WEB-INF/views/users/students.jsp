<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!DOCTYPE html>
<html>

<head>

<meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">

<title>List of Students</title>

<jsp:include page="../includes/includes.jsp"></jsp:include>
<script>
	$(function() {

		$('#usertable thead th')
				.each(
						function() {
							var title = $('#usertable thead th').eq(
									$(this).index()).text();
							$(this)
									.html(
											'<div class=""><input type="text" style="width:100%;font-weight:normal;font-size:smaller;margin-bottom:5px;border:1px solid lightgray" placeholder="Search '
													+ title + '" /></div>'+title+'');
						});

		var table = $("#usertable").DataTable({
			'paging' : false,
			'searching' : false
		});

		// Apply the search

		$('#usertable thead th input').each(function(idx) {
			$(this).on('keyup change', function() {
				table.column(idx).search(this.value).draw();
			});
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
					<h1 class="page-header">List of Students</h1>
				</div>
			</div>
			<div class="row col-xs-12">
				<div class="panel panel-default">
					<div class="panel-heading">These are your assigned students</div>
					<div class="panel-body">
						<table id="usertable"
							class="table table-striped table-bordered table-condensed table-hover">
							<thead>
								<tr>
									<th>Name</th>
									<th>Birthdate</th>
									<th>Gender</th>
									<th>School</th>
									<th>Classroom</th>
									<th>Profile</th>
								</tr>
							</thead>
							<tbody>
								<c:forEach items="${students}" var="o">
									<tr class="list-users">
										<td>${o.user.username}</td>
										<td><fmt:formatDate value="${o.user.birthdate}"
												var="formattedDate" type="date" pattern="dd.MM.yyyy" />
											${formattedDate}</td>
										<td>${o.user.gender}</td>
										<td>${o.studentDetails.school}</td>
										<td>${o.studentDetails.classRoom}</td>
										<td><a class="btn btn-default btn-xs"
											href="${pageContext.request.contextPath}/apps/users/${o.user.id}/profile">Profile</a>
										</td>
									</tr>
								</c:forEach>
							</tbody>
						</table>
					</div>
				</div>
			</div>
		</div>

	</div>

</body>