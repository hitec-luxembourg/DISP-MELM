app.controller('StartCtrl', [ '$scope', 'melmService', function($scope, melmService) {
  'use strict';
  
  $scope.go = function(path) {
    melmService.go(path);
  };

} ]);