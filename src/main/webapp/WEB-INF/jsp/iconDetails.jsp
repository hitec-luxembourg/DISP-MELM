<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/xml" prefix="x"%>
<c:set value="${pageContext.request.contextPath}" var="ctx" scope="request" />
<!DOCTYPE html>
<html lang="en" ng-app="app">
<head>
<title>Pulse Collection - Icon details</title>
<link rel="icon" href="${ctx}/favicon.ico" />
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<jsp:include page="css-includes.jsp" />
<jsp:include page="js-includes.jsp" />
<script type="text/javascript" src="${ctx}/js/custom/iconDetails.js"></script>
<script type="text/javascript">
  var imageId = "${it.id}";
</script>
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
        <td><img ng-src="{{link['LARGE']}}" ng-mouseenter="changeImage('LARGE', 'selected/')" ng-mouseleave="changeImage('LARGE', '')"></td>
        <td><img ng-src="{{link['MEDIUM']}}" ng-mouseenter="changeImage('MEDIUM', 'selected/')" ng-mouseleave="changeImage('MEDIUM', '')"></td>
        <td><img ng-src="{{link['SMALL']}}" ng-mouseenter="changeImage('SMALL', 'selected/')" ng-mouseleave="changeImage('SMALL', '')"></td>
        <td><img ng-src="{{link['TINY']}}" ng-mouseenter="changeImage('TINY', 'selected/')" ng-mouseleave="changeImage('TINY', '')"></td>
      </tr>
    </table>
    <hr />
    <button type="button" ng-click="go('/rest/icons')" class="btn btn-default">
      <span class="glyphicon glyphicon-remove"></span><span class="hidden-xs hidden-sm">Cancel</span>
    </button>
  </div>
  <jsp:include page="footer.jsp" />
</body>
</html>