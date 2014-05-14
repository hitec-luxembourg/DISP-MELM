app.controller('LibrariesCtrl', [ '$scope', '$http', '$location', 'Pagination', function($scope, $http, $location, Pagination) {
  $scope.loadResources = function() {
    $http.get(melmContextRoot + '/rest/libraries').success(function(data) {
      $scope.libraries = data;
      $scope.pagination.numPages = Math.ceil($scope.libraries.length / $scope.pagination.perPage);
    });
  };

  $scope.deleteResource = function(id) {
    if (!confirm("Do you really want to delete this resource ?")) {
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
      alert("Resource deletion threw an error.");
    });
  };

  $scope.go = function(path) {
    window.location = melmContextRoot + path;
  };

  $scope.pagination = Pagination.getNew(10);
  $scope.predicate = 'name';
  $scope.loadResources();
} ]);