<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/xml" prefix="x"%>
<c:set value="${pageContext.request.contextPath}" var="ctx" scope="request" />
<!DOCTYPE html>
<html lang="en" ng-app="app">
<head>
<title>MALM - List Libraries</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<jsp:include page="css-includes.jsp" />
<jsp:include page="js-includes.jsp" />
<script type="text/javascript" src="${ctx}/js/custom/libraries.js"></script>
</head>
<body ng-controller="LibrariesCtrl">
  <jsp:include page="header.jsp" />
  <div class="container">
    <div class="page-header">
      <h1>List Libraries</h1>
    </div>
    <table class="table table-striped">
      <tr>
        <td align="left" style="width:250px"><a href="" ng-click="predicate='name'; reverse=!reverse">Name</a></td>
        <td align="left" style="width:100px">Version</td>
        <td align="left" style="width:150px">Icon</td>
        <td align="left">Actions</td>
      </tr>
      <tr
        ng-repeat="library in libraries | orderBy:predicate:reverse | startFrom: pagination.page * pagination.perPage | limitTo: pagination.perPage">
        <td align="left">{{library.name}}</td>
        <td align="left">{{library.majorVersion}}.{{library.minorVersion}}</td>
        <td align="left"><img src="${ctx}/rest/libraries/icon/file/{{library.id}}"></td>
        <td align="left">
          <ul class="nav nav-pills">
            <li><button class="btn btn-list" ng-click="go('/rest/libraries/icons/'+library.id)">Elements</button></li>
            <li><button class="btn" ng-click="go('/rest/libraries/update/'+library.id)">
                <span class="glyphicon glyphicon-refresh"></span>Update
              </button></li>
            <li><button class="btn" ng-click="go('/rest/libraries/clone/'+library.id)">
                <span class="glyphicon glyphicon-random"></span>Clone
              </button></li>
            <li><button class="btn"
                ng-click="go('/rest/libraries/zip/'+library.name+'-'+library.majorVersion+'.'+library.minorVersion+'.zip')">
                <span class="glyphicon glyphicon-download"></span>Zip
              </button></li>
            <li><button class="btn" ng-click="deleteResource(library.id)">
                <span class="glyphicon glyphicon-remove"></span>Delete
              </button></li>
          </ul>
        </td>
      </tr>
    </table>
    <div class="pagination-centered">
      <ul class="pagination">
        <li><a ng-hide="pagination.page == 0" ng-click="pagination.prevPage()">&laquo;</a></li>
        <li ng-repeat="n in [] | range: pagination.numPages" ng-class="{current: n == pagination.page}"><a
          ng-click="pagination.toPageId(n)">{{n + 1}}</a></li>
        <li><a ng-hide="pagination.page + 1 >= pagination.numPages" ng-click="pagination.nextPage()">&raquo;</a></li>
      </ul>
    </div>
    <hr />
    <ul class="nav nav-pills">
      <li><button class="btn btn-add" ng-click="go('/rest/libraries/add')">
          <span>Add</span>
        </button></li>
      <li><button class="btn btn-import" ng-click="go('/rest/libraries/import')">
          <span>Import</span>
        </button></li>
    </ul>
  </div>
  <jsp:include page="footer.jsp" />
</body>
</html>