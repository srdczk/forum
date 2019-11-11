$(function(){
	$("#sendBtn").click(send_letter);
	$(".close").click(delete_msg);
});

// 发送数据
function send_letter() {
	$("#sendModal").modal("hide");

	//发送数据
	var toName = $("#recipient-name").val();
	var content = $("#message-text").val();

	$.ajax({
		type: "POST",
		url: "/forum/letter/send",
		contentType: "application/json;charset=utf-8",
		data: JSON.stringify({
			"toName" : toName,
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

function delete_msg() {
	// TODO 删除数据
	$(this).parents(".media").remove();
}