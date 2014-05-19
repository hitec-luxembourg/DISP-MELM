app.controller('UpdateLibraryIconCtrl', [ '$scope', '$window', function($scope, $window) {
  $scope.back = function() {
    $window.history.back();
  };

} ]);