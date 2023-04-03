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
<td>${x.id}</td>
<td>${x.name}</td>
<td>${x.lastName}</td>
<td>${x.documentNumber}</td>
<td>${x.documentType}</td>
<td>${x.nationality}</td>
<td>${x.email}</td>
<td>${x.phone}</td>
<td>${x.birthDate}</td>
</tr>`);
                });
        },
        error: function (data) {
            window.alert("Error al cargar la tabla");
        }
    });
}
