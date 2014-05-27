app.controller('CloneLibraryCtrl', [ '$scope', 'melmService', function($scope, melmService) {
  $scope.back = function() {
    melmService.back();
  };

  $scope.iconChoice = "existing";
} ]);