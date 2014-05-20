app.controller('AddLibraryIconCtrl', [ '$scope', '$http', '$window', 'Pagination', function($scope, $http, $window, Pagination) {
  $scope.loadResources = function() {
    $http.get(melmContextRoot + '/rest/icons/json').success(function(data) {
      $scope.icons = data;
      $scope.pagination.numPages = Math.ceil($scope.icons.length/$scope.pagination.perPage);
    });
  };
  
  $scope.back = function() {
    $window.history.back();
  };
  
  $scope.selectImage = function(id){
    $scope.id=id;
  };
  
  $scope.isSelected = function(id){
    return $scope.id===id;
  };
  
  $scope.id = -1;
  $scope.pagination = Pagination.getNew(36);
  $scope.loadResources();

} ]);