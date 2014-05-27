app.controller('PropertiesCtrl', [ '$scope', '$http', 'melmService', function($scope, $http, melmService) {
  $scope.loadResources = function(id) {
    melmService.loadResources($scope, '/rest/libraries/icons/properties/json/' + id);
  };

  $scope.customPropertyTypes = [ {
    id : 'DATE',
    title : 'DATE'
  }, {
    id : 'INTEGER',
    title : 'INTEGER'
  }, {
    id : 'STRING',
    title : 'STRING'
  } ];

  $scope.resetResource = function() {
    $scope.error = null;
    if (!$scope.newResource) {
      $scope.newResource = {};
    }
    $scope.newResource.uniqueName = "";
    $scope.newResource.type = "";
  };

  $scope.createResource = function(data) {
    var uniqueName = data ? data.uniqueName : "";
    var type = data ? data.type : "";
    var params = melmService.encodeParams({
      "id" : melmService.getRESTParameter('properties/'),
      "uniqueName" : typeof uniqueName !== "undefined" ? uniqueName : "",
      "type" : typeof type !== "undefined" ? type : ""
    });
    $http.post(melmContextRoot + '/rest/libraries/icons/properties/add', params, {
      headers : {
        'Content-Type' : 'application/x-www-form-urlencoded'
      }
    }).success(function() {
      $scope.resetResource();
      $scope.loadResources(melmService.getRESTParameter('properties/'));
    }).error(function(responseData) {
      $scope.error = responseData;
    });
  };

  $scope.updateResource = function(data, id) {
    var uniqueName = data ? data.uniqueName : "";
    var type = data ? data.type : "";
    var params = melmService.encodeParams({
      "id" : id,
      "uniqueName" : typeof uniqueName !== "undefined" ? uniqueName : "",
      "type" : typeof type !== "undefined" ? type : ""
    });
    $http.post(melmContextRoot + '/rest/libraries/icons/properties/update', params, {
      headers : {
        'Content-Type' : 'application/x-www-form-urlencoded'
      }
    }).success(function() {
      $scope.loadResources(melmService.getRESTParameter('properties/'));
    }).error(function(responseData) {
      $scope.error = responseData;
      $scope.loadResources(melmService.getRESTParameter('properties/'));
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
    var params = melmService.encodeParams({
      "id" : id
    });
    $http.post(melmContextRoot + '/rest/libraries/icons/properties/delete', params, {
      headers : {
        'Content-Type' : 'application/x-www-form-urlencoded'
      }
    }).success(function() {
      $scope.loadResources(melmService.getRESTParameter('properties/'));
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

  $scope.back = function() {
    melmService.back();
  };

  $scope.loadingVisible = false;
  $scope.error = null;
  $scope.itemsPerPage = 7;
  $scope.currentPage = 1;
  $scope.predicate = 'uniqueName';
  $scope.loadResources(melmService.getRESTParameter('properties/'));
} ]);
