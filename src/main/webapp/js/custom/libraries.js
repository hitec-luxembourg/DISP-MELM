var app = angular.module('app', []);

app.controller('LibrariesCtrl', function($scope, $http, $location) {
  $scope.loadResources = function() {
    $http.get(melmContextRoot + '/rest/libraries').success(function(data) {
      $scope.libraries = data;
    });
  };

  $scope.deleteResource = function(id) {
    if (!confirm("Do you really want to delete this item ?")) {
      return;
    }
    var params = encodeParams({
      "id" : id
    });
    $http.post(melmContextRoot + '/rest/libraries/delete', params, {
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

  $scope.predicate = 'name';
  $scope.loadResources();
});