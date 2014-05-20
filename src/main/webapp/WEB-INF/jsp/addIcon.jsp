<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/xml" prefix="x"%>
<c:set value="${pageContext.request.contextPath}" var="ctx" scope="request" />
<!DOCTYPE html>
<html lang="en">
<head>
<title>MALM - Add icon</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<jsp:include page="css-includes.jsp" />
<jsp:include page="js-includes.jsp" />
</head>
<body>
  <jsp:include page="header.jsp" />
  <div class="container">
    <div class="page-header">
      <h1>Add icon</h1>
    </div>
    <c:if test="${not empty it}">
      <div class="alert alert-danger">${it}</div>
    </c:if>
    <form method="POST" action="${ctx}/rest/icons/add" enctype='multipart/form-data' class="form-horizontal" role="form">
      <div class="form-group">
        <label for="displayName" class="col-sm-2 control-label">Display Name</label>
        <div class="col-sm-10">
          <input type="text" class="form-control" id="displayName" name="displayName" placeholder="Specify a display name">
        </div>
      </div>
      <div class="form-group">
        <label for="largeIconFile" class="col-sm-2 control-label">Large Icon File</label>
        <div class="col-sm-10">
          <input type="file" id="largeIconFile" name="largeIconFile" maxlength='1000000' accept='image/png'>
        </div>
      </div>
      <div class="form-group">
        <div class="col-sm-offset-2 col-sm-10">
          <button type="submit" class="btn btn-add">Add</button>
        </div>
      </div>
    </form>
  </div>
  <jsp:include page="footer.jsp" />
</body>
</html>