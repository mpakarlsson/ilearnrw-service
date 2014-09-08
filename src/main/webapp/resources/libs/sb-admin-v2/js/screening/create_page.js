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
	var str = wordPacks(obj.id, obj.relatedWords, '');

	element.innerHTML = 'Question #'+lastItemPosition+
		'<textarea id="the_question'+obj.id+'" class="question_text" disabled>'+obj.question+'</textarea>'+
		'Words to test<div id="rel_words_container'+obj.id+'" class="rel_words_div">'+str+'</div>'+
		'<input type="checkbox" id="attachWords'+obj.id+'" value="attach" '+
		(obj.attachRelWords == true?'checked':'')+' disabled> Attach \'words to test\' to the question ['+(obj.attachRelWords == true?'Yes':'No')+']<br>'+
		'<div id="hereiam'+obj.id+'">'+
		'</div><table><tr><td>'+
		'<button type="button" class="typeahead-button" onclick="deleteQuestion('+fname.toString()+', '+cluster+', '+obj.id+')">'+
		'Delete</button></td><td>'+
		'<button type="button" class="typeahead-button" onclick="switchButtonState(this, '+fname+', '+cluster+', '+obj.id+')">'+
		'Edit</button></td></tr></table>';
	document.getElementById('questionsListDiv').appendChild(element);
};

function loadAddQuestionField(fname, cluster) {
	var element = document.createElement('div');
	element.setAttribute("id", 'addquestion');
	element.setAttribute('class', 'question_box');
	element.innerHTML = 'Question:<textarea name="vrow" id="newQuestion" class="question_text" ></textarea>'+
		'Words to test<div id="newQuestionRelatedWords" class="rel_words_div"></div>'+
		'<input type="checkbox" id="attachWords" value="attach" checked> Attach \'words to test\' to the question<br>'+
		'<div id="hereiam"></div>'+
		'<button type="button" class="typeahead-button" onclick="saveQuestion('+fname.toString()+', '+cluster+')">Add</button>';
	document.getElementById('addQuestionsDiv').appendChild(element);
	suggestWords('hereiam', 'itsme', clusterWords, 'newQuestionRelatedWords');
	gogo('itsme');
};

function switchButtonState(button, fname, cluster, id){
	var words = readAllSpanTexts('rel_words_container'+id);
	if (button.textContent == 'Edit'){
		str = wordPacks(id, words, 'box');
		document.getElementById('the_question'+id).disabled = false;
		document.getElementById('attachWords'+id).disabled = false;
		document.getElementById('rel_words_container'+id).innerHTML = str;
		suggestWords('hereiam'+id, 'thisisme', clusterWords, 'rel_words_container'+id);
		gogo('thisisme');
		button.textContent = 'Done';
	}
	else{
		str = wordPacks(id, words, '');
		document.getElementById('rel_words_container'+id).innerHTML = str;
		updateQuestion(fname, cluster, id);
		document.getElementById('the_question'+id).disabled = true;
		document.getElementById('attachWords'+id).disabled = true;
		document.getElementById('hereiam'+id).innerHTML = '';
		button.textContent = 'Edit';
	}
};

function appendNewWord(id, newWord){
	var words = readAllSpanTexts(id);
	words.push(newWord);
	str = wordPacks(id, words, 'box');
	document.getElementById(id).innerHTML = str;
};

function removeWord(id){
	var div = document.getElementById(id);
	div.innerHTML = '';
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

function getNewQuestionData() {
	var q = document.getElementById('newQuestion').value.replace("\n", "\\n").replace("\t", "\\t");
	//var r = (document.getElementById('newRelatedWords').value.replace("\n", "\\n")).replace("\t", "\\t").split('#');
	var r = readAllSpanTexts('newQuestionRelatedWords');
	var a = document.getElementById('attachWords').checked;
	var data = {
			question: q,
			relatedWords: r,
			attachRelWords: a,
			id: -1
	};
	return data;
};

function getEditedQuestionData(theId) {
	alert(document.getElementById('attachWords'+theId).checked);
	var q = document.getElementById('the_question'+theId).value.replace("\n", "\\n").replace("\t", "\\t");
	var r = readAllSpanTexts('rel_words_container'+theId);
	var a = document.getElementById('attachWords'+theId).checked;
	var data = {
			question: q,
			relatedWords: r,
			attachRelWords: a,
			id: -1
	};
	return data;
};

function clearAddQuestionField() {
	var q = document.getElementById('newQuestion');
	q.value = '';
	var r = document.getElementById('newQuestionRelatedWords');
	r.innerHTML = '';
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