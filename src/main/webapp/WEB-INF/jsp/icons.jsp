<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/xml" prefix="x"%>
<c:set value="${pageContext.request.contextPath}" var="ctx" scope="request" />
<!DOCTYPE html>
<html lang="en" ng-app="app">
<head>
<title>List Icons</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<jsp:include page="css-includes.jsp" />
<jsp:include page="js-includes.jsp" />
<script src="${ctx}/js/custom/icons.js"></script>
</head>
<body ng-controller="IconsCtrl">
  <jsp:include page="header.jsp" />
  <div class="container">
    <div class="page-header">
      <h1>List Icons</h1>
    </div>
    <table class="table table-striped">
      <tr>
        <td align="left">Actions</td>
        <td align="left"><a href="" ng-click="predicate='displayName'; reverse=!reverse">Name</a></td>
        <td align="left">Preview</td>
      </tr>
      <tr ng-repeat="icon in icons | orderBy:predicate:reverse">
        <td align="left">
          <button class="btn btn-danger btn-custom-danger" ng-click="deleteIcon(icon.id)">Delete</button>
        </td>
        <td align="left">{{icon.displayName}}</td>
        <td align="left"><a href="${ctx}/rest/icons/details/{{icon.id}}"><img src="${ctx}/rest/icons/file/{{icon.id}}/LARGE"></a></td>
      </tr>
    </table>
  </div>
  <jsp:include page="footer.jsp" />
</body>
</html>