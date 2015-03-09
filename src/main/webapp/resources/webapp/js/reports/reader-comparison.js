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
		$("#usertable tbody").append($('<tr>')
				.append($('<td>').append(value.school))
				.append($('<td>').append(value.classroom))
				.append($('<td>').append(value.username))
				.append($('<td>').append(value.timeSpentReading))
				.append($('<td>').append(value.daysRead))
				.append($('<td>').append(value.textsRead))
				.append($('<td>').append(value.settingsUsed))
				.append($('<td>').append(value.textToSpeechUsed))
		);
	});
};