app.controller('UpdateIconCtrl', [ '$scope', '$window', 'melmService', 'fileReader', function($scope, $window, melmService, fileReader) {
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

  $scope.anchor = $window.anchor;

  $scope.iconChoice = "leave";
  $scope.iconSelectedChoice = "leave";
  
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
