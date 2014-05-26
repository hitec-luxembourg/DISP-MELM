app.controller('AddIconCtrl', [ '$scope', '$window', function($scope, $window) {
  $scope.go = function(path) {
    window.location = melmContextRoot + path;
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