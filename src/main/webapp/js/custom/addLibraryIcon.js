$(document).ready(function() {
  $('input:radio').on('click', function() {
    $('input:radio').parent().parent().removeClass('icon_selected');
    $(this).parent().parent().addClass('icon_selected');
  });
});