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
	fillHeader();
	$("#students,#date,#startDate,#endDate").change(function()
	{
		var filter = getBreakdownFilter();
		$("#usertable tbody").empty();
		$("#usertable tbody").html("Loading...");
		$.postJSON(jqueryUrl, JSON.stringify(filter), successFillTable,
	    function(jqXHR, textStatus, errorThrown) {
			$("#usertable tbody").html('Error: ' + jqXHR.status + ' ' + textStatus + ' ' + errorThrown);
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