var words = [];
var substringMatcher = function(strs) {
  return function findMatches(q, cb) {
    var matches, substrRegex;
 
    // an array that will be populated with substring matches
    matches = [];
 
    // regex used to determine if a string contains the substring `q`
    substrRegex = new RegExp(q, 'i');
 
    // iterate through the pool of strings and for any string that
    // contains the substring `q`, add it to the `matches` array
    $.each(strs, function(i, str) {
      if (substrRegex.test(str)) {
        // the typeahead jQuery plugin expects suggestions to a
        // JavaScript object, refer to typeahead docs for more info
        matches.push({ value: str });
      }
    });
 
    cb(matches);
  };
};

function setWords(json){
	words = [];
	obj = JSON.parse(json);
	for (var i=0; i<obj.length; i++){
		words.push(obj[i]);
	}
};

function suggestWords(parrentDivId, divId, clusterWords, target){
	w = document.getElementById(divId);
	if (w != null)
		document.getElementById(divId).parentNode.innerHTML = '';
	words = [];
	for (var i=0; i<clusterWords.length; i++){
		words.push(clusterWords[i]);
	}
	var element = document.createElement('div');
	element.setAttribute("id", divId);

	str = '<table><tr><td><input id="addWord'+divId+'" class="typeahead" type="text" placeholder="Suggestions">'+
		'</td><td><button type="button" class="typeahead-button" '+
		'onclick="appendNewWord(\''+target+'\', getWord(\'addWord'+divId+'\'))">Add Word</button></td>'+
		'<td><button type="button" class="typeahead-button" '+
		'onclick="appendRandomWordsToTest(\''+target+'\')">Add All Words</button></td></tr></table>';
	element.innerHTML = str;
	var parrent = document.getElementById(parrentDivId);
	parrent.innerHTML = '';
	parrent.appendChild(element);
};

function gogo(divId){
$('#'+divId+' .typeahead').typeahead({
	  hint: true,
	  highlight: true,
	  minLength: 1
	},
	{
	  name: 'words',
	  displayKey: 'value',
	  source: substringMatcher(words)
	});
};

function textSugestions(divId, className, texts){
	$('#'+divId+' .'+className).typeahead({
		hint: true,
		highlight: true,
		minLength: 1
	},
	{
		name: 'words',
		displayKey: 'value',
		source: substringMatcher(texts)
	});
};

function readAllSpanTexts(id){
	curWords = [];
	var div = document.getElementById(id);
	var spans = div.getElementsByTagName("span");
	for(var i=0;i<spans.length;i++){
		curWords.push(spans[i].innerHTML);
	}
	return curWords;
};

function randomWordPacks(id, curWords, startId){
	var items = '';
	for (var j=0; j<curWords.length; j++){
		items = items+'<div class="inline-link-2" id = "'+id+'_'+(j+startId)+'"><span>'+curWords[j]+'</span>'+
		'<a href="PleaseEnableJavascript.html" title="Click to delete the word" '+
		'onclick="removeWord(\''+id+'_'+(j+startId)+'\');return false;">[x]</a></div>';
	}
	return items;
} 

function wordPacks(id, curWords, type){
	var items = '';
	if (type == 'box'){
		for (var j=0; j<curWords.length; j++){
			items = items+'<div class="inline-link-1" id = "'+id+'_'+j+'"><span>'+curWords[j]+'</span>'+
			'<a href="PleaseEnableJavascript.html" title="Click to delete the word" onclick="removeWord(\''+id+'_'+j+'\');return false;">[x]</a></div>';
		}
	}
	else{
		for (var j=0; j<curWords.length; j++){
			items = items+'<span>'+curWords[j]+'</span> ';
		}
	}
	return items;
};

function appendRandomWordsToTest(id){
	var curWords = readAllSpanTexts(id);
	var lastId = 0;
	var str = '';
	if (curWords != null){
		str = wordPacks(id, curWords, 'box');
		lastId = curWords.length;
	}
	curWords = [];
	wordsToPick = words;
	while (wordsToPick.length>0 && curWords.length<50){
		var randomnumber=Math.floor(Math.random()*(wordsToPick.length));
		var t = wordsToPick.splice(randomnumber, 1);
		curWords.push(t);
	}
	str = str+randomWordPacks(id, curWords, lastId);
	document.getElementById(id).innerHTML = str;
};

function getWord(id){
	return document.getElementById(id).value;
};