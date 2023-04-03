$(document).ready(function () {
    console.log("ready");
    $("#date-form").submit(function (e) {
        e.preventDefault();
        loadTableDate();
    });
    $("#client-form").submit(function (e) {
        e.preventDefault();
        loadTableClient();
    });
    $("#concept-form").submit(function (e) {
        e.preventDefault();
        loadTableConcept();
    });
});

function loadTableDate() {
    var url = `http://localhost:8080/prueba/points_used/used_date/${$("#date").val()}`;
    loadTable(url);
}

function loadTableClient() {
    var url = `http://localhost:8080/prueba/points_used/client/${$("#client").val()}`;
    loadTable(url);
}

function loadTableConcept() {
    var url = `http://localhost:8080/prueba/points_used/concept/${$("#concept").val()}`;
    loadTable(url);
}

function loadTable(url) {
    console.log("loadTable");

    $("#table").empty();
    $.ajax({
        url: url,
        type: "GET",
        success: function (data) {
            data.forEach(data => {
                var details = "<table><tr><th>Bolsa ID</th><th>Usado</th>";
                data.details.forEach(x => {
                    details += `</tr><tr>
<td>${x.pointBag.id}</td>
<td>${x.pointBag.usedPoints}</td></tr>`});
                    details += "</table>";
                    $("#table").append(`
<tr>
    <td>${data.id}</td>
    <td>${data.client.name} ${data.client.lastName}</td>
    <td>${data.used_points}</td>
    <td>${data.concept.descripcion}</td>
    <td>${details}</td>
</tr>`);
                });
        },
        error: function (data) {
            window.alert("Error al cargar la tabla");
        }
    });
}
