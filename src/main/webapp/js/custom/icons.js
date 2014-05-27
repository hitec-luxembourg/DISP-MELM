app.controller('IconsCtrl', [ '$scope', '$http', 'melmService', function($scope, $http, melmService) {
  $scope.loadResources = function() {
    melmService.loadResources($scope, '/rest/icons/linked/json', function() {
      $scope.processLinks($scope.resources);
    });
  };

  $scope.links = {};

  $scope.processLinks = function(data) {
    $scope.links = {};
    if (data && 0 < data.length) {
      for (var i = 0; i < data.length; i++) {
        var icon = data[i].icon;
        $scope.links[icon.id] = melmContextRoot + "/rest/icons/file/" + icon.id + "/MEDIUM";

        // Extend the given data to perform libraries ordering in the UI
        var libraries = "";
        if (data[i] && data[i].libraries && 0 < data[i].libraries.length) {
          libraries = data[i].libraries.sort(function compare(a, b) {
            if (a.name < b.name)
              return -1;
            if (a.name > b.name)
              return 1;
            return 0;
          }).map(function(elem) {
            return elem.name;
          }).join(",");
        }
        angular.extend(data[i].icon, {
          librariesAsString : libraries
        });
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
    melmService.post({
      params : {
        "id" : id
      },
      url : '/rest/icons/delete',
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

  $scope.go = function(path) {
    melmService.go(path);
  };

  $scope.loadingVisible = false;
  $scope.itemsPerPage = 8;
  $scope.currentPage = 1;
  $scope.predicate = 'icon.displayName';
  $scope.loadResources();
} ]);
