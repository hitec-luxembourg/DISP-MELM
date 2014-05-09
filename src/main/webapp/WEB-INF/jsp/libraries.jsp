<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/xml" prefix="x"%>
<c:set value="${pageContext.request.contextPath}" var="ctx" scope="request" />
<!DOCTYPE html>
<html lang="en">
<head>
<title>List Libraries</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<link rel="stylesheet" type="text/css" href="${ctx}/css/bootstrap.min.css" />
<link rel="stylesheet" type="text/css" href="${ctx}/css/style.css" />
<link rel="stylesheet" type="text/css" href="${ctx}/css/sticky-footer-navbar.css" />
</head>
<body>
  <jsp:include page="header.jsp" />
  <div class="container">
    <div class="page-header">
      <h1>List Libraries</h1>
    </div>
    <table class="table table-striped">
      <tr>
        <td align="left">Actions</td>
        <td align="left">Name</td>
        <td align="left">Version</td>
        <td align="left">Icon</td>
      </tr>
      <c:forEach var="library" items="${it.libraries}">
        <tr>
          <td align="left">
            <ul class="nav nav-pills">
              <li><a href="${ctx}/rest/libraries/delete/${library.id}">delete</a></li>
              <li><a href="${ctx}/rest/libraries/icons/${library.name}/${library.majorVersion}/${library.minorVersion}">icons</a></li>
              <li><a href="${ctx}/rest/libraries/update/${library.id}">update</a></li>
              <li><a href="${ctx}/rest/libraries/zip/${library.name}-${library.majorVersion}.${library.minorVersion}.zip">zip</a></li>
            </ul>
          </td>
          <td align="left">${library.name}</td>
          <td align="left">${library.majorVersion}.${library.minorVersion}</td>
          <td align="left"><img src="${ctx}/rest/libraries/icon/file/${library.id}"></td>
        </tr>
      </c:forEach>
    </table>
  </div>
  <jsp:include page="footer.jsp" />
  <script src="//ajax.googleapis.com/ajax/libs/jquery/2.0.3/jquery.min.js"></script>
  <script src="${ctx}/js/bootstrap.min.js"></script>
</body>
</html>