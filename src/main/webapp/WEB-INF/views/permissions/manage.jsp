<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>

<head>

<meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">

<title>Manage users</title>

<jsp:include page="../includes/includes.jsp"></jsp:include>

</head>

<body>

	<div id="wrapper">

		<jsp:include page="../includes/top.jsp"></jsp:include>
		<jsp:include page="../includes/side.jsp"></jsp:include>

		<div id="page-wrapper">

			<table class="table table-striped table-bordered table-condensed">
				<thead>
					<tr>
						<th>Permission name</th>
						<th></th>
					</tr>
				</thead>
				<tbody>
					<c:forEach items="${permissions}" var="o">
						<tr class="list-users">
							<td>${o.name}</td>

							<td>
								<div class="btn-group">
									<a class="btn btn-default btn-xs dropdown-toggle" data-toggle="dropdown"
										href="#">Actions <span class="caret"></span></a>
									<ul class="dropdown-menu pull-right">
										<li><a href="${o.id}/edit"><i
												class="icon-pencil"></i> Edit</a></li>
										<li><a href="${o.id}/delete"><i
												class="icon-trash"></i> Delete</a></li>
									</ul>
								</div>
							</td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
			<a class="btn btn-success" href="new"> New Permission </a>
		</div>

	</div>

</body>