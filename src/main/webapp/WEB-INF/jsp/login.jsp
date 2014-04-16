<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/xml" prefix="x"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<c:set value="${pageContext.request.contextPath}" var="ctx" scope="request" />
<html>
<head>
<title>Login page</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<script src="//ajax.googleapis.com/ajax/libs/jquery/2.0.3/jquery.min.js"></script>
<script type="text/javascript">
  $(document).ready(function() {
    $("#userId").focus();
  });
</script>
<link rel="stylesheet" type="text/css" href="${ctx}/css/style.css" />
</head>
<body>
  <div id="wrapper">
    <jsp:include page="header.jsp" />
    <div id="content">
      <h3>Login page</h3>
      <form method="POST" action="">
        <div style="width: 200px;">
          <label for="userId">User id</label> <input type="text" name="userId" id="userId" required />
        </div>
        <div style="width: 200px;">
          <label for="password">Password</label> <input type="password" name="password" id="password" required />
        </div>
        <button type="submit">Submit</button>
      </form>
    </div>
    <jsp:include page="footer.jsp" />
  </div>
</body>
</html>