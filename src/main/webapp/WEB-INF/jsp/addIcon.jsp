<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/xml" prefix="x"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<c:set value="${pageContext.request.contextPath}" var="ctx" scope="request" />
<html>
<head>
<title>Add icon</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<script src="//ajax.googleapis.com/ajax/libs/jquery/2.0.3/jquery.min.js"></script>
<link rel="stylesheet" type="text/css" href="${ctx}/css/style.css" />
</head>
<body>
  <div id="wrapper">
    <jsp:include page="header.jsp" />
    <div id="content">
      <h1>Add icon</h1>
      <div>
        <form method="POST" action="${ctx}/rest/icons/add" enctype='multipart/form-data'>
          <fieldset>
            <legend>Add icon</legend>
            <label for="displayName" class="readonly">Display Name</label> 
            <input name="displayName" id="displayName" type="text" /> 
            <br /> 
            <label for="albumName" class="readonly">Album Name</label> 
            <input name="albumName" id="albumName" type="text" /> 
            <br /> 
            <label for="largeIconFile">Large Icon File</label> 
            <input name="largeIconFile" id="largeIconFile" type='file' size='20' maxlength='1000000' accept='image/png' />
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