<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/xml" prefix="x"%>
<c:set value="${pageContext.request.contextPath}" var="ctx" scope="request" />
<!DOCTYPE html>
<html lang="en" ng-app="app">
<head>
<title>Pulse Collection - Import library</title>
<link rel="icon" href="${ctx}/favicon.ico" />
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<jsp:include page="css-includes.jsp" />
<jsp:include page="js-includes.jsp" />
<script type="text/javascript" src="${ctx}/js/custom/importLibrary.js"></script>
</head>
<body ng-controller="ImportLibraryCtrl" ng-file-drop>
  <jsp:include page="header.jsp" />
  <div class="container">
    <div class="page-header">
      <h1>Import library</h1>
    </div>
    <form method="POST" class="form-horizontal" role="form">
      <div class="jumbotron">
        <p>
          Click on the following button or drag and drop your file in order to select a library file.<br />Then you can click on the 'import' button if you want
          to use this file or on the 'delete' button if you want to use another file.
        </p>
        <p>
          <span class="btn btn-primary btn-file"> <span class="glyphicon glyphicon-file"></span> Browse&hellip; <input type="file"
            id="libraryFile" name="libraryFile" maxlength='1000000' accept='application/zip' ng-file-select multiple />
          </span>
          <button type="button" ng-click="go('/rest/libraries')" class="btn btn-default">
            <span class="glyphicon glyphicon-remove"></span>Cancel
          </button>
        <div ng-show="uploader.isHTML5" class="well my-drop-zone" ng-file-over>Drag and drop your library file here</div>
      </div>
      <div class="form-group">
        <table class="table" ng-show="uploader.queue.length===1" style="width: 100%">
          <thead>
            <tr>
              <th width="20%">File Name</th>
              <th width="20%">Detected Name</th>
              <th width="20%">Detected Version</th>
              <th ng-show="uploader.isHTML5">Size</th>
              <th>Action</th>
            </tr>
          </thead>
          <tbody>
            <tr ng-repeat="item in uploader.queue">
              <td>{{item.file.name}}</td>
              <td>{{detectLibraryName(item.file.name)}}</td>
              <td>{{detectLibraryVersion(item.file.name)}}</td>
              <td ng-show="uploader.isHTML5" nowrap>{{ item.file.size/1024/1024|number:2 }} MB</td>
              <td nowrap>
                <button type="button" class="btn btn-import" ng-click="item.upload()"
                  ng-disabled="item.isReady || item.isUploading || item.isSuccess">Import</button>
                <button type="button" class="btn btn-danger" ng-click="item.remove()">
                  <span class="glyphicon glyphicon-trash"></span> Remove
                </button>
              </td>
            </tr>
          </tbody>
        </table>
      </div>
    </form>
  </div>
  <jsp:include page="footer.jsp" />
</body>
</html>