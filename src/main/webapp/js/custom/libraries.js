app.controller('LibrariesCtrl', [ '$scope', '$http', 'Pagination', function($scope, $http, Pagination) {
  $scope.loadResources = function() {
    $scope.loadingVisible = true;
    $http.get(melmContextRoot + '/rest/libraries/json').success(function(data) {
      $scope.loadingVisible = false;
      $scope.libraries = data;
      $scope.pagination.numPages = Math.ceil($scope.libraries.length / $scope.pagination.perPage);
    });
  };

  $scope.confirmDelete = function(id) {
    BootstrapDialog.confirm('Do you really want to delete this resource ?', function(result) {
      if (result) {
        $scope.deleteResource(id);
      }
    });
  };

  $scope.deleteResource = function(id) {
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
      BootstrapDialog.alert({
        title : 'ERROR',
        message : 'Resource deletion threw an error.',
        type : BootstrapDialog.TYPE_DANGER, // <-- Default value is BootstrapDialog.TYPE_PRIMARY
        closable : true, // <-- Default value is true
        buttonLabel : 'Close', // <-- Default value is 'OK',
        callback : function(result) {
        }
      });
    });
  };

  $scope.go = function(path) {
    window.location = melmContextRoot + path;
  };

  $scope.loadingVisible = false;
  $scope.pagination = Pagination.getNew(10);
  $scope.predicate = 'name';
  $scope.loadResources();
} ]);