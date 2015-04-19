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
		table.row.add([
				value.school,
				value.classroom,
				value.username,
				value.timeSpentPlaying,
				value.daysPlayed,
				value.activitiesPlayed,
				value.skillsPracticed,
				value.wordsSeen,
				value.changesToProfile,
				value.successRate
		]).draw();
	});
};