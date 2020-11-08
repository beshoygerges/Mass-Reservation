function editMass(id, date, time, totalSeats, reservedSeats) {
    $('#id').val(id);
    $('#massDate').val(date);
    $('#massTime').append($('<option>', {
        value: time + ':00',
        text: time
    }));
    $('#totalSeats').val(totalSeats);
    $('#editMassModal').modal('show');
}