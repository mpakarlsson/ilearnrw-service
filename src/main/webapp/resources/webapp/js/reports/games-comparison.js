var jqueryUrl = url("/apps/jquery/admin/tables/games-comparison");
var fillHeader = function() {
	$("#usertable thead").append($("<tr>")
			.append($('<th>').append("School"))
			.append($('<th>').append("Class"))
			.append($('<th>').append("Student name"))
			.append($('<th>').append("Time spent playing"))
			.append($('<th>').append("Days played"))
			.append($('<th>').append("Activities played"))
			.append($('<th>').append("Skills practiced"))
			.append($('<th>').append("Words seen"))
			.append($('<th>').append("Changes to profile"))
			.append($('<th>').append("Success rate"))
	);
};
var successFillTable = function(data) {
	$("#usertable tbody").empty();
	$.each(data, function(index, value) {
		$("#usertable tbody").append($('<tr>')
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
};