var plotData;
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
function populate(data, selectId)
{
	$select = $(selectId);
	$select.find('option').remove();
	$.each(data, function(key, value) {
		var name = value.name;
		if (name == null)
			name = value.username;
	    $('<option>').val(JSON.stringify(value)).text(name).appendTo($select);
	});
	$select.trigger("change");
}
function getBreakdownFilter()
{
	var filter = {};
	filter.dateType = JSON.parse($("#date").val());
	filter.startDate = $("#startDate").val();
	filter.endDate = $("#endDate").val();
	filter.school = JSON.parse($("#schools").val());
	filter.classroom = JSON.parse($("#classrooms").val());
	filter.student = JSON.parse($("#students").val());
	return filter;
}
function refreshPlot(data) {
	$.plot($("#flot-pie-chart"), data, {
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
}
$(function() {
	$("#classrooms").change(function() {
		$.postJSON( url("/apps/jquery/admin/students"), $("#classrooms").val(), function( data ) {
			populate(data, "#students");
		});
	});
	$("#schools").change(function() {
		$.postJSON( url("/apps/jquery/admin/classrooms"), $("#schools").val(), function( data ) {
			populate(data, "#classrooms");
		});
	});
	$.get( url("/apps/jquery/admin/schools"), function( data ) {
		populate(data, "#schools");
	});
	$(".custom-date").hide();
	$("#startDate").datepicker({
		maxDate : 0,
		changeMonth : true,
		changeYear : true,
		dateFormat : "dd.mm.yy"
	});
	$("#endDate").datepicker({
		maxDate : 0,
		changeMonth : true,
		changeYear : true,
		dateFormat : "dd.mm.yy"
	});
	
	if (isOverview)
	{
		$("#flot-panel").hide();
		$("#overview-panel").show();
	}
	
	$("#students,#date,#startDate,#endDate").change(function()
	{
		var filter = getBreakdownFilter();
		$.postJSON(breakdownUrl, JSON.stringify(filter), function(data) {
			if (!isOverview)
			{
				plotData = data;
				if (data.length > 0)
				{
					$("#flot-pie-chart").show();
					$("#flot-unavailable").hide();
					refreshPlot(data);
				}
				else
				{
					$("#flot-unavailable").show();
					$("#flot-pie-chart").hide();
				}
			}
			else
			{
				$("#overview-skills ul li").remove();
				$.each(data.skillsWorkedOn, function(key, value) {
						$("#overview-skills ul").append($('<li>').append(value));
				});
				$("#overview-time-spent").val(data.timeSpent);
				$("#overview-number-of-activities").val(data.numberOfActivities);
				$("#overview-success-rate").val(data.successRate);
			}
	    });
	});
	$("#date").change(function()
	{
		if ($(this).val() == 4) //Custom range
		{
			$(".custom-date").show();
		}
		else
		{
			$(".custom-date").hide();
		}
	});
	if (!isOverview)
	{
		function pieClick(event, pos, obj)
		{
			if (!obj)
		        return;
			$.postJSON(detailsUrl, 
					JSON.stringify({ filter:getBreakdownFilter(), dataPoint:plotData[obj.seriesIndex]}),
					function(data){
				$("#flot-skill-timeSpent").val(data.timeSpent + " sec.");
				$("#flot-skill-successRate").val(data.successRate);
				$("#flot-skill-correctAnswers").val(data.correctAnswers);
				$("#flot-skill-incorrectAnswers").val(data.incorrectAnswers);
				$("#flot-skill-nrOfApps").val(data.nrOfApps);
				$("#flot-skill").show();
			});
			$("#flot-click-title").html(obj.series.label);
			$("#flot-click-panel").show();
			$('html,body').animate({
		        scrollTop: $("#flot-click-panel").offset().top},
		        'slow');
		}
	    $("#flot-pie-chart").bind("plotclick", pieClick);
	}
});