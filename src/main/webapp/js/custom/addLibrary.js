app.controller('AddLibraryCtrl', [ '$scope', '$window', function($scope, $window) {
  $scope.go = function(path) {
    window.location = melmContextRoot + path;
  };

} ]);