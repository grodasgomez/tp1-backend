$(document).ready(function () {
    console.log("ready");
    $("#add-form").submit(function (e) {
        console.log("submit");
        e.preventDefault();
        var dataSer = $(this).serializeArray();
        var data = {};
        dataSer.forEach(x => {
            data[x.name] = x.value;
        });
        $.ajax({
            url: "http://localhost:8080/prueba/point_bags/",
            type: "POST",
            data: JSON.stringify(data),
            headers: {
                "Content-Type": "application/json"
              },
            datatype: "json",
            success: function (x) {
                $("#table").append(`
<tr>
    <td>${x.id}</td>
    <td>${x.client.name} ${x.client.lastName}</td>
    <td>${x.assignmentDate}</td>
    <td>${x.expirationDate}</td>
    <td>${x.points}</td>
    <td>${x.usedPoints}</td>
    <td>${x.pointsBalance}</td>
    <td>${x.operationAmount}</td>
</tr>`)
                $("#addModal").modal("hide");
            },
            error: function (data) {
                window.alert("Error al agregar");
            }
        });
    });
});
