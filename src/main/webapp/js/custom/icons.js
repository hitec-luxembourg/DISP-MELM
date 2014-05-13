var app = angular.module('app', []);

app.controller('IconsCtrl', function($scope, $http) {
  $scope.loadResources = function() {
    $http.get(melmContextRoot + '/rest/icons').success(function(data) {
      $scope.icons = data;
    });
  };

  $scope.deleteResource = function(id) {
    if (!confirm("Do you really want to delete this item ?")) {
      return;
    }
    var params = encodeParams({
      "id" : id
    });
    $http.post(melmContextRoot + '/rest/icons/delete', params, {
      headers : {
        'Content-Type' : 'application/x-www-form-urlencoded'
      }
    }).success(function() {
      $scope.loadResources();
    }).error(function() {
      alert("Item deletion threw an error.");
    });
  };

  $scope.go = function(path) {
    window.location = melmContextRoot + path;
  };

  $scope.predicate = 'displayName';
  $scope.loadResources();
});