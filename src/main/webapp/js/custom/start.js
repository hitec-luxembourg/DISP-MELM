app.controller('StartCtrl', function($scope, $http, $location) {
  $scope.go = function(path) {
    window.location = melmContextRoot + path;
  };

});