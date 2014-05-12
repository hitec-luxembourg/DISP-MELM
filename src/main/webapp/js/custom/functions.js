function encodeParams(data) {
  var result = [];
  for ( var d in data) {
    result.push(encodeURIComponent(d) + "=" + encodeURIComponent(data[d]));
  }
  return result.join("&");
};

function getRESTParameter(sep, suffix) {
  var partialString = window.location.href.split(sep)[1];
  var id = partialString.split(suffix)[0];
  return id;
}
