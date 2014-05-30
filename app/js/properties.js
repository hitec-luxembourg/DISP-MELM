app.controller('PropertiesCtrl', [ '$scope', '$http', 'melmService', function($scope, $http, melmService) {
  'use strict';
  
  $scope.loadResources = function(id) {
    melmService.loadResources($scope, '/rest/libraries/icons/properties/json/' + id, function() {
      var data = $scope.resources;
      if (data && data.properties && 0 < data.properties.length) {
        for (var i = 0; i < data.properties.length; i++) {
          var property = data.properties[i];
          angular.extend(property, {
            checked : false
          });
        }
      }
    });
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
    if (!$scope.newResource) {
      $scope.newResource = {};
    }
    $scope.newResource.uniqueName = "";
    $scope.newResource.type = "";
  };

  $scope.createResource = function(data) {
    var uniqueName = data ? data.uniqueName : "";
    var type = data ? data.type : "";
    melmService.post({
      params : {
        "id" : melmService.getRESTParameter('properties/'),
        "uniqueName" : typeof uniqueName !== "undefined" ? uniqueName : "",
        "type" : typeof type !== "undefined" ? type : ""
      },
      url : '/rest/libraries/icons/properties/add',
      successCallback : function() {
        $scope.resetResource();
        $scope.loadResources(melmService.getRESTParameter('properties/'));
      },
      errorCallback : function(data) {
        BootstrapDialog.alert({
          title : 'ERROR',
          message : data,
          type : BootstrapDialog.TYPE_DANGER,
          closable : true,
          buttonLabel : 'Close'
        });
      }
    });
  };

  $scope.updateResource = function(data, id) {
    var uniqueName = data ? data.uniqueName : "";
    var type = data ? data.type : "";
    melmService.post({
      params : {
        "id" : id,
        "uniqueName" : typeof uniqueName !== "undefined" ? uniqueName : "",
        "type" : typeof type !== "undefined" ? type : ""
      },
      url : '/rest/libraries/icons/properties/update',
      successCallback : function() {
        $scope.loadResources(melmService.getRESTParameter('properties/'));
      },
      errorCallback : function(data) {
        BootstrapDialog.alert({
          title : 'ERROR',
          message : data,
          type : BootstrapDialog.TYPE_DANGER,
          closable : true,
          buttonLabel : 'Close'
        });
      }
    });
  };

  $scope.confirmDelete = function(id) {
    melmService.confirmDelete($scope, id);
  };

  $scope.confirmDeleteMultiple = function() {
    melmService.confirmDeleteMultiple($scope);
  };

  $scope.deleteResource = function(id) {
    melmService.post({
      params : {
        "id" : id
      },
      url : '/rest/libraries/icons/properties/delete',
      successCallback : function() {
        $scope.loadResources(melmService.getRESTParameter('properties/'));
      },
      errorCallback : function() {
        BootstrapDialog.alert({
          title : 'ERROR',
          message : 'Resource deletion threw an error.',
          type : BootstrapDialog.TYPE_DANGER,
          closable : true,
          buttonLabel : 'Close'
        });
      }
    });
  };

  $scope.deleteResources = function() {
    var ids = [];
    if ($scope.resources && 0 < $scope.resources.length) {
      for (var i = 0; i < $scope.resources.length; i++) {
        var property = $scope.resources[i];
        if (property.checked) {
          ids.push(property.id);
        }
      }
    }
    melmService.post({
      params : {
        "ids" : ids
      },
      url : '/rest/libraries/icons/properties/deleteMultiple',
      successCallback : function() {
        $scope.loadResources(melmService.getRESTParameter('properties/'));
      },
      errorCallback : function() {
        BootstrapDialog.alert({
          title : 'ERROR',
          message : 'Resources deletion threw an error.',
          type : BootstrapDialog.TYPE_DANGER,
          closable : true,
          buttonLabel : 'Close'
        });
      }
    });
  };

  $scope.back = function() {
    melmService.back();
  };

  $scope.allClicked = function() {
    var newValue = !$scope.allChecked();
    for (var i = 0; i < $scope.resources.length; i++) {
      var property = $scope.resources[i];
      property.checked = newValue;
    }
  };

  $scope.allChecked = function() {
    var result = true;
    if (!$scope.resources || 0 === $scope.resources.length) {
      result = false;
    } else {
      for (var i = 0; i < $scope.resources.length; i++) {
        var property = $scope.resources[i];
        if (!property.checked) {
          result = false;
          break;
        }
      }
    }
    return result;
  };

  $scope.someSelected = function() {
    var result = false;
    if ($scope.resources && 0 < $scope.resources.length) {
      for (var i = 0; i < $scope.resources.length; i++) {
        var property = $scope.resources[i];
        if (property.checked) {
          result = true;
          break;
        }
      }
    }
    return result;
  };

  $scope.loadingVisible = false;
  $scope.itemsPerPage = 7;
  $scope.currentPage = 1;
  $scope.predicate = 'uniqueName';
  $scope.loadResources(melmService.getRESTParameter('properties/'));
} ]);
