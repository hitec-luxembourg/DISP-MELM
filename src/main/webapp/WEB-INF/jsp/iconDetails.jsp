<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/xml" prefix="x"%>
<c:set value="${pageContext.request.contextPath}" var="ctx" scope="request" />
<!DOCTYPE html>
<html lang="en" ng-app="app">
<head>
<title>MALM - Icon details</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<jsp:include page="css-includes.jsp" />
<jsp:include page="js-includes.jsp" />
<script type="text/javascript" src="${ctx}/js/custom/iconDetails.js"></script>
</head>
<body ng-controller="IconDetailsCtrl">
  <jsp:include page="header.jsp" />
  <div class="container">
    <div class="page-header">
      <h1>Icon details</h1>
    </div>
    <h3>${it.displayName}</h3>
    <table class="table table-striped">
      <tr>
        <td>LARGE</td>
        <td>MEDIUM</td>
        <td>SMALL</td>
        <td>TINY</td>
      </tr>
      <tr>
        <td><img src="${ctx}/rest/icons/file/${it.id}/LARGE"></td>
        <td><img src="${ctx}/rest/icons/file/${it.id}/MEDIUM"></td>
        <td><img src="${ctx}/rest/icons/file/${it.id}/SMALL"></td>
        <td><img src="${ctx}/rest/icons/file/${it.id}/TINY"></td>
      </tr>
    </table>
    <hr />
    <button type="button" ng-click="go('/rest/icons')" class="btn btn-default btn-custom-cancel">
      <span class="glyphicon glyphicon glyphicon-step-backward"></span><span class="hidden-xs hidden-sm">Back</span>
    </button>
  </div>
  <jsp:include page="footer.jsp" />
</body>
</html>