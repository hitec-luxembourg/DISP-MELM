<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/xml" prefix="x"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<c:set value="${pageContext.request.contextPath}" var="ctx" scope="request" />
<html>
<head>
<title>List Icons</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<script src="//ajax.googleapis.com/ajax/libs/jquery/2.0.3/jquery.min.js"></script>
<link rel="stylesheet" type="text/css" href="${ctx}/css/style.css" />
</head>
<body>
  <div id="wrapper">
    <jsp:include page="header.jsp" />
    <div id="content">
      <h1>List Icons</h1>
      <table>
        <tr>
          <td></td>
          <td align="center">Name</td>
          <td>Preview</td>
        </tr>
        <c:forEach var="icon" items="${it.icons}">
          <tr>
            <td><a href="${ctx}/rest/icons/delete/${icon.id}">delete</a></td>
            <td align="center">${icon.displayName}</td>
            <td><a href="${ctx}/rest/icons/details/${icon.id}"><img src="${ctx}/rest/icons/file/${icon.id}/MEDIUM"></a></td>
          </tr>
        </c:forEach>
      </table>
    </div>
    <jsp:include page="footer.jsp" />
  </div>
</body>
</html>