<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/xml" prefix="x"%>
<c:set value="${pageContext.request.contextPath}" var="ctx" scope="request" />
<!DOCTYPE html>
<html lang="en" ng-app="app">
<head>
<title>MALM - Add library</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<jsp:include page="css-includes.jsp" />
<jsp:include page="js-includes.jsp" />
<script type="text/javascript" src="${ctx}/js/custom/addLibrary.js"></script>
<script type="text/javascript" src="${ctx}/js/custom/inputFile.js"></script>
</head>
<body ng-controller="AddLibraryCtrl">
	<jsp:include page="header.jsp" />
	<div class="container">
		<div class="page-header">
			<h1>Add library</h1>
		</div>
		<c:if test="${not empty it}">
			<div class="alert alert-danger">${it}</div>
		</c:if>
		<form method="POST" action="${ctx}/rest/libraries/add" enctype='multipart/form-data' class="form-horizontal" role="form">
			<div class="form-group">
				<label for="libraryName" class="col-sm-2 control-label">Name</label>
				<div class="col-sm-10">
					<input type="text" class="form-control" id="libraryName" name="libraryName" placeholder="Specify a library name">
				</div>
			</div>
			<div class="form-group">
				<label for="version" class="col-sm-2 control-label">Version</label>
				<div class="col-sm-10">
					<input type="text" class="form-control" id="version" name="version" placeholder="Specify a library version">
				</div>
			</div>
			<div class="form-group">
				<label for="libraryIconFile" class="col-sm-2 control-label">Icon</label>
				<div class="col-sm-10">
					<div class="input-group">
						<span class="input-group-btn">
							<span class="btn btn-primary btn-file">
								Browse&hellip; <input type="file" id="libraryIconFile" name="libraryIconFile" maxlength='1000000' accept='image/png'>
							</span>
						</span>
						<input type="text" class="form-control" readonly>
					</div>
				</div>
			</div>
			<div class="form-group">
				<div class="col-sm-offset-2 col-sm-10">
					<button type="submit" class="btn btn-add">Add</button>
					<button type="button" ng-click="go('/rest/libraries')" class="btn btn-default btn-custom-cancel">
						<span class="glyphicon glyphicon glyphicon-step-backward"></span>
						Cancel
					</button>
				</div>
			</div>
		</form>
	</div>
	<jsp:include page="footer.jsp" />
</body>
</html>