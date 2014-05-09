<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/xml" prefix="x"%>
<c:set value="${pageContext.request.contextPath}" var="ctx" scope="request" />
<!DOCTYPE html>
<html lang="en">
<head>
<title>List Icons</title>
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
      <h1>List Icons</h1>
    </div>
    <table class="table table-striped">
      <tr>
        <td align="left">Actions</td>
        <td align="left">Name</td>
        <td align="left">Preview</td>
      </tr>
      <c:forEach var="icon" items="${it.icons}">
        <tr>
          <td align="left"><a href="${ctx}/rest/icons/delete/${icon.id}">delete</a></td>
          <td align="left">${icon.displayName}</td>
          <td align="left"><a href="${ctx}/rest/icons/details/${icon.id}"><img src="${ctx}/rest/icons/file/${icon.id}/LARGE"></a></td>
        </tr>
      </c:forEach>
    </table>
  </div>
  <jsp:include page="footer.jsp" />
  <script src="//ajax.googleapis.com/ajax/libs/jquery/2.0.3/jquery.min.js"></script>
  <script src="${ctx}/js/bootstrap.min.js"></script>
</body>
</html>