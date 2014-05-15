app.controller('PropertiesCtrl', [ '$scope', '$http', '$location', 'Pagination', function($scope, $http, $location, Pagination) {
  $scope.loadResources = function(id) {
    $http.get(melmContextRoot + '/rest/libraries/icons/properties/json/' + id).success(function(data) {
      $scope.properties = data;
      $scope.pagination.numPages = Math.ceil($scope.properties.length / $scope.pagination.perPage);
    });
  };

  $scope.customPropertyTypes = [ {
    id : 'BOOLEAN',
    title : 'Boolean'
  }, {
    id : 'DATE',
    title : 'Date'
  }, {
    id : 'INTEGER',
    title : 'Integer'
  }, {
    id : 'STRING',
    title : 'String'
  } ];

  $scope.resetResource = function() {
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
      "uniqueName" : uniqueName,
      "type" : type
    });
    $http.post(melmContextRoot + '/rest/libraries/icons/properties/add', params, {
      headers : {
        'Content-Type' : 'application/x-www-form-urlencoded'
      }
    }).success(function() {
      $scope.resetResource();
      $scope.loadResources(getRESTParameter('properties'));
    }).error(function() {
      alert("Resource creation threw an error.");
    });
  };

  /*$scope.updateResource = function(data, key, value) {
    var uniqueName = data ? data.uniqueName : "";
    var type = data ? data.type : "";
    var params = encodeParams({
      "id" : getRESTParameter('properties/'),
      "uniqueName" : uniqueName,
      "type" : type
    });
    $http.post(melmContextRoot + '/rest/libraries/icons/properties/update', params, {
      headers : {
        'Content-Type' : 'application/x-www-form-urlencoded'
      }
    }).success(function() {
      $scope.resetResource();
      $scope.loadResources(getRESTParameter('properties'));
    }).error(function() {
      alert("Resource creation threw an error.");
    });
  };*/

  $scope.deleteResource = function(id) {
    if (!confirm("Do you really want to delete this resource ?")) {
      return;
    }
    var params = encodeParams({
      "id" : id
    });
    $http.post(melmContextRoot + '/rest/libraries/icons/properties/delete', params, {
      headers : {
        'Content-Type' : 'application/x-www-form-urlencoded'
      }
    }).success(function() {
      $scope.loadResources(getRESTParameter('properties'));
    }).error(function() {
      alert("Resource deletion threw an error.");
    });
  };

  $scope.pagination = Pagination.getNew(10);
  $scope.predicate = 'uniqueName';
  $scope.loadResources(getRESTParameter('properties'));
} ]);