app.controller('LibraryIconsCtrl', [
    '$scope',
    '$http',
    '$location',
    '$window',
    'Pagination',
    function($scope, $http, $location, $window, Pagination) {
      $scope.loadResources = function(id) {
        $scope.loadingVisible = true;
        $http.get(melmContextRoot + '/rest/libraries/icons/json/' + id).success(function(data) {
          $scope.loadingVisible = false;
          $scope.libraryIconsModel = data;
          $scope.bigTotalItems = data.icons.length;
        });
      };

      $scope.deleteResource = function(id) {
        if (!confirm("Do you really want to delete this resource ?")) {
          return;
        }
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
          alert("Resource deletion threw an error.");
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
          alert("Resource move threw an error.");
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