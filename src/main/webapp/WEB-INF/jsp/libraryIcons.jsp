<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/xml" prefix="x"%>
<c:set value="${pageContext.request.contextPath}" var="ctx" scope="request" />
<!DOCTYPE html>
<html lang="en" ng-app="app">
<head>
<title>List Library Icons</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<jsp:include page="css-includes.jsp" />
<jsp:include page="js-includes.jsp" />
<script src="${ctx}/js/custom/libraryIcons.js"></script>
</head>
<body ng-controller="LibraryIconsCtrl">
  <jsp:include page="header.jsp" />
  <div class="container">
    <div class="page-header">
      <h1>List library Icons</h1>
    </div>
    <h3>{{libraryIconsModel.library.name}}-{{libraryIconsModel.library.majorVersion}}.{{libraryIconsModel.library.minorVersion}}</h3>
    <table class="table table-striped">
      <tr>
        <td align="left">Actions</td>
        <td align="left"><a href="" ng-click="predicate='indexOfIconInLibrary'; reverse=!reverse">Icon Index</a></td>
        <td align="left"><a href="" ng-click="predicate='iconNameInLibrary'; reverse=!reverse">Icon Name</a></td>
        <td align="left"><a href="" ng-click="predicate='iconDescriptionInLibrary'; reverse=!reverse">Icon Description</a></td>
        <td>Preview</td>
      </tr>
      <tr ng-repeat="icon in libraryIconsModel.icons | orderBy:predicate:reverse">
        <td>
          <ul class="nav nav-pills">
            <li><button class="btn btn-danger" ng-click="deleteResource(icon.id)"><span class="glyphicon glyphicon-remove"></span>  Delete</button></li>
            <li><button class="btn btn-primary" ng-click="go('/rest/libraries/icons/update/'+icon.id)"><span class="glyphicon glyphicon-refresh"></span>  Update</button></li>
          </ul>
        </td>
        <td align="left">{{icon.indexOfIconInLibrary}}</td>
        <td align="left">{{icon.iconNameInLibrary}}</td>
        <td align="left">{{icon.iconDescriptionInLibrary}}</td>
        <td align="left"><img src="${ctx}/rest/icons/file/{{icon.icon.id}}/LARGE"></td>
      </tr>
    </table>
    <hr />
    <button class="btn btn-info" ng-click="go('/rest/libraries/icons/add/'+libraryIconsModel.library.id)"><span class="glyphicon glyphicon-plus"></span>  Add</button>
  </div>
  <jsp:include page="footer.jsp" />
</body>
</html>