app.controller('LibraryIconsCtrl', [
    '$scope',
    '$http',
    '$location',
    '$window',
    function($scope, $http, $location, $window) {
      $scope.loadResources = function(id) {
        $scope.loadingVisible = true;
        $http.get(melmContextRoot + '/rest/libraries/icons/json/' + id).success(function(data) {
          $scope.loadingVisible = false;
          $scope.libraryIconsModel = data;
          $scope.totalItems = data.icons.length;
        });
      };

      $scope.confirmDelete = function(id) {
        BootstrapDialog.confirm('Do you really want to delete this resource ?', function(result) {
          if (result) {
            $scope.deleteResource(id);
          }
        });
      };

      $scope.deleteResource = function(id) {
        var params = encodeParams({
          "id" : id
        });
        $http.post(melmContextRoot + '/rest/libraries/icons/delete', params, {
          headers : {
            'Content-Type' : 'application/x-www-form-urlencoded'
          }
        }).success(function() {
          $scope.loadResources(getRESTParameter('icons/'));
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
        window.location = melmContextRoot + path;
      };

      $scope.move = function(which, id) {
        var params = encodeParams({
          "id" : id,
          "which" : which
        });
        $http.post(melmContextRoot + '/rest/libraries/icons/move', params, {
          headers : {
            'Content-Type' : 'application/x-www-form-urlencoded'
          }
        }).success(function() {
          $scope.loadResources(getRESTParameter('icons/'));
        }).error(function() {
          $scope.loadResources(getRESTParameter('icons/'));
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
      $scope.loadResources(getRESTParameter('icons/'));
    } ]);