function getAnswer(word) {
	for (var i = 0; i < word.childNodes.length; i++) {
		if (word.childNodes[i].tagName == "SELECT"){
			for (var j = 0; j < word.childNodes[i].childNodes.length; j++) {
				if (word.childNodes[i].childNodes[j].tagName == "OPTION" && word.childNodes[i].childNodes[j].selected){
					return word.childNodes[i].childNodes[j].value;
				}
			}
	    }
	}
	//null if the div does not contain check button
	return null;
};