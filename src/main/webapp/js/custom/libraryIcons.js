app.controller('LibraryIconsCtrl', [
    '$scope',
    '$http',
    'melmService',
    function($scope, $http, melmService) {
      $scope.loadResources = function(id) {
        melmService.loadResources($scope, '/rest/libraries/icons/json/' + id, function() {
          $scope.processLinks($scope.resources);
          // Override default instruction $scope.totalItems = $scope.resources.length;
          $scope.totalItems = $scope.resources.icons.length;
        });
      };

      $scope.confirmDelete = function(id) {
        BootstrapDialog.confirm('Do you really want to delete this resource ?', function(result) {
          if (result) {
            $scope.deleteResource(id);
          }
        });
      };

      $scope.links = {};

      $scope.processLinks = function(data) {
        $scope.links = {};
        if (data && data.icons && 0 < data.icons.length) {
          for (var i = 0; i < data.icons.length; i++) {
            var icon = data.icons[i].icon;
            $scope.links[icon.id] = melmContextRoot + "/rest/icons/file/" + icon.id + "/MEDIUM";
          }
        }
      };

      $scope.changeImage = function(id, which) {
        $scope.links[id] = melmContextRoot + "/rest/icons/file/" + which + id + "/MEDIUM";
      };

      $scope.deleteResource = function(id) {
        var params = melmService.encodeParams({
          "id" : id
        });
        $http.post(melmContextRoot + '/rest/libraries/icons/delete', params, {
          headers : {
            'Content-Type' : 'application/x-www-form-urlencoded'
          }
        }).success(function() {
          $scope.loadResources(melmService.getRESTParameter('icons/'));
        }).error(function() {
          BootstrapDialog.alert({
            title : 'ERROR',
            message : 'Resource deletion threw an error.',
            type : BootstrapDialog.TYPE_DANGER, // <-- Default value is BootstrapDialog.TYPE_PRIMARY
            closable : true, // <-- Default value is true
            buttonLabel : 'Close', // <-- Default value is 'OK',
            callback : function(result) {
            }
          });
        });
      };

      $scope.go = function(path) {
        melmService.go(path);
      };

      $scope.move = function(which, id) {
        var params = melmService.encodeParams({
          "id" : id,
          "which" : which
        });
        $http.post(melmContextRoot + '/rest/libraries/icons/move', params, {
          headers : {
            'Content-Type' : 'application/x-www-form-urlencoded'
          }
        }).success(function() {
          $scope.loadResources(melmService.getRESTParameter('icons/'));
        }).error(function() {
          $scope.loadResources(melmService.getRESTParameter('icons/'));
          BootstrapDialog.alert({
            title : 'ERROR',
            message : 'Resource move threw an error.',
            type : BootstrapDialog.TYPE_DANGER, // <-- Default value is BootstrapDialog.TYPE_PRIMARY
            closable : true, // <-- Default value is true
            buttonLabel : 'Close', // <-- Default value is 'OK',
            callback : function(result) {
            }
          });
        });
      };

      $scope.isFirst = function(id) {
        var result = $scope.libraryIconsModel && $scope.libraryIconsModel.icons && 0 < $scope.libraryIconsModel.icons.length
            && $scope.libraryIconsModel.icons[0].id === id;
        return result;
      };

      $scope.isLast = function(id) {
        var result = $scope.libraryIconsModel && $scope.libraryIconsModel.icons && 0 < $scope.libraryIconsModel.icons.length
            && $scope.libraryIconsModel.icons[$scope.libraryIconsModel.icons.length - 1].id === id;
        return result;
      };

      $scope.loadingVisible = false;
      $scope.itemsPerPage = 8;
      $scope.currentPage = 1;
      $scope.predicate = 'indexOfIconInLibrary';
      $scope.loadResources(melmService.getRESTParameter('icons/'));
    } ]);