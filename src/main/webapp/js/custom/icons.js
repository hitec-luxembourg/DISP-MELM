app.controller('IconsCtrl', [ '$scope', '$http', 'melmService', function($scope, $http, melmService) {
  $scope.loadResources = function() {
    melmService.loadResources($scope, '/rest/icons/linked/json', function() {
      $scope.processLinks($scope.resources);
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

  $scope.hasLibraries = function(anIcon) {
    return anIcon && anIcon.libraries && 0 < anIcon.libraries.length;
  };
  
  $scope.confirmDelete = function(id) {
    melmService.confirmDelete($scope, id);
  };
  
  $scope.deleteResource = function(id) {
    $scope.error = null;
    melmService.post({
      params : {
        "id" : id
      },
      url : '/rest/icons/delete',
      successCallback : function() {
        $scope.loadResources();
      },
      errorCallback : function() {
        $scope.error = responseData;
      }
    });
  };

  $scope.go = function(path) {
    melmService.go(path);
  };

  $scope.loadingVisible = false;
  $scope.error = null;
  $scope.itemsPerPage = 8;
  $scope.currentPage = 1;
  $scope.predicate = 'icon.displayName';
  $scope.loadResources();
} ]);
