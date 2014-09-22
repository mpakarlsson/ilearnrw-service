var lastItemPosition = 0;
var clusterWords = [];
var wordsInsideCategory = [];
var activeQuestions = [];

function loadClusterParameters(fname, cluster, jsonQuestions, jsonWords, jsonWordsInsideCategory, jsonActiveQuestions) {
	obj = JSON.parse(jsonQuestions);
	for (var i=0; i<obj.length; i++){
		appendQuestion(fname, cluster, obj[i]);
	}
	clusterWords = [];
	obj2 = JSON.parse(jsonWords);
	for (var i=0; i<obj2.length; i++){
		clusterWords.push(obj2[i]);
	}
	activeQuestions = [];
	obj3 = JSON.parse(jsonActiveQuestions);
	for (var i=0; i<obj3.length; i++){
		activeQuestions.push(obj3[i]);
	}
	wordsInsideCategory = [];
	obj4 = JSON.parse(jsonWordsInsideCategory);
	for (var i=0; i<obj4.length; i++){
		wordsInsideCategory.push(obj4[i]);
	}
};

function getSelectorWithQuestions(target){
	var qs = '';
	for (var i=0; i<activeQuestions.length; i++){
		qs = qs + '<option value="'+activeQuestions[i]+'">'+activeQuestions[i]+'</option>';
	}
	return '<select name="forma"  onchange="test(\''+target+'\', this.options[this.selectedIndex].value);" class="styled" >'+
	'<option value="">Sugestions</option>'+qs+'</select>';
}

function appendQuestion(fname, cluster, obj) {
	lastItemPosition++;
	var element = document.createElement('div');
	element.setAttribute("id", 'question'+obj.id);
	element.setAttribute('class', 'question_box');
	var str = wordPacks(obj.id, obj.relatedWords, '');

	element.innerHTML = 'Question #'+lastItemPosition+'<div id="selector'+obj.id+'"></div>'+
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
	element.innerHTML = 'Question: '+getSelectorWithQuestions("newQuestion")+
		'<textarea name="vrow" id="newQuestion" class="question_text" style="background:#FFFFFF"></textarea><br>'+
		'Words to test<div id="newQuestionRelatedWords" class="rel_words_div" style="background:#FFFFFF"></div>'+
		'<input type="checkbox" id="attachWords" value="attach" checked> Attach \'words to test\' to the question<br>'+
		'<div id="hereiam"></div>'+
		'<button type="button" class="typeahead-button" onclick="saveQuestion('+fname.toString()+', '+cluster+')">Add</button>';
	document.getElementById('addQuestionsDiv').appendChild(element);
	suggestWords('hereiam', 'itsme', clusterWords, 'newQuestionRelatedWords');
	gogo('itsme');
	
	$(function(){
		$('.styled').selectric();
		$('select').on('selectric-before-init');

	});
};

function test(id, text) {
	if (text !='')
		document.getElementById(id).value = text;
}

function switchButtonState(button, fname, cluster, id){
	var words = readAllSpanTexts('rel_words_container'+id);
	if (button.textContent == 'Edit'){
		str = wordPacks(id, words, 'box');
		document.getElementById('the_question'+id).disabled = false;
		document.getElementById('the_question'+id).style.backgroundColor = "#FFFFFF";
		document.getElementById('attachWords'+id).disabled = false;
		document.getElementById('rel_words_container'+id).innerHTML = str;
		document.getElementById('rel_words_container'+id).style.backgroundColor = "#FFFFFF";
		document.getElementById('selector'+id).innerHTML = getSelectorWithQuestions('the_question'+id);
		suggestWords('hereiam'+id, 'thisisme', clusterWords, 'rel_words_container'+id);
		gogo('thisisme');
		button.textContent = 'Done';
		
		$(function(){
			$('.styled').selectric();
			$('select').on('selectric-before-init');

		});
	}
	else{
		str = wordPacks(id, words, '');
		document.getElementById('rel_words_container'+id).innerHTML = str;
		document.getElementById('selector'+id).innerHTML = '';
		if (updateQuestion(fname, cluster, id)){
			document.getElementById('rel_words_container'+id).style.backgroundColor = "#F8F8F8";
			document.getElementById('the_question'+id).disabled = true;
			document.getElementById('the_question'+id).style.backgroundColor = "#F8F8F8";
			document.getElementById('attachWords'+id).disabled = true;
			document.getElementById('hereiam'+id).innerHTML = '';
			button.textContent = 'Edit';
		}
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
	var element = document.createElement('div');
	element.setAttribute("id", 'activeFiles');
	//element.setAttribute('class', 'question_box');
	var str = '<h2>Available Tests</h2><form action="">';
	str = str +'<input type="radio" name="test" value="'+defaultTest+'" checked> '+
		'<a href="screening?fname='+defaultTest+'"><strong>'+defaultTest+'</strong></a> [Default Test] <br>';
	for (var i=0; i<files.length; i++){
		files[i] = files[i].trim();
		if (defaultTest != files[i]){
			str = str +'<input type="radio" name="test" value="'+files[i]+'" onclick="setDefaultTest(\''+files[i]+'\');return false;"> '+
				'<a href="screening?fname='+files[i]+'">'+files[i]+'</a> '+
				'<a href="PleaseEnableJavascript.html" onclick="confirmDeleteTest(\''+files[i]+'\');return false;" style="color:red;">Delete</a><br>';
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

function confirmDeleteTest(name) {
    if (confirm('Are you sure you want to delete \''+name+'\'?') == true) {
    	deleteTest(name);
    }
}

function newFileForm(){
	var newTestForm = document.createElement('form');
	newTestForm.innerHTML = 'File name: <input type="text" name="FileName" value="" id="newFileName">'+
	'<input type="submit" onclick="addNewTest()" value="Submit">';
	document.getElementById('addNewTest').innerHTML = '';
	document.getElementById("addNewTest").appendChild(newTestForm);
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