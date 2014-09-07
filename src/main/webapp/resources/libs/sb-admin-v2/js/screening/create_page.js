var lastItemPosition = 0;
var clusterWords = [];
var ilearnurl = 'http://localhost:8080/test/apps';

function loadClusterParameters(fname, cluster, jsonQuestions, jsonWords) {
	obj = JSON.parse(jsonQuestions);
	for (var i=0; i<obj.length; i++){
		appendQuestion(fname, cluster, obj[i]);
	}
	clusterWords = [];
	obj2 = JSON.parse(jsonWords);
	for (var i=0; i<obj2.length; i++){
		clusterWords.push(obj2[i]);
	}
};

function appendQuestion(fname, cluster, obj) {
	lastItemPosition++;
	var element = document.createElement('div');
	element.setAttribute("id", 'question'+obj.id);
	element.setAttribute('class', 'question_box');
	var str = '';
	for (var j=0; j<obj.relatedWords.length-1; j++){
		str = str+obj.relatedWords[j]+', ';
	}
	if (obj.relatedWords.length>0)
		str = str+obj.relatedWords[obj.relatedWords.length-1];
	element.innerHTML = 'Question #'+lastItemPosition+'<textarea id="the_question'+obj.id+'" class="question_text" disabled>'+
	obj.question+'</textarea>Related Words<textarea id="the_words'+obj.id+'" class="rel_words_text" disabled>'+str+'</textarea>'+
	   '<div id="hereiam'+obj.id+'"></div>'+
	'<button type="button" onclick="deleteQuestion('+fname.toString()+', '+cluster+', '+obj.id+')">Delete</button>'+
	'<button type="button" onclick="switchButtonState(this, '+fname+', '+cluster+', '+obj.id+')">Edit</button>';
	document.getElementById('questionsListDiv').appendChild(element);
};

function loadAddQuestionField(fname, cluster) {
	var element = document.createElement('div');
	element.setAttribute("id", 'addquestion');
	element.setAttribute('class', 'question_box');
	element.innerHTML = 'Question:<textarea name="vrow" id="newQuestion" class="question_text" ></textarea>'+
	   'Related Words:<textarea name="vrow" id="newRelatedWords" class="rel_words_text" ></textarea>'+
	   '<div id="hereiam'+'"></div>'+
	   '<button type="button" onclick="saveQuestion('+fname.toString()+', '+cluster+')" class="ph-button ph-btn-blue">Add</button>';
	document.getElementById('addQuestionsDiv').appendChild(element);
	suggestWords('hereiam', 'itsme', clusterWords);
	gogo('itsme');
};

function switchButtonState(button, fname, cluster, id){
	if (button.textContent == 'Edit'){
		document.getElementById('the_question'+id).disabled = false;
		document.getElementById('the_words'+id).disabled = false;
		suggestWords('hereiam'+id, 'thisisme', clusterWords);
		gogo('thisisme');
		button.textContent = 'Done';
	}
	else{
		updateQuestion(fname, cluster, id);
		document.getElementById('the_question'+id).disabled = true;
		document.getElementById('the_words'+id).disabled = true;
		button.textContent = 'Edit';
	}
};

function filenamesList(json) {
	obj = JSON.parse(json);
	var files = obj.filenames;
	var defaultTest = obj.defaultTest;
	var basepath = ilearnurl+"/screening";
	var element = document.createElement('div');
	element.setAttribute("id", 'activeFiles');
	//element.setAttribute('class', 'question_box');
	var str = '<h2>Available Tests</h2><form action="">';
	str = str +'<input type="radio" name="test" value="'+defaultTest+'" checked> '+
		'<a href="'+basepath+'?fname='+defaultTest+'"><strong>'+defaultTest+'</strong></a> [Default Test] <br>';
	for (var i=0; i<files.length; i++){
		files[i] = files[i].trim();
		if (defaultTest != files[i]){
			str = str +'<input type="radio" name="test" value="'+files[i]+'" onclick="setDefaultTest(\''+files[i]+'\');return false;"> '+
				'<a href="'+basepath+'?fname='+files[i]+'">'+files[i]+'</a> '+
				'<a href="PleaseEnableJavascript.html" onclick="deleteTest(\''+files[i]+'\');return false;">Delete</a><br>';
		}
	}
	str = str+'<div id = "addNewTest">';
	str = str+'<a href="PleaseEnableJavascript.html" onclick="newFileForm();return false;">[+] Add New Test</a><br>';
	str = str+'</div>';
	str = str +'</form>';
	element.innerHTML = str;
	document.getElementById('filenamesListDiv').innerHTML = '';
	document.getElementById('filenamesListDiv').appendChild(element);
};

function newFileForm(){
	var newTestForm = document.createElement('form');
	newTestForm.innerHTML = 'File name: <input type="text" name="FileName" value="" id="newFileName">'+
	'<input type="submit" onclick="addNewTest()" value="Submit">';
	document.getElementById('addNewTest').innerHTML = '';
	document.getElementById("addNewTest").appendChild(newTestForm);
};

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
	appendQuestion(fname, cluster, data);
	clearAddQuestionField();
};

function updateQuestion(fname, cluster, id) {
	data = getEditedQuestionData(id);
	id = httpPost(ilearnurl+"/updatecluster?fname="+fname+"&cluster="+cluster+"&id="+id+"&action=update", JSON.stringify(data));
	data.id = id;
};

function deleteQuestion(fname, cluster, id) {
	data = getNewQuestionData();
	respId = httpPost(ilearnurl+"/updatecluster?fname="+fname+"&cluster="+cluster+"&action=delete"+"&id="+id, JSON.stringify(data));
	if (respId == id){
		var el = document.getElementById('question'+id);
	el.parentNode.removeChild( el );
	}
	//document.getElementById("\""+id+"\"").remove();
};

function getNewQuestionData() {
	var q = document.getElementById('newQuestion').value.replace("\n", "\\n").replace("\t", "\\t");
	var r = (document.getElementById('newRelatedWords').value.replace("\n", "\\n")).replace("\t", "\\t").split('#');
	var data = {
			question: q,
			relatedWords: r,
			id: -1
	};
	return data;
};

function getEditedQuestionData(theId) {
	var q = document.getElementById('the_question'+theId).value.replace("\n", "\\n").replace("\t", "\\t");
	var r = (document.getElementById('the_words'+theId).value.replace("\n", "\\n")).replace("\t", "\\t").split('#');
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