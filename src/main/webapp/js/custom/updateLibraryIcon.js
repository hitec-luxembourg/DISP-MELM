app.controller('UpdateLibraryIconCtrl', [ '$scope', '$http', '$window', function($scope, $http, $window) {
  $scope.loadResources = function() {
    $scope.loadingVisible = true;
    $http.get(melmContextRoot + '/rest/icons/linked/json').success(function(data) {
      $scope.loadingVisible = false;
      $scope.icons = data;
      $scope.processLinks(data);
      $scope.totalItems = $scope.icons.length;
    });
  };

  $scope.links = {};
  
  $scope.processLinks = function(data) {
    $scope.links = {};
    if(data && 0 < data.length) {
      for(var i = 0; i < data.length; i++) {
        var icon = data[i].icon;
        $scope.links[icon.id] = melmContextRoot + "/rest/icons/file/" + icon.id + "/MEDIUM";
      }
    }
  };

  $scope.changeImage = function(id, which) {
    $scope.links[id] = melmContextRoot + "/rest/icons/file/" + which + id + "/MEDIUM";
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
  $scope.itemsPerPage = 48;
  $scope.currentPage = 1;
  $scope.loadResources();

} ]);