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

</head>

<script>
$.postJSON = function(url, data, callback) {
    return jQuery.ajax({
    headers: { 
        'Accept': 'application/json',
        'Content-Type': 'application/json' 
    },
    'type': 'POST',
    'url': url,
    'data': data,
    'dataType': 'json',
    'success': callback
    });
};
</script>

<script type="text/javascript">
$(function() {
	function populate(data, selectId)
	{
		$select = $(selectId);
		$select.find('option').remove();
		$.each(data, function(key, value) {              
		    $('<option>').val(value).text(value).appendTo($select);
		});
		$select.trigger("change");
	}
	$("#classrooms").change(function() {
		$.postJSON( "${pageContext.request.contextPath}/apps/jquery/admin/students", $("#classrooms").val(), function( data ) {
			populate(data, "#students");
		});
	});
	$("#schools").change(function() {
		$.postJSON( "${pageContext.request.contextPath}/apps/jquery/admin/classrooms", $("#schools").val(), function( data ) {
			populate(data, "#classrooms");
		});
	});
	$.get( "${pageContext.request.contextPath}/apps/jquery/admin/schools", function( data ) {
		populate(data, "#schools");
	});
});
</script>


<script src="${pageContext.request.contextPath}/apps/resources/libs/sb-admin-v2/js/plugins/flot/excanvas.min.js"></script>
<script src="${pageContext.request.contextPath}/apps/resources/libs/sb-admin-v2/js/plugins/flot/jquery.flot.js"></script>
<script src="${pageContext.request.contextPath}/apps/resources/libs/sb-admin-v2/js/plugins/flot/jquery.flot.pie.js"></script>
<script src="${pageContext.request.contextPath}/apps/resources/libs/sb-admin-v2/js/plugins/flot/jquery.flot.resize.js"></script>
<script src="${pageContext.request.contextPath}/apps/resources/libs/sb-admin-v2/js/plugins/flot/jquery.flot.tooltip.min.js"></script>
<script type="text/javascript">
$(function() {
    $.postJSON("${pageContext.request.contextPath}/apps/jquery/admin/plot/skill/breakdown", "", function(data) {
        var plotObj = $.plot($("#flot-pie-chart"), data, {
            series: {
                pie: {
                    show: true,
                    radius: 1,
                    innerRadius: 0.4,
                    label: {
                        show: true,
                        radius: 0.7,
                        formatter: function labelFormatter(label, series) {
                    		return "<div style='font-size:10pt; font-weight:bold; text-align:center; padding:2px; color:black; pointer-events: none;'>" + label + "<br/>" + Math.round(series.percent) + "%</div>";
                    	},
                    },
                }
            },
            grid: {
                hoverable: true,
                clickable: true
            },
            legend: {
            	show : false
            }
        });
        $("#flot-pie-chart").bind("plotclick", pieClick);
    });
});

function pieClick(event, pos, obj)
{
	if (!obj)
        return;
	$.postJSON("${pageContext.request.contextPath}/apps/jquery/admin/plot/skill/details", obj.series.id, function(data){
		$("#flot-skill-timeSpent").val(data.timeSpent);
		$("#flot-skill-successRate").val(data.successRate);
		$("#flot-skill-correctAnswers").val(data.correctAnswers);
		$("#flot-skill-incorrectAnswers").val(data.incorrectAnswers);
		$("#flot-skill").show();
	});
	$("#flot-click-title").html(obj.series.label);
	$("#flot-click-panel").show();
	$('html,body').animate({
        scrollTop: $("#flot-click-panel").offset().top},
        'slow');
}
</script>

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
			        	<form>
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
							        	<option>Last played</option>
							        	<option>This week</option>
							        	<option>This month</option>
							        	<option>Custom range</option>
							        	<option>Total</option>
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