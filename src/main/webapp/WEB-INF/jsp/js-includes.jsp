<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<script type="text/javascript">
  var melmContextRoot = "${ctx}";
</script>
<script type="text/javascript" src="${ctx}/js/vendor/jquery.min.js"></script>
<script type="text/javascript" src="${ctx}/js/vendor/jquery-ui.min.js"></script>
<script type="text/javascript" src="${ctx}/js/vendor/angular.min.js"></script>
<script type="text/javascript" src="${ctx}/js/vendor/angular-animate.min.js"></script>
<script type="text/javascript" src="${ctx}/js/vendor/sortable.min.js"></script>
<script type="text/javascript" src="${ctx}/js/vendor/angular-sanitize.min.js"></script>
<script type="text/javascript" src="${ctx}/js/vendor/angular-translate.min.js"></script>
<script type="text/javascript" src="${ctx}/js/vendor/ui-bootstrap-tpls.min.js"></script>
<script type="text/javascript" src="${ctx}/js/vendor/dialogs.min.js"></script>
<script type="text/javascript" src="${ctx}/js/vendor/xeditable.min.js"></script>
<script type="text/javascript" src="${ctx}/js/vendor/es5-shim.min.js"></script>
<script type="text/javascript" src="${ctx}/js/vendor/angular-file-upload.min.js"></script>
<script type="text/javascript" src="${ctx}/js/vendor/loading-bar.min.js"></script>
<script type="text/javascript" src="${ctx}/js/vendor/bootstrap.min.js"></script>
<script type="text/javascript">
  var app = angular.module('app', [ 'xeditable', 'ui.sortable', 'ui.bootstrap', 'angularFileUpload', 'dialogs.main', 'pascalprecht.translate', 'angular-loading-bar', 'ngAnimate']);
    app.config(function(cfpLoadingBarProvider) {
    cfpLoadingBarProvider.includeSpinner = true;
    cfpLoadingBarProvider.latencyThreshold = 400;
    });
</script>
<script type="text/javascript" src="${ctx}/js/custom/functions.js"></script>
<script type="text/javascript" src="${ctx}/js/custom/filters.js"></script>
<script type="text/javascript" src="${ctx}/js/custom/directives.js"></script>
