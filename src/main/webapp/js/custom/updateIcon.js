app.controller('UpdateIconCtrl', [ '$scope', '$window', 'melmService', function($scope, $window, melmService) {
  $scope.go = function(path) {
    melmService(path);
  };

  $scope.setAnchor = function(anchor) {
    $scope.anchor = anchor;
  };

  $scope.getClass = function(anchor) {
    var mainClass = 'anchor' + ' anchor-' + anchor;
    if ($scope.anchor === anchor) {
      return mainClass + ' anchor-selected';
    }
    return mainClass;
  };

  $scope.anchor = $window.anchor;

  $scope.iconChoice = "leave";
  $scope.iconSelectedChoice = "leave";

} ]);