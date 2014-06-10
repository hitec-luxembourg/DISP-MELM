app.controller('CloneLibraryCtrl', [ '$scope', 'melmService', 'fileReader', function($scope, melmService, fileReader) {
  'use strict';
  
  $scope.back = function() {
    melmService.back();
  };

  $scope.iconChoice = "existing";
  
  $scope.getFile = function(which) {
    fileReader.readAsDataUrl($scope.file, $scope).then(function(result) {
      switch (which) {
      case 'libraryIconFile':
        $scope.imageSrc = result;
        break;

      default:
        break;
      }
    });
  };
} ]);