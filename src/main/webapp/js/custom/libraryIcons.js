var app = angular.module('app', []);

app.controller('LibraryIconsCtrl', function($scope, $http, $location) {
  $scope.loadResources = function(id) {
    $http.get(melmContextRoot + '/rest/libraries/icons/'+id).success(function(data) {
      $scope.libraryIconsModel = data;
    });
  };

  $scope.deleteResource = function(id) {
    if (!confirm("Do you really want to delete this item ?")) {
      return;
    }
    var params = encodeParams({
      "id" : id
    });
    $http.post(melmContextRoot + '/rest/libraries/icons/delete', params, {
      headers : {
        'Content-Type' : 'application/x-www-form-urlencoded'
      }
    }).success(function() {
      $scope.loadResources(getRESTParameter('icons'));
    }).error(function() {
      alert("Item deletion threw an error.");
    });
  };

  $scope.go = function(path) {
    window.location = melmContextRoot + path;
  };

  $scope.predicate = 'indexOfIconInLibrary';
  $scope.loadResources(getRESTParameter('icons'));
});