<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/xml" prefix="x"%>
<c:set value="${pageContext.request.contextPath}" var="ctx" scope="request" />
<!DOCTYPE html>
<html lang="en">
<head>
<title>Start page</title>
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
      <h1>Start page</h1>
    </div>
    <h2>Libraries</h2>
    <ul class="nav nav-pills">
      <li><a href="${ctx}/rest/libraries">List</a></li>
      <li><a href="${ctx}/rest/libraries/import">Import</a></li>
      <li><a href="${ctx}/rest/libraries/add">Add</a></li>
    </ul>
    <h2>Icons</h2>
    <ul class="nav nav-pills">
      <li><a href="${ctx}/rest/icons">List</a></li>
      <li><a href="${ctx}/rest/icons/add">Add</a></li>
    </ul>
  </div>
  <jsp:include page="footer.jsp" />
  <script src="//ajax.googleapis.com/ajax/libs/jquery/2.0.3/jquery.min.js"></script>
  <script src="${ctx}/js/bootstrap.min.js"></script>
</body>
</html>