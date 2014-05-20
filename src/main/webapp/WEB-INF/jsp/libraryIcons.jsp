<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/xml" prefix="x"%>
<c:set value="${pageContext.request.contextPath}" var="ctx" scope="request" />
<!DOCTYPE html>
<html lang="en" ng-app="app">
<head>
<title>MALM - List Library elements</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<jsp:include page="css-includes.jsp" />
<jsp:include page="js-includes.jsp" />
<script type="text/javascript" src="${ctx}/js/custom/libraryIcons.js"></script>
</head>
<body ng-controller="LibraryIconsCtrl">
  <jsp:include page="header.jsp" />
  <div class="container">
    <div class="page-header">
      <h1>
        List library elements <small>{{libraryIconsModel.library.name}}-{{libraryIconsModel.library.majorVersion}}.{{libraryIconsModel.library.minorVersion}}</small>
      </h1>
    </div>
    <div class="table-responsive">
      <table class="table table-striped">
        <tr>
          <td align="left" style="width: 150px"><a href="" ng-click="predicate='indexOfIconInLibrary'; reverse=!reverse">Element Index</a></td>
          <td align="left" style="width: 250px"><a href="" ng-click="predicate='iconNameInLibrary'; reverse=!reverse">Element Name</a></td>
          <td align="left" style="width: 250px"><a href="" ng-click="predicate='iconDescriptionInLibrary'; reverse=!reverse">Element
              Description</a></td>
          <td style="width: 110px">Preview</td>
          <td align="left">Actions</td>
        </tr>
        <tr
          ng-repeat="icon in libraryIconsModel.icons | orderBy:predicate:reverse | startFrom: pagination.page * pagination.perPage | limitTo: pagination.perPage">
          <td align="left">{{icon.indexOfIconInLibrary}}</td>
          <td align="left">{{icon.iconNameInLibrary}}</td>
          <td align="left">{{icon.iconDescriptionInLibrary}}</td>
          <td align="left"><img src="${ctx}/rest/icons/file/{{icon.icon.id}}/MEDIUM"></td>
          <td>
            <ul class="nav nav-pills">
              <li><button class="btn btn-list" ng-click="go('/rest/libraries/icons/properties/'+icon.id)">
                  <span class="hidden-xs hidden-sm hidden-md">Properties</span>
                </button></li>
              <li><button class="btn" ng-click="go('/rest/libraries/icons/update/'+icon.id)">
                  <span class="glyphicon glyphicon-refresh"></span><span class="hidden-xs hidden-sm hidden-md">Update</span>
                </button></li>
              <li><button class="btn" ng-click="deleteResource(icon.id)">
                  <span class="glyphicon glyphicon-remove"></span><span class="hidden-xs hidden-sm hidden-md">Delete</span>
                </button></li>
            </ul>
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
    <button class="btn btn-add" ng-click="go('/rest/libraries/icons/add/'+libraryIconsModel.library.id)">Add</button>
    <button type="button" ng-click="go('/rest/libraries')" class="btn btn-default btn-custom-cancel">
      <span class="glyphicon glyphicon glyphicon-step-backward"></span><span class="hidden-xs hidden-sm">Cancel</span>
    </button>
  </div>
  <jsp:include page="footer.jsp" />
</body>
</html>