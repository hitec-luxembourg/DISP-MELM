<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/xml" prefix="x"%>
<c:set value="${pageContext.request.contextPath}" var="ctx" scope="request" />
<!DOCTYPE html>
<html lang="en" ng-app="app">
<head>
<title>Pulse Collection - Import library</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<jsp:include page="css-includes.jsp" />
<jsp:include page="js-includes.jsp" />
<script type="text/javascript" src="${ctx}/js/custom/importLibrary.js"></script>
<script type="text/javascript" src="${ctx}/js/custom/inputFile.js"></script>
<script type="text/javascript">
  $(document).ready(function() {
    $("#libraryFile").on("change", function() {
      var fileName = $("#libraryFile").val().split('\\').pop();
      var split = fileName.split("-");
      var libraryName = split[0];
      var secondPart = split[1];
      var version = secondPart.substr(0, secondPart.length - 4);
      $('#libraryName').val(libraryName);
      $('#version').val(version);
    });
  });
</script>
</head>
<body ng-controller="ImportLibraryCtrl">
  <jsp:include page="header.jsp" />
  <div class="container">
    <div class="page-header">
      <h1>Import library</h1>
    </div>
    <c:if test="${not empty it}">
      <div class="alert alert-danger">${it}</div>
    </c:if>
    <form method="POST" action="${ctx}/rest/libraries/import" enctype='multipart/form-data' class="form-horizontal" role="form">
      <div class="form-group">
        <label for="libraryName" class="col-sm-2 control-label">Detected Name</label>
        <div class="col-sm-10">
          <input type="text" class="form-control" id="libraryName" name="libraryName" placeholder="Detected library name" readonly="readonly">
        </div>
      </div>
      <div class="form-group">
        <label for="version" class="col-sm-2 control-label">Detected Version</label>
        <div class="col-sm-10">
          <input type="text" class="form-control" id="version" name="version" placeholder="Detected library version" readonly="readonly">
        </div>
      </div>
      <div class="form-group">
        <label for="libraryFile" class="col-sm-2 control-label">File</label>
        <div class="col-sm-10">
					<div class="input-group">
						<span class="input-group-btn">
							<span class="btn btn-primary btn-file" >
								Browse&hellip; <input type="file" id="libraryFile" name="libraryFile" maxlength='1000000' accept='application/zip'>
							</span>
						</span>
						<input type="text" class="form-control" readonly>
					</div>
        </div>
      </div>
      <div class="form-group">
        <div class="col-sm-offset-2 col-sm-10">
          <button type="submit" class="btn btn-import">Import</button>
          <button type="button" ng-click="go('/rest/libraries')" class="btn btn-default">
            <span class="glyphicon glyphicon-remove"></span>Cancel
          </button>
        </div>
      </div>
    </form>
  </div>
  <jsp:include page="footer.jsp" />
</body>
</html>