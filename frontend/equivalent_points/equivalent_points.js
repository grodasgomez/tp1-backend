$(document).ready(function () {
    $("#add-form").submit(function (e) {
        e.preventDefault();
        var dataSer = $(this).serializeArray();
        var data = {};
        dataSer.forEach(x => {
            data[x.name] = x.value;
        });
        $.ajax({
            url: `http://localhost:8080/prueba/rules/amount/${data.amount}`,
            type: "GET",
            success: function (x) {
                $("#table").append(`
<tr>
    <td>${data.amount}</td>
    <td>${x}</td>
</tr>`);
                $("#addModal").modal("hide");
            },
            error: function (data) {
                window.alert("Error al agregar");
            }
        });
    });
});