app.controller('IconDetailsCtrl', function($scope, $http) {
  $scope.imageId = imageId;
  
  $scope.link = {};
  
  $scope.link["LARGE"] = melmContextRoot + "/rest/icons/file/" + $scope.imageId + "/LARGE";
  $scope.link["MEDIUM"] = melmContextRoot + "/rest/icons/file/" + $scope.imageId + "/MEDIUM";
  $scope.link["SMALL"] = melmContextRoot + "/rest/icons/file/" + $scope.imageId + "/SMALL";
  $scope.link["TINY"] = melmContextRoot + "/rest/icons/file/" + $scope.imageId + "/TINY";
  
  $scope.changeImage = function(size, which) {
      $scope.link[size] = melmContextRoot + "/rest/icons/file/" + which + $scope.imageId + "/" + size;
  };
  
  $scope.go = function(path) {
    window.location = melmContextRoot + path;
  };

});