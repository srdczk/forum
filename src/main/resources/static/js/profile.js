$(function(){
	$(".follow-btn").click(follow);
});

function follow() {
	var btn = this;
	if($(btn).hasClass("btn-info")) {
		// 关注TA
		$.ajax({
			type: "POST",
			url: "/forum/follow",
			contentType: "application/json;charset=utf-8",
			data: JSON.stringify({
				"entityType" : 1,
				"entityId" : $("#userId").val(),
			}),
			dataType: "json",
			success:function (response) {
				if (!response.code) {
					window.location.reload();
				} else alert(response.msg);
			}
		});
		// $(btn).text("已关注").removeClass("btn-info").addClass("btn-secondary");
	} else {
		// 取消关注
		$.ajax({
			type: "POST",
			url: "/forum/unfollow",
			contentType: "application/json;charset=utf-8",
			data: JSON.stringify({
				"entityType" : 1,
				"entityId" : $("#userId").val(),
			}),
			dataType: "json",
			success:function (response) {
				if (!response.code) {
					window.location.reload();
				} else alert(response.msg);
			}
		});
		// $(btn).text("关注TA").removeClass("btn-secondary").addClass("btn-info");
	}
}