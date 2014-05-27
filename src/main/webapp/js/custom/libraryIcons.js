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
        melmService.confirmDelete($scope, id);
      };

      $scope.deleteResource = function(id) {
        melmService.post({
          params : {
            "id" : id
          },
          url : '/rest/libraries/icons/delete',
          successCallback : function() {
            $scope.loadResources(melmService.getRESTParameter('icons/'));
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

      $scope.go = function(path) {
        melmService.go(path);
      };

      $scope.move = function(which, id) {
        melmService.post({
          params : {
            "id" : id,
            "which" : which
          },
          url : '/rest/libraries/icons/move',
          successCallback : function() {
            $scope.loadResources(melmService.getRESTParameter('icons/'));
          },
          errorCallback : function() {
            BootstrapDialog.alert({
              title : 'ERROR',
              message : 'Resource move threw an error.',
              type : BootstrapDialog.TYPE_DANGER,
              closable : true,
              buttonLabel : 'Close'
            });
          }
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