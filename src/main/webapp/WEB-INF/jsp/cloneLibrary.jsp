<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/xml" prefix="x"%>
<c:set value="${pageContext.request.contextPath}" var="ctx" scope="request" />
<!DOCTYPE html>
<html lang="en" ng-app="app">
<head>
<title>Pulse Collection - Clone library</title>
<link rel="icon" href="${ctx}/favicon.ico" />
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<jsp:include page="css-includes.jsp" />
<jsp:include page="js-includes.jsp" />
<script type="text/javascript" src="${ctx}/js/custom/cloneLibrary.js"></script>
<script type="text/javascript" src="${ctx}/js/custom/inputFile.js"></script>
<script type="text/javascript" src="${ctx}/js/custom/fileUploadPreview.js"></script>
</head>
<body ng-controller="CloneLibraryCtrl">
	<jsp:include page="header.jsp" />
	<div class="container">
		<div class="page-header">
			<h1>Clone library</h1>
		</div>
		<c:if test="${not empty it.error}">
			<div class="alert alert-danger">${it.error}</div>
		</c:if>
		<form method="POST" action="${ctx}/rest/libraries/clone" enctype='multipart/form-data' class="form-horizontal" role="form">
			<input name="id" id="id" type="hidden" value="${it.library.id}" />
			<div class="form-group">
				<label for="libraryName" class="col-sm-2 control-label">Name</label>
				<div class="col-sm-10">
					<input type="text" class="form-control" id="libraryName" name="libraryName" placeholder="Specify a library name" value="${it.library.name}">
				</div>
			</div>
			<div class="form-group">
				<label for="version" class="col-sm-2 control-label">Version</label>
				<div class="col-sm-10">
					<input type="text" class="form-control" id="version" name="version" placeholder="Specify a library version" value="${it.library.majorVersion}.${it.library.minorVersion}">
				</div>
			</div>
			<div class="form-group">
				<label for="libraryIconFile" class="col-sm-2 control-label">Icon</label>
				<div class="col-sm-10">
					<div>
					<input type="radio" name="iconChoice" ng-model="iconChoice" value="existing">Use existing icon
					<br><img src="${ctx}/rest/libraries/icon/file/${it.library.id}">
					</div>
					<div style="margin-top: 6px;">
					<input type="radio" name="iconChoice" ng-model="iconChoice" value="new">Upload a new one<br>
						<i ng-hide="imageSrc">No image chosen</i> <img ng-src="{{imageSrc}}" />
						<div class="input-group">
							<span class="input-group-btn">
								<span class="btn btn-primary btn-file" ng-disabled="iconChoice != 'new'">
									Browse&hellip; <input type="file" ng-file-select="onFileSelect($files)" id="libraryIconFile" name="libraryIconFile" maxlength='1000000' accept='image/png'>
								</span>
							</span>
							<input type="text" class="form-control" readonly>
						</div>
					</div>
				</div>
			</div>
			<div class="form-group">
				<div class="col-sm-offset-2 col-sm-10">
					<button type="submit" class="btn">
						<span class="glyphicon glyphicon-random"></span>
						Clone
					</button>
					<button type="button" ng-click="back()" class="btn btn-default">
						<span class="glyphicon glyphicon-remove"></span>
						Cancel
					</button>
				</div>
			</div>
		</form>
	</div>
	<jsp:include page="footer.jsp" />
</body>
</html>