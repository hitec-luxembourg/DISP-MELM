app.controller('CloneLibraryCtrl', [ '$scope', 'melmService', function($scope, melmService) {
  'use strict';
  
  $scope.back = function() {
    melmService.back();
  };

  $scope.iconChoice = "existing";
} ]);