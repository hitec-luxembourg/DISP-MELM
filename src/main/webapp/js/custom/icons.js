app.controller('IconsCtrl', ['$scope', '$http', 'Pagination', function($scope, $http, Pagination) {
  $scope.loadResources = function() {
    $http.get(melmContextRoot + '/rest/icons/linked/json').success(function(data) {
      $scope.icons = data;
      $scope.pagination.numPages = Math.ceil($scope.icons.length/$scope.pagination.perPage);
    });
  };

  $scope.confirmDelete = function(id) {
    BootstrapDialog.confirm('Do you really want to delete this resource ?', function(result) {
      if (result) {
        $scope.deleteResource(id);
      }
    });
  };

  $scope.hasLibraries = function(anIcon) {
    return anIcon && anIcon.libraries && 0 < anIcon.libraries.length;
  };
  
  $scope.deleteResource = function(id) {
    $scope.error = null;
    var params = encodeParams({
      "id" : id
    });
    $http.post(melmContextRoot + '/rest/icons/delete', params, {
      headers : {
        'Content-Type' : 'application/x-www-form-urlencoded'
      }
    }).success(function() {
      $scope.loadResources();
    }).error(function(responseData) {
      $scope.error = responseData;
    });
  };

  $scope.go = function(path) {
    window.location = melmContextRoot + path;
  };

  $scope.error = null;
  $scope.pagination = Pagination.getNew(10);
  $scope.predicate = 'icon.displayName';
  $scope.loadResources();
}]);