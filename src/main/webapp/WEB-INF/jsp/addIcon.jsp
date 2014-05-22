<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/xml" prefix="x"%>
<c:set value="${pageContext.request.contextPath}" var="ctx" scope="request" />
<!DOCTYPE html>
<html lang="en" ng-app="app">
<head>
<title>MALM - Add icon</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<jsp:include page="css-includes.jsp" />
<jsp:include page="js-includes.jsp" />
<script type="text/javascript" src="${ctx}/js/custom/addIcon.js"></script>
</head>
<body ng-controller="AddIconCtrl">
  <jsp:include page="header.jsp" />
  <div class="container">
    <div class="page-header">
      <h1>Add icon</h1>
    </div>
    <c:if test="${not empty it}">
      <div class="alert alert-danger">${it}</div>
    </c:if>
    <form method="POST" action="${ctx}/rest/icons/add" enctype='multipart/form-data' class="form-horizontal" role="form">
      <div class="form-group">
        <label for="displayName" class="col-sm-2 control-label">Display Name</label>
        <div class="col-sm-10">
          <input type="text" class="form-control" id="displayName" name="displayName" placeholder="Specify a display name">
        </div>
      </div>
      <div class="form-group">
        <label for="anchor" class="col-sm-2 control-label">Anchor</label>
        <div class="col-sm-10">
          <input type="hidden" class="form-control" id="anchor" name="anchor" ng-value="anchor">
          <div class="anchor-wrapper ">
              <a href="" ng-click="setAnchor('SW')" ng-class="getClass('SW')" style="border-top-left-radius:7px; position: absolute; top: 0px; left: 0px;"></a> 
              <a href="" ng-click="setAnchor('S')" ng-class="getClass('S')" style="position: absolute; top: 0px; left: 80px;"></a>
              <a href="" ng-click="setAnchor('SE')" ng-class="getClass('SE')" style="border-top-right-radius:7px; position: absolute; top: 0px; left: 160px;"></a>
              <a href="" ng-click="setAnchor('W')" ng-class="getClass('W')" style="position: absolute; top: 80px; left: 0px;"></a> 
              <a href="" ng-click="setAnchor('E')" ng-class="getClass('E')" style="position: absolute; top: 80px; left: 160px;"></a>
              <a href="" ng-click="setAnchor('NW')" ng-class="getClass('NW')" style="border-bottom-left-radius:7px; position: absolute; top: 160px; left: 0px;"></a> 
              <a href="" ng-click="setAnchor('N')" ng-class="getClass('N')" style="position: absolute; top: 160px; left: 80px;"></a>
              <a href="" ng-click="setAnchor('NE')" ng-class="getClass('NE')" style="border-bottom-right-radius:7px; position: absolute; top: 160px; left: 160px;"></a>
          </div>
        </div>
      </div>
      <div class="form-group">
        <label for="largeIconFile" class="col-sm-2 control-label">Large Icon File</label>
        <div class="col-sm-10">
          <input type="file" id="largeIconFile" name="largeIconFile" maxlength='1000000' accept='image/png'>
        </div>
      </div>
      <div class="form-group">
        <div class="col-sm-offset-2 col-sm-10">
          <button type="submit" class="btn btn-add">Add</button>
          <button type="button" ng-click="go('/rest/icons')" class="btn btn-default btn-custom-cancel">
            <span class="glyphicon glyphicon glyphicon-step-backward"></span>Cancel
          </button>
        </div>
      </div>
    </form>
  </div>
  <jsp:include page="footer.jsp" />
</body>
</html>