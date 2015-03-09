<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>

<head>

<meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">

<title><c:out value="Reports: ${title}"></c:out></title>

<jsp:include page="../includes/includes.jsp"></jsp:include>

<script type="text/javascript">
	function url(link) {
		return "${pageContext.request.contextPath}" + link;
	}
</script>
<script
	src="${pageContext.request.contextPath}/apps/resources/webapp/js/reports/<c:out value="${js}"></c:out>"></script>
<script
	src="${pageContext.request.contextPath}/apps/resources/webapp/js/reports/tables.js"></script>

</head>
<body>

	<div id="wrapper">

		<jsp:include page="../includes/navigation.jsp"></jsp:include>

		<div id="page-wrapper">
			<div class="row">
				<div class="col-lg-12">
					<h1 class="page-header">
						Reports:
						<c:out value="${title}"></c:out>
					</h1>
				</div>
			</div>
			<div class="row">
				<div class="col-lg-4">
					<div class="panel panel-default">
						<div class="panel-heading">Filter</div>
						<div class="panel-body">
							<form id="filter-form" class="form-horizontal">
								<div class="form-group">
									<label class="col-sm-2 control-label">School</label>
									<div class="col-sm-10 ">
										<select class="col-sm-10 form-control" id="schools"></select>
									</div>
								</div>
								<div class="form-group">
									<label class="col-sm-2 control-label">Class</label>
									<div class="col-sm-10 ">
										<select class="col-sm-10 form-control" id="classrooms"></select>
									</div>
								</div>
								<div class="form-group">
									<label class="col-sm-2 control-label">Student</label>
									<div class="col-sm-10 ">
										<select class="col-sm-10 form-control" id="students"></select>
									</div>
								</div>
								<div class="form-group">
									<label class="col-sm-2 control-label">Date range</label>
									<div class="col-sm-10 ">
										<select class="col-sm-10 form-control" id="date">
											<option value="0">Total</option>
											<option value="1">Today</option>
											<option value="2" selected="selected">This week</option>
											<option value="3">This month</option>
											<option value="4">Custom range</option>
										</select>
									</div>
								</div>
								<div class="form-group custom-date">
									<label class="col-sm-2 control-label">Start date</label>
									<div class="col-sm-10 ">
										<input type="text" class="col-sm-10 form-control"
											id="startDate" placeholder="Click to select the date"></input>
									</div>
								</div>
								<div class="form-group custom-date">
									<label class="col-sm-2 control-label">End date</label>
									<div class="col-sm-10 ">
										<input type="text" class="col-sm-10 form-control" id="endDate"
											placeholder="Click to select the date"></input>
									</div>
								</div>
							</form>
						</div>
					</div>
				</div>
				<div class="row col-xs-12">
					<div class="panel panel-default">
						<div class="panel-heading"><c:out value="${title}"></c:out> table</div>
						<div class="panel-body">
							<table id="usertable"
								class="table table-striped table-bordered table-condensed table-hover">
								<thead>
									<tr>
										<th>School</th>
										<th>Class</th>
										<th>Student name</th>
										<th>Time spent playing</th>
										<th>Days played</th>
										<th>Activities played</th>
										<th>Skills practiced</th>
										<th>Words seen</th>
										<th>Changes to profile</th>
										<th>Success rate</th>
									</tr>
								</thead>
								<tbody>
								</tbody>
							</table>
						</div>
					</div>
				</div>
			</div>
		</div>

	</div>

</body>