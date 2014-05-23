app.controller('CloneLibraryCtrl', [ '$scope', function($scope) {
  $scope.go = function(path) {
    window.location = melmContextRoot + path;
  };

  $scope.iconChoice = "existing";
} ]);