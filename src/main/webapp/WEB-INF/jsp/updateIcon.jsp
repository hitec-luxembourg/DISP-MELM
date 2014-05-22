<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/xml" prefix="x"%>
<c:set value="${pageContext.request.contextPath}" var="ctx" scope="request" />
<!DOCTYPE html>
<html lang="en" ng-app="app">
<head>
<title>MALM - Update icon</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<jsp:include page="css-includes.jsp" />
<jsp:include page="js-includes.jsp" />
<script type="text/javascript" src="${ctx}/js/custom/updateIcon.js"></script>
</head>
<body ng-controller="UpdateIconCtrl">
  <jsp:include page="header.jsp" />
  <div class="container">
    <div class="page-header">
      <h1>Update icon</h1>
    </div>
    <c:if test="${not empty it.error}">
      <div class="alert alert-danger">${it.error}</div>
    </c:if>
    <form method="POST" action="${ctx}/rest/icons/update" enctype='multipart/form-data' class="form-horizontal" role="form">
      <input name="id" id="id" type="hidden" value="${it.icon.id}" />
      <div class="form-group">
        <label for="displayName" class="col-sm-2 control-label">Display Name</label>
        <div class="col-sm-10">
          <input type="text" class="form-control" id="displayName" name="displayName" placeholder="Specify a display name" value="${it.icon.displayName}">
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
          <button type="submit" class="btn">
            <span class="glyphicon glyphicon-refresh"></span>Update
          </button>
          <button type="button" ng-click="go('/rest/icons')" class="btn btn-default btn-custom-cancel">
            <span class="glyphicon glyphicon glyphicon-step-backward"></span>Cancel
          </button>
        </div>
      </div>
    </form>
  </div>
  <jsp:include page="footer.jsp" />
</body>
</html>