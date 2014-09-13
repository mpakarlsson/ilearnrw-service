<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>

<head>

    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">

    <title>iLearnRW</title>
    
    <jsp:include page="../includes/includes.jsp"></jsp:include>
    
    <!-- Page-Level Plugin CSS - Dashboard -->
    <link href="${pageContext.request.contextPath}/apps/resources/libs/sb-admin-v2/css/plugins/morris/morris-0.4.3.min.css" rel="stylesheet">
    
    <!-- Page-Level Plugin Scripts - Dashboard -->
	<script src="${pageContext.request.contextPath}/apps/resources/libs/sb-admin-v2/js/plugins/morris/raphael-2.1.0.min.js"></script>
	<script src="${pageContext.request.contextPath}/apps/resources/libs/sb-admin-v2/js/plugins/morris/morris.js"></script>

	<script type="text/javascript">
		$(function() {
			var usersURL = "${pageContext.request.contextPath}/logs/users";
			var wordsURL = "${pageContext.request.contextPath}/logs/words";
			var users = [];
			var words = [];
			var outputData = [];
			var chart = null;
			$.when($.getJSON(usersURL), $.getJSON(wordsURL)).done(function (A, B){
				users = A[0];
				var wordsResult = B[0];
				
				$.each(wordsResult.list, function( index, value ) {
					words.push(value.word);
					var dataPoint = {"word":value.word};
					$.each(users, function(index, value){
						dataPoint[value] = 0;
					});
					outputData.push(dataPoint);
				});
			}).then(function() {
				chart = Morris.Bar({
					  element: 'words-encountered',
					  data: outputData,
					  xkey: "word",
					  ykeys: users,
					  labels: users
					});
			}).then(function() {
				$.each(users, function( index, value ) {
					var user = value;
					var call = $.getJSON( "${pageContext.request.contextPath}/logs/" + user + "/words").done(function( data ) {
						$.each(data.list, function( index, value ) {
							var returnedData = $.grep(outputData, function(element, index){
							      return element.word == value.word;
							});
							returnedData[0][user] = value.count;
							chart.setData(outputData);
						});
					});
				});
			});
		});
	</script>

</head>

<body>

    <div id="wrapper">

        <jsp:include page="../includes/navigation.jsp"></jsp:include>

        <div id="page-wrapper">
            <div class="row">
                <div class="col-lg-12">
                <sec:authorize ifAnyGranted="PERMISSION_ADMIN">
        			<h1 class="page-header">Admin statistics</h1>
    			</sec:authorize>
                <sec:authorize ifAnyGranted="PERMISSION_TEACHER">
        			<h1 class="page-header">Teacher statistics</h1>
    			</sec:authorize>
    			<sec:authorize ifAnyGranted="PERMISSION_STUDENT">
        			<h1 class="page-header">Student statistics</h1>
    			</sec:authorize>
                </div>
            </div>
            <div class="row">
            	<div class="col-lg-12">
                	Username: ${student.username}
                	Birthdate: ${student.birthdate}
                </div>
            </div>
            <div class="row">
                <div class="col-lg-12">
                    <div class="panel panel-default">
                        <div class="panel-heading">
                            <i class="fa fa-bar-chart-o fa-fw"></i> Words encountered
                        </div>
                        <div class="panel-body">
                            <div id="words-encountered"></div>
                        </div>
                    </div>
                </div>
            </div>
        </div>

    </div>

</body>

</html>
