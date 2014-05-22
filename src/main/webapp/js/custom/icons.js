app.controller('IconsCtrl', [ '$scope', '$http', function($scope, $http) {
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
  
  $scope.confirmDelete = function(id) {
    BootstrapDialog.confirm('Do you really want to delete this resource ?', function(result) {
      if (result) {
        $scope.deleteResource(id);
      }
    });
  };

  $scope.hasLibraries = function(anIcon) {
    return anIcon && anIcon.libraries && 0 < anIcon.libraries.length;
  };
  
  $scope.deleteResource = function(id) {
    $scope.error = null;
    var params = encodeParams({
      "id" : id
    });
    $http.post(melmContextRoot + '/rest/icons/delete', params, {
      headers : {
        'Content-Type' : 'application/x-www-form-urlencoded'
      }
    }).success(function() {
      $scope.loadResources();
    }).error(function(responseData) {
      $scope.error = responseData;
    });
  };

  $scope.go = function(path) {
    window.location = melmContextRoot + path;
  };

  $scope.loadingVisible = false;
  $scope.error = null;
  $scope.itemsPerPage = 8;
  $scope.currentPage = 1;
  $scope.predicate = 'icon.displayName';
  $scope.loadResources();
} ]);
