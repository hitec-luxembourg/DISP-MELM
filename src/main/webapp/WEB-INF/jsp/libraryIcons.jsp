<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/xml" prefix="x"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<c:set value="${pageContext.request.contextPath}" var="ctx" scope="request" />
<html>
<head>
<title>List Library Icons</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<script src="//ajax.googleapis.com/ajax/libs/jquery/2.0.3/jquery.min.js"></script>
<link rel="stylesheet" type="text/css" href="${ctx}/css/style.css" />
</head>
<body>
  <div id="wrapper">
    <jsp:include page="header.jsp" />
    <div id="content">
      <h1>List library Icons</h1>
      <h3>${it.library.name}-${it.library.majorVersion}.${it.library.minorVersion}</h3>
      <table>
        <tr>
          <td></td>
          <td></td>
          <td align="center">Icon Index</td>
          <td align="center">Icon Name</td>
          <td align="center">Icon Description</td>
          <td>Preview</td>
        </tr>
        <c:forEach var="icon" items="${it.icons}">
          <tr>
            <td><a href="${ctx}/rest/libraries/icons/delete/${icon.library.name}/${it.library.majorVersion}/${it.library.minorVersion}/${icon.id}">delete</a></td>
            <td><a href="${ctx}/rest/libraries/icons/update/${icon.id}">update</a></td>
            <td align="center">${icon.indexOfIconInLibrary}</td>
            <td align="center">${icon.iconNameInLibrary}</td>
            <td align="center">${icon.iconDescriptionInLibrary}</td>
            <td><img src="${ctx}/rest/icons/file/${icon.icon.id}/MEDIUM"></td>
          </tr>
        </c:forEach>
      </table>
      <hr/>
      <a href="${ctx}/rest/libraries/icons/add/${it.library.name}/${it.library.majorVersion}/${it.library.minorVersion}">add</a>
    </div>
    <jsp:include page="footer.jsp" />
  </div>
</body>
</html>