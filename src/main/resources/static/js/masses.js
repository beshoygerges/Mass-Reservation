$(document).ready(function () {
    $('#dtBasicExample').DataTable({
        "bInfo": false, // hide showing entries
        "bPaginate": false, //hide pagination
    });
    $('.dataTables_length').addClass('bs-select');
});