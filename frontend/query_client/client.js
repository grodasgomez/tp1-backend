$(document).ready(function () {
    console.log("ready");
    loadTable();
    $("#name-form").submit(function (e) {
        e.preventDefault();
        loadTable("http://localhost:8080/prueba/clients/name/" + $("#name").val());
        $("#nameModal").modal("hide");
    });
    $("#lastName-form").submit(function (e) {
        e.preventDefault();
        loadTable("http://localhost:8080/prueba/clients/last_name/" + $("#lastName").val());
        $("#lastNameModal").modal("hide");
    });
    $("#birthdate-form").submit(function (e) {
        e.preventDefault();
        loadTable("http://localhost:8080/prueba/clients/birth_date/" + $("#birthdate").val());
        $("#birthdateModal").modal("hide");
    });
});

function loadTable(url) {
    console.log("loadTable");

    // Clear table
    $("#table").empty();
    $.ajax({
        url: url,
        type: "GET",
        success: function (data) {
            data.forEach(x => {
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
    <td>
        <a href="#editModal" class="edit open-editModal" data-toggle="modal"><i class="material-icons" data-toggle="tooltip" title="" data-original-title="Editar"></i></a>
        <a href="#deleteModal" class="delete open-deleteModal" data-toggle="modal"><i class="material-icons" data-toggle="tooltip" title="" data-original-title="Borrar"></i></a>
    </td>
</tr>`)
            });
        },
        error: function (data) {
            window.alert("Error al cargar la tabla");
        }
    });
}