<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/xml" prefix="x"%>
<c:set value="${pageContext.request.contextPath}" var="ctx" scope="request" />
<!DOCTYPE html>
<html lang="en">
<head>
<title>Update library icon</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<jsp:include page="css-includes.jsp" />
<jsp:include page="js-includes.jsp" />
</head>
<body>
  <jsp:include page="header.jsp" />
  <div class="container">
    <div class="page-header">
      <h1>Update library icon</h1>
    </div>
    <h3>${it.libraryIcon.library.name}-${it.libraryIcon.library.majorVersion}.${it.libraryIcon.library.minorVersion}</h3>
    <form method="POST" action="${ctx}/rest/libraries/icons/update" enctype='multipart/form-data' class="form-horizontal" role="form">
      <input name="libraryIconId" id="libraryIconId" type="hidden" value="${it.libraryIcon.id}" /> <input name="libraryName"
        id="libraryName" type="hidden" value="${it.libraryIcon.library.name}" /> <input name="majorVersion" id="majorVersion" type="hidden"
        value="${it.libraryIcon.library.majorVersion}" /> <input name="minorVersion" id="minorVersion" type="hidden"
        value="${it.libraryIcon.library.minorVersion}" />
      <table class="table table-striped">
        <tr>
          <td></td>
          <td align="left">Name</td>
          <td align="left">Preview</td>
        </tr>
        <c:forEach var="icon" items="${it.icons}">
          <tr>
            <td><input type="radio" name="iconId" value="${icon.id}" ${it.libraryIcon.icon.id==icon.id?'checked':''} /></td>
            <td align="left">${icon.displayName}</td>
            <td align="left"><img src="${ctx}/rest/icons/file/${icon.id}/LARGE"></td>
          </tr>
        </c:forEach>
      </table>
      <div class="form-group">
        <label for="iconIndex" class="col-sm-2 control-label">Icon index</label>
        <div class="col-sm-10">
          <input type="text" class="form-control" id="iconIndex" name="iconIndex" placeholder="iconIndex"
            value="${it.libraryIcon.indexOfIconInLibrary}">
        </div>
      </div>
      <div class="form-group">
        <label for="iconName" class="col-sm-2 control-label">Icon name</label>
        <div class="col-sm-10">
          <input type="text" class="form-control" id="iconName" name="iconName" placeholder="iconName"
            value="${it.libraryIcon.iconNameInLibrary}">
        </div>
      </div>
      <div class="form-group">
        <label for="iconDescription" class="col-sm-2 control-label">Icon description</label>
        <div class="col-sm-10">
          <input type="text" class="form-control" id="iconDescription" name="iconDescription" placeholder="iconDescription"
            value="${it.libraryIcon.iconDescriptionInLibrary}">
        </div>
      </div>
      <div class="form-group">
        <div class="col-sm-offset-2 col-sm-10">
          <button type="submit" class="btn btn-default">Submit</button>
        </div>
      </div>
    </form>
    <jsp:include page="footer.jsp" />
</body>
</html>