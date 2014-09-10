var ilearnurl = 'http://localhost:8080/test/apps';
function addNewTest(){
	name = document.getElementById('newFileName').value.trim();
	var reg = new RegExp('^[a-zA-Z][a-zA-Z0-9\_]+$');
	if (reg.test(name)){
		var res = httpPost(ilearnurl+"/updatetests?testName="+name+"&action=addTest", null);
		filenamesList(res);
	}
};

function setDefaultTest(name){
	var reg = new RegExp('^[a-zA-Z][a-zA-Z0-9\_]+$');
	if (reg.test(name)){
		var res = httpPost(ilearnurl+"/updatetests?testName="+name+"&action=defaultTest", null);
		filenamesList(res);
	}
};

function deleteTest(name){
	var reg = new RegExp('^[a-zA-Z][a-zA-Z0-9\_]+$');
	if (reg.test(name)){
		var res = httpPost(ilearnurl+"/updatetests?testName="+name+"&action=deleteTest", null);
		filenamesList(res);
	}
};

function saveQuestion(fname, cluster) {
	data = getNewQuestionData();
	id = httpPost(ilearnurl+"/updatecluster?fname="+fname+"&cluster="+cluster+"&action=add", JSON.stringify(data));
	data.id = id;
	appendQuestion('\''+fname+'\'', cluster, data);
	clearAddQuestionField();
};

function updateQuestion(fname, cluster, id) {
	data = getEditedQuestionData(id);
	id = httpPost(ilearnurl+"/updatecluster?fname="+fname+"&cluster="+cluster+"&id="+id+"&action=update", JSON.stringify(data));
	data.id = id;
};

function deleteQuestion(fname, cluster, id) {
	respId = httpPost(ilearnurl+"/updatecluster?fname="+fname+"&cluster="+cluster+"&action=delete"+"&id="+id, '{}');
	if (respId == id){
		var el = document.getElementById('question'+id);
		el.parentNode.removeChild( el );
	}
	//document.getElementById("\""+id+"\"").remove();
};


// test page

function sendStudentsAnswers(user, userId) {
	var array = [];
	var elements = document.getElementsByClassName("word");
	for(var i = 0; i < elements.length; i++) {
		var current = elements[i];
		var ok = isCorrect(current);
		if (isDisplayed(current) != 1 || ok == -1){
			continue;
		}
		var data = {
				word: current.dataset.word,
				cluster: current.dataset.cluster,
				correct: (ok == 1)
		};
		
		var log = {
				username: user,
				applicationId: "PROFILE_SETUP",
				tag: "WORD_DISPLAYED",
				word: current.dataset.word,
				problem_category: -1,
				problem_index: -1,
				duration: 0,
				level: "cluster "+current.dataset.cluster,
				mode: "EVALUATION_MODE",
				value: current.dataset.cluster,
				timestamp: new Date().getTime(),
		};
		array.push(log);
		
		var log = {
				username: user,
				applicationId: "PROFILE_SETUP",
				tag: (ok == 1)?"WORD_SUCCESS":"WORD_FAILED",
				word: current.dataset.word,
				problem_category: -1,
				problem_index: -1,
				duration: 0,
				level: "cluster "+current.dataset.cluster,
				mode: "EVALUATION_MODE",
				value: current.dataset.cluster,
				timestamp: new Date().getTime(),
		};
		array.push(log);
	}
	httpPost(ilearnurl+"/setupstudent?userId="+userId, JSON.stringify(array));
	alert('Data Sent!');
};