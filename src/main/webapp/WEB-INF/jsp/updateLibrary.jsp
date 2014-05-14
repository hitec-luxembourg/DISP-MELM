<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/xml" prefix="x"%>
<c:set value="${pageContext.request.contextPath}" var="ctx" scope="request" />
<!DOCTYPE html>
<html lang="en">
<head>
<title>MELM - Update library</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<jsp:include page="css-includes.jsp" />
<jsp:include page="js-includes.jsp" />
</head>
<body>
  <jsp:include page="header.jsp" />
  <div class="container">
    <div class="page-header">
      <h1>Update library</h1>
    </div>
    <form method="POST" action="${ctx}/rest/libraries/update" enctype='multipart/form-data' class="form-horizontal" role="form">
      <input name="id" id="id" type="hidden" value="${it.id}" />
      <div class="form-group">
        <label for="libraryName" class="col-sm-2 control-label">Name</label>
        <div class="col-sm-10">
          <input type="text" class="form-control" id="libraryName" name="libraryName" placeholder="libraryName" value="${it.name}">
        </div>
      </div>
      <div class="form-group">
        <label for="version" class="col-sm-2 control-label">Version</label>
        <div class="col-sm-10">
          <input type="text" class="form-control" id="version" name="version" placeholder="version"
            value="${it.majorVersion}.${it.minorVersion}">
        </div>
      </div>
      <div class="form-group">
        <label class="col-sm-2 control-label">Current Icon</label>
        <div class="col-sm-10">
          <img src="${ctx}/rest/libraries/icon/file/${it.id}">
        </div>
      </div>
      <div class="form-group">
        <label for="libraryIconFile" class="col-sm-2 control-label">New Icon</label>
        <div class="col-sm-10">
          <input type="file" id="libraryIconFile" name="libraryIconFile" maxlength='1000000' accept='image/png'>
        </div>
      </div>
      <div class="form-group">
        <div class="col-sm-offset-2 col-sm-10">
          <button type="submit" class="btn btn-primary"><span class="glyphicon glyphicon-refresh"></span>  Update</button>
        </div>
      </div>
    </form>
  </div>
  <jsp:include page="footer.jsp" />
</body>
</html>