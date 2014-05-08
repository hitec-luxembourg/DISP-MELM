<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/xml" prefix="x"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<c:set value="${pageContext.request.contextPath}" var="ctx" scope="request" />
<html>
<head>
<title>Update library</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<script src="//ajax.googleapis.com/ajax/libs/jquery/2.0.3/jquery.min.js"></script>
<link rel="stylesheet" type="text/css" href="${ctx}/css/style.css" />
</head>
<body>
  <div id="wrapper">
    <jsp:include page="header.jsp" />
    <div id="content">
      <h1>Update library</h1>
      <div>
        <form method="POST" action="${ctx}/rest/libraries/update" enctype='multipart/form-data'>
          <input name="id" id="id" type="hidden" value="${it.id}"/> 
          <fieldset>
            <legend>Add library</legend>
            <label for="libraryName">Library Name</label> 
            <input name="libraryName" id="libraryName" type="text" value="${it.name}"/> 
            <br /> 
            <label for="version">Version</label> 
            <input name="version" id="version" type="text" value="${it.majorVersion}.${it.minorVersion}"/>
            <br /> 
            <label>Current Icon</label> 
            <img src="${ctx}/rest/libraries/icon/file/${it.name}/${it.majorVersion}/${it.minorVersion}">
            <br /> 
            <label for="libraryIconFile">New Icon</label> 
            <input name="libraryIconFile" id="libraryIconFile" type='file' size='20' maxlength='1000000' accept='image/png' />
            <br /> 
            <input value="Save" type="submit" /> 
          </fieldset>
        </form>
      </div>
    </div>
    <jsp:include page="footer.jsp" />
  </div>
</body>
</html>