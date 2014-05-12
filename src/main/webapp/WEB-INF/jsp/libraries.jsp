<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/xml" prefix="x"%>
<c:set value="${pageContext.request.contextPath}" var="ctx" scope="request" />
<!DOCTYPE html>
<html lang="en" ng-app="app">
<head>
<title>List Libraries</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<jsp:include page="css-includes.jsp" />
<jsp:include page="js-includes.jsp" />
<script src="${ctx}/js/custom/libraries.js"></script>
</head>
<body ng-controller="LibrariesCtrl">
  <jsp:include page="header.jsp" />
  <div class="container">
    <div class="page-header">
      <h1>List Libraries</h1>
    </div>
    <table class="table table-striped">
      <tr>
        <td align="left">Actions</td>
        <td align="left"><a href="" ng-click="predicate='name'; reverse=!reverse">Name</a></td>
        <td align="left">Version</td>
        <td align="left">Icon</td>
      </tr>
      <tr ng-repeat="library in libraries | orderBy:predicate:reverse">
        <td align="left">
          <ul class="nav nav-pills">
            <li><button class="btn btn-danger" ng-click="deleteResource(library.id)">
                <span class="glyphicon glyphicon-remove"></span> Delete
              </button></li>
            <li><button class="btn btn-primary" ng-click="go('/rest/libraries/icons/'+library.id)">
                <span class="glyphicon glyphicon-th-list"></span> Icons
              </button></li>
            <li><button class="btn btn-primary" ng-click="go('/rest/libraries/update/'+library.id)">
                <span class="glyphicon glyphicon-refresh"></span> Update
              </button></li>
            <li><button class="btn btn-primary"
                ng-click="go('/rest/libraries/zip/'+library.name+'-'+library.majorVersion+'.'+library.minorVersion+'.zip')">
                <span class="glyphicon glyphicon-download"></span> Zip</a></button></li>
          </ul>
        </td>
        <td align="left">{{library.name}}</td>
        <td align="left">{{library.majorVersion}}.{{library.minorVersion}}</td>
        <td align="left"><img src="${ctx}/rest/libraries/icon/file/{{library.id}}"></td>
      </tr>
    </table>
    <hr />
    <button class="btn btn-info" ng-click="go('/rest/libraries/add')">
      <span class="glyphicon glyphicon-plus"></span> Add</a>
    </button>
  </div>
    <jsp:include page="footer.jsp" />
</body>
</html>