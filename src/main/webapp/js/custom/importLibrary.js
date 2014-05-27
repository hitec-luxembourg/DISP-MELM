app.controller('ImportLibraryCtrl', [ '$scope', 'melmService', function($scope, melmService) {
  $scope.go = function(path) {
    melmService.go(path);
  };

} ]);