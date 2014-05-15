<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/xml" prefix="x"%>
<c:set value="${pageContext.request.contextPath}" var="ctx" scope="request" />
<!DOCTYPE html>
<html lang="en">
<head>
<title>MALM - Update library element</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<jsp:include page="css-includes.jsp" />
<jsp:include page="js-includes.jsp" />
</head>
<body>
  <jsp:include page="header.jsp" />
  <div class="container">
    <div class="page-header">
      <h1>
        Update library element <small>${it.libraryIcon.library.name}-${it.libraryIcon.library.majorVersion}.${it.libraryIcon.library.minorVersion}</small>
      </h1>
    </div>
    <form method="POST" action="${ctx}/rest/libraries/icons/update" enctype='multipart/form-data' class="form-horizontal" role="form">
      <input name="libraryIconId" id="libraryIconId" type="hidden" value="${it.libraryIcon.id}" /> <input name="id" id="id" type="hidden"
        value="${it.libraryIcon.library.id}" />
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
          <input type="number" class="form-control" id="iconIndex" name="iconIndex" placeholder="iconIndex"
            value="${it.libraryIcon.indexOfIconInLibrary}" />
        </div>
      </div>
      <div class="form-group">
        <label for="iconName" class="col-sm-2 control-label">Icon name</label>
        <div class="col-sm-10">
          <input type="text" class="form-control" id="iconName" name="iconName" placeholder="iconName"
            value="${it.libraryIcon.iconNameInLibrary}" />
        </div>
      </div>
      <div class="form-group">
        <label for="iconDescription" class="col-sm-2 control-label">Icon description</label>
        <div class="col-sm-10">
          <input type="text" class="form-control" id="iconDescription" name="iconDescription" placeholder="iconDescription"
            value="${it.libraryIcon.iconDescriptionInLibrary}" />
        </div>
      </div>
      <div class="form-group">
        <div class="col-sm-offset-2 col-sm-10">
          <button type="submit" class="btn">
            <span class="glyphicon glyphicon-refresh"></span> Update
          </button>
        </div>
      </div>
    </form>
  </div>
  <jsp:include page="footer.jsp" />
</body>
</html>