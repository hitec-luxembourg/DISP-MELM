<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/xml" prefix="x"%>
<c:set value="${pageContext.request.contextPath}" var="ctx" scope="request" />
<!DOCTYPE html>
<html lang="en">
<head>
<title>Login</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<jsp:include page="css-includes.jsp" />
<jsp:include page="js-includes.jsp" />
<script type="text/javascript">
  $(document).ready(function() {
    $("#userId").focus();
  });
</script>
</head>
<body>
  <jsp:include page="login-header.jsp" />
  <div class="container">
    <div class="page-header">
      <h1>Login</h1>
    </div>
    <c:if test="${not empty it}">
      <div class="alert alert-danger">Wrong User id or Password!</div>
    </c:if>
    <form method="POST" action="" class="form-horizontal" role="form">
      <div class="form-group">
        <label for="userId" class="col-sm-2 control-label">User id</label>
        <div class="col-sm-10">
          <input type="text" class="form-control" id="userId"  name="userId" placeholder="userId">
        </div>
      </div>
      <div class="form-group">
        <label for="password" class="col-sm-2 control-label">Password</label>
        <div class="col-sm-10">
          <input type="password" class="form-control" id="password" name="password" placeholder="password">
        </div>
      </div>
      <div class="form-group">
        <div class="col-sm-offset-2 col-sm-10">
          <button type="submit" class="btn btn-default"><span class="glyphicon glyphicon-log-in"></span>  Log-in</button>
        </div>
      </div>
    </form>
  </div>
  <jsp:include page="footer.jsp" />
</body>
</html>