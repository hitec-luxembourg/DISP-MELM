<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/xml" prefix="x"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<c:set value="${pageContext.request.contextPath}" var="ctx" scope="request" />
<html>
<head>
<title>Import library</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<script src="//ajax.googleapis.com/ajax/libs/jquery/2.0.3/jquery.min.js"></script>
<script>
$( document ).ready(function() {
  $("#file").on("change", function() {
      var fileName = $("#file").val().split('\\').pop();
      var split = fileName.split("-");
      var libraryName = split[0];
      var secondPart = split[1];
      var version = secondPart.substr(0, secondPart.length - 4);
      $('#libraryName').val(libraryName);
      $('#version').val(version);
  });
});
</script>
<link rel="stylesheet" type="text/css" href="${ctx}/css/style.css" />
</head>
<body>
  <div id="wrapper">
    <jsp:include page="header.jsp" />
    <div id="content">
      <h1>Import library</h1>
      <div>
        <form method="POST" action="${ctx}/rest/libraries/import" enctype='multipart/form-data'>
          <fieldset>
            <legend>Import library</legend>
            <label for="file">File</label> 
            <input name="file" id="file" type='file' size='20' maxlength='1000000' accept='application/zip' />
            <input value="Save" type="submit" /> 
            <br /> 
            <label for="libraryName" class="readonly">Detected Name</label> 
            <input name="libraryName" id="libraryName" type="text" /> 
            <br /> 
            <label for="version" class="readonly">Detected Version</label> 
            <input name="version" id="version" type="text" />
          </fieldset>
        </form>
      </div>
    </div>
    <jsp:include page="footer.jsp" />
  </div>
</body>
</html>