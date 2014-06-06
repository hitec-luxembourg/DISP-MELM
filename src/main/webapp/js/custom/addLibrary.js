app.controller('AddLibraryCtrl', [ '$scope', 'melmService', '$fileUploader', 'dialogs', function($scope, melmService, $fileUploader, dialogs) {
  'use strict';

  // create a uploader with options
  var uploader = $scope.uploader = $fileUploader.create({
    scope : $scope,
    url : melmContextRoot + '/rest/libraries/add'
  });

  // REGISTER HANDLERS

  uploader.bind('afteraddingfile', function(event, item) {
    $('#libraryIconFile').attr("disabled", "disabled").css("opacity", 0.4).css("filter", "alpha(opacity=40)");
    item.remove = function() {
      uploader.removeFromQueue(this);
      $('#libraryIconFile').removeAttr("disabled").css("opacity", 0).css("filter", "alpha(opacity=0)");
    };
  });

  uploader.bind('beforeupload', function(event, item) {
    item.alias = 'libraryIconFile';
    item.formData = [];
    item.formData.push({
      libraryName : typeof $scope.libraryName !== "undefined" ? $scope.libraryName : "",
      version : typeof $scope.version !== "undefined" ? $scope.version : "",
    });
  });

  uploader.bind('success', function(event, xhr, item, response) {
    melmService.go('/rest/libraries/');
  });

  uploader.bind('error', function(event, xhr, item, response) {
    dialogs.error('Error', response);
  });

  uploader.filters.push(function() {
    return uploader.queue.length !== 1; // only one file in the queue
  });

  uploader.filters.push(function(item /* {File|HTMLInputElement} */) {
    var type = uploader.isHTML5 ? item.type : '/' + item.value.slice(item.value.lastIndexOf('.') + 1);
    type = '|' + type.toLowerCase().slice(type.lastIndexOf('/') + 1) + '|';
    return '|png|'.indexOf(type) !== -1;
  });

  $scope.go = function(path) {
    melmService.go(path);
  };

  $scope.isAddActive = function() {
    return $scope.libraryName && "" != $scope.libraryName && $scope.version && "" != $scope.version && uploader.queue.length === 1; 
  };

  $scope.create = function() {
    return uploader.queue[0].upload(); 
  };
} ]);