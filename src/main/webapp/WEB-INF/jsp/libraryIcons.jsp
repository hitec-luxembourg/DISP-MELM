<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/xml" prefix="x"%>
<c:set value="${pageContext.request.contextPath}" var="ctx" scope="request" />
<!DOCTYPE html>
<html lang="en" ng-app="app">
<head>
<title>Pulse Collection - List Library elements</title>
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
    <progressbar class="progress-striped active" value="100" type="warning" ng-show="loadingVisible"> <i>Loading resources</i></progressbar>
    <div class="table-responsive library-icons">
      <table class="table table-striped">
        <tr>
          <!-- td align="left" style="width: 150px"><a href="" ng-click="predicate='indexOfIconInLibrary'; reverse=!reverse">Element Index</a></td -->
          <td align="left" style="width: 300px"><a href="" ng-click="predicate='iconNameInLibrary'; reverse=!reverse">Element Name</a></td>
          <td align="left" style="width: 300px"><a href="" ng-click="predicate='iconDescriptionInLibrary'; reverse=!reverse">Element
              Description</a></td>
          <td style="width: 110px">Preview</td>
          <td align="left" style="width: 300px">Actions</td>
        </tr>
        <tr
          ng-repeat="icon in libraryIconsModel.icons | orderBy:predicate:reverse | startFrom: (currentPage - 1) * itemsPerPage | limitTo: itemsPerPage">
          <!-- td align="left">{{icon.indexOfIconInLibrary}}</td -->
          <td align="left">{{icon.iconNameInLibrary}}</td>
          <td align="left">{{icon.iconDescriptionInLibrary}}</td>
          <td align="left"><a ng-href="${ctx}/rest/icons/details/{{icon.icon.id}}"><img ng-src="{{links[icon.icon.id]}}" ng-mouseenter="changeImage(icon.icon.id, 'selected/')" ng-mouseleave="changeImage(icon.icon.id, '')"></a></td>
          <td>
            <ul class="nav nav-pills">
              <li><button class="btn" ng-click="go('/rest/libraries/icons/properties/'+icon.id)" tooltip-placement="top"
                  tooltip="Properties">
                  <span class="glyphicon glyphicon-pencil"></span>
                </button></li>
              <li><button class="btn" ng-click="go('/rest/libraries/icons/update/'+icon.id)" tooltip-placement="top" tooltip="Update">
                  <span class="glyphicon glyphicon-refresh"></span>
                </button></li>
              <li><button class="btn" ng-click="confirmDelete(icon.id)" tooltip-placement="top" tooltip="Delete">
                  <span class="glyphicon glyphicon-trash"></span>
                </button></li>
              <li><button class="btn" ng-click="move('up', icon.id)" ng-style="isFirst(icon.id) && {'display': 'none'}"
                  tooltip-placement="top" tooltip="Move up">
                  <span class="glyphicon glyphicon-arrow-up"></span>
                </button></li>
              <li><button class="btn" ng-click="move('down', icon.id)" ng-style="isLast(icon.id) && {'display': 'none'}"
                  tooltip-placement="top" tooltip="Move down">
                  <span class="glyphicon glyphicon-arrow-down"></span>
                </button></li>
            </ul>
          </td>
        </tr>
      </table>
    </div>
    <div class="pagination-centered">
      <pagination total-items="totalItems" ng-model="currentPage" max-size="5" class="pagination-sm" boundary-links="true" rotate="false"
        items-per-page="itemsPerPage"></pagination>
    </div>
    <hr />
    <button class="btn btn-add" ng-disabled="!libraryIconsModel.iconsAvailable"
      ng-click="go('/rest/libraries/icons/add/'+libraryIconsModel.library.id)">Add</button>
    <button type="button" ng-click="go('/rest/libraries')" class="btn btn-default">
      <span class="glyphicon glyphicon-remove"></span>Back
    </button>
  </div>
  <jsp:include page="footer.jsp" />
</body>
</html>
