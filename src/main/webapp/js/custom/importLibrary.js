app.controller('ImportLibraryCtrl', [ '$scope', 'melmService', '$fileUploader', 'dialogs',
    function($scope, melmService, $fileUploader, dialogs) {
      'use strict';

      // create a uploader with options
      var uploader = $scope.uploader = $fileUploader.create({
        scope : $scope,
        url : melmContextRoot + '/rest/libraries/import'
      });

      // REGISTER HANDLERS

      uploader.bind('afteraddingfile', function(event, item) {
        item.formData = [];
        item.formData.push({
          libraryName : $scope.detectLibraryName(item.file.name),
          version : $scope.detectLibraryVersion(item.file.name),
        });
        item.alias = 'libraryFile';
        // $('#libraryFile').attr("disabled", "disabled").css("opacity", 0.4).css("filter", "alpha(opacity=40)");
        // item.remove = function() {
        // uploader.removeFromQueue(this);
        // $('#libraryFile').removeAttr("disabled").css("opacity", 0).css("filter", "alpha(opacity=0)");
        // };
      });

      uploader.bind('success', function(event, xhr, item, response) {
        dialogs.notify('Notification', item.file.name + ' has been successfully imported!');
        // melmService.go('/rest/libraries/');
      });

      uploader.bind('error', function(event, xhr, item, response) {
        dialogs.error('Error', response);
        // BootstrapDialog.alert({
        // title : 'ERROR',
        // message : response,
        // type : BootstrapDialog.TYPE_DANGER,
        // closable : true,
        // buttonLabel : 'Close'
        // });
      });

      // uploader.filters.push(function() {
      // return uploader.queue.length !== 1; // only one file in the queue
      // });

      uploader.filters.push(function(item /* {File|HTMLInputElement} */) {
        var type = uploader.isHTML5 ? item.type : '/' + item.value.slice(item.value.lastIndexOf('.') + 1);
        type = '|' + type.toLowerCase().slice(type.lastIndexOf('/') + 1) + '|';
        return '|x-zip-compressed|'.indexOf(type) !== -1;
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