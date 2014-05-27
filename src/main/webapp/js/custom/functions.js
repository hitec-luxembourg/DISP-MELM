app.service('melmService', [ '$http', '$window', function($http, $window) {
  this.encodeParams = function(data) {
    var result = [];
    for ( var d in data) {
      result.push(encodeURIComponent(d) + "=" + encodeURIComponent(data[d]));
    }
    return result.join("&");
  };

  this.getRESTParameter = function(sep, suffix) {
    var partialString = $window.location.href.split(sep)[1];
    return partialString.split(suffix)[0];
  };

  this.loadResources = function(theScope, url, successCallback, errorCallback) {
    theScope.loadingVisible = true;
    return $http.get(melmContextRoot + url).success(function(data, status, headers, config) {
      theScope.resources = data;
      theScope.totalItems = data.length;
      theScope.loadingVisible = false;
      if (successCallback) {
        successCallback();
      }
    }).error(function(data, status, headers, config) {
      if (errorCallback) {
        errorCallback();
      }
    });
  };

  this.go = function(path) {
    $window.location = melmContextRoot + path;
  };

  this.back = function() {
    $window.history.back();
  };

  // Post a request
  // In options, there must be :
  // - an url to post to
  // - some params
  // - a callback for success
  // - a callback for error
  this.post = function(options) {
    var params = this.encodeParams(options.params);
    return $http.post(melmContextRoot + options.url, params, {
      headers : {
        'Content-Type' : 'application/x-www-form-urlencoded'
      }
    }).success(function(data, status, headers, config) {
      // this callback will be called asynchronously
      // when the response is available
      if (options.successCallback) {
        options.successCallback(data, status, headers, config);
      }
    }).error(function(data, status, headers, config) {
      // called asynchronously if an error occurs
      // or server returns response with an error status.
      if (options.errorCallback) {
        options.errorCallback(data, status, headers, config);
      }
    });
  };

  this.confirmDelete = function(theScope, id) {
    BootstrapDialog.confirm('Do you really want to delete this resource ?', function(result) {
      if (result) {
        theScope.deleteResource(id);
      }
    });
  };

} ]);