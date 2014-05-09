<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/xml" prefix="x"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<c:set value="${pageContext.request.contextPath}" var="ctx" scope="request" />
<html>
<head>
<title>Update library icon</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<script src="//ajax.googleapis.com/ajax/libs/jquery/2.0.3/jquery.min.js"></script>
<link rel="stylesheet" type="text/css" href="${ctx}/css/style.css" />
</head>
<body>
  <div id="wrapper">
    <jsp:include page="header.jsp" />
    <div id="content">
      <h1>Update library icon</h1>
      <h3>${it.libraryIcon.library.name}-${it.libraryIcon.library.majorVersion}.${it.libraryIcon.library.minorVersion}</h3>
      <form method="POST" action="${ctx}/rest/libraries/icons/update" enctype='multipart/form-data'>
        <input name="libraryIconId" id="libraryIconId" type="hidden" value="${it.libraryIcon.id}"/> 
        <input name="libraryName" id="libraryName" type="hidden" value="${it.libraryIcon.library.name}"/> 
        <input name="majorVersion" id="majorVersion" type="hidden" value="${it.libraryIcon.library.majorVersion}" /> 
        <input name="minorVersion" id="minorVersion" type="hidden" value="${it.libraryIcon.library.minorVersion}" /> 
        <fieldset>
          <legend>Update library icon</legend>
          <table>
            <tr>
              <td></td>
              <td align="center">Name</td>
              <td>Preview</td>
            </tr>
            <c:forEach var="icon" items="${it.icons}">
              <tr>
                <td><input type="radio" name="iconId" value="${icon.id}" ${it.libraryIcon.icon.id==icon.id?'checked':''} /></td>
                <td align="center">${icon.displayName}</td>
                <td><img src="${ctx}/rest/icons/file/${icon.id}/MEDIUM"></td>
              </tr>
            </c:forEach>
          </table>
          <label for="iconIndex">Icon index</label> 
          <input name="iconIndex" id="iconIndex" type="text" value="${it.libraryIcon.indexOfIconInLibrary}"/> 
          <br /> 
          <label for="iconName">Icon name</label> 
          <input name="iconName" id="iconName" type="text" value="${it.libraryIcon.iconNameInLibrary}" />
          <br /> 
          <label for="iconDescription">Icon description</label> 
          <input name="iconDescription" id="iconDescription" type="text" value="${it.libraryIcon.iconDescriptionInLibrary}" />
          <br /> 
          <input value="Save" type="submit" /> 
        </fieldset>
      </form>
    </div>
    <jsp:include page="footer.jsp" />
  </div>
</body>
</html>