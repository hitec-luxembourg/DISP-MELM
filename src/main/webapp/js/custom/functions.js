function encodeParams(data) {
  var result = [];
  for ( var d in data) {
    result.push(encodeURIComponent(d) + "=" + encodeURIComponent(data[d]));
  }
  return result.join("&");
};