<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Screening Test Creator</title>
<!-- <link rel="stylesheet" type="text/css" href="css/screening_style.css">  -->	
<link href="${pageContext.request.contextPath}/apps/resources/libs/sb-admin-v2/css/screening/screening_style.css" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/apps/resources/libs/sb-admin-v2/css/plugins/timeline/timeline.css" rel="stylesheet">


<script>

window.onload = function() {
	obj = JSON.parse('${ clustersQuestions}');
	 alert(obj[0].question);
	}

function myFunction1() {
    document.getElementById("myTextarea").value = "Fifth Avenue, New York City";
}
</script>


</head>
<body>

<div id="navcontainer">
<ul id="navlist">
<li><a href="#">View Full Test</a></li>
<li>Go to a cluster
<select name="forma"  onchange="location = this.options[this.selectedIndex].value;">

 <option value="screening">Select Cluster</option>
<c:forEach items="${profileClusters.getClustersNumbers()}" var="res" varStatus="inner">
 <option value="screening?cluster=${res}">Cluster ${res}</option>
</c:forEach>
</select>
</li>
</ul>

<div class="question_box">
   Address: <br>
<p>
${screeningTest.getClusterQuestions(0).get(0).getQuestion()}</p>

<button type="button" onclick="myFunction()">Try it</button>

${ clustersQuestions }

</div>

<div class="question_box">
   Question: <br>

<p>${screeningTest.getClusterQuestions(0).get(1).getQuestion()}</p>

<button type="button" onclick="myFunction()">Try it</button>

<script>
function myFunction1() {
    document.getElementById("myTextarea").value = "Fifth Avenue, New York City";
}
</script>
</div>

<div class="question_box">
   Address: <br>
<textarea name="vrow" id="myTextarea">
${screeningTest.getClusterQuestions(0).get(1).getQuestion()}</textarea>

<p>Click the button to change the contents of the text area.</p>

<button type="button" onclick="myFunction()">Try it</button>

<script>
function myFunction1() {
    document.getElementById("myTextarea").value = "Fifth Avenue, New York City";
}
</script>
</div>

<script>
function myFunction() {
    var array = [];
    
    /*var elements = document.getElementById("myTextarea");
    
    for(var i = 0; i < elements.length; i++) {
       var current = elements[i];
        console.log(current, current.children)
        if(current.children.length === 0 && current.textContent.replace(/ |\n/g,'') !== '') {
           array.push(current.textContent);
        }
    } */
//console.log(array)
//alert(JSON.stringify(array))

var elements = document.getElementsByName("vrow");

for(var i = 0; i < elements.length; i++) {
   var current = elements[i];
    console.log(current, current.children)
    if(current.children.length === 0 && current.textContent.replace(/ |\n/g,'') !== '') {
       array.push(current.textContent);
    }
} 

//alert(array[0]);

var arr = [];

var defaults = {
		 question: 'one simple question',
		 relatedWords: ['sun','mon','tue','wed','thu','fri','sat']
		};
		
arr.push(defaults);
defaults = {
		 question: 'second question',
		 relatedWords: ['a','b','c']
		};
arr.push(defaults);

var cluster = {
		clusterNumber: 0,
		clusterQuestions: arr
		};

alert(JSON.stringify(cluster));
alert(httpGet("http://localhost:8080/test/apps/GR/updatecluster"));

$('#cluster').val(JSON.stringify(cluster));
$('form').submit();
}
</script>

<script>
function httpGet(theUrl)
{
    var xmlHttp = null;

    xmlHttp = new XMLHttpRequest();
    xmlHttp.open( "GET", theUrl, false );
    xmlHttp.send( null );
    return xmlHttp.responseText;
}
</script>

</div>	

</body>
</html>
