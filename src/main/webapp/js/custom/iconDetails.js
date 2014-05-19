app.controller('IconDetailsCtrl', function($scope, $http) {
  $scope.go = function(path) {
    window.location = melmContextRoot + path;
  };

});