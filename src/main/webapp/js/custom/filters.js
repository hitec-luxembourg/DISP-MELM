app.filter('startFrom', function() {
  return function(input, start) {
    if (input === undefined) {
      return input;
    } else {
      return input.slice(+start);
    }
  };
}).filter('checkmark', function() {
  return function(input) {
    return input ? '\u2713' : '\u2718';
  };
});