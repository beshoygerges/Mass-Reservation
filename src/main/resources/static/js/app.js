currentDate = new Date();
currentDate.setDate(currentDate.getDate() + 1);

reservationDate.min = currentDate.toISOString().split("T")[0];

cancelDate.min = currentDate.toISOString().split("T")[0];

searchDate.min = currentDate.toISOString().split("T")[0];

seatsDate.min = currentDate.toISOString().split("T")[0];

reservationDate.value = currentDate.toISOString().split("T")[0];

cancelDate.value = currentDate.toISOString().split("T")[0];

searchDate.value = currentDate.toISOString().split("T")[0];

seatsDate.value = currentDate.toISOString().split("T")[0];

function getMassesByDate(date, times) {
    var massDate = date.toISOString().slice(0, 10);

    $('#' + times + '').empty()

    $.ajax({
        type: "GET",
        contentType: "application/json",
        url: "/reservations/masses?date=" + massDate,
        dataType: 'json',
        cache: false,
        timeout: 600000,
        success: function (data) {
            var masses = JSON.parse(JSON.stringify(data));
            $('#' + times + '').prop('disabled', false);
            var exist = false;
            for (i = 0; i < masses.length; i++) {
                if (masses[i].enabled) {
                    exist = true;
                    $('#' + times + '').append(
                        `<option value="` + masses[i].time + `">`
                        + masses[i].time.substring(0, 5)
                        + `</option>`);
                }
            }
            if (!exist) {
                $('#' + times + '').prop('disabled', true);
                $('#' + times + '').append(
                    `<option value="" disabled selected hidden>لا توجد قداسات لهذا اليوم</option>`);
            }
        },
        error: function (e) {
            $('#' + times + '').prop('disabled', true);
            $('#' + times + '').append(
                `<option value="" disabled selected hidden>لا توجد قداسات لهذا اليوم</option>`);
        }
    });
}

$('#reservationDate').change(function () {
    var date = $(this).val();
    date = new Date(date);
    getMassesByDate(date, 'reservationTimes');
});

$('#cancelDate').change(function () {
    var date = $(this).val();
    date = new Date(date);
    getMassesByDate(date, 'cancelTimes');
});

$('#searchDate').change(function () {
    var date = $(this).val();
    date = new Date(date);
    getMassesByDate(date, 'searchTimes');
});

$('#seatsDate').change(function () {
    var date = $(this).val();
    date = new Date(date);
    getMassesByDate(date, 'seatsTimes');
});

$(document).ready(function () {
    $("#reservationForm").submit(function (event) {
        event.preventDefault();
        reserveMass();
    });

    $("#cancelReservationForm").submit(function (event) {
        event.preventDefault();
        cancelReservation();
    });

    $("#searchReservationForm").submit(function (event) {
        event.preventDefault();
        searchReservation();
    });

    $("#seatsForm").submit(function (event) {
        event.preventDefault();
        getAvailableSeats();
    });

    $("#addUserForm").submit(function (event) {
        event.preventDefault();
        addUser();
    });

});

function addUser() {
    $("#addUserResponse").text('');

    var request = {}
    request["nationalId"] = $("#addUserNationalId").val();
    request["name"] = $("#addUserName").val();
    request["mobileNumber"] = $("#addUserPhone").val();

    $("#addUserBtn").prop("disabled", true);

    $.ajax({
        type: "POST",
        contentType: "application/json",
        url: "/users",
        data: JSON.stringify(request),
        dataType: 'json',
        cache: false,
        timeout: 600000,
        success: function (data) {
            var user = JSON.parse(JSON.stringify(data));
            $('#addUserModal').modal('toggle');
            $('#successModal').modal('show');
            $('#header').html('بيانات المستخدم');
            $('#reservationDetails').html(
                '<strong>الاسم        :   ' + user.name + '</strong><br>' +
                '<strong>الرقم القومي   :   ' + user.nationalId + '</strong><br>' +
                '<strong>الموبايل   :   ' + user.mobileNumber + '</strong><br>' +
                '<strong>السن:   ' + user.age + '</strong><br>' +
                '<strong>تاريخ الميلاد  :   ' + user.birthdate + '</strong><br>'
            )
        },
        error: function (e) {
            console.log(e);
            var res = JSON.parse(e.responseText)
            console.log(res);
            $("#addUserResponse").text(res.message);
            $("#addUserBtn").prop("disabled", false);
        }
    });
}

function reserveMass() {

    $("#response").text('');

    var request = {}
    request["nationalId"] = $("#nationalId").val();
    request["massTime"] = $("#reservationTimes").val();
    request["massDate"] = $("#reservationDate").val();

    $("#reserveBtn").prop("disabled", true);

    $.ajax({
        type: "POST",
        contentType: "application/json",
        url: "/reservations/masses",
        data: JSON.stringify(request),
        dataType: 'json',
        cache: false,
        timeout: 600000,
        success: function (data) {
            var details = JSON.parse(JSON.stringify(data));
            $('#myModal').modal('toggle');
            $('#successModal').modal('show');
            $('#header').html('بيانات الحجز');
            $('#reservationDetails').html(
                '<strong>الاسم        :   ' + details.name + '</strong><br>' +
                '<strong>رقم المقعد   :   ' + details.seatNumber + '</strong><br>' +
                '<strong>مكان المقعد   :   ' + details.place + '</strong><br>' +
                '<strong>التاريخ:   ' + details.massDate + '</strong><br>' +
                '<strong>الوقت:   ' + details.massTime + '</strong><br>' +
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

    var request = {}

    request["nationalId"] = $("#cancelNationalId").val();
    request["massTime"] = $("#cancelTimes").val();
    request["massDate"] = $("#cancelDate").val();

    $("#cancelReserveBtn").prop("disabled", true);

    $.ajax({
        type: "POST",
        contentType: "application/json",
        url: "/reservations/masses/delete",
        data: JSON.stringify(request),
        dataType: 'json',
        cache: false,
        timeout: 600000,
        success: function (data) {
            var details = JSON.parse(JSON.stringify(data));

            $('#myModal2').modal('toggle');
            $('#successModal').modal('show');
            $('#reservationDetails').html(
                '<strong>الاسم        :   ' + details.name + '</strong><br>' +
                '<strong>رقم المقعد   :   ' + details.seatNumber + '</strong><br>' +
                '<strong>مكان المقعد   :   ' + details.place + '</strong><br>' +
                '<strong>تاريخ القداس:   ' + details.massDate + '</strong><br>' +
                '<strong>وقت القداس  :   ' + details.massTime + '</strong><br>' +
                '<strong>حالة الحجز  :   تم الإلغاء</strong>'
            )

        },
        error: function (e) {
            console.log(e)
            var res = JSON.parse(e.responseText)
            $("#cancelResponse").text(res.message);
            $("#cancelReserveBtn").prop("disabled", false);
        }
    });

}

function searchReservation() {

    $("#searchResponse").text('');

    var request = {}

    request["nationalId"] = $("#searchNationalId").val();
    request["massTime"] = $("#searchTimes").val();
    request["massDate"] = $("#searchDate").val();

    var nationalId = $("#searchNationalId").val();

    $("#searchReserveBtn").prop("disabled", true);

    $.ajax({
        type: "POST",
        contentType: "application/json",
        url: "/reservations/masses/search",
        data: JSON.stringify(request),
        dataType: 'json',
        cache: false,
        timeout: 600000,
        success: function (data) {
            var details = JSON.parse(JSON.stringify(data));
            $('#myModal3').modal('toggle');
            $('#successModal').modal('show');
            $('#reservationDetails').html(
                '<strong>الاسم        :   ' + details.name + '</strong><br>' +
                '<strong>رقم المقعد   :   ' + details.seatNumber + '</strong><br>' +
                '<strong>مكان المقعد   :   ' + details.place + '</strong><br>' +
                '<strong>التاريخ:   ' + details.massDate + '</strong><br>' +
                '<strong>الوقت:   ' + details.massTime + '</strong><br>' +
                '<strong>حالة الحجز  :   تم التأكيد</strong>'
            )
        },
        error: function (e) {
            var res = JSON.parse(e.responseText)
            $("#searchResponse").text(res.message);
            $("#searchReserveBtn").prop("disabled", false);
        }
    });

}

function getAvailableSeats() {

    $("#seatsResponse").text('');

    var request = {}

    request["massTime"] = $("#seatsTimes").val();

    request["massDate"] = $("#seatsDate").val();

    $("#seatsBtn").prop("disabled", true);

    $.ajax({
        type: "POST",
        contentType: "application/json",
        url: "/reservations/masses/seats",
        data: JSON.stringify(request),
        dataType: 'json',
        cache: false,
        timeout: 600000,
        success: function (data) {
            var details = JSON.parse(JSON.stringify(data));
            console.log(details)
            $("#seatsResponse").text(
                ' عدد المقاعد المتاحة ' + details.availableSeats);
            $("#seatsBtn").prop("disabled", false);
        },
        error: function (e) {
            var res = JSON.parse(e.responseText)
            $("#seatsResponse").text(res.message);
            $("#seatsBtn").prop("disabled", false);
        }
    });
}

function resetTimes(times) {
    $('#' + times + '').empty().append(
        '<option selected="selected" value="">لا توجد قداسات لهذا اليوم</option>')
    $('#' + times + '').prop('disabled', true);
}

$('#myModal').on('hidden.bs.modal', function () {
    $("#reserveBtn").prop("disabled", false);
    $("#response").text('');
    $('#reservationForm').trigger("reset");
    reservationDate.value = currentDate.toISOString().split("T")[0];
    resetTimes('reservationTimes');
});

$('#myModal').on('shown.bs.modal', function () {

    var date = $("#reservationDate").val();
    date = new Date(date);
    getMassesByDate(date, 'reservationTimes');
});

$('#myModal2').on('hidden.bs.modal', function () {
    $("#cancelReserveBtn").prop("disabled", false);
    $("#cancelResponse").text('');
    $('#cancelReservationForm').trigger("reset");
    cancelDate.value = currentDate.toISOString().split("T")[0];
    resetTimes('cancelTimes');
});

$('#myModal2').on('shown.bs.modal', function () {
    var date = $("#cancelDate").val();
    date = new Date(date);
    getMassesByDate(date, 'cancelTimes');
});

$('#myModal3').on('hidden.bs.modal', function () {
    $("#searchReserveBtn").prop("disabled", false);
    $("#searchResponse").text('');
    $('#searchReservationForm').trigger("reset");
    searchDate.value = currentDate.toISOString().split("T")[0];
    resetTimes('searchTimes');
});

$('#myModal3').on('shown.bs.modal', function () {
    var date = $("#searchDate").val();
    date = new Date(date);
    getMassesByDate(date, 'searchTimes');
});

$('#myModal4').on('hidden.bs.modal', function () {
    $("#seatsBtn").prop("disabled", false);
    $("#seatsResponse").text('');
    $('#seatsForm').trigger("reset");
    seatsDate.value = currentDate.toISOString().split("T")[0];
    resetTimes('seatsTimes');
});

$('#myModal4').on('shown.bs.modal', function () {
    var date = $("#seatsDate").val();
    date = new Date(date);
    getMassesByDate(date, 'seatsTimes');
});

$('#addUserModal').on('hidden.bs.modal', function () {
    $("#addUserBtn").prop("disabled", false);
    $("#addUserResponse").text('');
    $('#addUserForm').trigger("reset");
});

