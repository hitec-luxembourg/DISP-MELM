<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/xml" prefix="x"%>
<c:set value="${pageContext.request.contextPath}" var="ctx" scope="request" />
<!DOCTYPE html>
<html lang="en" ng-app="app">
<head>
<title>MALM - Add library element</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<jsp:include page="css-includes.jsp" />
<jsp:include page="js-includes.jsp" />
<script type="text/javascript" src="${ctx}/js/custom/addLibraryIcon.js"></script>
</head>
<body ng-controller="AddLibraryIconCtrl">
  <jsp:include page="header.jsp" />
  <div class="container">
    <div class="page-header">
      <h1>
        Add library element <small>${it.library.name}-${it.library.majorVersion}.${it.library.minorVersion}</small>
      </h1>
    </div>
    <c:if test="${not empty it.error}">
      <div class="alert alert-danger">${it.error}</div>
    </c:if>
    <form method="POST" action="${ctx}/rest/libraries/icons/add" class="form-horizontal" role="form">
      <input name="id" id="id" type="hidden" value="${it.library.id}" /> <input type="radio" name="iconId" value="-1"
        ng-checked="isSelected(-1)" style="display:none" />
      <div class="row">
        <div ng-click="selectImage(icon.id)"
          ng-class="isSelected(icon.id) ? 'col-xs-4 col-sm-2 col-md-1 icon-wrapper icon_selected' : 'col-xs-4 col-sm-2 col-md-1 icon-wrapper'"
          ng-repeat="icon in icons | startFrom: pagination.page * pagination.perPage | limitTo: pagination.perPage">
          <label class="icon" for="iconId-{{icon.id}}"><input type="radio" id="iconId-{{icon.id}}" name="iconId" value="{{icon.id}}"
            ng-checked="isSelected(icon.id)" /><img src="${ctx}/rest/icons/file/{{icon.id}}/MEDIUM" alt="{{icon.displayName}}" /></label><br />{{icon.displayName}}
        </div>
      </div>
      <div class="pagination-centered">
        <ul class="pagination">
          <li><a ng-hide="pagination.page == 0" ng-click="pagination.prevPage()">&laquo;</a></li>
          <li ng-repeat="n in [] | range: pagination.numPages" ng-class="{current: n == pagination.page}"><a
            ng-click="pagination.toPageId(n)">{{n + 1}}</a></li>
          <li><a ng-hide="pagination.page + 1 >= pagination.numPages" ng-click="pagination.nextPage()">&raquo;</a></li>
        </ul>
      </div>
      <div class="form-group">
        <label for="iconIndex" class="col-sm-2 control-label">Element index</label>
        <div class="col-sm-10">
          <input type="number" class="form-control" id="iconIndex" name="iconIndex" placeholder="Specify an element index" />
        </div>
      </div>
      <div class="form-group">
        <label for="iconName" class="col-sm-2 control-label">Element name</label>
        <div class="col-sm-10">
          <input type="text" class="form-control" id="iconName" name="iconName" placeholder="Specify an element name" />
        </div>
      </div>
      <div class="form-group">
        <label for="iconDescription" class="col-sm-2 control-label">Element description</label>
        <div class="col-sm-10">
          <input type="text" class="form-control" id="iconDescription" name="iconDescription" placeholder="Specify an element description" />
        </div>
      </div>
      <div class="form-group">
        <div class="col-sm-offset-2 col-sm-10">
          <button type="submit" class="btn btn-add">Add</button>
          <button type="button" ng-click="back()" class="btn btn-default btn-custom-cancel">
            <span class="glyphicon glyphicon glyphicon-step-backward"></span><span class="hidden-xs hidden-sm">Cancel</span>
          </button>
        </div>
      </div>
    </form>
  </div>
  <jsp:include page="footer.jsp" />
</body>
</html>