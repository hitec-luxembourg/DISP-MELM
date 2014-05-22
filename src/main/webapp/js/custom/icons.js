app.controller('IconsCtrl', [ '$scope', '$http', function($scope, $http) {
  $scope.loadResources = function() {
    $scope.loadingVisible = true;
    $http.get(melmContextRoot + '/rest/icons/linked/json').success(function(data) {
      $scope.loadingVisible = false;
      $scope.icons = data;
      $scope.totalItems = $scope.icons.length;
    });
  };

  $scope.deleteResource = function(id) {
    $scope.error = null;
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
    }).error(function(responseData) {
      $scope.error = responseData;
    });
  };

  $scope.go = function(path) {
    window.location = melmContextRoot + path;
  };

  $scope.loadingVisible = false;
  $scope.error = null;
  $scope.itemsPerPage = 8;
  $scope.currentPage = 1;
  $scope.predicate = 'icon.displayName';
  $scope.loadResources();
} ]);