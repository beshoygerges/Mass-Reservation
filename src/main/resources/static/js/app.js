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

updateTimes(currentDate.getDay(), 'reservationTimes');
updateTimes(currentDate.getDay(), 'cancelTimes');
updateTimes(currentDate.getDay(), 'searchTimes');
updateTimes(currentDate.getDay(), 'seatsTimes');

function updateTimes(day, times) {
  $('#' + times + '').empty();
  if (day == 0 || day == 5) {
    $('#' + times + '').prop('disabled', false);
    $('#' + times + '').append(`<option value="06:00:00">06:00</option>`);
    $('#' + times + '').append(`<option value="08:00:00">08:00</option>`);
  } else if (day == 3) {
    $('#' + times + '').append(`<option value="08:00:00">08:00</option>`);
    $('#' + times + '').prop('disabled', false);
  } else {
    $('#' + times + '').prop('disabled', true);
    $('#' + times + '').append(
        `<option value="" disabled selected hidden>لا توجد قداسات لهذا اليوم</option>`);
  }
}

$('#reservationDate').change(function () {
  var date = $(this).val();
  date = new Date(date);
  updateTimes(date.getDay(), 'reservationTimes');
});

$('#cancelDate').change(function () {
  var date = $(this).val();
  date = new Date(date);
  updateTimes(date.getDay(), 'cancelTimes');
});

$('#searchDate').change(function () {
  var date = $(this).val();
  date = new Date(date);
  updateTimes(date.getDay(), 'searchTimes');
});

$('#seatsDate').change(function () {
  var date = $(this).val();
  date = new Date(date);
  updateTimes(date.getDay(), 'seatsTimes');
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

  $("#eveningReservationForm").submit(function (event) {
    event.preventDefault();
    reserveEvening();
  });

  $("#eveningCancelReservationForm").submit(function (event) {
    event.preventDefault();
    cancelEveningReservation();
  });

  $("#addUserForm").submit(function (event) {
    event.preventDefault();
    addUser();
  });

  $("#younanReservationForm").submit(function (event) {
    event.preventDefault();
    reserveYounanMass();
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

function reserveYounanMass() {

  $("#younanResponse").text('');

  var request = {}
  request["nationalId"] = $("#younanNationalId").val();
  request["massTime"] = $("#younanReservationTime").val();
  request["massDate"] = $("#younanReservationDate").val();
  request["yonanMass"] = true;

  $("#younanReserveBtn").prop("disabled", true);

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
      $('#younanModal').modal('toggle');
      $('#successModal').modal('show');
      $('#header').html('بيانات الحجز');
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
      $("#younanResponse").text(res.message);
      $("#younanReserveBtn").prop("disabled", false);
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
          '<strong>رقم الحجز   :   ' + details.reservationId + '</strong><br>' +
          '<strong>رقم المقعد   :   ' + details.seatNumber + '</strong><br>' +
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
          '<strong>رقم الحجز   :   ' + details.reservationId + '</strong><br>' +
          '<strong>رقم المقعد   :   ' + details.seatNumber + '</strong><br>' +
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
          '<strong>رقم الحجز   :   ' + details.reservationId + '</strong><br>' +
          '<strong>رقم المقعد   :   ' + details.seatNumber + '</strong><br>' +
          '<strong>تاريخ القداس:   ' + details.massDate + '</strong><br>' +
          '<strong>وقت القداس  :   ' + details.massTime + '</strong><br>' +
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

function reserveEvening() {

  $("#eveningResponse").text('');

  var request = {}

  request["nationalId"] = $("#eveningNationalId").val();
  request["eveningId"] = parseInt($("#eveningId").val());

  $("#eveningReserveBtn").prop("disabled", true);

  $.ajax({
    type: "POST",
    contentType: "application/json",
    url: "/reservations/evenings",
    data: JSON.stringify(request),
    dataType: 'json',
    cache: false,
    timeout: 600000,
    success: function (data) {
      var details = JSON.parse(JSON.stringify(data));
      $('#myModal5').modal('toggle');
      $('#successModal').modal('show');
      $('#header').html('بيانات الحجز');
      $('#reservationDetails').html(
          '<strong>الاسم        :   ' + details.name + '</strong><br>' +
          '<strong>رقم الحجز   :   ' + details.reservationId + '</strong><br>' +
          '<strong>تاريخ السهرة:   ' + details.eveningDate + '</strong><br>' +
          '<strong>حالة الحجز  :   تم التاكيد</strong>'
      )
    },
    error: function (e) {
      var res = JSON.parse(e.responseText)
      console.log("ERROR : ", res);
      $("#eveningResponse").text(res.message);
      $("#eveningReserveBtn").prop("disabled", false);
    }
  });

}

function cancelEveningReservation() {

  $("#eveningResponse").text('');

  var request = {}

  request["nationalId"] = $("#eveningCancelNationalId").val();
  request["eveningId"] = parseInt($("#eveningCancelId").val());

  $("#eveningCancelReserveBtn").prop("disabled", true);

  $.ajax({
    type: "POST",
    contentType: "application/json",
    url: "/reservations/evenings/disable",
    data: JSON.stringify(request),
    dataType: 'json',
    cache: false,
    timeout: 600000,
    success: function (data) {
      var details = JSON.parse(JSON.stringify(data));
      $('#myModal6').modal('toggle');
      $('#successModal').modal('show');
      $('#reservationDetails').html(
          '<strong>الاسم        :   ' + details.name + '</strong><br>' +
          '<strong>رقم الحجز   :   ' + details.reservationId + '</strong><br>' +
          '<strong>تاريخ السهرة:   ' + details.eveningDate + '</strong><br>' +
          '<strong>حالة الحجز  :   تم الالغاء</strong>'
      )
    },
    error: function (e) {
      var res = JSON.parse(e.responseText)
      $("#eveningCancelResponse").text(res.message);
      $("#eveningCancelReserveBtn").prop("disabled", false);
    }
  });

}

$('#myModal').on('hidden.bs.modal', function () {
  $("#reserveBtn").prop("disabled", false);
  $("#response").text('');
  $('#reservationForm').trigger("reset");
  reservationDate.value = currentDate.toISOString().split("T")[0];
  updateTimes(currentDate.getDay(), 'reservationTimes')
});

$('#myModal2').on('hidden.bs.modal', function () {
  $("#cancelReserveBtn").prop("disabled", false);
  $("#cancelResponse").text('');
  $('#cancelReservationForm').trigger("reset");
  cancelDate.value = currentDate.toISOString().split("T")[0];
  updateTimes(currentDate.getDay(), 'cancelTimes')
});

$('#myModal3').on('hidden.bs.modal', function () {
  $("#searchReserveBtn").prop("disabled", false);
  $("#searchResponse").text('');
  $('#searchReservationForm').trigger("reset");
  searchDate.value = currentDate.toISOString().split("T")[0];
  updateTimes(currentDate.getDay(), 'searchTimes')
});

$('#myModal4').on('hidden.bs.modal', function () {
  $("#seatsBtn").prop("disabled", false);
  $("#seatsResponse").text('');
  $('#seatsForm').trigger("reset");
  seatsDate.value = currentDate.toISOString().split("T")[0];
  updateTimes(currentDate.getDay(), 'seatsTimes')
});

$('#myModal5').on('hidden.bs.modal', function () {
  $("#eveningReserveBtn").prop("disabled", false);
  $("#eveningResponse").text('');
  $('#eveningReservationForm').trigger("reset");
});

$('#myModal6').on('hidden.bs.modal', function () {
  $("#eveningCancelReserveBtn").prop("disabled", false);
  $("#eveningCancelResponse").text('');
  $('#eveningCancelReservationForm').trigger("reset");
});

$('#addUserModal').on('hidden.bs.modal', function () {
  $("#addUserBtn").prop("disabled", false);
  $("#addUserResponse").text('');
  $('#addUserForm').trigger("reset");
});

$('#younanModal').on('hidden.bs.modal', function () {
  $("#younanReserveBtn").prop("disabled", false);
  $("#younanResponse").text('');
  $('#younanReservationForm').trigger("reset");
});
