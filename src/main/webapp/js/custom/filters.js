app.filter('startFrom', function() {
  'use strict';
  
  return function(input, start) {
    if (input === undefined) {
      return input;
    } else {
      return input.slice(+start);
    }
  };
}).filter('checkmark', function() {
  'use strict';
  
  return function(input) {
    return input ? '\u2713' : '\u2718';
  };
});