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
                    <div class="panel panel-default">
                        <div class="panel-heading">
                            <i class="fa fa-bar-chart-o fa-fw"></i> Welcome
                        </div>
                        <div class="panel-body">
                            There should be some introductory text here soon.
                        </div>
                    </div>
                </div>
            </div>
        </div>

    </div>

</body>

</html>
