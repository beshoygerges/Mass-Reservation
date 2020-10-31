currentDate = new Date();
currentDate.setDate(currentDate.getDate() + 1);
datePickerId.min = currentDate.toISOString().split("T")[0];
datePickerId.value = currentDate.toISOString().split("T")[0];
populateMassTimes(currentDate.getDay());

function populateMassTimes(day) {
    $('#times').empty()
    if (day == 0 || day == 5) {
        $('#times').prop('disabled', false);
        $('#times').append(`<option value="06:00:00">06:00</option>`);
        $('#times').append(`<option value="08:00:00">08:00</option>`);
    } else if (day == 3) {
        $('#times').append(`<option value="08:00:00">08:00</option>`);
        $('#times').prop('disabled', false);
    } else {
        $('#times').prop('disabled', true);
        $('#times').append(`<option value="" disabled selected hidden>لا توجد قداسات لهذا اليوم</option>`);
    }
}

$('#datePickerId').change(function () {
    var date = $(this).val();
    date = new Date(date);
    var day = date.getDay()
    populateMassTimes(day);

});

$(document).ready(function () {
    $("#reservationForm").submit(function (event) {
        event.preventDefault();
        createReservation();
    });

    $("#cancelReservationForm").submit(function (event) {
        event.preventDefault();
        cancelReservation();
    });

    $("#searchReservationForm").submit(function (event) {
        event.preventDefault();
        searchReservation();
    });
});


function createReservation() {

    $("#response").text('');
    var request = {}
    request["name"] = $("#name").val();
    request["mobileNumber"] = $("#phone").val();
    request["nationalId"] = $("#nationalId").val();
    request["massTime"] = $("#times").val();
    request["massDate"] = $("#datePickerId").val();

    $("#reserveBtn").prop("disabled", true);

    $.ajax({
        type: "POST",
        contentType: "application/json",
        url: "/rest/v1/reservation",
        data: JSON.stringify(request),
        dataType: 'json',
        cache: false,
        timeout: 600000,
        success: function (data) {
            var details = JSON.parse(JSON.stringify(data));
            console.log("SUCCESS : ", details);
            console.log("SUCCESS : ", details.name);
            $('#myModal').modal('toggle');
            $('#successModal').modal('show');
            $('#reservationDetails').html(
                '<strong>الاسم        :   ' + details.name + '</strong><br>' +
                '<strong>رقم الحجز   :   ' + details.reservationId + '</strong><br>' +
                '<strong>تاريخ القداس:   ' + details.massDate + '</strong><br>' +
                '<strong>وقت القداس  :   ' + details.massTime + '</strong><br>' +
                '<strong>حالة الحجز  :   تم التاكيد</strong>'
            )

        },
        error: function (e) {
            var res = JSON.parse(e.responseText)
            console.log("ERROR : ", res);
            $("#response").text(res.message);
            $("#reserveBtn").prop("disabled", false);
        }
    });

}

function cancelReservation() {
    $("#cancelResponse").text('');

    var nationalId = $("#cancelNationalId").val();

    console.log(nationalId);

    $("#cancelReserveBtn").prop("disabled", true);

    $.ajax({
        type: "DELETE",
        url: "/rest/v1/reservation?nationalId=" + nationalId,
        dataType: 'json',
        cache: false,
        timeout: 600000,
        success: function (data) {
            var details = JSON.parse(JSON.stringify(data));

            $('#myModal2').modal('toggle');
            $('#successModal').modal('show');
            $('#reservationDetails').html(
                '<strong>الاسم        :   ' + details.name + '</strong><br>' +
                '<strong>رقم الحجز   :   ' + details.reservationId + '</strong><br>' +
                '<strong>تاريخ القداس:   ' + details.massDate + '</strong><br>' +
                '<strong>وقت القداس  :   ' + details.massTime + '</strong><br>' +
                '<strong>حالة الحجز  :   تم الإلغاء</strong>'
            )

        },
        error: function (e) {
            var res = JSON.parse(e.responseText)
            console.log("ERROR : ", res);
            $("#cancelResponse").text(res.message);
            $("#cancelReserveBtn").prop("disabled", false);
        }
    });

}

function searchReservation() {

    $("#searchResponse").text('');

    var nationalId = $("#searchNationalId").val();

    $("#searchReserveBtn").prop("disabled", true);

    $.ajax({
        type: "GET",
        contentType: "application/json",
        url: "/rest/v1/reservation?nationalId=" + nationalId,
        dataType: 'json',
        cache: false,
        timeout: 600000,
        success: function (data) {
            var details = JSON.parse(JSON.stringify(data));
            $('#myModal3').modal('toggle');
            $('#successModal').modal('show');
            $('#reservationDetails').html(
                '<strong>الاسم        :   ' + details.name + '</strong><br>' +
                '<strong>رقم الحجز   :   ' + details.reservationId + '</strong><br>' +
                '<strong>تاريخ القداس:   ' + details.massDate + '</strong><br>' +
                '<strong>وقت القداس  :   ' + details.massTime + '</strong><br>' +
                '<strong>حالة الحجز  :   تم التأكيد</strong>'
            )
        },
        error: function (e) {
            var res = JSON.parse(e.responseText)
            console.log("ERROR : ", res);
            $("#searchResponse").text(res.message);
            $("#searchReserveBtn").prop("disabled", false);
        }
    });

}

$('#myModal').on('hidden.bs.modal', function () {
    $("#reserveBtn").prop("disabled", false);
    $("#response").text('');
    $('#reservationForm').trigger("reset");
    datePickerId.value = currentDate.toISOString().split("T")[0];
    populateMassTimes(currentDate.getDay())
});

$('#myModal2').on('hidden.bs.modal', function () {
    $("#cancelReserveBtn").prop("disabled", false);
    $("#cancelResponse").text('');
    $('#cancelReservationForm').trigger("reset");
});

$('#myModal3').on('hidden.bs.modal', function () {
    $("#searchReserveBtn").prop("disabled", false);
    $("#searchResponse").text('');
    $('#searchReservationForm').trigger("reset");
});
