$(document).ready(function () {
    $("#table").empty();
    $("#range-form").submit(function (e) {
        e.preventDefault();
        loadTableDate();
        $("#rangeModal").modal("hide");
    });
    $("#client-form").submit(function (e) {
        e.preventDefault();
        loadTableClient();
        $("#clientModal").modal("hide");
    });
});

function loadTableDate() {
    var url = `http://localhost:8080/prueba/point_bags/range/${
        URLSearchParams($("#range-form").serialize()).toString()
    }`;
    loadTable(url);
}

function loadTableClient() {
    var url = `http://localhost:8080/prueba/point_bags/client/${$("#client").val()}`;
    loadTable(url);
}

function loadTable(url) {
    $("#table").empty();
    $.ajax({
        url: url,
        type: "GET",
        success: function (data) {
            data.forEach(data => {
                    $("#table").append(`
<tr>
    <td>${data.id}</td>
    <td>${data.client.id}</td>
    <td>${data.assignmentDate}</td>
    <td>${data.expirationDate}</td>
    <td>${data.points}</td>
    <td>${data.usedPoints}</td>
    <td>${data.pointsBalance}</td>
    <td>${data.operationAmount}</td>
</tr>`);
                });
        },
        error: function (data) {
            window.alert("Error al cargar la tabla");
        }
    });
}
