var app = angular.module('app', []);

app.controller('IconsCtrl', function($scope, $http) {
  $scope.loadIcons = function() {
    $http.get(melmContextRoot + '/rest/icons').success(function(data) {
      $scope.icons = data;
    });
  };

  $scope.deleteIcon = function(id) {
    if (!confirm("Do you really want to delete this item ?")) {
      return;
    }
    var params = encodeParams({
      "id" : id
    });
    $http.post(melmContextRoot + '/rest/icons/delete', params, {
      headers : {
        'Content-Type' : 'application/x-www-form-urlencoded'
      }
    }).success(function() {
      $scope.loadIcons();
    }).error(function() {
      alert("Icon deletion threw an error.");
    });
  };

  $scope.predicate = 'displayName';
  $scope.loadIcons();
});