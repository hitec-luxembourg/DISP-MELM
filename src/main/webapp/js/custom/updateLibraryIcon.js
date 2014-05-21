app.controller('UpdateLibraryIconCtrl', [ '$scope', '$http', '$window', 'Pagination', function($scope, $http, $window, Pagination) {
  $scope.loadResources = function() {
    $scope.loadingVisible = true;
    $http.get(melmContextRoot + '/rest/icons/linked/json').success(function(data) {
      $scope.loadingVisible = false;
      $scope.icons = data;
      $scope.pagination.numPages = Math.ceil($scope.icons.length / $scope.pagination.perPage);
    });
  };

  $scope.back = function() {
    $window.history.back();
  };

  $scope.selectImage = function(id, libraries) {
    if (libraries.length > 0) {
      for (var i = 0; i < libraries.length; i++) {
        if (libraries[i].id === $scope.libraryId && id !== $scope.initialIconId) {
          return;
        }
      }
    }
    $scope.iconId = id;
  };

  $scope.isSelected = function(id) {
    return $scope.iconId === id;
  };

  $scope.getClasses = function(id, libraries) {
    var mainClasses = 'col-xs-4 col-sm-2 col-md-1 icon-wrapper';
    if ($scope.iconId === id) {
      return mainClasses + ' icon-selected';
    }
    if (libraries.length > 0) {
      for (var i = 0; i < libraries.length; i++) {
        if (libraries[i].id === $scope.libraryId && id !== $scope.initialIconId) {
          return mainClasses + ' icon-wrapper-disabled icon-disabled';
        }
      }
    }
    return mainClasses;
  };

  $scope.loadingVisible = false;
  $scope.libraryId = $window.libraryId;
  $scope.iconId = $window.iconId;
  $scope.initialIconId = $window.iconId;
  $scope.pagination = Pagination.getNew(48);
  $scope.loadResources();

} ]);