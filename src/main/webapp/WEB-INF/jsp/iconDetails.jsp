<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/xml" prefix="x"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<c:set value="${pageContext.request.contextPath}" var="ctx" scope="request" />
<html>
<head>
<title>Icon details</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<script src="//ajax.googleapis.com/ajax/libs/jquery/2.0.3/jquery.min.js"></script>
<script>
function notYetImplemented(){
  alert("Not yet implemented");
}
</script>
<link rel="stylesheet" type="text/css" href="${ctx}/css/style.css" />
</head>
<body>
  <div id="wrapper">
    <jsp:include page="header.jsp" />
    <div id="content">
      <h1>Icon details</h1>
      <h3>${it.displayName}</h3>
      <table width="50%">
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
        <tr>
          <td><a href="javascript:void(0)" onclick="notYetImplemented();">update</a></td>
          <td><a href="javascript:void(0)" onclick="notYetImplemented();">update</a></td>
          <td><a href="javascript:void(0)" onclick="notYetImplemented();">update</a></td>
          <td><a href="javascript:void(0)" onclick="notYetImplemented();">update</a></td>
        </tr>
      </table>
    </div>
    <jsp:include page="footer.jsp" />
  </div>
</body>
</html>