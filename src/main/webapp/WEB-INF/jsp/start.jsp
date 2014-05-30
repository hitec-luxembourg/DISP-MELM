<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/xml" prefix="x"%>
<c:set value="${pageContext.request.contextPath}" var="ctx" scope="request" />
<!DOCTYPE html>
<html lang="en" ng-app="app">
<head>
<title>Pulse Collection</title>
<link rel="icon" href="${ctx}/favicon.ico" />
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<jsp:include page="css-includes.jsp" />
<jsp:include page="js-includes.jsp" />
</head>
<body ng-controller="StartCtrl">
  <jsp:include page="header.jsp" />
  <div class="container">
    <div class="page-header">
      <h1>Map Assessment Library Manager</h1>
    </div>
    <h2>Libraries</h2>
    <ul class="nav nav-pills">
      <li><button class="btn btn-list" ng-click="go('/rest/libraries')">List</button></li>
      <li><button class="btn btn-add" ng-click="go('/rest/libraries/add')">Add</button></li>
      <li><button class="btn btn-import" ng-click="go('/rest/libraries/import')">Import</button></li>
    </ul>
    <h2>Icons</h2>
    <ul class="nav nav-pills">
      <li><button class="btn btn-list" ng-click="go('/rest/icons')">List</button></li>
      <li><button class="btn btn-add" ng-click="go('/rest/icons/add')">Add</button></li>
    </ul>
  </div>
  <jsp:include page="footer.jsp" />
</body>
</html>