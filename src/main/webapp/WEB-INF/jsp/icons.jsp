<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/xml" prefix="x"%>
<c:set value="${pageContext.request.contextPath}" var="ctx" scope="request" />
<!DOCTYPE html>
<html lang="en" ng-app="app">
<head>
<title>MALM - List Icons</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<jsp:include page="css-includes.jsp" />
<jsp:include page="js-includes.jsp" />
<script type="text/javascript" src="${ctx}/js/custom/icons.js"></script>
</head>
<body ng-controller="IconsCtrl">
  <jsp:include page="header.jsp" />
  <div class="container">
    <div class="page-header">
      <h1>List Icons</h1>
    </div>
    <div class="table-responsive">
      <table class="table table-striped">
        <tr>
          <td align="left" style="width: 500px"><a href="" ng-click="predicate='displayName'; reverse=!reverse">Name</a></td>
          <td align="left" style="width: 300px">Preview</td>
          <td align="left">Actions</td>
        </tr>
        <tr
          ng-repeat="icon in icons | orderBy:predicate:reverse | startFrom: pagination.page * pagination.perPage | limitTo: pagination.perPage">
          <td align="left">{{icon.displayName}}</td>
          <td align="left"><a href="${ctx}/rest/icons/details/{{icon.id}}"><img src="${ctx}/rest/icons/file/{{icon.id}}/MEDIUM"></a></td>
          <td align="left">
            <button class="btn" ng-click="deleteResource(icon.id)">
              <span class="glyphicon glyphicon-remove"></span>Delete
            </button>
          </td>
        </tr>
      </table>
    </div>
    <div class="pagination-centered">
      <ul class="pagination">
        <li><a ng-hide="pagination.page == 0" ng-click="pagination.prevPage()">&laquo;</a></li>
        <li ng-repeat="n in [] | range: pagination.numPages" ng-class="{current: n == pagination.page}"><a
          ng-click="pagination.toPageId(n)">{{n + 1}}</a></li>
        <li><a ng-hide="pagination.page + 1 >= pagination.numPages" ng-click="pagination.nextPage()">&raquo;</a></li>
      </ul>
    </div>
    <hr />
    <button class="btn btn-add" ng-click="go('/rest/icons/add')">Add</button>
  </div>
  <hr />
  <jsp:include page="footer.jsp" />
</body>
</html>