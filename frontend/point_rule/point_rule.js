$(document).on("click", ".open-editModal", function () {
    var id = $(this).parent().siblings().first().text();
    $("#edit-id").text(id);

    // Copy values from table to form
    var lowerLimit = $(this).parent().siblings().eq(1).text();
    $("#edit-lowerLimit").val(lowerLimit);
    var upperLimit = $(this).parent().siblings().eq(2).text();
    $("#edit-upperLimit").val(upperLimit);
    var conversionRate = $(this).parent().siblings().eq(3).text();
    $("#edit-conversionRate").val(conversionRate);
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
            url: "http://localhost:8080/prueba/rules/",
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
            url: `http://localhost:8080/prueba/rules/${id}`,
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
            url: `http://localhost:8080/prueba/rules/${id}`,
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
        url: "http://localhost:8080/prueba/rules/",
        type: "GET",
        success: function (data) {
            data.forEach(x => {
                $("#table").append(`
<tr>
    <td>${x.id}</td>
    <td>${x.lowerLimit}</td>
    <td>${x.upperLimit}</td>
    <td>${x.conversionRate}</td>
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