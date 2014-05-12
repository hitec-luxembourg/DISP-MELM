<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/xml" prefix="x"%>
<c:set value="${pageContext.request.contextPath}" var="ctx" scope="request" />
<!DOCTYPE html>
<html lang="en">
<head>
<title>List Library Icons</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<jsp:include page="css-includes.jsp" />
<jsp:include page="js-includes.jsp" />
</head>
<body>
  <jsp:include page="header.jsp" />
  <div class="container">
    <div class="page-header">
      <h1>List library Icons</h1>
    </div>
    <h3>${it.library.name}-${it.library.majorVersion}.${it.library.minorVersion}</h3>
    <table class="table table-striped">
      <tr>
        <td align="left">Actions</td>
        <td align="left">Icon Index</td>
        <td align="left">Icon Name</td>
        <td align="left">Icon Description</td>
        <td>Preview</td>
      </tr>
      <c:forEach var="icon" items="${it.icons}">
        <tr>
          <td>
            <ul class="nav nav-pills">
              <li><a
                href="${ctx}/rest/libraries/icons/delete/${icon.library.name}/${it.library.majorVersion}/${it.library.minorVersion}/${icon.id}">delete</a></li>
              <li><a href="${ctx}/rest/libraries/icons/update/${icon.id}">update</a></li>
            </ul>
          </td>
          <td align="left">${icon.indexOfIconInLibrary}</td>
          <td align="left">${icon.iconNameInLibrary}</td>
          <td align="left">${icon.iconDescriptionInLibrary}</td>
          <td align="left"><img src="${ctx}/rest/icons/file/${icon.icon.id}/LARGE"></td>
        </tr>
      </c:forEach>
    </table>
    <hr />
    <a href="${ctx}/rest/libraries/icons/add/${it.library.name}/${it.library.majorVersion}/${it.library.minorVersion}">add</a>
  </div>
  <jsp:include page="footer.jsp" />
</body>
</html>