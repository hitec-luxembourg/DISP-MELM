<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/xml" prefix="x"%>
<c:set value="${pageContext.request.contextPath}" var="ctx" scope="request" />
<!DOCTYPE html>
<html lang="en">
<head>
<title>Icon details</title>
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
      <h1>Icon details</h1>
    </div>
    <h3>${it.displayName}</h3>
    <table class="table table-striped">
      <tr>
        <td>LARGE</td>
        <td>MEDIUM</td>
        <td>SMALL</td>
        <td>TINY</td>
      </tr>
      <tr>
        <td><img src="${ctx}/rest/icons/file/${it.id}/LARGE"></td>
        <td><img src="${ctx}/rest/icons/file/${it.id}/MEDIUM"></td>
        <td><img src="${ctx}/rest/icons/file/${it.id}/SMALL"></td>
        <td><img src="${ctx}/rest/icons/file/${it.id}/TINY"></td>
      </tr>
    </table>
  </div>
  <jsp:include page="footer.jsp" />
  <script src="//ajax.googleapis.com/ajax/libs/jquery/2.0.3/jquery.min.js"></script>
  <script src="${ctx}/js/bootstrap.min.js"></script>
</body>
</html>