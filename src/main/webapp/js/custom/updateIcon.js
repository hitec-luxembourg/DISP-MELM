app.controller('UpdateIconCtrl', [ '$scope', '$window', function($scope, $window) {
  $scope.go = function(path) {
    window.location = melmContextRoot + path;
  };

<<<<<<< HEAD
  $scope.setAnchor = function(anchor) {
=======
  /*$scope.setAnchor = function(anchor) {
>>>>>>> 8652af4b4a50103a82114231e8ad4f65f5d0615b
    $scope.anchor = anchor;
  };

  $scope.getClass = function(anchor) {
    var mainClass = 'anchor';
    if ($scope.anchor === anchor) {
      return mainClass + ' anchor-selected';
    }
    return mainClass;
  };

<<<<<<< HEAD
  $scope.anchor = $window.anchor;
=======
  $scope.anchor = "SE";*/
>>>>>>> 8652af4b4a50103a82114231e8ad4f65f5d0615b

} ]);