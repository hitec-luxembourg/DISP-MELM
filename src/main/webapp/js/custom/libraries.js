app.controller('LibrariesCtrl', [ '$scope', '$http', 'melmService', function($scope, $http, melmService) {
  $scope.loadResources = function() {
    melmService.loadResources($scope, '/rest/libraries/json', function() {
      var data = $scope.resources;
      if (data && data.libraries && 0 < data.libraries.length) {
        for (var i = 0; i < data.libraries.length; i++) {
          var library = data.libraries[i];
          angular.extend(library, {
            checked : false
          });
        }
      }
    });
  };

  $scope.go = function(path) {
    melmService.go(path);
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
      url : '/rest/libraries/delete',
      successCallback : function() {
        $scope.loadResources();
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
        var library = $scope.resources[i];
        if (library.checked) {
          ids.push(library.id);
        }
      }
    }
    melmService.post({
      params : {
        "ids" : ids
      },
      url : '/rest/libraries/deleteMultiple',
      successCallback : function() {
        $scope.loadResources();
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

  $scope.allClicked = function() {
    var newValue = !$scope.allChecked();
    for (var i = 0; i < $scope.resources.length; i++) {
      var library = $scope.resources[i];
      library.checked = newValue;
    }
  };

  $scope.allChecked = function() {
    var result = true;
    if (!$scope.resources || 0 === $scope.resources.length) {
      result = false;
    } else {
      for (var i = 0; i < $scope.resources.length; i++) {
        var library = $scope.resources[i];
        if (!library.checked) {
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
        var library = $scope.resources[i];
        if (library.checked) {
          result = true;
          break;
        }
      }
    }
    return result;
  };

  $scope.loadingVisible = false;
  $scope.itemsPerPage = 8;
  $scope.currentPage = 1;
  $scope.predicate = 'name';
  $scope.loadResources();
} ]);
