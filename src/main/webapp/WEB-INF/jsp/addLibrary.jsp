<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/xml" prefix="x"%>
<c:set value="${pageContext.request.contextPath}" var="ctx" scope="request" />
<!DOCTYPE html>
<html lang="en" ng-app="app">
<head>
<title>Pulse Collection - Add library</title>
<link rel="icon" href="${ctx}/favicon.ico" />
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<jsp:include page="css-includes.jsp" />
<jsp:include page="js-includes.jsp" />
<script type="text/javascript" src="${ctx}/js/custom/addLibrary.js"></script>
</head>
<body ng-controller="AddLibraryCtrl" ng-file-drop>
  <jsp:include page="header.jsp" />
  <div class="container">
    <div class="page-header">
      <h1>Add library</h1>
    </div>
    <form method="POST" class="form-horizontal" role="form" name="addLibraryForm">
      <div class="jumbotron">
        <p>
          Click on the following button or drag and drop your file in order to select a <b>library icon</b>. <br /> Then you can click on
          the 'add' button if you want to create the library or on the 'delete' button if you want to use another file.
        </p>
        <p>
          <span class="btn btn-primary btn-file"> <span class="glyphicon glyphicon-file"></span> Browse&hellip; <input type="file"
            id="libraryIconFile" name="libraryIconFile" maxlength='1000000' accept='image/png' ng-file-select multiple />
          </span>
          <button type="button" ng-click="go('/rest/libraries')" class="btn btn-default">
            <span class="glyphicon glyphicon-remove"></span> Cancel
          </button>
        <div ng-show="uploader.isHTML5" class="well my-drop-zone" ng-file-over>
          Drag and drop your <b>library icon</b> file here
        </div>
      </div>
      <div ng-show="uploader.queue.length===1">
        <div class="form-group">
          <label for="libraryName" class="col-sm-2 control-label">Name</label>
          <div class="col-sm-10">
            <input type="text" class="form-control" id="libraryName" name="libraryName" placeholder="Specify a library name"
              ng-model="libraryName" ng-minlength="3" ng-maxlength="40" ng-required="true" /> <span class="error"
              ng-show="addLibraryForm.libraryName.$error.minlength"> Too short (3-40)!</span> <span class="error"
              ng-show="addLibraryForm.libraryName.$error.maxlength"> Too long (3-40)!</span>
          </div>
        </div>
        <div class="form-group">
          <label for="version" class="col-sm-2 control-label">Version</label>
          <div class="col-sm-10">
            <input type="text" class="form-control" id="version" name="version" placeholder="Specify a library version" ng-model="version" ng-pattern="/^[0-9]+\.[0-9]+$/" ng-required="true" /><span class="error"
              ng-show="addLibraryForm.version.$error.pattern"> Wrong pattern ([0-9]+\.[0-9]+)!</span>
          </div>
        </div>
        <div class="form-group">
          <label for="libraryIconFile" class="col-sm-2 control-label">Icon</label>
          <div class="col-sm-10">
            <table class="table" style="width: 100%">
              <thead>
                <tr>
                  <th width="20%">File Name</th>
                  <th ng-show="uploader.isHTML5">Size</th>
                  <th>Action</th>
                </tr>
              </thead>
              <tbody>
                <tr ng-repeat="item in uploader.queue">
                  <td>{{item.file.name}}
                    <div ng-show="uploader.isHTML5" ng-thumb="{ file: item.file, height: 40 }"></div>
                  </td>
                  <td ng-show="uploader.isHTML5" nowrap>{{ item.file.size/1024|number:2 }} KB</td>
                  <td nowrap>
                    <button type="button" class="btn btn-danger" ng-click="item.remove()">
                      <span class="glyphicon glyphicon-trash"></span> Remove
                    </button>
                  </td>
                </tr>
              </tbody>
            </table>
          </div>
          <button type="button" class="btn btn-add" ng-click="create()" ng-disabled="!isAddActive()">Add</button>
        </div>
      </div>
    </form>
  </div>
  <jsp:include page="footer.jsp" />
</body>
</html>