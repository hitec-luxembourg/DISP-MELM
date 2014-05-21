app.controller('AddLibraryIconCtrl', [ '$scope', '$http', '$window', 'Pagination', function($scope, $http, $window, Pagination) {
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

  $scope.loadingVisible = false;
  $scope.libraryId = $window.libraryId;
  $scope.id = -1;
  $scope.pagination = Pagination.getNew(36);
  $scope.loadResources();

} ]);