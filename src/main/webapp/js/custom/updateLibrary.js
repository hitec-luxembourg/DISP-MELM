app.controller('UpdateLibraryCtrl', [ '$scope', function($scope) {
  $scope.go = function(path) {
    window.location = melmContextRoot + path;
  };

} ]);