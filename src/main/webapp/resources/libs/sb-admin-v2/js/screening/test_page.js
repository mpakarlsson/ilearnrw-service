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