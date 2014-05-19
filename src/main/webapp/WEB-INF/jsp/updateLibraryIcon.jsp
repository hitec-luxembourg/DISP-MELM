<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/xml" prefix="x"%>
<c:set value="${pageContext.request.contextPath}" var="ctx" scope="request" />
<!DOCTYPE html>
<html lang="en" ng-app="app">
<head>
<title>MALM - Update library element</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<jsp:include page="css-includes.jsp" />
<jsp:include page="js-includes.jsp" />
<script type="text/javascript">
  $(document).ready(function() {
    $('input:radio').on('click', function() {
      $('input:radio').parent().parent().removeClass('icon_selected');
      $(this).parent().parent().addClass('icon_selected');
    });
  });
</script>
<script type="text/javascript" src="${ctx}/js/custom/updateLibraryIcon.js"></script>
</head>
<body ng-controller="UpdateLibraryIconCtrl">
  <jsp:include page="header.jsp" />
  <div class="container">
    <div class="page-header">
      <h1>
        Update library element <small>${it.libraryIcon.library.name}-${it.libraryIcon.library.majorVersion}.${it.libraryIcon.library.minorVersion}</small>
      </h1>
    </div>
    <form method="POST" action="${ctx}/rest/libraries/icons/update" class="form-horizontal" role="form">
      <input name="libraryIconId" id="libraryIconId" type="hidden" value="${it.libraryIcon.id}" /> <input name="id" id="id" type="hidden"
        value="${it.libraryIcon.library.id}" />
      <div class="row">
        <c:forEach var="icon" items="${it.icons}">
          <div class="col-xs-4 col-sm-2 col-md-1 icon-wrapper ${it.libraryIcon.icon.id==icon.id?'icon_selected':''}">
            <label class="icon" for="iconId-${icon.id}"><input type="radio" id="iconId-${icon.id}" name="iconId" value="${icon.id}"
              ${it.libraryIcon.icon.id==icon.id?'checked':''} />&nbsp;<img src="${ctx}/rest/icons/file/${icon.id}/MEDIUM"></label><br />${icon.displayName}</div>
        </c:forEach>
      </div>
      <div class="form-group">
        <label for="iconIndex" class="col-sm-2 control-label">Icon index</label>
        <div class="col-sm-10">
          <input type="number" class="form-control" id="iconIndex" name="iconIndex" placeholder="iconIndex"
            value="${it.libraryIcon.indexOfIconInLibrary}" />
        </div>
      </div>
      <div class="form-group">
        <label for="iconName" class="col-sm-2 control-label">Icon name</label>
        <div class="col-sm-10">
          <input type="text" class="form-control" id="iconName" name="iconName" placeholder="iconName"
            value="${it.libraryIcon.iconNameInLibrary}" />
        </div>
      </div>
      <div class="form-group">
        <label for="iconDescription" class="col-sm-2 control-label">Icon description</label>
        <div class="col-sm-10">
          <input type="text" class="form-control" id="iconDescription" name="iconDescription" placeholder="iconDescription"
            value="${it.libraryIcon.iconDescriptionInLibrary}" />
        </div>
      </div>
      <div class="form-group">
        <div class="col-sm-offset-2 col-sm-10">
          <button type="submit" class="btn">
            <span class="glyphicon glyphicon-refresh"></span> Update
          </button>
          <button type="button" ng-click="back()" class="btn btn-default btn-custom-cancel">
            <span class="glyphicon glyphicon glyphicon-step-backward"></span><span class="hidden-xs hidden-sm">Cancel</span>
          </button>
        </div>
      </div>
    </form>
  </div>
  <jsp:include page="footer.jsp" />
</body>
</html>