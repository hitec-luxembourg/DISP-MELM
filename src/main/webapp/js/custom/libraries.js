app.controller('LibrariesCtrl', [ '$scope', '$http', 'melmService', function($scope, $http, melmService) {
  $scope.loadResources = function() {
    melmService.loadResources($scope, '/rest/libraries/json');
  };

  $scope.go = function(path) {
    melmService.go(path);
  };

  $scope.confirmDelete = function(id) {
    BootstrapDialog.confirm('Do you really want to delete this resource ?', function(result) {
      if (result) {
        $scope.deleteResource(id);
      }
    });
  };

  $scope.deleteResource = function(id) {
    var params = melmService.encodeParams({
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

  $scope.loadingVisible = false;
  $scope.itemsPerPage = 8;
  $scope.currentPage = 1;
  $scope.predicate = 'name';
  $scope.loadResources();
} ]);
