var lastItemPosition = 0;
function loadClusterQuestions(json) {
	obj = JSON.parse(json);
	for (var i=0; i<obj.length; i++){
		appendQuestion(obj[i]);
	}
};

function appendQuestion(obj) {
	lastItemPosition++;
	var element = document.createElement('div');
	element.setAttribute("id", 'question'+obj.id);
	element.setAttribute('class', 'question_box');
	element.innerHTML = '<label>Question</label>';
	var str = '<p>';
	for (var j=0; j<obj.relatedWords.length-1; j++){
		str = str+obj.relatedWords[j]+', ';
	}
	if (obj.relatedWords.length>0)
		str = str+obj.relatedWords[obj.relatedWords.length-1];
	element.innerHTML = '<h4>Question #'+lastItemPosition+'</h4><p>'+obj.question+'</p>'+'<h4>Related Words</h4><p>'+str+'</p>';
	document.getElementById('questionsListDiv').appendChild(element);
};

function loadAddQuestionField(cluster) {
	var element = document.createElement('div');
	element.setAttribute("id", 'addquestion');
	element.setAttribute('class', 'question_box');
	element.innerHTML = 'Question: <br><textarea name="vrow" id="newQuestion"></textarea>'+
	   '<p>Click the button to change the contents of the text area.</p><textarea name="vrow" id="newRelatedWords"></textarea>'+
	   '<button type="button" onclick="saveQuestion('+cluster+')">Try it</button>';
	document.getElementById('addQuestionsDiv').appendChild(element);
};
function saveQuestion(cluster) {
	data = getNewQuestionData();
	httpPost("http://localhost:8080/test/apps/updatecluster?cluster="+cluster, JSON.stringify(data));
	appendQuestion(data);
	clearAddQuestionField();
};

function getNewQuestionData() {
	var q = document.getElementById('newQuestion').value;
	var r = (document.getElementById('newRelatedWords').value).split('#');
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
    xmlHttp.open("POST", theUrl, true);
    xmlHttp.setRequestHeader('Content-Type', 'application/json; charset=UTF-8');

    // send the collected data as JSON
    xmlHttp.send(jsondata);

    xmlHttp.onloadend = function () {
    	//alert(xmlHttp.responseText);
    };
    return xmlHttp.responseText;
};