$(function(){
	$("#publishBtn").click(publish);
});

function publish() {
	$("#publishModal").modal("hide");

	var title =$("#recipient-name").val();
	var content = $("#message-text").val();

    $.ajax({
        type: "POST",
        url: "/forum/discuss/add",
        contentType: "application/json;charset=utf-8",
        data: JSON.stringify({
            "title" : title,
            "content" : content
        }),
        dataType: "json",
        success:function (response) {

            $("#hintBody").text(response.msg);

            $("#hintModal").modal("show");
            setTimeout(function(){
                $("#hintModal").modal("hide");
                if (!response.code) window.location.reload();
            }, 2000);

        }
    });

}