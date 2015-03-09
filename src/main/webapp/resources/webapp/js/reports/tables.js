var plotData;
$.postJSON = function(url, data, callback, errorf) {
    return jQuery.ajax({
    headers: { 
        'Accept': 'application/json',
        'Content-Type': 'application/json' 
    },
    'type': 'POST',
    'url': url,
    'data': data,
    'dataType': 'json',
    'success': callback,
    'error': errorf
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
	/*
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
	*/
	$("#students,#date,#startDate,#endDate").change(function()
	{
		var filter = getBreakdownFilter();
		var table2 = $("#usertable tbody");
		table2.empty();
		table2.html("Loading...");
		$.postJSON(jqueryUrl, JSON.stringify(filter), function(data) {
			table2.empty();
			$.each(data, function(index, value) {
				table2.append($('<tr>')
						.append($('<td>').append(value.school))
						.append($('<td>').append(value.classroom))
						.append($('<td>').append(value.username))
						.append($('<td>').append(value.timeSpentPlaying))
						.append($('<td>').append(value.daysPlayed))
						.append($('<td>').append(value.activitiesPlayed))
						.append($('<td>').append(value.skillsPracticed))
						.append($('<td>').append(value.wordsSeen))
						.append($('<td>').append(value.changesToProfile))
						.append($('<td>').append(value.successRate))
				);
			});
	    },
	    function(jqXHR, textStatus, errorThrown) {
			table2.html('Error: ' + jqXHR.status + ' ' + textStatus + ' ' + errorThrown);
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
});