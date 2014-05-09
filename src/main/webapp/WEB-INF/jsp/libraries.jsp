<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/xml" prefix="x"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<c:set value="${pageContext.request.contextPath}" var="ctx" scope="request" />
<html>
<head>
<title>List Libraries</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<script src="//ajax.googleapis.com/ajax/libs/jquery/2.0.3/jquery.min.js"></script>
<link rel="stylesheet" type="text/css" href="${ctx}/css/style.css" />
</head>
<body>
  <div id="wrapper">
    <jsp:include page="header.jsp" />
    <div id="content">
      <h1>List Libraries</h1>
      <table>
        <tr>
          <td></td>
          <td></td>
          <td></td>
          <td></td>
          <td align="center">Name</td>
          <td align="center">Version</td>
          <td align="center">Icon</td>
        </tr>
        <c:forEach var="library" items="${it.libraries}">
          <tr>
            <td><a href="${ctx}/rest/libraries/delete/${library.id}">delete</a></td>
            <td><a href="${ctx}/rest/libraries/icons/${library.name}/${library.majorVersion}/${library.minorVersion}">icons</a></td>
            <td><a href="${ctx}/rest/libraries/update/${library.id}">update</a></td>
            <td><a href="${ctx}/rest/libraries/zip/${library.name}-${library.majorVersion}.${library.minorVersion}.zip">zip</a></td>
            <td align="center">${library.name}</td>
            <td align="center">${library.majorVersion}.${library.minorVersion}</td>
            <td><img src="${ctx}/rest/libraries/icon/file/${library.id}"></td>
          </tr>
        </c:forEach>
      </table>
    </div>
    <jsp:include page="footer.jsp" />
  </div>
</body>
</html>