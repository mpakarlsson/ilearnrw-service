<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<html>
<head>
<title>Login</title>
<jsp:include page="../includes/includes.jsp"></jsp:include>
<link rel="stylesheet" type="text/css" href="resources/webapp/css/login.css"></link>
</head>
<body>
    <div class="container">

      <form method="POST" class="form-signin" action="<c:url value='login'/>">
        <h2 class="form-signin-heading">Please sign in</h2>
        <input name="username" type="text" class="input-block-level" placeholder="Username">
        <input name="pass" type="password" class="input-block-level" placeholder="Password">
        <button class="btn btn-large btn-primary" type="submit">Sign in</button>
      </form>

    </div> <!-- /container -->
</body>
</html>