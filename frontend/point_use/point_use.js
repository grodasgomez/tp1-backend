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
    $("#add-form").submit(function (e) {
        console.log("submit");
        e.preventDefault();
        var dataSer = $(this).serializeArray();
        var data = {};
        dataSer.forEach(x => {
            data[x.name] = x.value;
        });
        $.ajax({
            url: "http://localhost:8080/prueba/points_used/",
            type: "POST",
            data: JSON.stringify(data),
            headers: {
                "Content-Type": "application/json"
              },
            datatype: "json",
            success: function (data) {
                var details = "<table><tr><th>Bolsa ID</th><th>Usado</th>";
                data.details.forEach(x => {
                    details += `</tr><tr>
    <td>${x.pointBag.id}</td>
    <td>${x.usedPoints}</td></tr>`});
                details += "</table>";
                $("#table").append(`
    <tr>
        <td>${data.id}</td>
        <td>${data.client.name} ${data.client.lastName}</td>
        <td>${data.used_points}</td>
        <td>${data.concept.description}</td>
        <td>${details}</td>
    </tr>`);
                $("#addModal").modal("hide");
            },
            error: function (data) {
                window.alert(data.message);
            }
        });
    });
});
