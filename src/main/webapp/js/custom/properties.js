app.controller('PropertiesCtrl', [ '$scope', '$http', '$window', 'Pagination', function($scope, $http, $window, Pagination) {
  $scope.loadResources = function(id) {
    $scope.loadingVisible = true;
    $http.get(melmContextRoot + '/rest/libraries/icons/properties/json/' + id).success(function(data) {
      $scope.loadingVisible = false;
      $scope.properties = data;
      $scope.pagination.numPages = Math.ceil($scope.properties.length / $scope.pagination.perPage);
    });
  };

  $scope.customPropertyTypes = [ {
    id : 'BOOLEAN',
    title : 'BOOLEAN'
  }, {
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
    var params = encodeParams({
      "id" : getRESTParameter('properties/'),
      "uniqueName" : typeof uniqueName !== "undefined" ? uniqueName : "",
      "type" : typeof type !== "undefined" ? type : ""
    });
    $http.post(melmContextRoot + '/rest/libraries/icons/properties/add', params, {
      headers : {
        'Content-Type' : 'application/x-www-form-urlencoded'
      }
    }).success(function() {
      $scope.resetResource();
      $scope.loadResources(getRESTParameter('properties/'));
    }).error(function(responseData) {
      $scope.error = responseData;
    });
  };

  $scope.updateResource = function(data, id) {
    var uniqueName = data ? data.uniqueName : "";
    var type = data ? data.type : "";
    var params = encodeParams({
      "id" : id,
      "uniqueName" : typeof uniqueName !== "undefined" ? uniqueName : "",
      "type" : typeof type !== "undefined" ? type : ""
    });
    $http.post(melmContextRoot + '/rest/libraries/icons/properties/update', params, {
      headers : {
        'Content-Type' : 'application/x-www-form-urlencoded'
      }
    }).success(function() {
      $scope.loadResources(getRESTParameter('properties/'));
    }).error(function(responseData) {
      $scope.error = responseData;
      $scope.loadResources(getRESTParameter('properties/'));
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
    $http.post(melmContextRoot + '/rest/libraries/icons/properties/delete', params, {
      headers : {
        'Content-Type' : 'application/x-www-form-urlencoded'
      }
    }).success(function() {
      $scope.loadResources(getRESTParameter('properties/'));
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
    $window.history.back();
  };

  $scope.loadingVisible = false;
  $scope.error = null;
  $scope.pagination = Pagination.getNew(10);
  $scope.predicate = 'uniqueName';
  $scope.loadResources(getRESTParameter('properties/'));
} ]);