// Initialize all tooltips on the page
var tooltipTriggerList = [].slice.call(document.querySelectorAll('[data-bs-toggle="tooltip"]'))
var tooltipList = tooltipTriggerList.map(function (tooltipTriggerEl) {
  return new bootstrap.Tooltip(tooltipTriggerEl)
})

// Or if using jQuery (and Bootstrap's jQuery plugins)
// $(function () {
//   $('[data-bs-toggle="tooltip"]').tooltip()
// })