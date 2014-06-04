app.controller('AddIconCtrl', [ '$scope', 'melmService', 'fileReader', function($scope, melmService, fileReader) {
  'use strict';

  $scope.go = function(path) {
    melmService.go(path);
  };

  $scope.setAnchor = function(anchor) {
    $scope.anchor = anchor;
  };

  $scope.getClass = function(anchor) {
    var mainClass = 'anchor' + ' anchor-' + anchor;
    if ($scope.anchor === anchor) {
      return mainClass + ' anchor-selected';
    }
    return mainClass;
  };

  $scope.anchor = "SE";
  $scope.iconSelectedChoice = "generate";
  
  $scope.getFile = function(which) {
    fileReader.readAsDataUrl($scope.file, $scope).then(function(result) {
      switch (which) {
      case 'largeIconFile':
        $scope.imageSrc = result;
        break;
      case 'largeIconSelectedFile':
        $scope.selectedImageSrc = result;
        break;

      default:
        break;
      }
    });
  };

} ]);
