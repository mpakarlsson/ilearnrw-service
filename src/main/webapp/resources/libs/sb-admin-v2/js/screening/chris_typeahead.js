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

function suggestWords(parrentDivId, divId, clusterWords){
	w = document.getElementById(divId);
	if (w != null)
		document.getElementById(divId).parentNode.innerHTML = '';
	words = [];
	for (var i=0; i<clusterWords.length; i++){
		words.push(clusterWords[i]);
	}
	var element = document.createElement('div');
	element.setAttribute("id", divId);
	str = '<input class="typeahead" type="text" placeholder="Suggestions">';
	element.innerHTML = str;
	document.getElementById(parrentDivId).innerHTML = '';
	document.getElementById(parrentDivId).appendChild(element);
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