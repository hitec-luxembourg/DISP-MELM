app.controller('AddLibraryIconCtrl', [ '$scope', '$http', '$window', 'melmService', function($scope, $http, $window, melmService) {
  'use strict';
  
  $scope.loadResources = function() {
    melmService.loadResources($scope, '/rest/icons/linked/json');
  };

  $scope.back = function() {
    melmService.back();
  };

  $scope.selectImage = function(id, libraries) {
    if (libraries.length > 0) {
      for (var i = 0; i < libraries.length; i++) {
        if (libraries[i].id === $scope.libraryId) {
          return;
        }
      }
    }
    $scope.id = id;
  };

  $scope.isSelected = function(id) {
    return $scope.id === id;
  };

  $scope.getClasses = function(id, libraries) {
    var mainClasses = 'col-xs-4 col-sm-2 col-md-1 icon-wrapper';
    if (libraries.length > 0) {
      for (var i = 0; i < libraries.length; i++) {
        if (libraries[i].id === $scope.libraryId) {
          return mainClasses + ' icon-wrapper-disabled icon-disabled';
        }
      }
    }
    if ($scope.id === id) {
      return mainClasses + ' icon-selected';
    }
    return mainClasses;
  };

  $scope.isAddActive = function() {
    return $scope.iconName && "" !== $scope.iconName && $scope.iconDescription && "" !== $scope.iconDescription; 
  };

  $scope.loadingVisible = false;
  $scope.libraryId = $window.libraryId;
  $scope.id = -1;
  $scope.itemsPerPage = 48;
  $scope.currentPage = 1;
  $scope.loadResources();

} ]);