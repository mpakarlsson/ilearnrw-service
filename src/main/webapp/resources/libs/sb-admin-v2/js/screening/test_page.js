var ilearnurl = 'http://localhost:8080/test/apps';
function isDisplayed(word) {
	for (var i = 0; i < word.childNodes.length; i++) {
	    if (word.childNodes[i].tagName == "INPUT" && word.childNodes[i].name== "displayed"){
	    	if (word.childNodes[i].checked)
	    		return 1;
	    	else
	    		return 0;
	    }
	}
	//-1 if the div does not contain check button
	return -1;
};

function isCorrect(word) {
	for (var i = 0; i < word.childNodes.length; i++) {
	    if (word.childNodes[i].tagName == "INPUT" && word.childNodes[i].name== "correct"){
	    	if (word.childNodes[i].checked)
	    		return 1;
	    	else
	    		return 0;
	    }
	}
	//-1 if the div does not contain check button
	return -1;
};

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

    xmlHttp.onloadend = function () {
    	//alert(xmlHttp.responseText);
    };
    return xmlHttp.responseText;
};