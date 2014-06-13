app.controller('LibrariesCtrl', [ '$scope', '$http', 'melmService', function($scope, $http, melmService) {
  'use strict';

  $scope.loadResources = function() {

    // Load the libraries
    melmService.loadResources($scope, '/rest/libraries/json', function() {
      var data_ = $scope.resources;
      if (data_ && 0 < data_.length) {

        // See if the libraries have elements
        $http.get(melmContextRoot + '/rest/countLibrariesElements/json').success(function(data, status, headers, config) {
          $scope.librariesElements = data;
          for (var i = 0; i < $scope.resources.length; i++) {
            var library = $scope.resources[i];
            angular.extend(library, {
              checked : false
            });
            if ($scope.librariesElements && $scope.librariesElements[library.id] && 0 < $scope.librariesElements[library.id]) {
              angular.extend(library, {
                hasElements : true
              });
            } else {
              angular.extend(library, {
                hasElements : false
              });
            }
          }
        }).error(function(data, status, headers, config) {
          // N/A
        });
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
        dialogs.error('Error', 'Resource deletion threw an error.');
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
        dialogs.error('Error', 'Resources deletion threw an error.');
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
