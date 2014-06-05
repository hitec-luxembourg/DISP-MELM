app.controller('UpdateLibraryCtrl', [ '$scope', 'melmService', 'fileReader', function($scope, melmService, fileReader) {
  'use strict';
  
  $scope.go = function(path) {
    melmService.go(path);
  };

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