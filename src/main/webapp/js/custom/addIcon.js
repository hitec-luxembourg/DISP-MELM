app.controller('AddIconCtrl', [ '$scope', 'melmService', function($scope, melmService) {
  $scope.go = function(path) {
    melmService.go(path);
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

  $scope.anchor = "SE";
  $scope.iconSelectedChoice = "generate";

} ]);