app.controller('LibraryIconsCtrl', [ '$scope', '$http', '$location', 'Pagination', function($scope, $http, $location, Pagination) {
  $scope.loadResources = function(id) {
    $http.get(melmContextRoot + '/rest/libraries/icons/'+id).success(function(data) {
      $scope.libraryIconsModel = data;
      $scope.pagination.numPages = Math.ceil($scope.libraryIconsModel.icons.length / $scope.pagination.perPage);
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

  $scope.pagination = Pagination.getNew(10);
  $scope.predicate = 'indexOfIconInLibrary';
  $scope.loadResources(getRESTParameter('icons'));
}]);