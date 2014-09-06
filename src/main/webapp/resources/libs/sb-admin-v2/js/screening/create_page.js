var lastItemPosition = 0;
function loadClusterQuestions(fname, cluster, json) {
	obj = JSON.parse(json);
	for (var i=0; i<obj.length; i++){
		appendQuestion(fname, cluster, obj[i]);
	}
};

function appendQuestion(fname, cluster, obj) {
	lastItemPosition++;
	var element = document.createElement('div');
	element.setAttribute("id", 'question'+obj.id);
	element.setAttribute('class', 'question_box');
	element.innerHTML = '<label>Question</label>';
	var str = '';
	for (var j=0; j<obj.relatedWords.length-1; j++){
		str = str+obj.relatedWords[j]+', ';
	}
	if (obj.relatedWords.length>0)
		str = str+obj.relatedWords[obj.relatedWords.length-1];
	element.innerHTML = '<h4>Question #'+lastItemPosition+'</h4><textarea id="the_question'+obj.id+'" disabled>'+
	obj.question+'</textarea><h4>Related Words</h4><textarea id="the_words'+obj.id+'" disabled>'+str+'</textarea>'+
	'<button type="button" onclick="deleteQuestion('+fname.toString()+', '+cluster+', '+obj.id+')">Delete</button>'+
	'<button type="button" onclick="switchButtonState(this, '+fname+', '+cluster+', '+obj.id+')">Edit</button>';
	document.getElementById('questionsListDiv').appendChild(element);
};

function switchButtonState(button, fname, cluster, id){
	if (button.textContent == 'Edit'){
		document.getElementById('the_question'+id).disabled = false;
		document.getElementById('the_words'+id).disabled = false;
		button.textContent = 'Done';
	}
	else{
		updateQuestion(fname, cluster, id);
		document.getElementById('the_question'+id).disabled = true;
		document.getElementById('the_words'+id).disabled = true;
		button.textContent = 'Edit';
	}
}

function loadAddQuestionField(fname, cluster) {
	var element = document.createElement('div');
	element.setAttribute("id", 'addquestion');
	element.setAttribute('class', 'question_box');
	element.innerHTML = 'Question: <br><textarea name="vrow" id="newQuestion"></textarea>'+
	   '<p>Click the button to change the contents of the text area.</p><textarea name="vrow" id="newRelatedWords"></textarea>'+
	   '<button type="button" onclick="saveQuestion('+fname.toString()+', '+cluster+')">Add</button>';
	document.getElementById('addQuestionsDiv').appendChild(element);
};

function saveQuestion(fname, cluster) {
	data = getNewQuestionData();
	id = httpPost("http://localhost:8080/test/apps/updatecluster?fname="+fname+"&cluster="+cluster+"&action=add", JSON.stringify(data));
	data.id = id;
	appendQuestion(fname, cluster, data);
	clearAddQuestionField();
};

function updateQuestion(fname, cluster, id) {
	data = getEditedQuestionData(id);
	id = httpPost("http://localhost:8080/test/apps/updatecluster?fname="+fname+"&cluster="+cluster+"&id="+id+"&action=update", JSON.stringify(data));
	data.id = id;
};

function deleteQuestion(fname, cluster, id) {
	data = getNewQuestionData();
	respId = httpPost("http://localhost:8080/test/apps/updatecluster?fname="+fname+"&cluster="+cluster+"&action=delete"+"&id="+id, JSON.stringify(data));
	if (respId == id){
		var el = document.getElementById('question'+id);
	el.parentNode.removeChild( el );
	}
	//document.getElementById("\""+id+"\"").remove();
};

function getNewQuestionData() {
	var q = document.getElementById('newQuestion').value.replace("\n", "\\n");
	var r = (document.getElementById('newRelatedWords').value.replace("\n", "\\n")).split('#');
	var data = {
			question: q,
			relatedWords: r,
			id: -1
	};
	return data;
};

function getEditedQuestionData(theId) {
	var q = document.getElementById('the_question'+theId).value.replace("\n", "\\n");
	var r = (document.getElementById('the_words'+theId).value.replace("\n", "\\n")).split('#');
	var data = {
			question: q,
			relatedWords: r,
			id: -1
	};
	return data;
};

function clearAddQuestionField() {
	var q = document.getElementById('newQuestion');
	q.value = '';
	var r = document.getElementById('newRelatedWords');
	r.value = '';
};

function httpGet(theUrl){
    var xmlHttp = null;

    xmlHttp = new XMLHttpRequest();
    xmlHttp.open( "GET", theUrl, false );
    xmlHttp.send( null );
    return xmlHttp.responseText;
};

function httpPost(theUrl, jsondata){
    var xmlHttp = null;

    xmlHttp = new XMLHttpRequest();
    xmlHttp.open("POST", theUrl, false);
    xmlHttp.setRequestHeader('Content-Type', 'application/json; charset=UTF-8');

    // send the collected data as JSON
    xmlHttp.send(jsondata);

    while (xmlHttp.onprogress){}
    
    xmlHttp.onloadend = function () {
        return xmlHttp.responseText;
    };
    return xmlHttp.responseText;
};