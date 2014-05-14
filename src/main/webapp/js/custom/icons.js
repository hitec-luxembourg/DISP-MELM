app.controller('IconsCtrl', ['$scope', '$http', 'Pagination', function($scope, $http, Pagination) {
  $scope.loadResources = function() {
    $http.get(melmContextRoot + '/rest/icons').success(function(data) {
      $scope.icons = data;
      $scope.pagination.numPages = Math.ceil($scope.icons.length/$scope.pagination.perPage);
    });
  };

  $scope.deleteResource = function(id) {
    if (!confirm("Do you really want to delete this resource ?")) {
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
      alert("Resource deletion threw an error.");
    });
  };

  $scope.go = function(path) {
    window.location = melmContextRoot + path;
  };

  $scope.pagination = Pagination.getNew(10);
  $scope.predicate = 'displayName';
  $scope.loadResources();
}]);