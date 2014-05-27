app.controller('StartCtrl', [ '$scope', 'melmService', function($scope, melmService) {
  $scope.go = function(path) {
    melmService.go(path);
  };

} ]);