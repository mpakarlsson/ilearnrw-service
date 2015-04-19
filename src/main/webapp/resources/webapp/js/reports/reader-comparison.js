var jqueryUrl = url("/apps/jquery/admin/tables/reader-comparison");
var fillHeader = function() {
	$("#usertable thead").append($("<tr>")
			.append($('<th>').append("School"))
			.append($('<th>').append("Class"))
			.append($('<th>').append("Student name"))
			.append($('<th>').append("Time spent reading"))
			.append($('<th>').append("Days read"))
			.append($('<th>').append("Texts read"))
			.append($('<th>').append("Settings used"))
			.append($('<th>').append("Text to speech used"))
	);
};
var successFillTable = function(data) {
	$("#usertable tbody").empty();
	$.each(data, function(index, value) {
		table.row.add([
				value.school,
				value.classroom,
				value.username,
				value.timeSpentReading,
				value.daysRead,
				value.textsRead,
				value.settingsUsed,
				value.textToSpeechUsed
				]).draw();
	});
};