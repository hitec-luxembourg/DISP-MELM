<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/xml" prefix="x"%>
<c:set value="${pageContext.request.contextPath}" var="ctx" scope="request" />
<!DOCTYPE html>
<html lang="en">
<head>
<title>Pulse Collection - 404 error</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<jsp:include page="css-includes.jsp" />
</head>
<body>
  <jsp:include page="login-header.jsp" />
  <div class="container">
    <div class="jumbotron">
      <h1>Resource not found</h1>
      <p>Sorry, but we could not find the resource you are looking for.</p>
      <p>Please find more informations on the <a href="http://www.hitec.lu/techblog">blog</a></p>
    </div>
  </div>
  <jsp:include page="footer.jsp" />
</body>
</html>