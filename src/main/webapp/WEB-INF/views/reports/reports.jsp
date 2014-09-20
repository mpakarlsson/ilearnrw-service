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

<script src="${pageContext.request.contextPath}/apps/resources/libs/sb-admin-v2/js/plugins/flot/excanvas.min.js"></script>
<script src="${pageContext.request.contextPath}/apps/resources/libs/sb-admin-v2/js/plugins/flot/jquery.flot.js"></script>
<script src="${pageContext.request.contextPath}/apps/resources/libs/sb-admin-v2/js/plugins/flot/jquery.flot.pie.js"></script>
<script src="${pageContext.request.contextPath}/apps/resources/libs/sb-admin-v2/js/plugins/flot/jquery.flot.resize.js"></script>
<script src="${pageContext.request.contextPath}/apps/resources/libs/sb-admin-v2/js/plugins/flot/jquery.flot.tooltip.min.js"></script>
<script type="text/javascript">
function url(link)
{
	return "${pageContext.request.contextPath}" + link;
}
</script>
<script src="${pageContext.request.contextPath}/apps/resources/webapp/js/reports/<c:out value="${js}"></c:out>"></script>
<script src="${pageContext.request.contextPath}/apps/resources/webapp/js/reports/common.js"></script>

</head>
<body>

	<div id="wrapper">

		<jsp:include page="../includes/navigation.jsp"></jsp:include>

		<div id="page-wrapper">
			<div class="row">
				<div class="col-lg-12">
					<h1 class="page-header">Reports: <c:out value="${title}"></c:out></h1>
				</div>
			</div>
			<div class="row">
			    <div class="panel panel-default">
			        <div class="panel-heading">Filter</div>
			        <div class="panel-body">
			        	<form id="filter-form">
				        	<div class="form-group">
							    <label class="col-sm-2 control-label">Select school</label>
							    <div class="col-sm-10 ">
							        <select class="col-sm-10 form-control" id="schools"></select>
							    </div>
							</div>
				        	<div class="form-group">
							    <label class="col-sm-2 control-label">Select class</label>
							    <div class="col-sm-10 ">
							        <select class="col-sm-10 form-control" id="classrooms"></select>
							    </div>
							</div>
				        	<div class="form-group">
							    <label class="col-sm-2 control-label">Select student</label>
							    <div class="col-sm-10 ">
							        <select class="col-sm-10 form-control" id="students"></select>
							    </div>
							</div>
				        	<div class="form-group">
							    <label class="col-sm-2 control-label">Select date</label>
							    <div class="col-sm-10 ">
							        <select class="col-sm-10 form-control" id="date">
							        	<option value="0">Total</option>
							        	<option value="1">Today</option>
							        	<option value="2">This week</option>
							        	<option value="3">This month</option>
							        	<option value="4">Custom range</option>
							        </select>
							    </div>
							</div>
						</form>
			        </div>
			    </div>
			</div>
			<div class="row">
			    <div class="panel panel-default">
			        <div class="panel-heading"><c:out value="${title}"></c:out></div>
			        <div class="panel-body">
			        	<div class = "row">
							<div id="flot-pie-chart" style="height:500px"></div>
						</div>
						<div class="alert alert-danger" style="display:none" id="flot-unavailable">
                                No data found.
                        </div>
					</div>
				</div>
			</div>
			<div class="row">
			    <div id="flot-click-panel" class="panel panel-default" style="display:none">
			        <div id="flot-click-title" class="panel-heading"></div>
			        <div class="panel-body">
			        	<div class = "row">
							<div id="flot-pie-chart"></div>
						</div>
						<div class="row">
							<div id="flot-click">
								<div class="form-group">
								    <label class="col-sm-2 control-label">Time spent</label>
								    <div class="col-sm-10 ">
								        <input type="text"  class="col-sm-10 form-control" id="flot-skill-timeSpent" disabled></input>
								    </div>
								</div>
					        	<div class="form-group">
								    <label class="col-sm-2 control-label">Success rate</label>
								    <div class="col-sm-10 ">
								        <input type="text"  class="col-sm-10 form-control" id="flot-skill-successRate" disabled></input>
								    </div>
								</div>
					        	<div class="form-group">
								    <label class="col-sm-2 control-label">No. of correct answers</label>
								    <div class="col-sm-10 ">
								        <input type="text"  class="col-sm-10 form-control" id="flot-skill-correctAnswers" disabled></input>
								    </div>
								</div>
								<div class="form-group">
								    <label class="col-sm-2 control-label">No. of incorrect answers</label>
								    <div class="col-sm-10 ">
								        <input type="text" class="col-sm-10 form-control" id="flot-skill-incorrectAnswers" disabled></input>
								    </div>
								</div>
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>

	</div>

</body>