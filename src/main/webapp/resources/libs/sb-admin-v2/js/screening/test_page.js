function getAnswer(word) {
	for (var i = 0; i < word.childNodes.length; i++) {
		if (word.childNodes[i].tagName == "FORM"){
			for (var j = 0; j < word.childNodes[i].childNodes.length; j++) {
				if (word.childNodes[i].childNodes[j].tagName == "INPUT" && word.childNodes[i].childNodes[j].checked){
					return word.childNodes[i].childNodes[j].value;
				}
			}
	    }
	}
	//null if the div does not contain check button
	return null;
};