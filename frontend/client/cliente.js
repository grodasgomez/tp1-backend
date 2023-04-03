$(document).on("click", ".open-editModal", function () {
    var id = $(this).parent().siblings().first().text();
    $("#edit-id").text(id);

    // Copy values from table to form
    var name = $(this).parent().siblings().eq(1).text();
    $("#edit-name").val(name);
    var lastName = $(this).parent().siblings().eq(2).text();
    $("#edit-lastName").val(lastName);
    var documentNumber = $(this).parent().siblings().eq(3).text();
    $("#edit-documentNumber").val(documentNumber);
    var documentType = $(this).parent().siblings().eq(4).text();
    $("#edit-documentType").val(documentType);
    var nationality = $(this).parent().siblings().eq(5).text();
    $("#edit-nationality").val(nationality);
    var email = $(this).parent().siblings().eq(6).text();
    $("#edit-email").val(email);
    var phone = $(this).parent().siblings().eq(7).text();
    $("#edit-phone").val(phone);
    var birthDate = $(this).parent().siblings().eq(8).text();
    $("#edit-birthDate").val(birthDate);
});
$(document).on("click", ".open-deleteModal", function () {
    var id = $(this).parent().siblings().first().text();
    $("#delete-id").text(id);
});

$(document).ready(function () {
    console.log("ready");
    loadTable();
    $("#add-form").submit(function (e) {
        console.log("submit");
        e.preventDefault();
        var dataSer = $(this).serializeArray();
        var data = {};
        dataSer.forEach(x => {
            data[x.name] = x.value;
        });
        $.ajax({
            url: "http://localhost:8080/prueba/clients/",
            type: "POST",
            data: JSON.stringify(data),
            headers: {
                "Content-Type": "application/json"
              },
            datatype: "json",
            success: function (data) {
                loadTable();
                $("#addModal").modal("hide");
            },
            error: function (data) {
                window.alert("Error al agregar");
            }
        });
    });

    $("#edit-form").submit(function (e) {
        console.log("submit");
        e.preventDefault();
        var dataSer = $(this).serializeArray();
        var data = {};
        dataSer.forEach(x => {
            data[x.name] = x.value;
        });
        var id = $("#edit-id").text();
        $.ajax({
            url: `http://localhost:8080/prueba/clients/${id}`,
            type: "PUT",
            data: JSON.stringify(data),
            headers: {
                "Content-Type": "application/json"
              },
            datatype: "json",
            success: function (data) {
                loadTable();
                $("#editModal").modal("hide");
            },
            error: function (data) {
                window.alert("Error al editar");
            }
        });
    });

    $("#delete-form").submit(function (e) {
        e.preventDefault();
        var id = $("#delete-id").text();
        $.ajax({
            url: `http://localhost:8080/prueba/clients/${id}`,
            type: "DELETE",
            success: function (data) {
                loadTable();
                $("#deleteModal").modal("hide");
            },
            error: function (data) {
                window.alert("Error al borrar");
            }
        });
    });
});

function loadTable() {
    console.log("loadTable");

    // Clear table
    $("#table").empty();
    $.ajax({
        url: "http://localhost:8080/prueba/clients/",
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