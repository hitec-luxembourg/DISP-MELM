app.controller('LibraryIconsCtrl',
    [
        '$scope',
        '$document',
        'melmService',
        'dialogs',
        function($scope, $document, melmService, dialogs) {
          'use strict';

          $document.bind('keypress', function(event) {
            console.debug(event);
            if (124 === event.charCode) {
              alert(angular.toJson($scope.resources));
            }
          });

          $scope.loadResources = function(id) {
            melmService.loadResources($scope, '/rest/libraries/icons/json/' + id, function() {
              $scope.processLinks($scope.resources);
              // Override default instruction $scope.totalItems = $scope.resources.length;
              $scope.totalItems = $scope.resources.icons.length;
            });
          };

          $scope.confirmDelete = function(id) {
            melmService.confirmDelete($scope, id);
          };

          $scope.confirmDeleteMultiple = function() {
            melmService.confirmDeleteMultiple($scope);
          };

          $scope.deleteResource = function(id) {
            melmService.post({
              params : {
                "id" : id
              },
              url : '/rest/libraries/icons/delete',
              successCallback : function() {
                $scope.loadResources(melmService.getRESTParameter('icons/'));
              },
              errorCallback : function() {
                dialogs.error('Error', 'Resource deletion threw an error.');
                // BootstrapDialog.alert({
                // title : 'ERROR',
                // message : 'Resource deletion threw an error.',
                // type : BootstrapDialog.TYPE_DANGER,
                // closable : true,
                // buttonLabel : 'Close'
                // });
              }
            });
          };

          $scope.deleteResources = function() {
            var ids = [];
            if ($scope.resources && $scope.resources.icons && 0 < $scope.resources.icons.length) {
              for (var i = 0; i < $scope.resources.icons.length; i++) {
                var icon = $scope.resources.icons[i];
                if (icon.icon.checked) {
                  ids.push(icon.id);
                }
              }
            }
            melmService.post({
              params : {
                "ids" : ids
              },
              url : '/rest/libraries/icons/deleteMultiple',
              successCallback : function() {
                $scope.loadResources(melmService.getRESTParameter('icons/'));
              },
              errorCallback : function() {
                dialogs.error('Error', 'Resources deletion threw an error.');
                // BootstrapDialog.alert({
                // title : 'ERROR',
                // message : 'Resources deletion threw an error.',
                // type : BootstrapDialog.TYPE_DANGER,
                // closable : true,
                // buttonLabel : 'Close'
                // });
              }
            });
          };

          $scope.links = {};

          $scope.processLinks = function(data) {
            $scope.links = {};
            if (data && data.icons && 0 < data.icons.length) {
              for (var i = 0; i < data.icons.length; i++) {
                var icon = data.icons[i].icon;
                $scope.links[icon.id] = melmContextRoot + "/rest/icons/file/" + icon.id + "/MEDIUM";
                angular.extend(data.icons[i].icon, {
                  checked : false
                });
              }
            }
          };

          $scope.changeImage = function(id, which) {
            $scope.links[id] = melmContextRoot + "/rest/icons/file/" + which + id + "/MEDIUM";
          };

          $scope.go = function(path) {
            melmService.go(path);
          };

          $scope.move = function(which, id) {
            melmService.post({
              params : {
                "id" : id,
                "which" : which
              },
              url : '/rest/libraries/icons/move',
              successCallback : function() {
                $scope.loadResources(melmService.getRESTParameter('icons/'));
              },
              errorCallback : function() {
                dialogs.error('Error', 'Resource move threw an error.');
                // BootstrapDialog.alert({
                // title : 'ERROR',
                // message : 'Resource move threw an error.',
                // type : BootstrapDialog.TYPE_DANGER,
                // closable : true,
                // buttonLabel : 'Close'
                // });
              }
            });
          };

          $scope.isFirst = function(id) {
            var result = $scope.resources && $scope.resources.icons && 0 < $scope.resources.icons.length
                && $scope.resources.icons[0].id === id;
            return result;
          };

          $scope.isLast = function(id) {
            var result = $scope.resources && $scope.resources.icons && 0 < $scope.resources.icons.length
                && $scope.resources.icons[$scope.resources.icons.length - 1].id === id;
            return result;
          };

          $scope.allClicked = function() {
            var newValue = !$scope.allChecked();
            for (var i = 0; i < $scope.resources.icons.length; i++) {
              var icon = $scope.resources.icons[i];
              icon.icon.checked = newValue;
            }
          };

          $scope.allChecked = function() {
            var result = true;
            if (!$scope.resources || !$scope.resources.icons || 0 === $scope.resources.icons.length) {
              result = false;
            } else {
              for (var i = 0; i < $scope.resources.icons.length; i++) {
                var icon = $scope.resources.icons[i];
                if (!icon.icon.checked) {
                  result = false;
                  break;
                }
              }
            }
            return result;
          };

          $scope.someSelected = function() {
            var result = false;
            if ($scope.resources && $scope.resources.icons && 0 < $scope.resources.icons.length) {
              for (var i = 0; i < $scope.resources.icons.length; i++) {
                var icon = $scope.resources.icons[i];
                if (icon.icon.checked) {
                  result = true;
                  break;
                }
              }
            }
            return result;
          };

          $scope.loadingVisible = false;
          $scope.itemsPerPage = 8;
          $scope.currentPage = 1;
          $scope.predicate = 'indexOfIconInLibrary';
          $scope.loadResources(melmService.getRESTParameter('icons/'));
        } ]);