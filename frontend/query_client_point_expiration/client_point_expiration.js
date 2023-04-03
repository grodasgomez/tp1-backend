$(document).ready(function () {
    $("#table").empty();
    $("#expire-form").submit(function (e) {
        e.preventDefault();
        loadTableClient();
        $("#expireModal").modal("hide");
    });
});

function loadTableClient() {
    var url = `http://localhost:8080/prueba/point_bags/expires/${$("#days").val()}`;
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
<td>${data.name}</td>
<td>${data.lastName}</td>
<td>${data.documentNumber}</td>
<td>${data.documentType}</td>
<td>${data.nationality}</td>
<td>${data.email}</td>
<td>${data.phone}</td>
<td>${data.birthDate}</td>
</tr>`);
                });
        },
        error: function (data) {
            window.alert("Error al cargar la tabla");
        }
    });
}
