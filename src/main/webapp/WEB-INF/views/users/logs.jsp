<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>View user logs</title>
<jsp:include page="../includes/includes.jsp"></jsp:include>
</head>
<body>
	<div id="wrapper">
		<jsp:include page="../includes/navigation.jsp"></jsp:include>
		<div id="page-wrapper">
			<div class="form-group">
				<div class="row">
					<div class="col-lg-12">
						<h1 class="page-header">View logs</h1>
					</div>
				</div>
				<c:if test="${logEntryResult.page > 1}">
					<a href="<c:out value="${logEntryResult.page - 1}"/>"> Previous
						page </a>
				</c:if>
				Page
				<c:out value="${logEntryResult.page}" />
				out of
				<c:out value="${logEntryResult.totalPages}" />
				<c:if test="${logEntryResult.page < logEntryResult.totalPages}">
					<a href="<c:out value="${logEntryResult.page + 1}"/>"> Next
						page </a>
				</c:if>
				<div class="panel panel-default">
					<div class="panel-heading">Current filters</div>
					<c:if
						test="${ empty sessionScope.tags and empty sessionScope.applicationId and empty sessionScope.timestart and empty sessionScope.timeend}">
			There are no current filters applied.
		</c:if>
					<c:if test="${not empty sessionScope.tags}">
			Tags: <c:out value="${sessionScope.tags}" />
						<br />
					</c:if>
					<c:if test="${not empty sessionScope.applicationId}">
			Application ID: <c:out value="${sessionScope.applicationId}" />
						<br />
					</c:if>
					<c:if test="${not empty sessionScope.timestart}">
			Time start: <c:out value="${sessionScope.timestart}" />
						<br />
					</c:if>
					<c:if test="${not empty sessionScope.timeend}">
			Time end: <c:out value="${sessionScope.timeend}" />
						<br />
					</c:if>
				</div>
				<form:form action="" method="POST" class="form-vertical">
					<div class="panel panel-default">
						<div class="panel-heading">New filters</div>
						<div class="form-group">
							<label class="control-label col-lg-2">Tags:</label>
							<div class="col-lg-10">
								<input class="form-control " type="text" name="tags"
									value="<c:out value="${sessionScope.tags}"/>" />
							</div>
							<label class="control-label col-lg-2">Application ID:</label>
							<div class="col-lg-10">
								<input class="form-control" type="text" name="applicationId"
									value="<c:out value="${sessionScope.applicationId}"/>" />
							</div>
							<label class="control-label col-lg-2">Time start:</label>
							<div class="col-lg-10">
								<input class="form-control" type="text" name="timestart"
									value="<c:out value="${sessionScope.timestart}"/>" />
							</div>
							<label class="control-label col-lg-2">Time end:</label>
							<div class="col-lg-10">
								<input class="form-control" type="text" name="timeend"
									value="<c:out value="${sessionScope.timeend}"/>" />
							</div>
						</div>

						<button type="submit" class="btn btn-primary">Submit</button>
					</div>
				</form:form>
				<div class="panel panel-default">
					<div class="panel-heading">Logs</div>
					<c:choose>
						<c:when test="${ not empty logEntryResult.results}">
							<table border="1">
								<tr>
									<th>Username</th>
									<th>Tag</th>
									<th>Value</th>
									<th>Application ID</th>
									<th>Time</th>
								</tr>
								<c:forEach items="${logEntryResult.results}" var="log">
									<tr>
										<td>${log.username}</td>
										<td>${log.tag}</td>
										<td>${log.value}</td>
										<td>${log.applicationId}</td>
										<td>${log.timestamp}</td>
									</tr>
								</c:forEach>
							</table>
						</c:when>
						<c:otherwise>
		        There are no logs for the current user with the current filters.
		    </c:otherwise>
					</c:choose>
				</div>
			</div>
		</div>
	</div>
</body>
</html>