<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1" />
<title>Endpoints</title>
<link rel="stylesheet" type="text/css"
	href="${pageContext.request.contextPath}/apps/resources/css/style.css"></link>
<style>
th,td {
	text-align: left;
}
</style>
</head>
<body>
	<div id="wrapper">
		<h1>ILearnRW Endpoints</h1>
		<fieldset id="users">
			<legend> ${title} </legend>
			<table border="1">
				<tr>
					<th>Endpoint URL</th>
					<th>HTML method</th>
					<th>Java class</th>
					<th>Java method</th>
					
					<th>Return type</th>
					<th>Parameters</th>
					<th>Annotations</th>
				</tr>
				<c:forEach items="${handlerMethods}" var="entry">
						<tr>
							<td><c:forEach
									items="${entry.key.patternsCondition.patterns}" var="pattern">
									<a href="${fn:substringAfter(pattern, '/')}">${pattern}</a>
								</c:forEach></td>
							<td><c:forEach items="${entry.key.methodsCondition.methods}"
									var="method">
${method}
</c:forEach></td>
							<td><c:if
									test="${entry.value.method.declaringClass ne lastClass}">${fn:substringAfter(entry.value.method.declaringClass,'ilearnrw.')}</c:if></td>
							<c:set var="lastClass"
								value="${entry.value.method.declaringClass}"></c:set>
							<td>${entry.value.method.name}</td>
							
							<td>${entry.value.method.returnType}</td>
							<td>
							<c:forEach items="${entry.value.method.parameterTypes}" var="parameter">
								${parameter.name}
							</c:forEach>
							<td>
							<c:forEach items="${entry.value.method.declaredAnnotations}" var="annotation">
								${annotation}
							</c:forEach>
						</tr>
				</c:forEach>
			</table>
		</fieldset>
	</div>
</body>
</html>