<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/xml" prefix="x"%>
<c:set value="${pageContext.request.contextPath}" var="ctx" scope="request" />
<!DOCTYPE html>
<html lang="en" ng-app="app">
<head>
<title>MALM - List Library element properties</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<jsp:include page="css-includes.jsp" />
<jsp:include page="js-includes.jsp" />
<script type="text/javascript" src="${ctx}/js/custom/properties.js"></script>
</head>
<body ng-controller="PropertiesCtrl">
  <jsp:include page="header.jsp" />
  <div class="container">
    <div class="page-header">
      <h1>Properties</h1>
    </div>
    <div class="alert alert-danger" ng-show="error!=null">{{error}}</div>
    <div>
      <h3>Add property</h3>
      <form novalidate name="createResourceForm">
        <input type="hidden" id="newResource_id" ng-model="newResource.id" value="${it}" />
        <table class="table table-striped">
          <tr>
            <td><input type="text" class="form-control" placeholder="Specify a property unique name" id="newResource_unique_name"
              ng-model="newResource.uniqueName" required /></td>
            <td><select id="newResource_type" class="form-control" ng-model="newResource.type"
              ng-options="item.id as item.title for item in customPropertyTypes"></select></td>
            <td style="white-space: nowrap">
              <button class="btn btn-info btn-custom-default" ng-click="createResource(newResource)">
                <span class="glyphicon glyphicon-plus"></span>Add
              </button>
            </td>
          </tr>
        </table>
      </form>
    </div>
    <progressbar class="progress-striped active" value="100" type="info" ng-show="loadingVisible"> <i>Loading</i></progressbar>
    <h3>List properties</h3>
    <div class="table-responsive">
      <table class="table table-striped">
        <tr>
          <td align="left" style="width: 300px"><a href="" ng-click="predicate='uniqueName'; reverse=!reverse">Unique name</a></td>
          <td align="left" style="width: 200px"><a href="" ng-click="predicate='type'; reverse=!reverse">Type</a></td>
          <td align="left">Actions</td>
        </tr>
        <tr
          ng-repeat="property in properties | orderBy:predicate:reverse | startFrom: pagination.page * pagination.perPage | limitTo: pagination.perPage">
          <td align="left"><span editable-text="property.uniqueName" e-class="form-control" e-name="uniqueName" e-form="rowform">{{property.uniqueName}}</span></td>
          <td align="left"><span editable-select="property.type" e-class="form-control" e-name="type" e-form="rowform"
            e-ng-options="item.id as item.title for item in customPropertyTypes">{{property.type}}</span></td>
          <td>
            <form editable-form name="rowform" onbeforesave="updateResource($data, property.id)" ng-show="rowform.$visible"
              class="form-buttons form-inline">
              <button type="submit" ng-disabled="rowform.$waiting" class="btn btn-primary btn-custom-default">
                <span class="glyphicon glyphicon-refresh"></span><span class="hidden-xs hidden-sm">Update</span>
              </button>
              <button type="button" ng-disabled="rowform.$waiting" ng-click="rowform.$cancel()" class="btn btn-default btn-custom-cancel">
                <span class="glyphicon glyphicon glyphicon-step-backward"></span><span class="hidden-xs hidden-sm">Cancel</span>
              </button>
            </form>
            <ul class="nav nav-pills" ng-show="!rowform.$visible">
              <li><button class="btn" ng-click="rowform.$show()">
                  <span class="glyphicon glyphicon-refresh"></span><span class="hidden-xs hidden-sm">Edit</span>
                </button></li>
              <li><button class="btn" ng-click="deleteResource(property.id)">
                  <span class="glyphicon glyphicon-remove"></span><span class="hidden-xs hidden-sm">Delete</span>
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
    <button type="button" ng-click="back()" class="btn btn-default btn-custom-cancel">
      <span class="glyphicon glyphicon glyphicon-step-backward"></span>Back
    </button>
  </div>
  <jsp:include page="footer.jsp" />
</body>
</html>