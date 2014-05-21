<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/xml" prefix="x"%>
<c:set value="${pageContext.request.contextPath}" var="ctx" scope="request" />
<!DOCTYPE html>
<html lang="en" ng-app="app">
<head>
<title>MALM - Update library</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<jsp:include page="css-includes.jsp" />
<jsp:include page="js-includes.jsp" />
<script type="text/javascript" src="${ctx}/js/custom/updateLibrary.js"></script>
</head>
<body ng-controller="UpdateLibraryCtrl">
  <jsp:include page="header.jsp" />
  <div class="container">
    <div class="page-header">
      <h1>Update library</h1>
    </div>
    <c:if test="${not empty it.error}">
      <div class="alert alert-danger">${it.error}</div>
    </c:if>
    <form method="POST" action="${ctx}/rest/libraries/update" enctype='multipart/form-data' class="form-horizontal" role="form">
      <input name="id" id="id" type="hidden" value="${it.library.id}" />
      <div class="form-group">
        <label for="libraryName" class="col-sm-2 control-label">Name</label>
        <div class="col-sm-10">
          <input type="text" class="form-control" id="libraryName" name="libraryName" placeholder="Specify a library name"
            value="${it.library.name}">
        </div>
      </div>
      <div class="form-group">
        <label for="version" class="col-sm-2 control-label">Version</label>
        <div class="col-sm-10">
          <input type="text" class="form-control" id="version" name="version" placeholder="Specify a library version"
            value="${it.library.majorVersion}.${it.library.minorVersion}">
        </div>
      </div>
      <div class="form-group">
        <label class="col-sm-2 control-label">Current Icon</label>
        <div class="col-sm-10">
          <img src="${ctx}/rest/libraries/icon/file/${it.library.id}">
        </div>
      </div>
      <div class="form-group">
        <label for="libraryIconFile" class="col-sm-2 control-label">New Icon</label>
        <div class="col-sm-10">
          <input type="file" id="libraryIconFile" name="libraryIconFile" maxlength='1000000' accept='image/png'>
        </div>
      </div>
      <div class="form-group">
        <div class="col-sm-offset-2 col-sm-10">
          <button type="submit" class="btn">
            <span class="glyphicon glyphicon-refresh"></span>Update
          </button>
          <button type="button" ng-click="go('/rest/libraries')" class="btn btn-default btn-custom-cancel">
            <span class="glyphicon glyphicon glyphicon-step-backward"></span>Cancel
          </button>
        </div>
      </div>
    </form>
  </div>
  <jsp:include page="footer.jsp" />
</body>
</html>