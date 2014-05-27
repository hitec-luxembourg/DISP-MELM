app.controller('ImportLibraryCtrl', [ '$scope', 'melmService', '$fileUploader', function($scope, melmService, $fileUploader) {
  'use strict';

  // create a uploader with options
  var uploader = $scope.uploader = $fileUploader.create({
    scope : $scope,
    url : melmContextRoot + '/rest/libraries/import'
  });

  // REGISTER HANDLERS

  uploader.bind('afteraddingfile', function(event, item) {
    item.formData.push({
      libraryName : $scope.detectLibraryName(item.file.name),
      version : $scope.detectLibraryVersion(item.file.name),
    });
    item.alias = 'libraryFile';
    $('#libraryFile').attr("disabled", "disabled").css("opacity", 0.4).css("filter", "alpha(opacity=40)");
    item.remove = function() {
      uploader.removeFromQueue(this);
      $('#libraryFile').removeAttr("disabled").css("opacity", 0).css("filter", "alpha(opacity=0)");
    };
  });

  uploader.bind('success', function(event, xhr, item, response) {
    melmService.go('/rest/libraries/');
  });

  uploader.bind('error', function(event, xhr, item, response) {
    BootstrapDialog.alert({
      title : 'ERROR',
      message : response,
      type : BootstrapDialog.TYPE_DANGER,
      closable : true,
      buttonLabel : 'Close'
    });
  });

  uploader.filters.push(function() {
    return uploader.queue.length !== 1; // only one file in the queue
  });

  $scope.go = function(path) {
    melmService.go(path);
  };

  $scope.detectLibraryName = function(fileName) {
    var split = fileName.split("-");
    return split[0];
  };

  $scope.detectLibraryVersion = function(fileName) {
    var split = fileName.split("-");
    var secondPart = split[1];
    return secondPart.substr(0, secondPart.length - 4);
  };

} ]);